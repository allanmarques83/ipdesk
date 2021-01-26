package main.gui.stage.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;

import main.configuration.Language;
import main.configuration.UserSystem;
import main.gui.Gui;
import main.gui.swing.Label;
import main.gui.swing.Panel;
import main.gui.swing.TextField;
import main.resources.Constants;

public class ClientIdentity extends Panel
{
    TextField _TEXTFIELD_CLIENT_ID;
    String _SYSTEM_PASSWORD;
    Language _LANGUAGE;

	public ClientIdentity(UserSystem user_system, Gui gui_components) {
		super();

        _SYSTEM_PASSWORD = user_system.getPassword();
        _LANGUAGE = user_system.getLanguage();
        
        gui_components.textfield_client_id = new TextField("")
        .defBackground(Constants.Colors.mercury)
        .defForeground(Constants.Colors.black)
        .defFont(16, Font.PLAIN)
        .defEditable(false) 
        .defBorder(null);
        _TEXTFIELD_CLIENT_ID = gui_components.textfield_client_id;

		this.defBackground(Constants.Colors.mercury);
	}

	public Panel getPanel() {

		this
            .fill(GridBagConstraints.NONE)
			.grid(0,0)
            .weight(0,0)
            .insets(5,5,5,5)
            .anchor(GridBagConstraints.NORTHWEST)
            .attach(
            	new Label(_LANGUAGE.translate("Your ID:"))
            		.defForeground(Constants.Colors.blue)
            		.defFont(16, Font.ITALIC), "client_id_title");

        this
            .fill(GridBagConstraints.NONE)
			.grid(1,0)
            .weight(1,0)
            .insets(5,5,5,5)
            .attach(_TEXTFIELD_CLIENT_ID, "client_id_value");

        this
            .fill(GridBagConstraints.NONE)
			.grid(2,0)
            .weight(0,0)
            .insets(5,5,5,5)
            .attach(
            	new Label(_LANGUAGE.translate("Password:"))
            		.defForeground(Constants.Colors.blue)
            		.defFont(16, Font.ITALIC), "client_pass_title");

        this
            .fill(GridBagConstraints.NONE)
			.grid(3,0)
            .weight(0,0)
            .attach(
            	new TextField(_SYSTEM_PASSWORD)
            		.defForeground(Constants.Colors.black)
            		.defFont(16, Font.PLAIN)
                    .defBorder(null)
                    .defEditable(false)
                    .defBackground(Constants.Colors.mercury), "client_pass_value");

        return this;
	}

}