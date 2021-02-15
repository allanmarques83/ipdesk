package gui;

import javax.swing.JTabbedPane;

import gui.screen.Screen;
import gui.stage.Stage;
import gui.stage.buttons.ButtonConnect;
import gui.stage.tables.ActiveRemoteAccessTable;
import gui.swing.Label;
import gui.swing.TextField;

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
