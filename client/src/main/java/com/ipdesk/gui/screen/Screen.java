package gui.screen;

import java.awt.GridBagConstraints;

import javax.swing.JFrame;

import configuration.Language;
import gui.screen.panels.ScreenTabsPanel;
import gui.swing.Frame;
import gui.swing.Panel;
import remote.ServerConnection;
import resources.Constants;
import services.screen.ScreenCapture;

public class Screen extends Frame 
{
	ServerConnection _SERVER_CONNECTION;
	Language _LANGUAGE;
	public ScreenCapture _SCREEN_CAPTURE;

	public ScreenTabsPanel _SCREEN_TABS;

	public Screen(ServerConnection server_connection) 
	{
		_SERVER_CONNECTION = server_connection;
		_LANGUAGE = _SERVER_CONNECTION.getLanguage();

		_SCREEN_CAPTURE = new ScreenCapture(server_connection);

		_SCREEN_TABS = new ScreenTabsPanel(_SERVER_CONNECTION);
		_SCREEN_TABS.setCloseTab(params -> this.closeTab(params));


		this
            .defTitle("Screen View")
            .defDefaultCloseOperation( JFrame.HIDE_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,800, 600)
            .attach(getMainPanel())
            .onCloseEvent(params -> _SCREEN_TABS.closeAllTabs(), null)
            .defVisible( false );
	}

	private Panel getMainPanel() {

        Panel stage_panel = new Panel()
            .defBackground(Constants.Colors.mercury);

        stage_panel
	        .fill(GridBagConstraints.BOTH)
	        .grid(0,0)
	        .weight(1,1)
	        .anchor(GridBagConstraints.NORTH)
	        .attach(_SCREEN_TABS.getPanel(), 
	        	"tabbet_remote_access");

        return stage_panel;
    }

    public void newScreen(String remote_id) {
    	this.setVisible(true);
    	_SCREEN_TABS.addTab(remote_id);
    }

    private void closeTab(Object[] params) {
		_SERVER_CONNECTION._OUTCOMING_USER_ACTION.stopScreen(params[0].toString());

    	if((boolean)params[1])
    		this.setVisible(false);
    }
}