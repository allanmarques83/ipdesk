package client.gui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import java.util.HashMap;
import javax.swing.border.Border;
import javax.swing.JPanel;

public class Panel extends JPanel
{
	GridBagConstraints grid;

	 Map<String, Component> components;

    public Panel() {

    	grid = new GridBagConstraints();

    	this.setLayout(new GridBagLayout());

    	components = new HashMap<String, Component>();

    }
    public Panel fill(int fill) {
        grid.fill = fill;

        return this;
    }
    public Panel grid(int gridx, int gridy) {
        grid.gridx = gridx;
        grid.gridy = gridy;

        return this;
    }

    public Panel ipad(int ipadx, int ipady) {
        grid.ipadx = ipadx;
        grid.ipady = ipady;

        return this;
    }

    public Panel insets(int top, int left, int bottom, int right) {
        grid.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public Panel gridheight(int gridheight) {
        grid.gridheight = gridheight;
        return this;
    }

    public Panel gridwidth(int gridwidth) {
        grid.gridwidth = gridwidth;
        return this;
    }

    public Panel weight(int weightx, int weighty) {
        grid.weightx = weightx;
        grid.weighty = weighty;

        return this;
    }

    public Panel anchor(int anchor) {
        grid.anchor = anchor;
        return this;
    }

    public Panel defBorder(Border border) {
        this.setBorder(border);
        return this;
    }

    public Panel defBackground(Color color) {
        this.setBackground(color);
        return this;
    }

    public Panel attach(Component component, String name) {
    	components.put(name, component);
    	this.add(component, grid);

    	return this;
    }
}