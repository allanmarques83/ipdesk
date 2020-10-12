package client.gui;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.Color;

import client.language.Language;
import client.configuration.Config;
import client.remote.SystemActions;
import client.gui.swing.*;
import client.resources.Constants;
import client.gui.screen.panels.ScreenTabsPanel;
import client.services.screen.ScreenView;

public class Screen extends Frame 
{
	private Language language;
	private Config config;
	private SystemActions system_actions;
	private ScreenView screen_view;


	private ScreenTabsPanel tabs_panel;

	public Screen(Config config, Language language, SystemActions system_actions) {
		this.language = language;
		this.system_actions = system_actions;
		this.config = config;

		this.screen_view = new ScreenView(system_actions);

		this.tabs_panel = new ScreenTabsPanel(language);
		this.tabs_panel.setCloseTab(params -> this.closeTab(params));

		system_actions.addAction("Screen.newScreen", params -> 
			newScreen(params));

		system_actions.addAction("Screen.screenHandler", params -> 
			this.tabs_panel.screenHandler(params));

		this
            .defTitle("Screen View")
            .defDefaultCloseOperation( JFrame.HIDE_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,1024, 600)
            .attach(getMainPanel())
            .onCloseEvent(params -> closeAllTabs(), null)
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

    private void closeTab(Object[] params) {
    	this.system_actions.getAction("stopScreen").accept(params);

    	if((boolean)params[1])
    		this.setVisible(false);
    }

    private void closeAllTabs() {
    	this.tabs_panel.closeAllTabs();
    }
}