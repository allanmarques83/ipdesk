package client.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.Color;

import client.gui.swing.Panel;
import client.gui.swing.Label;
import client.resources.Utils;
import client.configuration.Config;

public class LogoImage extends Panel
{
	String logo_file_path;

	public LogoImage(Config config) {
		super();

		logo_file_path = config.getLogoFilePath();

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