package gui.file_manager;

import javax.swing.WindowConstants;
import java.awt.GridBagConstraints;
import java.util.function.Consumer;

import gui.file_manager.panels.FileManagerButtonsPanel;
import gui.file_manager.panels.FileManagerTreePanel;
import gui.file_manager.progress_bar.FileManagerProgressBarPanel;
import gui.swing.Frame;
import gui.swing.Panel;
import remote.ServerConnection;
import resources.Constants;
import services.file_manager.FileManagerSource;


public class FileManager extends Frame
{
    public FileManagerButtonsPanel _BTN_CONTROLLER;
    public FileManagerButtonsPanel _BTN_CONTROLLED;
    public FileManagerTreePanel _TREES;
    FileManagerProgressBarPanel _PROGRESS;

    public FileManager(Consumer<String> event) {

        _BTN_CONTROLLER = new FileManagerButtonsPanel(event, "CONTROLLER");
        _BTN_CONTROLLED = new FileManagerButtonsPanel(event, "CONTROLLED");
        _TREES = new FileManagerTreePanel(event);
        _PROGRESS = new FileManagerProgressBarPanel();

        this
            .defTitle("File Manager")
            .defDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,600, 500)
            .attach(getMainPanel())
            .defVisible( false );
    }
    private Panel getMainPanel() {

        Panel stage_panel = new Panel()
            .defBackground(Constants.Colors.white);
        
        stage_panel
	        .fill(GridBagConstraints.HORIZONTAL)
	        .grid(0,0)
	        .weight(1,0)
	        .anchor(GridBagConstraints.NORTH)
	        .attach(_BTN_CONTROLLER, "");
            
        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(1,0)
            .weight(1,0)
            .anchor(GridBagConstraints.NORTH)
            .attach(_BTN_CONTROLLED, "");
        
        stage_panel
            .fill(GridBagConstraints.BOTH)
            .grid(0,1)
            .weight(1,1)
            .gridwidth(2)
            .anchor(GridBagConstraints.NORTH)
            .attach(_TREES, "");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,2)
            .weight(1,0)
            .gridwidth(2)
            .anchor(GridBagConstraints.NORTH)
            .attach(_PROGRESS, "");

        return stage_panel;
    }

    public void enableButtonsForTransfer(boolean enable) {
        _BTN_CONTROLLED.setEnableButtons(enable);
        _BTN_CONTROLLER.setEnableButtons(enable);
    }
}
