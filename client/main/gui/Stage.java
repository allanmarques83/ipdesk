package gui;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.Color;

import org.json.JSONObject;

import gui.swing.*;
import gui.panels.*;
import gui.menu.TopMenuBar;
import language.Language;
import main.SystemConfig;
import main.remote.Client;

public class Stage extends Frame
{
    Language language;

    public SystemConfig system_conf;

    private StatusSystemConnection status_system_connection;

    public Stage() {

        system_conf = new SystemConfig();
        language = new Language(system_conf.getLanguage());

        status_system_connection = new StatusSystemConnection();

        new Client()
            .addConsumers(
                status_system_connection.getChangeStatus());
        
        this
            .defTitle(system_conf.getTitle())
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
            .attach(new ClientIdentity(system_conf).getPanel(language), 
                "client_identity");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,1)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new LogoImage(system_conf).getPanel(), "logo_image");

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
            .attach(status_system_connection.getPanel(), "status_system");

        return stage_panel;
    }

    

    // public Panel getMainStage()
    // {

    // }
}