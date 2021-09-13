package gui.file_manager.panels;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.GridBagConstraints;
import java.io.File;

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
import resources.Utils;
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
        String text_node = selected_node.getUserObject().toString();
        String path_content = text_node.replaceAll("<(.+?):(.+?)>", "");
        String type_content = Utils.getExpression("<(.+?):(.+?)>", text_node, 1);
        System.out.println(text_node);

        if(tree_name.equals("_TREE_CONTROLLER") && !type_content.equals("file")) 
        {
            JSONArray dirs_and_files = FileManagerSource.getDirContent(path_content);
            fillNodeTreeWithContent(tree_name, dirs_and_files);
        }
        if(tree_name.equals("_TREE_CONTROLLED") && !type_content.equals("file")) {
            _EVENT.accept(
                String.format("<GET_CONTROLLED_DIRECTORY_CONTENT:%s>", path_content)
            );
        }
        return false;
    }

    public void fillNodeTreeWithContent(String tree_name, JSONArray dirs_and_files) {
        JTree tree = tree_name.equals("_TREE_CONTROLLER") ? _TREE_CONTROLLER : _TREE_CONTROLLED;

        DefaultMutableTreeNode selected_node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        selected_node.removeAllChildren();

        Iterator<Object> iterator = dirs_and_files.iterator();

        while (iterator.hasNext()) {
            selected_node.add(new DefaultMutableTreeNode( iterator.next().toString() ) );
        }

        tree.expandPath(new TreePath(selected_node.getPath()));
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.reload(selected_node);
    }

    public boolean hasSelectionInBothTrees() {
        TreePath[] selected_controller = _TREE_CONTROLLER.getSelectionPaths();	
        TreePath[] selected_controlled = _TREE_CONTROLLED.getSelectionPaths();	

		return selected_controller != null && selected_controlled != null;
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

    public String getSelectDestinationPath(String tree_name) {
        JTree tree = tree_name.equals("_TREE_CONTROLLER") ? _TREE_CONTROLLER : _TREE_CONTROLLED;

        DefaultMutableTreeNode selection_node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        String node_path_name = selection_node.getUserObject().toString();

        String node_type = Utils.getExpression("(.*?)<(.*?):(.*?)>", node_path_name, 2);
        
        if(node_type.equals("file")) {
            selection_node = (DefaultMutableTreeNode)tree.getSelectionPath().getParentPath().getLastPathComponent();
            node_path_name = selection_node.getUserObject().toString();
        }
        return Utils.getExpression("(.*?)<(.*?):(.*?)>", node_path_name, 1);
    }

    public JSONArray getSelectedNodes(String tree_name) {
        JTree tree = tree_name.equals("_TREE_CONTROLLER") ? _TREE_CONTROLLER : _TREE_CONTROLLED;
        Stream<TreePath> nodes = Arrays.stream(tree.getSelectionPaths());

        return new JSONArray(nodes.map(node -> {
            DefaultMutableTreeNode tree_node = (DefaultMutableTreeNode)node.getLastPathComponent();
            String node_name = tree_node.getUserObject().toString();
            
            return Utils.getExpression("(.*?)<(.*?):(.*?)>", node_name, 1);
        }).collect(Collectors.toList()));
    }

    public void enableTree(boolean enabled, String tree_name) {
        JTree tree = tree_name.equals("_TREE_CONTROLLER") ? _TREE_CONTROLLER : _TREE_CONTROLLED;
        tree.setEnabled(enabled);
    }
}
