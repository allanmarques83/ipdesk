package gui.file_manager;

import javax.swing.WindowConstants;
import java.awt.GridBagConstraints;

import gui.file_manager.panels.FileManagerButtonsPanel;
import gui.file_manager.panels.FileManagerTreePanel;
import gui.swing.Frame;
import gui.swing.Panel;
import remote.ServerConnection;
import resources.Constants;


public class FileManager extends Frame
{
    ServerConnection _SERVER_CONNECTION;

    public FileManager(ServerConnection server_connection) {
        _SERVER_CONNECTION = server_connection;

        this
            .defTitle("File Manager")
            .defDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,600, 500)
            .attach(getMainPanel())
            .defVisible( true );
    }
    private Panel getMainPanel() {

        Panel stage_panel = new Panel()
            .defBackground(Constants.Colors.white);
        
        stage_panel
	        .fill(GridBagConstraints.HORIZONTAL)
	        .grid(0,0)
	        .weight(1,0)
	        .anchor(GridBagConstraints.NORTH)
	        .attach(new FileManagerButtonsPanel(), 
	        	"");
            
        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(1,0)
            .weight(1,0)
            .anchor(GridBagConstraints.NORTH)
            .attach(new FileManagerButtonsPanel(), 
                "");
        
        stage_panel
            .fill(GridBagConstraints.BOTH)
            .grid(0,1)
            .weight(1,1)
            .gridwidth(2)
            .anchor(GridBagConstraints.NORTH)
            .attach(new FileManagerTreePanel(), 
                "");

        return stage_panel;
    }
}
