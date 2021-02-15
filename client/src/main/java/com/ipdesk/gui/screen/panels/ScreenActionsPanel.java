package gui.screen.panels;

import java.awt.GridBagConstraints;
import java.awt.Font;

import resources.Constants;
import gui.swing.ComboBox;
import gui.swing.Panel;

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
	        .attach(new ComboBox<String>(new String[]{
	        	"Fit Screen", "Original", "Optimized"})
	        		.defBackground(Constants.Colors.white)
	        		.defFont(10, Font.PLAIN), "");

	    this.fill(GridBagConstraints.NONE)
            .grid(1,0)
            .weight(1,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ComboBox<String>(new String[]{"Normal", "Best"})
	        		.defBackground(Constants.Colors.white)
	        		.defFont(10, Font.PLAIN), "");

	}
}