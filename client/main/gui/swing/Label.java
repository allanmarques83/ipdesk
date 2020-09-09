package gui.swing;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Label extends JLabel
{
	public Label(String text) {
		super(text);
	}	

	public Label(String text, ImageIcon image) {
		super(text);

		this.setIcon(image);
	}	

	public Label defForeground(Color foreground) {
		this.setForeground(foreground);

		return this;
	}

	public Label defFont(int size, int font_style)
    {
        this.setFont(new Font("Arial", font_style, size));
        return this;
    }
}