package gui.screen.panels;

import java.awt.GridBagConstraints;
import java.awt.Font;

import resources.Constants;
import gui.swing.ComboBox;
import gui.swing.Label;
import gui.swing.Panel;
import remote.ServerConnection;

public class ScreenActionsPanel extends Panel
{
	public ScreenActionsPanel(String remote_id, ServerConnection server_connection) {
		super();

		this.defBackground(Constants.Colors.mercury);

		this.fill(GridBagConstraints.NONE)
            .grid(0,0)
            .weight(0,0)
	        .ipad(8,0)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new Label("Resolution: ").defFont(11, Font.PLAIN), "");

		this.fill(GridBagConstraints.NONE)
            .grid(1,0)
            .weight(0,0)
	        .ipad(8,0)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new Label("Quality: ").defFont(11, Font.PLAIN), "");

		this.fill(GridBagConstraints.NONE)
            .grid(0,1)
            .weight(0,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ComboBox<String>(new String[]{
	        	"1024", "1366"})
	        		.defBackground(Constants.Colors.white)
	        		.defFont(11, Font.PLAIN)
					.defItemListener(e -> server_connection._OUTCOMING_USER_ACTION.setScreenResolution(
							remote_id, Integer.parseInt(e.getItem().toString())
						)
					), "");

	    this.fill(GridBagConstraints.NONE)
            .grid(1,1)
            .weight(1,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ComboBox<String>(new String[]{"Normal", "Best"})
	        		.defBackground(Constants.Colors.white)
	        		.defFont(11, Font.PLAIN)
					.defItemListener(e -> server_connection._OUTCOMING_USER_ACTION.setScreenQuality(
							remote_id, e.getItem().toString()
						)
					), "");

	}
}