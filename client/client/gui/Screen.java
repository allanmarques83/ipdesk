package client.gui;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.Color;

import client.language.Language;
import client.configuration.Config;
import client.remote.ServerActions;
import client.remote.ClientActions;
import client.gui.swing.*;
import client.resources.Constants;
import client.gui.screen.panels.ScreenTabsPanel;

public class Screen extends Frame 
{
	private Language language;
	private Config config;
	private ServerActions server_actions;
	private ClientActions client_actions;

	private ScreenTabsPanel tabs_panel;

	public Screen(Config config, Language language, ServerActions server_actions, 
            ClientActions client_actions) {
		this.language = language;
		this.server_actions = server_actions;
		this.client_actions = client_actions;
		this.config = config;

		this.tabs_panel = new ScreenTabsPanel(language);

		client_actions.addAction("Screen.newScreen", params -> 
			newScreen(params));

		server_actions.addAction("Screen.screenHandler", params -> 
			this.tabs_panel.screenHandler(params));

		this
            .defTitle("Screen View")
            .defDefaultCloseOperation( JFrame.HIDE_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,1024, 600)
            .attach(getMainPanel())
            .defVisible( false );
	}

	private Panel getMainPanel() {

        Panel stage_panel = new Panel()
            .defBackground(Constants.Colors.mercury);

        stage_panel
	        .fill(GridBagConstraints.HORIZONTAL)
	        .grid(0,0)
	        .weight(1,1)
	        .anchor(GridBagConstraints.NORTH)
	        .attach(this.tabs_panel.getPanel(), 
	        	"tabbet_remote_access");

        return stage_panel;
    }

    private void newScreen(Object[] params) {
    	this.setVisible(true);
    	this.tabs_panel.addTab((String)params[0]);
    }
}