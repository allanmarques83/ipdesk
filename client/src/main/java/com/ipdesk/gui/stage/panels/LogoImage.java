package gui.stage.panels;

import java.awt.GridBagConstraints;

import gui.swing.Panel;
import gui.swing.Label;
import resources.Utils;
import configuration.UserSystem;
import resources.Constants;

public class LogoImage extends Panel
{
	String logo_file_path;

	public LogoImage(UserSystem user_system) {
		super();

		logo_file_path = user_system.getLogoFilePath();

		this.defBackground(Constants.Colors.white);
	}

	public Panel getPanel() {

		this
            .fill(GridBagConstraints.NONE)
			.grid(0,0)
            .weight(0,0)
            .insets(5,5,5,5)
            .anchor(GridBagConstraints.NORTHWEST)
            .attach(
            	new Label("", Utils.toIcon(logo_file_path)), 
            		"client_id_title");

        return this;
	}
}