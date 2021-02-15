package gui.swing;

import resources.Utils;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.Border;


public class TextField extends JTextField
{
	public TextField(String text) {
		super(text);
	}

	public TextField defForeground(Color color) {
        this.setForeground(color);
        return this;
    }
    
    public TextField defBackground(Color color) {
        this.setBackground(color);
        return this;
    }

    public TextField defFont(int size, int font_style) {
        this.setFont(new Font("Arial", font_style, size));
        return this;
    }
	
	public TextField defEnabled(boolean enable) {
        this.setEnabled(enable);
        return this;
    }

    public TextField defEditable(boolean editable) {
        this.setEditable(editable);
        return this;
    }

    public TextField setPreferredSize(int sizex, int sizey) {
        this.setPreferredSize(new Dimension(sizex, sizey));
        return this;
    }

    public TextField setMinimumSize(int sizex, int sizey) {
        this.setMinimumSize(new Dimension(sizex, sizey));
        return this;
    }

    public TextField defBorder(Border border) {
        this.setBorder(border);
        return this;
    }

    public TextField setHorizontalAlignment() {
        this.setHorizontalAlignment(JTextField.CENTER);
        return this;
    }

    public TextField setTextLimit(int sizeCaracters) {
    	JTextField textfield = this;

        this.addKeyListener(new KeyAdapter()
        {   
            public void keyTyped(KeyEvent e) 
            {  
                if(textfield.getText().length() >= sizeCaracters)  
                    e.consume();   
            }
        });
        return this;
    }

    public TextField setOnlyNumbers(int sizeCaracters)
    {
        JTextField textfield = this;

        textfield.addKeyListener(new KeyAdapter()
        {   
            public void keyReleased( KeyEvent e ) 
            {
                String txt = textfield.getText();
                String txt2 = String.valueOf(e.getKeyChar());
                                
                if( Utils.isInt(txt2) && txt.length() < sizeCaracters) 
                    textfield.setText(txt+txt2); 
                else
                    textfield.setText(txt);
            }
            public void keyTyped( KeyEvent e ) 
            {
                e.consume();
            } 
        });
        return this;
    }

    public TextField fireButtonClickWhenPressEnter(JButton btn)
    {
        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event)
            {
                if(event.getKeyCode() == KeyEvent.VK_ENTER) {
                    btn.doClick();
                }
            }
        });
        return this;
    }

    public TextField setPlaceHolder(String txto, int font_size) {

        JTextField textField = this;

        textField.setForeground(new Color(140,140,140));

        textField.addFocusListener(new FocusListener()
        { 
            public void focusGained(FocusEvent e) 
            {       
                String str = textField.getText();
                textField.setForeground(Color.BLUE);
                textField.setFont(new Font("Arial", Font.PLAIN, font_size));
            
                if(str.equals(txto))
                    textField.setText("");
                
            }  
            public void focusLost(FocusEvent e) 
            {
                String str = textField.getText();
                if(str.equals(txto)) 
                {
                    textField.setText("");
                    textField.setForeground(new Color(140,140,140));
                    textField.setFont(new Font("Arial", Font.ITALIC, font_size));
                }
              
                if(str.equals("")) 
                {
                    textField.setText(txto);
                    textField.setForeground(new Color(140,140,140));
                    textField.setFont(new Font("Arial", Font.ITALIC, font_size));
                }
            }           
        });
        
        if(textField.getText().equals(""))
            textField.setText(txto);
        
        if(textField.getText().equals(txto))
        {
            textField.setForeground(new Color(140,140,140));
            textField.setFont(new Font("Arial", Font.ITALIC, font_size));
        }
        else
        {
            textField.setForeground(new Color(0,0,255));
            textField.setFont(new Font("Arial", Font.PLAIN, font_size));   
        }
        return this;
    }
}