package gui.swing;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
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

	public Label defHorizontalAlignment(int alignment) {
		this.setHorizontalAlignment(alignment);
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
    	// scroll = new JScrollPane(
		// 	this, 
		// 	ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		// 	ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		// );
		// scroll.getViewport().setView(this);
		// scroll.getViewport().setExtentSize(new Dimension(
			// this.getIcon().getIconWidth(),
			// this.getIcon().getIconHeight()
		// ));
		scroll = new JScrollPane();
		scroll.getViewport().setBorder(null);
		scroll.getViewport().setLayout(new GridBagLayout());
		GridBagConstraints grid = new GridBagConstraints();
		grid.weightx = 1;
		grid.weighty = 1;
		grid.gridx = 0;
		grid.gridy = 0;
		grid.anchor = GridBagConstraints.NORTHWEST;
		scroll.getViewport().add(this, grid);


		return scroll;
    }

    public Label validateScroll() {
    	scroll.revalidate();
    	scroll.repaint();
    	scroll.validate();
    	return this;
    }
}
