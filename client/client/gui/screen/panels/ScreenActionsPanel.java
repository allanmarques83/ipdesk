package client.gui.screen.panels;

import java.awt.GridBagConstraints;

import client.resources.Constants;
import client.gui.swing.ComboBox;
import client.gui.swing.Panel;

public class ScreenActionsPanel extends Panel
{
	public ScreenActionsPanel() {
		super();

		this.defBackground(Constants.Colors.mercury);

		this.fill(GridBagConstraints.NONE)
            .grid(0,0)
            .weight(0,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ComboBox<String>(new String[]{"Resolution:"})
	        		.defBackground(Constants.Colors.white), "");

	    this.fill(GridBagConstraints.NONE)
            .grid(1,0)
            .weight(1,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ComboBox<String>(new String[]{"Quality:"})
	        		.defBackground(Constants.Colors.white), "");

	}
}