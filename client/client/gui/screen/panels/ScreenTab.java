package client.gui.screen.panels;

import java.awt.GridBagConstraints;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import client.resources.Constants;
import client.resources.Utils;
import client.gui.swing.Panel;
import client.gui.swing.Label;
import client.gui.screen.panels.ScreenActionsPanel;

public class ScreenTab extends Panel
{
	private Label screen_view;

	public ScreenTab(String remote_id) {
		super();

		screen_view = new Label("", Utils.toIcon("images/test_image.png"));

		this.defBackground(Constants.Colors.white)
			.defBorder(Constants.Borders.lowered);

		this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ScreenActionsPanel(), "");

	    this.fill(GridBagConstraints.BOTH)
            .grid(0,1)
            .weight(0,1)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(screen_view.getScroll(), "");
	} 	

	public void setImage(byte[] bytes_image) {
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream( bytes_image ));
			screen_view.setIcon(new ImageIcon(image));
	        screen_view.validate();
	        screen_view.validateScroll();
	    }
	    catch (Exception exception) {
	    	exception.printStackTrace();
	    }
	}
}