package main.gui;

import javax.swing.JTabbedPane;

import main.gui.screen.Screen;
import main.gui.stage.Stage;
import main.gui.stage.buttons.ButtonConnect;
import main.gui.stage.tables.ActiveRemoteAccessTable;
import main.gui.swing.Label;
import main.gui.swing.TextField;

public class Gui {
    public TextField textfield_client_id;
    public TextField remote_user_id;
    public TextField remote_user_password;
    public ButtonConnect button_connect;
    public JTabbedPane remote_users_tab;
    public ActiveRemoteAccessTable table_remote_users;
    public Label label_status;
    public Screen screen_frame;
    public Stage stage_frame;
}
