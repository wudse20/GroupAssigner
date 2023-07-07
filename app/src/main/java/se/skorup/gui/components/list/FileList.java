package se.skorup.gui.components.list;

import se.skorup.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Arrays;

/**
 * A wrapper for the JList with code for adding files.
 * */
public class FileList extends JList<File>
{
    /**
     * Creates a new FileList.
     * */
    public FileList()
    {
        this.setCellRenderer(new FileListCellRenderer());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Set the items in the list.
     *
     * @param files the files that to be shown in the list.
     * */
    public void setItems(File[] files)
    {
        Arrays.sort(files, (a, b) -> {
            if (a.isDirectory() && b.isFile())
                return -1;
            else if (b.isDirectory() && a.isFile())
                return 1;
            else
                return a.getName().compareTo(b.getName());
        });

        this.setListData(files);
    }

    /** A ListCellRender for the file dialog. */
    private static class FileListCellRenderer extends DefaultListCellRenderer
    {
        private final Color selectedColor = Utils.SELECTED_COLOR;
        private final Color backgroundSelectedColor = Utils.BACKGROUND_COLOR;
        private final Color nonSelectedColor = Utils.FOREGROUND_COLOR;
        private final Color backgroundNonSelectedColor = Utils.COMPONENT_BACKGROUND_COLOR;

        private final JLabel label;
        private final FileSystemView fsv;

        private FileListCellRenderer()
        {
            this.label = new JLabel();
            this.fsv = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean selected, boolean expanded
        )
        {
            var f = (File) value;
            label.setOpaque(true);
            label.setIcon(fsv.getSystemIcon(f));
            label.setText(fsv.getSystemDisplayName(f));
            label.setToolTipText(f.getPath());

            if (selected)
            {
                label.setBackground(backgroundSelectedColor);
                label.setForeground(selectedColor);
            }
            else
            {
                label.setBackground(backgroundNonSelectedColor);
                label.setForeground(nonSelectedColor);
            }

            return label;
        }
    }
}
