package gui.swing;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import java.util.Vector;
import java.awt.event.ItemListener;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ComboBox<String> extends JComboBox<String>
{
	public ComboBox(String[] values) {
		super(values);
	}

	public ComboBox defEnabled(boolean enable) {
        this.setEnabled(enable);
        return this;
    }

    public ComboBox defForeground(Color color) {
        this.setForeground(color);
        return this;
    }

    public ComboBox defBackground(Color color) {
        this.setBackground(color);
        return this;
    }

    public ComboBox defFont(int size, int font_style) {
        this.setFont(new Font("Arial", font_style, size));
        return this;
    }

    public ComboBox defBorder(Border border) {
        this.setBorder(border);
        return this;
    }

    public ComboBox setTitleBorder(Border border, String text, Color text_color) {
        TitledBorder border_title = BorderFactory.createTitledBorder(border, text.toString(), 
            	TitledBorder.DEFAULT_POSITION, 
            		TitledBorder.DEFAULT_POSITION, 
            			new Font("Arial",Font.PLAIN, 12), 
            				text_color); 
        
        this.setBorder(border_title);
        return this;
    }

    public ComboBox defItemListener(ItemListener listner) {
        this.addItemListener(listner);
        return this;
    }

    public ComboBox defModel(Vector<String> data) {
        this.setModel(new DefaultComboBoxModel<String>(data));
        return this;
    }

    public ComboBox defModel(String[] data) {
        this.setModel(new DefaultComboBoxModel<String>(data));
        return this;
    }
}