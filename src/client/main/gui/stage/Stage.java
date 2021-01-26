package main.gui.stage;

import java.awt.GridBagConstraints;

import javax.swing.JFrame;

import main.configuration.UserSystem;
import main.gui.Gui;
import main.gui.stage.menu.TopMenuBar;
import main.gui.stage.panels.ClientIdentity;
import main.gui.stage.panels.LogoImage;
import main.gui.stage.panels.RemoteAccessForm;
import main.gui.stage.panels.RemoteAccessTabbet;
import main.gui.stage.panels.StatusSystemConnection;
import main.gui.swing.Frame;
import main.gui.swing.Panel;
import main.remote.ServerConnection;
import main.resources.Constants;

public class Stage extends Frame
{
    UserSystem _USER_SYSTEM;
    ServerConnection _SERVER_CONNECTION;

    public Stage(Gui gui_components, ServerConnection server_connection) {

        _USER_SYSTEM = server_connection;
        _SERVER_CONNECTION = server_connection;

        this
            .defTitle(_USER_SYSTEM.getTitle())
            .defDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )
            .defLocationRelativeTo(null)
            .defBounds(0,0,380, 550)
            .defResizable(false)
            .defJMenuBar(new TopMenuBar(
                _USER_SYSTEM.getLanguage()
            ).build())
            .attach(getMainPanel(gui_components))
            .defVisible( true );
    }

    private Panel getMainPanel(Gui gui_components) {

        Panel stage_panel = new Panel()
            .defBackground(Constants.Colors.white);

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTHWEST)
            .attach(new ClientIdentity(_USER_SYSTEM, gui_components).getPanel(), 
                "client_identity");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,1)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new LogoImage(_USER_SYSTEM).getPanel(), "logo_image");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,2)
            .weight(1,0)
            .ipad(5,5)
            .anchor(GridBagConstraints.NORTH)
            .attach(new RemoteAccessForm(_SERVER_CONNECTION, gui_components)
                .getPanel(), "remote_access_form");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,3)
            .weight(1,0)
            .anchor(GridBagConstraints.NORTH)
            .attach(new RemoteAccessTabbet(_SERVER_CONNECTION, gui_components)
                .getPanel(), "tabbet_remote_access");

        stage_panel
            .fill(GridBagConstraints.HORIZONTAL)
            .grid(0,4)
            .weight(1,1)
            .anchor(GridBagConstraints.NORTH)
            .attach(new StatusSystemConnection(gui_components).getPanel(),
                "status_system");

        return stage_panel;
    }
}