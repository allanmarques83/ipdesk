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
import client.remote.Connection;

public class Stage extends Frame
{
    Language language;

    public Config conf;

    Connection connection;

    public Stage() {

        conf = new Config();
        language = new Language(conf.getLanguage());

        connection = new Connection(conf, language);
        
        this
            .defTitle(conf.getTitle())
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
            .defBackground(Color.decode("#FFFFFF"));

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTHWEST)
            .attach(new ClientIdentity(conf).getPanel(language), 
                "client_identity");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,1)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new LogoImage(conf).getPanel(), "logo_image");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,2)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new RemoteAccessForm().getPanel(language), "remote_access_form");

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
            .attach(new StatusSystemConnection(connection).getPanel(),
                "status_system");

        return stage_panel;
    }

    

    // public Panel getMainStage()
    // {

    // }
}