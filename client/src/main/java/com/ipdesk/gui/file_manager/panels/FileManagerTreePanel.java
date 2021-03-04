package gui.file_manager.panels;

import java.util.Iterator;
import java.util.function.Consumer;
import java.awt.GridBagConstraints;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONArray;

import gui.file_manager.tree.FileManagerTree;
import gui.swing.Panel;
import resources.Constants;
import services.file_manager.FileManagerSource;

public class FileManagerTreePanel extends Panel 
{
    FileManagerTree _TREE_CONTROLLER;
    FileManagerTree _TREE_CONTROLLED;

    Consumer<String> _EVENT;
    
    public FileManagerTreePanel(Consumer<String> event) {
        super();

        _EVENT = event;

        this.defBackground(Constants.Colors.mercury);

        _TREE_CONTROLLER = new FileManagerTree();
        _TREE_CONTROLLED = new FileManagerTree();

        _TREE_CONTROLLER.addTreeSelectionListener(
            getSelectionListener(_TREE_CONTROLLER, _TREE_CONTROLLED, "_TREE_CONTROLLER")
        );
        _TREE_CONTROLLED.addTreeSelectionListener(
            getSelectionListener(_TREE_CONTROLLED, _TREE_CONTROLLER, "_TREE_CONTROLLED")
        );

        fillTreeWithDrives(
            "_TREE_CONTROLLER", FileManagerSource.getDrives()
        );
        
        buildPanel();
    }

    private void buildPanel() {
        this.fill(GridBagConstraints.BOTH)
            .grid(0,0)
            .weight(1,1)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new JScrollPane(_TREE_CONTROLLER.getScrollView()), "");
            
        this.fill(GridBagConstraints.BOTH)
            .grid(1,0)
            .weight(1,1)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new JScrollPane(_TREE_CONTROLLED.getScrollView()), "");
    }

    public TreeSelectionListener getSelectionListener(
        JTree tree_selected, JTree tree_opponent, String tree_indentity
    ) 
    {
        return (e -> {
            TreePath[] selected_paths = tree_selected.getSelectionPaths();

            if(selected_paths != null) 
            {
                int selectionType = TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION;

                if(selected_paths.length > 1) {
                    selectionType = TreeSelectionModel.SINGLE_TREE_SELECTION;
                }
                tree_opponent.getSelectionModel().setSelectionMode(selectionType);
            }

            DefaultMutableTreeNode selected_node = (DefaultMutableTreeNode)tree_selected.getLastSelectedPathComponent();
            
            if(selected_node != null) { 
                executeSelectionEvent(
                    selected_node, tree_indentity
                );
            }
        });
    }

    public boolean executeSelectionEvent(DefaultMutableTreeNode selected_node, String tree_name) {
        String text_node = selected_node.getUserObject().toString().replaceAll(
            "<(.+?):(.+?)>",""
        );

        if(tree_name.equals("_TREE_CONTROLLER") && !text_node.contains("<file:")) 
        {
            JSONArray array = FileManagerSource.getDirContent(text_node);
            expandPathTree(tree_name, array);
        }
        if(tree_name.equals("_TREE_CONTROLLED") && !text_node.contains("<file:")) {
            _EVENT.accept(
                String.format("<GET_CONTROLLED_DIRECTORY_CONTENT:%s>", text_node)
            );
        }
        return false;
    }

    public void expandPathTree(String tree_name, JSONArray array) {
        JTree tree = tree_name.equals("_TREE_CONTROLLER") ? _TREE_CONTROLLER : _TREE_CONTROLLED;

        DefaultMutableTreeNode selected_node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

        Iterator<Object> iterator = array.iterator();

        while (iterator.hasNext()) {
            selected_node.add(new DefaultMutableTreeNode( iterator.next().toString() ) );
        }

        tree.expandPath(new TreePath(selected_node.getPath()));
    }

    public void fillTreeWithDrives(String tree_name, JSONArray array) 
    {
        JTree tree = tree_name.equals("_TREE_CONTROLLER") ? _TREE_CONTROLLER : _TREE_CONTROLLED;

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

        Iterator<Object> iterator = array.iterator();

        while (iterator.hasNext()) 
        {
            root.add(
                new DefaultMutableTreeNode(iterator.next().toString())
            );
        }
        model.reload(root);
    }
}
