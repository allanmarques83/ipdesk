package gui.screen.panels;

import java.awt.GridBagConstraints;
import java.util.function.Consumer;

import gui.swing.Panel;
import gui.swing.Label;
import resources.Utils;


public class ScreenTabButtonClose extends Panel 
{
	public ScreenTabButtonClose(String remote_id, 
			Consumer<Object[]> closeTab) {
		super();

		this.defOpaque(false);

		this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
	        .ipad(5,8)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new Label(String.format("%s:", remote_id)), "");

	    this.fill(GridBagConstraints.NONE)
            .grid(1,0)
            .weight(1,0)
	        .ipad(1,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new Label("", Utils.toIcon("images/close_tab.png"))
	        		.addMouseListener(closeTab, null), "");
	}
}