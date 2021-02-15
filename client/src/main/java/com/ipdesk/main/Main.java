package main;

import javax.swing.ToolTipManager;

import gui.Gui;
import gui.screen.Screen;
import gui.stage.Stage;
import remote.ServerConnection;
import resources.Utils;


public class Main
{
    ServerConnection _server_connection;
    Gui _gui_components;

    public Main() {
        
        Utils.applyNimbusLookAndFeel();
        
        _gui_components = new Gui();
        
        _server_connection = new ServerConnection(_gui_components);
        _gui_components.stage_frame = new Stage(_gui_components, _server_connection);
        _gui_components.screen_frame = new Screen(_server_connection);
    }
    
    public void start() {
        _server_connection.establish("Try to establish connection in: [%ds]", 3);
    }

    public static void main(String[] args) {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        new Main().start();
    }
}