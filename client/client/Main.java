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

public class Main
{
    Language language;
    Config config;
    Connection connection;
    ServerActions server_actions;
    ClientActions client_actions;

    public Main() {
        
        Utils.applyNimbusLookAndFeel();

        config = new Config();
        language = new Language(config.getLanguage());
        connection = new Connection(config, language);
        server_actions = new ServerActions(connection, language, config);
        client_actions = new ClientActions(connection, language);

        connection.setActions(server_actions, client_actions);

        new Stage(config, language, server_actions, client_actions);
        new Screen(config, language, server_actions, client_actions);

        connection.establish("Try to establish connection in: [%ds]", 3);
    }

    public static void main(String[] args) {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        new Main();
    }
}