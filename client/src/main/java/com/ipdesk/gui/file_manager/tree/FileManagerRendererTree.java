package gui.file_manager.tree;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import gui.swing.Label;
import resources.Constants;
import resources.Utils;

public class FileManagerRendererTree implements TreeCellRenderer 
{
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus
    ) 
	{
        String tree_data = value.toString();

        String tree_type = Utils.getExpression("<(.+?):(.+?)>", tree_data);
        String tree_value = Utils.getExpression(
            String.format("<%s:(.*?)>", tree_type), tree_data
        );

        Label leaf_label = new Label(
            tree_value, Utils.toIcon(String.format("images/%s.png", tree_type))
        );

        if(row == 0) {
			leaf_label.setIcon(Utils.toIcon("images/computer.png"));
        }

		if(selected) {
			leaf_label.setOpaque(true);
			leaf_label.setBackground(Constants.Colors.mercury);
		}

        return leaf_label;
    }
}