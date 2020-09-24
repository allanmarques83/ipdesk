package client.gui.stage.panels;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;

import org.json.JSONObject;

import client.language.Language;
import client.resources.Utils;
import client.resources.Constants;
import client.gui.swing.*;
import client.remote.SystemActions;
import client.gui.stage.buttons.ButtonConnect;


public class RemoteAccessForm extends Panel
{
      TextField remote_client_id, remote_client_password;
      ButtonConnect button_connect;

      Language language;
      SystemActions system_actions;

	public RemoteAccessForm(SystemActions system_actions, Language language) {
		super();

            this.system_actions = system_actions;
            this.language = language;

            remote_client_id = new TextField("")
                  .setPlaceHolder(language.translate("Remote ID"), 16)
                  .setOnlyNumbers(6)
                  .setPreferredSize(100,24)
                  .setHorizontalAlignment()
                  .fireButtonClickWhenPressEnter(button_connect)
                  .defFont(16, Font.PLAIN);

            button_connect = new ButtonConnect(language, system_actions);
            button_connect.defActionListener((e) -> 
                  button_connect.fireActionButton(remote_client_id.getText(),
                        remote_client_password.getText()));
            // button_connect.defActionListener((e) -> client_actions.getScreenView(null));

            remote_client_password = new TextField("")
                  .setPlaceHolder(language.translate("Password"), 16)
                  .setPreferredSize(100,24)
                  .setTextLimit(8)
                  .fireButtonClickWhenPressEnter(button_connect)
                  .setHorizontalAlignment()
                  .defFont(16, Font.PLAIN);

            system_actions.addAction("setEnabledButtonConnect", params -> 
                  button_connect.defEnabled((boolean)params[0]));

            system_actions.addAction("setButtonConnectionAction", params -> 
                  button_connect.setButtonConnectionAction(params));

            system_actions.addData("ButtonConnect", () -> new JSONObject()
                  .put("getActionCommand", button_connect.getActionCommand()));

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
                  	new Label(language.translate("Remote Access:"))
                  		.defFont(16, Font.ITALIC), "label_remote_access");

            this
      		.fill(GridBagConstraints.NONE)
      		.grid(0,1)
                  .weight(0,1)
                  .insets(5,5,2,5)
                  .ipad(5,20)
                  .gridwidth(1)
                  .anchor(GridBagConstraints.NORTH)
                  .attach(remote_client_id, "label_remote_access");

            this
      		.fill(GridBagConstraints.NONE)
      		.grid(1,1)
                  .weight(0,0)
                  .gridwidth(1)
                  .anchor(GridBagConstraints.NORTHEAST)
                  .attach(remote_client_password, "label_remote_access");

            this
      		.fill(GridBagConstraints.NONE)
      		.grid(0,2)
                  .weight(0,1)
                  .gridwidth(2)
                  .anchor(GridBagConstraints.CENTER)
                  .attach(
                  	button_connect,
                  		"label_remote_access");


		return this;
	}


}