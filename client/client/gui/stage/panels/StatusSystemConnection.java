package client.gui.stage.panels;

import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;

import client.gui.swing.Panel;
import client.gui.swing.Label;
import client.resources.Utils;
import client.resources.Constants;
import client.remote.ServerActions;

public class StatusSystemConnection extends Panel
{
	private Label label_status;

	public StatusSystemConnection(ServerActions server_actions) {
		super();

		this.defBackground(Constants.Colors.black);

		label_status = new Label("", Utils.toIcon("images/tip.png"))
			.defFont(11, Font.BOLD);
		
		server_actions.addAction("setStatusSystem", params -> 
			setStatus((String)params[0], (Color)params[1]));
	}

	public Panel getPanel() {

		this
			.fill(GridBagConstraints.BOTH)
			.grid(0,0)
			.weight(1,1)
			.insets(5,0,5,0)
			.anchor(GridBagConstraints.NORTHWEST)
			.attach(label_status, "status_label");

		return this;
	}

	private void setStatus(String text, Color color) {
		label_status.setText(text);
		label_status.setForeground(color);
	}
}