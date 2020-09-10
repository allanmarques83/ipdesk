package client.gui.swing;

import javax.swing.ImageIcon;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;


public class Button extends JButton
{
	public Button(String text, ImageIcon icon) {
		super(text, icon);
	}

	public Button defBorder(Border border) {
        this.setBorder(border);
        return this;
    }

    public Button setPreferredSize(int sizeX, int sizeY) {
        this.setPreferredSize(new Dimension(sizeX, sizeY));
        return this;
    }

    public Button defToolTipText(String text) {
        this.setToolTipText(text);
        return this;
    }

    public Button defHorizontalAlignment(int align) {
        this.setHorizontalAlignment(align);
        return this;
    }

    public Button defFocusPainted(boolean focus) {
        this.setFocusPainted(focus);
        return this;
    }

    public Button defBackground(Color color) {
        this.setBackground(color);
        return this;
    }
}