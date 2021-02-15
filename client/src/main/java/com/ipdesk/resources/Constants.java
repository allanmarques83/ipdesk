package resources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;

public class Constants
{
	public static class Colors {
		public static final Color mercury = Color.decode("#E4E4E4");
		public static final Color white = Color.decode("#FFFFFF");
		public static final Color bittersweet = Color.decode("#FF7575");
		public static final Color black = Color.decode("#000000");
		public static final Color blue = Color.decode("#0000FF");
		public static final Color anakiwa = Color.decode("#9DE5FF");
		public static final Color red = Color.decode("#FF0000");
		public static final Color bright_green = Color.decode("#66FF00");
	}

	public static class Borders {
		public static final Border lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	}

	public static class Monitor {
		public static final Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		public static final Integer width = screen_size.width;
		public static final Integer height = screen_size.height;
	}
}