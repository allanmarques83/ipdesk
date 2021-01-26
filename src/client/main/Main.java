package main;

import javax.swing.ToolTipManager;

import main.gui.Gui;
import main.gui.screen.Screen;
import main.gui.stage.Stage;
import main.remote.ServerConnection;
import main.resources.Utils;


public class Main
{
    public Main() {
        
        Utils.applyNimbusLookAndFeel();
        
        Gui gui_components = new Gui();
        
        ServerConnection server_connection = new ServerConnection(gui_components);
        
        gui_components.stage_frame = new Stage(gui_components, server_connection);
        gui_components.screen_frame = new Screen(server_connection);

        server_connection.establish("Try to establish connection in: [%ds]", 3);
    }

    public static void main(String[] args) {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        new Main();
    }
}