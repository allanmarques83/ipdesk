package gui.file_manager.tree;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileManagerTree extends JTree {
    public FileManagerTree() {
        super(new DefaultMutableTreeNode());

        this.setCellRenderer(new FileManagerRendererTree());
    }

    public JScrollPane getScrollView() {
        JScrollPane scroll = new JScrollPane(this);

        scroll.setPreferredSize(new Dimension(200, 200));
        scroll.setBorder(null);
        return scroll;
    }
}
