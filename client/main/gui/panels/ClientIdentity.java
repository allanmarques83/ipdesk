package gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;

import language.Language;
import main.SystemConfig;
import resources.Utils;
import gui.swing.Panel;
import gui.swing.Label;
import gui.swing.TextField;

public class ClientIdentity extends Panel
{
	String system_password;

	public ClientIdentity(SystemConfig system_config) {
		super();

		system_password = system_config.getPassword();

		this.defBackground(Color.decode("#E4E4E4"));
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
            		.defForeground(Color.decode("#0000FF"))
            		.defFont(16, Font.ITALIC), "client_id_title");

        this
            .fill(GridBagConstraints.NONE)
			.grid(1,0)
            .weight(1,0)
            .insets(5,5,5,5)
            .attach(
            	new Label("123456")
            		.defForeground(Color.decode("#000000"))
            		.defFont(16, Font.PLAIN), "client_id_value");

        this
            .fill(GridBagConstraints.NONE)
			.grid(2,0)
            .weight(0,0)
            .insets(5,5,5,5)
            .attach(
            	new Label(language.translate("Password:"))
            		.defForeground(Color.decode("#0000FF"))
            		.defFont(16, Font.ITALIC), "client_pass_title");

        this
            .fill(GridBagConstraints.NONE)
			.grid(3,0)
            .weight(0,0)
            .attach(
            	new Label(system_password)
            		.defForeground(Color.decode("#000000"))
            		.defFont(16, Font.PLAIN), "client_pass_value");

        return this;
	}

}