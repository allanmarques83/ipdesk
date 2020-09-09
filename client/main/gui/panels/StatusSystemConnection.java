package gui.panels;

import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;
import java.util.function.Consumer;

import gui.swing.Panel;
import gui.swing.Label;
import language.Language;
import resources.Utils;


public class StatusSystemConnection extends Panel
{
	private Label label_status;

	private Consumer<Object[]> change_status;

	public StatusSystemConnection() {
		super();

		this.defBackground(Color.decode("#E4E4E4"));


		label_status = new Label("", Utils.toIcon("images/tip.png"))
			.defFont(12, Font.BOLD);

		change_status = params -> setStatus((String)params[0], (Color)params[1]);
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
	}

	public Consumer<Object[]> getChangeStatus() {
		return change_status;
	}

}