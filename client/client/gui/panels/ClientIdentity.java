package client.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;

import client.language.Language;
import client.configuration.Config;
import client.resources.*;
import client.gui.swing.Panel;
import client.gui.swing.Label;
import client.gui.swing.TextField;
import client.remote.ServerActions;

public class ClientIdentity extends Panel
{
    TextField textfield_client_id;
	String system_password;

	public ClientIdentity(Config config, ServerActions server_actions) {
		super();

        server_actions.addAction("setRemoteClientId", params -> 
            textfield_client_id.setText((String)params[0]));

		this.system_password = config.getPassword();

        this.textfield_client_id = new TextField("")
            .defBackground(Constants.Colors.mercury)
            .defForeground(Constants.Colors.black)
            .defFont(16, Font.PLAIN)
            .defEditable(false) 
            .defBorder(null);

		this.defBackground(Constants.Colors.mercury);
	}

	public Panel getPanel(Language language) {

		this
            .fill(GridBagConstraints.NONE)
			.grid(0,0)
            .weight(0,0)
            .insets(5,5,5,5)
            .anchor(GridBagConstraints.NORTHWEST)
            .attach(
            	new Label(language.translate("Your ID:"))
            		.defForeground(Constants.Colors.blue)
            		.defFont(16, Font.ITALIC), "client_id_title");

        this
            .fill(GridBagConstraints.NONE)
			.grid(1,0)
            .weight(1,0)
            .insets(5,5,5,5)
            .attach(this.textfield_client_id, "client_id_value");

        this
            .fill(GridBagConstraints.NONE)
			.grid(2,0)
            .weight(0,0)
            .insets(5,5,5,5)
            .attach(
            	new Label(language.translate("Password:"))
            		.defForeground(Constants.Colors.blue)
            		.defFont(16, Font.ITALIC), "client_pass_title");

        this
            .fill(GridBagConstraints.NONE)
			.grid(3,0)
            .weight(0,0)
            .attach(
            	new TextField(system_password)
            		.defForeground(Constants.Colors.black)
            		.defFont(16, Font.PLAIN)
                    .defBorder(null)
                    .defEditable(false)
                    .defBackground(Constants.Colors.mercury), "client_pass_value");

        return this;
	}

}