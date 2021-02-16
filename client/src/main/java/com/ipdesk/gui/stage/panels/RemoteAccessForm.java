package gui.stage.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;

import configuration.Language;
import gui.Gui;
import gui.stage.buttons.ButtonConnect;
import gui.swing.Label;
import gui.swing.Panel;
import gui.swing.TextField;
import remote.ServerConnection;
import resources.Constants;


public class RemoteAccessForm extends Panel
{
      TextField _REMOTE_USER_ID, _REMOTE_USER_PASSWORD;
      ButtonConnect _BUTTON_CONNECT;

      Language _LANGUAGE;

	public RemoteAccessForm(ServerConnection server_connection, Gui gui_components) {
            super();
            
            _LANGUAGE = server_connection.getLanguage();
            
            gui_components.remote_user_id = new TextField("")
            .setPlaceHolder(_LANGUAGE.translate("Remote ID"), 16)
            .setOnlyNumbers(6)
            .setPreferredSize(100,24)
            .setHorizontalAlignment()
            .fireButtonClickWhenPressEnter(_BUTTON_CONNECT)
            .defFont(16, Font.PLAIN);
            _REMOTE_USER_ID = gui_components.remote_user_id;
            
            gui_components.button_connect = new ButtonConnect(server_connection);
            _BUTTON_CONNECT = gui_components.button_connect;
            
            _BUTTON_CONNECT.defActionListener(e -> 
            _BUTTON_CONNECT.fireActionButton(_REMOTE_USER_ID.getText(),
            _REMOTE_USER_PASSWORD.getText()));
            // button_connect.defActionListener((e) -> client_actions.getScreenView(null));
            
            gui_components.remote_user_password = new TextField("")
            .setPlaceHolder(_LANGUAGE.translate("Password"), 16)
            .setPreferredSize(100,24)
            .setTextLimit(8)
            .fireButtonClickWhenPressEnter(_BUTTON_CONNECT)
            .setHorizontalAlignment()
            .defFont(16, Font.PLAIN);
            _REMOTE_USER_PASSWORD = gui_components.remote_user_password;
            
		this.defBackground(Constants.Colors.white);
	}

	public Panel getPanel() {	

		this
			.fill(GridBagConstraints.NONE)
			.grid(0,0)
                  .weight(0,0)
                  .insets(5,5,5,5)
                  .gridwidth(2)
                  .anchor(GridBagConstraints.CENTER)
                  .attach(
                  	new Label(_LANGUAGE.translate("Remote Access:"))
                  		.defFont(16, Font.ITALIC), "label_remote_access");

            this
      		.fill(GridBagConstraints.NONE)
      		.grid(0,1)
                  .weight(0,1)
                  .insets(5,5,2,5)
                  .ipad(5,20)
                  .gridwidth(1)
                  .anchor(GridBagConstraints.NORTH)
                  .attach(_REMOTE_USER_ID, "label_remote_access");

            this
      		.fill(GridBagConstraints.NONE)
      		.grid(1,1)
                  .weight(0,0)
                  .gridwidth(1)
                  .anchor(GridBagConstraints.NORTHEAST)
                  .attach(_REMOTE_USER_PASSWORD, "label_remote_access");

            this
      		.fill(GridBagConstraints.NONE)
      		.grid(0,2)
                  .weight(0,1)
                  .gridwidth(2)
                  .anchor(GridBagConstraints.CENTER)
                  .attach(
                  	_BUTTON_CONNECT,
                  		"label_remote_access");


		return this;
	}


}