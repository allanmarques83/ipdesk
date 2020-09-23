package client.gui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.util.function.Consumer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Label extends JLabel
{
	JScrollPane scroll;

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

	public Label defFont(int size, int font_style) {
        this.setFont(new Font("Arial", font_style, size));
        return this;
    }

    public Label addMouseListener(Consumer<Object[]> action, Object[] args) {
    	this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {} 
			public void mouseEntered(MouseEvent e) {} 
			public void mouseExited(MouseEvent e) {} 
			public void mousePressed(MouseEvent e) {} 
			public void mouseReleased(MouseEvent e) {
                action.accept(args);
            }
		});
    	return this;
    }

    public JScrollPane getScroll() {
    	scroll = new JScrollPane();
		scroll.getViewport().setBorder(null);
		scroll.setViewportView(this);
		scroll.setMinimumSize(new Dimension(1024,600));

		return scroll;
    }

    public Label validateScroll() {
    	scroll.revalidate();
    	scroll.repaint();
    	scroll.validate();
    	return this;
    }
}