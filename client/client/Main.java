package client;

import javax.swing.ToolTipManager;

import client.resources.Utils;
import client.gui.Stage;
import client.gui.Screen;
import client.language.Language;
import client.configuration.Config;
import client.remote.Connection;
import client.remote.ServerActions;
import client.remote.ClientActions;
import client.remote.SystemActions;

public class Main
{
    Language language;
    Config config;
    Connection connection;
    ServerActions server_actions;
    ClientActions client_actions;
    SystemActions system_actions;

    public Main() {
        
        Utils.applyNimbusLookAndFeel();

        config = new Config();
        language = new Language(config.getLanguage());
        connection = new Connection(config, language);
        system_actions = new SystemActions();
        server_actions = new ServerActions(connection, language, config, system_actions);
        client_actions = new ClientActions(connection, language, system_actions);

        connection.setActions(server_actions, system_actions);

        new Stage(config, language, system_actions);
        new Screen(config, language, system_actions);

        connection.establish("Try to establish connection in: [%ds]", 3);
    }

    public static void main(String[] args) {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        new Main();
    }
}