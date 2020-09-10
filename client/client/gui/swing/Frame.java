package client.gui.swing;

import javax.swing.JMenuBar;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame
{
    public Frame() {

    }

    public Frame defTitle(String title) {
        super.setTitle(title);
        return this;
    }
    public Frame defDefaultCloseOperation(int inst) {
        super.setDefaultCloseOperation(inst);
        return this;
    }
    public Frame defLocationRelativeTo(Component inst) {
        super.setLocationRelativeTo(inst);
        return this;
    }
    public Frame defBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        return this;
    }
    public Frame defVisible(boolean visible) {
        super.setVisible(visible);
        return this;
    }
    public Frame defResizable(boolean resize) {
        this.setResizable(resize);
        return this;
    }
    public Frame defJMenuBar(JMenuBar jmenu_bar) {
        super.setJMenuBar(jmenu_bar);
        return this;
    }
    public Frame attach(JPanel panel) {
        super.add(panel);
        return this;
    }
}