package client.gui;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.Color;


import org.json.JSONObject;

import client.gui.swing.*;
import client.gui.panels.*;
import client.gui.menu.TopMenuBar;
import client.language.Language;
import client.configuration.Config;
import client.remote.ClientActions;
import client.remote.ServerActions;
import client.resources.Constants;

public class Stage extends Frame
{
    private Language language;
    private Config config;
    private ServerActions server_actions;
    private ClientActions client_actions;

    public Stage(Config config, Language language, ServerActions server_actions, 
            ClientActions client_actions) {

        this.config = config;
        this.language = language;
        this.server_actions = server_actions;
        this.client_actions = client_actions;

        this
            .defTitle(config.getTitle())
            .defDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,380, 550)
            .defResizable(false)
            .defJMenuBar(new TopMenuBar(language).build())
            .attach(getMainPanel())
            .defVisible( true );
    }

    public Panel getMainPanel() {

        Panel stage_panel = new Panel()
            .defBackground(Constants.Colors.white);

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTHWEST)
            .attach(new ClientIdentity(config, server_actions).getPanel(language), 
                "client_identity");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,1)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new LogoImage(config).getPanel(), "logo_image");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,2)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new RemoteAccessForm(server_actions, client_actions,
                    language).getPanel(), "remote_access_form");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,3)
            .weight(1,0)
            .anchor(GridBagConstraints.NORTH)
            .attach(new RemoteAccessTabbet(language).getPanel(), "tabbet_remote_access");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,4)
            .weight(1,1)
            .anchor(GridBagConstraints.NORTH)
            .attach(new StatusSystemConnection(server_actions).getPanel(),
                "status_system");

        return stage_panel;
    }
}