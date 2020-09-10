package client.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;
import java.util.function.Consumer;

import client.gui.swing.Panel;
import client.gui.swing.Label;
import client.language.Language;
import client.resources.Utils;
import client.remote.Connection;

public class StatusSystemConnection extends Panel
{
	private Label label_status;

	public StatusSystemConnection(Connection connection) {
		super();

		this.defBackground(Color.decode("#000000"));

		label_status = new Label("Establish server connection...", 
			Utils.toIcon("images/tip.png")).defFont(11, Font.BOLD);
		
		connection.setStatusSystem(params -> setStatus((String)params[0], (Color)params[1]));
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