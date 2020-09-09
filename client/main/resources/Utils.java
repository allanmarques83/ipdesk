package resources;

import java.awt.Font;
import java.awt.Color;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

import main.Main;

public class Utils {

	public static ImageIcon toIcon(String path) {
		java.net.URL imgURL = Main.class.getResource("../".concat(path));
		if (imgURL != null) 
			return new ImageIcon(imgURL);
		else 
		{
			System.out.println("../".concat(path));
			return null;
		}
	}

	public static String getRandomPassword() {
		return UUID.randomUUID().toString().substring(0,8);
	}

	public static Border getTitleBorder(String title, Color text_color)
    {
    	Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        return BorderFactory.createTitledBorder(border, 
            title, 
            TitledBorder.DEFAULT_POSITION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Arial",Font.PLAIN, 12), 
            text_color); 
    }

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
