package main.gui.stage.panels;

import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;

import main.gui.swing.Panel;
import main.gui.Gui;
import main.gui.swing.Label;
import main.resources.Utils;
import main.resources.Constants;

public class StatusSystemConnection extends Panel
{
	Label _LABEL_STATUS;

	public StatusSystemConnection(Gui gui_components) {
		super();
		
		this.defBackground(Constants.Colors.black);
		
		gui_components.label_status = new Label("", Utils.toIcon("images/tip.png"))
		.defFont(11, Font.BOLD);

		_LABEL_STATUS = gui_components.label_status;
	}

	public Panel getPanel() {

		this
			.fill(GridBagConstraints.BOTH)
			.grid(0,0)
			.weight(1,1)
			.insets(5,0,5,0)
			.anchor(GridBagConstraints.NORTHWEST)
			.attach(_LABEL_STATUS, "status_label");

		return this;
	}
}