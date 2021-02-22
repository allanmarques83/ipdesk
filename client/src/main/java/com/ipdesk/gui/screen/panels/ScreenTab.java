package gui.screen.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import resources.Constants;
import resources.Utils;
import services.screen.mouse.MouseEventScreen;
import gui.swing.Panel;
import remote.ServerConnection;
import gui.swing.Label;

public class ScreenTab extends Panel
{
	private Label screen_view;

	MouseEventScreen _MOUSE_EVENT;

	ServerConnection _SERVER_CONNECTION;

	public ScreenTab(String remote_id, ServerConnection server_connection) {
		super();

		_SERVER_CONNECTION = server_connection;
		_MOUSE_EVENT = new MouseEventScreen(params -> sendMouseEvent(remote_id, params));

		screen_view = new Label("", Utils.toIcon("images/waitScreenView.png"))
		.defHorizontalAlignment(SwingConstants.CENTER);
		screen_view.addMouseListener(_MOUSE_EVENT.getSceenMouseListner());
		screen_view.addMouseMotionListener(_MOUSE_EVENT.getSceenMouseMotionListner());
		screen_view.addMouseWheelListener(_MOUSE_EVENT.getMouseWheelListener());
		

		this.defBackground(Constants.Colors.white)
			.defBorder(Constants.Borders.lowered);

		this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
	        .ipad(5,5)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new ScreenActionsPanel(remote_id, server_connection), "");

	    this.fill(GridBagConstraints.BOTH)
            .grid(0,1)
            .weight(1,1)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(screen_view.getScroll(), "");
	} 	

	public void setImage(byte[] bytes_image) {
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream( bytes_image ));
			screen_view.setIcon(new ImageIcon(image));
			screen_view.setMaximumSize(new Dimension(
				screen_view.getIcon().getIconWidth(),
				screen_view.getIcon().getIconHeight()
			));
	        screen_view.validate();
	        screen_view.validateScroll();
	    }
	    catch (Exception exception) {
	    	exception.printStackTrace();
	    }
	}

	public void sendMouseEvent(String remote_id, Object[] event_params) {
		_SERVER_CONNECTION._OUTCOMING_USER_ACTION.mouseScreenEvent(
			remote_id,
			event_params[0].toString(),
			(int)event_params[1],
			(int)event_params[2],
			(int)event_params[3],
			(int)event_params[4],
			(int)event_params[5],
			screen_view.getIcon().getIconWidth(),
			screen_view.getIcon().getIconHeight()
		);
	}
}