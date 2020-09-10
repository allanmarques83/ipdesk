package client.gui.swing;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.DefaultCellEditor;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JTable;
import java.awt.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class Table extends JTable
{
	JScrollPane scroll_table;

	public Table() {
		super();
	}

	public Table defModel(DefaultTableModel model) {
		this.setModel(model);
		return this;
	}

	public Table setDefaultRenderer(TableCellRenderer render) {
        this.setDefaultRenderer(Object.class, render);
        return this;
    }

    public Table addMouseListener(MouseAdapter mouse) {
        this.addMouseListener(mouse);
        return this;
    }

    public Table defRowHeight(int size) {
        this.setRowHeight(size);
        return this;
    }

    public Table defAutoCreateRowSorter(boolean enable) {
        this.setAutoCreateRowSorter(enable);
        return this;
    }

    public Table defAutoResizeMode(int resize) {
        this.setAutoResizeMode(resize);
        return this;
    }

    public Table setNumsOfclicksToEdit(int numClicks) {
        ((DefaultCellEditor) this.getDefaultEditor(Object.class)).setClickCountToStart(numClicks);
        return this;
    }

    public Table setSizeOfColunms(int[][] sizes) {
        for(int[] size : sizes) {
            this.getColumnModel().getColumn(size[0]).setMinWidth(size[1]);
            this.getColumnModel().getColumn(size[0]).setMaxWidth(size[2]);
        }
        return this;
    }

    public Table setReorderingAllowed(boolean enable) {
        this.getTableHeader().setReorderingAllowed(enable);
        return this;
    }

    public Table setHeaderColor(Color color) {
        this.getTableHeader().setBackground(color);
        return this;
    }

    public Table defBackground(Color color) {
        this.setBackground(color);
        return this;
    }

    public Table setScrollBar(int vertical_scroll, int horizontal_scroll) {
        scroll_table = new JScrollPane(this, vertical_scroll, horizontal_scroll);
        return this;
    }

    public Table setScrollPreferredSize(int sizeX, int sizeY) {
        scroll_table.setPreferredSize(new Dimension(sizeX, sizeY));
        return this;
    }

    public Table setScrollBorder(Border border) {
        scroll_table.setBorder(border);
        return this;
    }

    public Table setScrollBackground(Color color) {
        scroll_table.setBackground(color);
        return this;
    }

    public JScrollPane getTableScroll() {
        return scroll_table;
    }

    public Table setScrollSize(int sizeX, int sizeY) {
        scroll_table.setMinimumSize(new Dimension(sizeX, sizeY));
        return this;
    }

    public void addRow(Object[] row) {
    	((DefaultTableModel)this.getModel()).addRow(row);
    }
}