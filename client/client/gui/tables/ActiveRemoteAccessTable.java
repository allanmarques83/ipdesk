package client.gui.tables;

import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.util.Vector;
import java.util.Arrays;

import client.gui.swing.Table;
import client.gui.swing.Button;
import client.language.Language;
import client.resources.Utils;
import client.resources.Constants;

public class ActiveRemoteAccessTable extends Table
{
	Language language;

	public ActiveRemoteAccessTable(Language language) {
		super();

		this.language = language;

		this
			.defModel(getTableModel())
			.setDefaultRenderer(getRenderTable())
            .setHeaderColor(Constants.Colors.mercury)
            .defBackground(Constants.Colors.mercury)
            .defRowHeight(20)
            .defAutoCreateRowSorter(true)
            .setSizeOfColunms(new int[][]{ 
                {1,30,30},
                {2,35,35},
                {3,35,35},
                {4,30,30},
                {5,30,30}
             })
            .setScrollBar(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
            .setScrollSize(300,120);
	}

	private DefaultTableModel getTableModel() {
        return new DefaultTableModel(null, new Vector<String>(Arrays.asList(
            new String[]{ language.translate("Remote ID:"), "SC:", "FM:", "CH:", "MI:", "SV:"}
            ))) {
            	public boolean isCellEditable(int row, int col) {
                	return false;
        	}
    	};
	}

	private TableCellRenderer getRenderTable()
    {
        return new TableCellRenderer() 
        {
            public Component getTableCellRendererComponent(JTable table, 
                Object value, boolean isSelected, boolean hasFocus, int row, int column) 
            {
             
                String text_content = String.valueOf(value);
                
                Button btn = new Button(text_content, null)
                    .defBorder(null)
                    .defFocusPainted(false)
                    .defBackground(Constants.Colors.mercury);

                String column_name = table.getColumnName(column);

                if(column_name.equals("SC:")) {
                	btn.setIcon(Utils.toIcon("images/screen.png"));
                	btn.setToolTipText(language.translate("Screen View"));
                }
                if(column_name.equals("FM:")) {
                	btn.setIcon(Utils.toIcon("images/filemanager.png"));
                	btn.setToolTipText(language.translate("File Manager"));
                }
                if(column_name.equals("MI:")) {
                	btn.setIcon(Utils.toIcon("images/mic.png"));
                	btn.setToolTipText(language.translate("Microphone"));
                }
                if(column_name.equals("SV:")) {
                	btn.setIcon(Utils.toIcon("images/save.png"));
                	btn.setToolTipText(language.translate("Save Remote ID"));
                }
                if(column_name.equals("CH:")) {
                	btn.setIcon(Utils.toIcon("images/chat.png"));
                	btn.setToolTipText(language.translate("Chat"));
                }
                    
                if(isSelected)
                    btn.setBackground(Constants.Colors.anakiwa); 
                  
                return btn;
            }
        };
    }
}