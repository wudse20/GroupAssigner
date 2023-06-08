package se.skorup.gui.dialog;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.FileList;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.ScrollPane;
import se.skorup.util.Log;
import se.skorup.util.localization.Localization;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A file dialog.
 * */
public final class FileDialog extends Dialog<File>
{
    private static final String title = Localization.getValue("ui.title.dialog.file");

    private String path;
    private File selected;
    private Set<String> fileTypes;

    private final Button btnChoose;
    private final Button btnClose;
    private final Button btnHome;
    private final Button btnBack;

    private final Label lblPath;
    private final Label lblSelected;

    private final FileList file;

    /** The FileDialog should never be directly instantiated. */
    private FileDialog()
    {
        this.type = Dialog.FILE_DIALOG;
        this.fileTypes = Set.of();
        this.path = "..";
        this.file = new FileList();
        this.lblSelected = new Label(
            Localization.getValuef(
                "ui.text.dialog.file",
                Localization.getValue("ui.text.dialog.empty")
            )
        );
        this.btnChoose = new Button("ui.button.dialog.choose");
        this.btnClose = new Button("ui.button.dialog.close");
        this.btnHome = new Button(homeIcon);
        this.btnBack = new Button(returnIcon);
        this.lblPath = new Label("ui.text.bug", true);
        this.addListeners();
    }

    /**
     * Adds all the listeners.
     * */
    private void addListeners()
    {
        btnHome.addActionListener(e -> showPath(System.getProperty("user.home")));
        btnBack.addActionListener(e -> showPath(new File(path).getParent()));
        btnClose.addActionListener(e -> close());
        btnChoose.addActionListener(e -> {
            if (selected == null || selected.isFile())
            {
                result = selected;
                close();
                return;
            }

            showPath(selected.getAbsolutePath());
        });

        file.addListSelectionListener(e -> {
            var file = this.file.getSelectedValue();
            selected = file;

            if (file == null)
            {
                btnChoose.setText(Localization.getValue("ui.button.dialog.choose"));
                lblSelected.setText(
                    Localization.getValuef(
                        "ui.text.dialog.file",
                        Localization.getValue("ui.text.dialog.empty"))
                );
                return;
            }

            lblSelected.setText(Localization.getValuef("ui.text.dialog.file", file.getName()));
            btnChoose.setText(
                file.isDirectory() ?
                Localization.getValue("ui.button.dialog.open") :
                Localization.getValue("ui.button.dialog.choose")
            );
        });
    }

    /**
     * Shows a path in the dialog. I.e., it lists all the files.
     *
     * @param path the path to view in the dialog.
     * */
    private void showPath(String path)
    {
        try
        {
            this.path = path == null || path.equals("null") ? this.path : path;
            var pathLabel = new File(this.path).getCanonicalPath().replaceAll("\\\\", "/");
            this.lblPath.setText(
                Localization.getValuef(
                        "ui.text.dialog.path",
                        pathLabel.substring(pathLabel.lastIndexOf('/') + 1)
                )
            );

            if (fileTypes.isEmpty())
            {
                file.setItems(new File(this.path).listFiles());
                return;
            }

            var candidates = new File(this.path).listFiles();
            assert candidates != null;
            var items = Arrays.stream(candidates)
                              .parallel()
                              .filter(f ->
                                  f.isDirectory() ||
                                  fileTypes.contains(
                                      f.getAbsolutePath()
                                       .substring(
                                           f.getAbsolutePath()
                                                      .lastIndexOf('.') + 1
                                       ).toLowerCase()
                                  )
                              ).toList();

            file.setItems(items.toArray(new File[0]));
        }
        catch (IOException e)
        {
            Log.errorf("Unexpected error: %s", e);
        }
    }

    /**
     * Shows the currently selected path
     * */
    private void showPath()
    {
        showPath(path);
    }

    @Override
    protected void setupFrame(DialogFrame frame)
    {
        frame.setTitle(title);
        showPath();

        var cp = frame.getContentPane();

        var bp = new Panel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(btnClose);
        bp.add(btnChoose);

        var fp = new Panel(new FlowLayout(FlowLayout.CENTER));
        fp.add(lblSelected);

        var bottomPanel = new Panel(null);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(new Label(" "));
        bottomPanel.add(fp);
        bottomPanel.add(bp);

        var topPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(lblPath);
        topPanel.add(new Label("   "));
        topPanel.add(btnHome);
        topPanel.add(btnBack);

        var scr = new ScrollPane(file);
        var cont = new Panel(new BorderLayout());

        cont.add(topPanel, BorderLayout.PAGE_START);
        cont.add(scr, BorderLayout.CENTER);
        cont.add(bottomPanel, BorderLayout.PAGE_END);

        cp.add(new Label("   "), BorderLayout.LINE_START);
        cp.add(cont, BorderLayout.CENTER);
        cp.add(new Label("   "), BorderLayout.LINE_END);

        frame.pack();

        frame.setSize(
            new Dimension(
                (int) Math.max(title.length() * 15, 5 * scr.getPreferredSize().getWidth()),
                (int) (
                    3 * bottomPanel.getPreferredSize().getHeight() +
                    scr.getPreferredSize().getHeight() +
                    3 * topPanel.getPreferredSize().getHeight()
                )
        ));
    }

    /**
     * Creates the builder for the FileDialog.
     *
     * @return the next step in building the file dialog.
     * */
    public static PathStep create()
    {
        return new DialogBuilder();
    }

    /** The path step in building the file dialog. */
    public interface PathStep
    {
        /**
         * Set the path where the file dialog should start.
         *
         * @param path the path of the starting point for the file dialog.
         * @return the next step in the chain for building the dialog.
         * */
        FileTypeStep setPath(String path);
    }

    /** The file type step in building the dialog. */
    public interface FileTypeStep
    {
        /**
         * Sets the allowed file extensions.
         *
         * @param allowed the allowed file extensions.
         * @return the building is now complete, and the file dialog is returned.
         * */
        FileDialog setAllowedFileExtensions(Set<String> allowed);

        /**
         * Allows all file extensions.
         *
         * @return the building is now complete, and the file dialog is returned.
         * */
        FileDialog allowAllFileExtensions();
    }

    /** The class responsible for building the file dialog. */
    public static final class DialogBuilder implements PathStep, FileTypeStep
    {
        private final FileDialog dialog;

        /** No one should ever instantiate this class, but FileDialog. */
        private DialogBuilder()
        {
            dialog = new FileDialog();
        }

        @Override
        public FileTypeStep setPath(String path)
        {
            dialog.path = path;
            return this;
        }

        @Override
        public FileDialog setAllowedFileExtensions(Set<String> allowed)
        {
            dialog.fileTypes = allowed.stream()
                                      .map(String::toLowerCase)
                                      .collect(Collectors.toSet());
            return dialog;
        }

        @Override
        public FileDialog allowAllFileExtensions()
        {
            dialog.fileTypes = Set.of();
            return dialog;
        }
    }
}
