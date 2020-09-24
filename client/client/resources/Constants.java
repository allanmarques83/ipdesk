package client.resources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;

public class Constants
{
	public static class Colors {
		public static Color mercury = Color.decode("#E4E4E4");
		public static Color white = Color.decode("#FFFFFF");
		public static Color bittersweet = Color.decode("#FF7575");
		public static Color black = Color.decode("#000000");
		public static Color blue = Color.decode("#0000FF");
		public static Color anakiwa = Color.decode("#9DE5FF");
		public static Color red = Color.decode("#FF0000");
		public static Color bright_green = Color.decode("#66FF00");
	}

	public static class Borders {
		public static Border lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	}

	public static class Monitor {
		public static Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		public static Integer width = screen_size.width;
		public static Integer height = screen_size.height;
	}
}