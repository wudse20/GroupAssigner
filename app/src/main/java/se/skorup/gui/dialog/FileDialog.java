package se.skorup.gui.dialog;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.FileList;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.gui.components.TextField;
import se.skorup.gui.helper.MyScrollBarUI;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

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
    public FileDialog()
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

            var items = new ArrayList<File>();
            var candidates = new File(this.path).listFiles();

            assert candidates != null;
            for (var f : candidates)
            {
                if (f.isDirectory())
                {
                    items.add(f);
                    continue;
                }

                var type = f.getAbsolutePath()
                        .substring(f.getAbsolutePath().lastIndexOf('.'))
                        .toLowerCase();

                if (fileTypes.contains(type))
                    items.add(f);
            }

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

        var scr = new JScrollPane(file);
        scr.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        scr.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        scr.setForeground(Utils.FOREGROUND_COLOR);
        scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        var scrBar = scr.getVerticalScrollBar();
        scrBar.setUI(new MyScrollBarUI());

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
                (int) Math.max(title.length() * 15, 3 * scr.getPreferredSize().getWidth()),
                (int) (
                    3 * bottomPanel.getPreferredSize().getHeight() +
                    scr.getPreferredSize().getHeight() +
                    3 * topPanel.getPreferredSize().getHeight()
                )
        ));
    }
}
