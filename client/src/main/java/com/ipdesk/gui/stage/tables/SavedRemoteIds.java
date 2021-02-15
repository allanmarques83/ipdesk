package gui.stage.tables;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import configuration.Language;
import gui.swing.Button;
import gui.swing.Table;
import resources.Constants;
import resources.Utils;

public class SavedRemoteIds extends Table
{
	Language language;

	public SavedRemoteIds(Language language) {
		super();

		this.language = language;

		this
			.defModel(getTableModel())
			.defDefaultRenderer(getRenderTable())
            .setHeaderColor(Constants.Colors.mercury)
            .defBackground(Constants.Colors.mercury)
            .defRowHeight(20)
            .defAutoCreateRowSorter(true)
            .setNumsOfclicksToEdit(1)
            .setSizeOfColunms(new int[][]{ 
                {1,90,90},
                {2,90,95},
                {3,35,35},
                {4,30,30}
             })
            .setScrollBar(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
            .setScrollSize(300,120);

	}

	private DefaultTableModel getTableModel() {
        return new DefaultTableModel(null, new Vector<String>(Arrays.asList(
            new String[]{ 
            	language.translate("Description:"), 
            	language.translate("Remote ID:"), 
            	language.translate("Password:"),
            	"CN:",
            	""}
            ))) {
            	public boolean isCellEditable(int row, int col) {
            		String column_name = this.getColumnName(col);

            		if(column_name.equals("") | column_name.equals("CN:"))
                		return false;
                	return true;
        	}
    	};
	}

	private TableCellRenderer getRenderTable() {
        return new TableCellRenderer() 
        {
            public Component getTableCellRendererComponent(JTable table, 
                Object value, boolean isSelected, boolean hasFocus, int row, int column) 
            {
             
                String text_content = String.valueOf(value);
                
                Button btn = new Button(text_content, null)
                    .defBorder(null)
                    .defFocusPainted(false)
                    .defBackground(Color.decode("#E4E4E4"));

                String column_name = table.getColumnName(column);

                if(column_name.equals("CN:")) {
                	btn.setIcon(Utils.toIcon("images/connect.png"));
                	btn.setToolTipText(language.translate("Connect"));
                }
                if(column_name.equals("")) {
                	btn.setIcon(Utils.toIcon("images/del.gif"));
                	btn.setToolTipText(language.translate("Delete"));
                }
                    
                if(isSelected)
                    btn.setBackground(Constants.Colors.anakiwa); 
                  
                return btn;
            }
        };
    }
}