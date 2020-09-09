package gui.panels;

import java.awt.GridBagConstraints;
import java.awt.Color;

import gui.swing.Panel;
import gui.swing.Label;
import resources.Utils;
import main.SystemConfig;

public class LogoImage extends Panel
{
	String logo_file_path;

	public LogoImage(SystemConfig system_config) {
		super();

		logo_file_path = system_config.getLogoFilePath();

		this.defBackground(Color.decode("#FFFFFF"));
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