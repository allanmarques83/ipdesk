package gui.stage.tables;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.Consumer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import configuration.Language;
import gui.swing.Button;
import gui.swing.Table;
import remote.ServerConnection;
import resources.Constants;
import resources.Utils;

public class ActiveRemoteAccessTable extends Table
{
    Language _LANGUAGE;
    ServerConnection _SERVER_CONNECTION;

	public ActiveRemoteAccessTable(ServerConnection server_connection) {
        super();
        
        _SERVER_CONNECTION = server_connection;
        _LANGUAGE = server_connection.getLanguage();

		this
			.defModel(getTableModel())
			.defDefaultRenderer(getRenderTable())
            .setHeaderColor(Constants.Colors.mercury)
            .defBackground(Constants.Colors.mercury)
            .defRowHeight(20)
            .defAutoCreateRowSorter(true)
            .setSizeOfColunms(new int[][]{ 
                {1,30,30},
                {2,35,35},
                {3,35,35},
                {4,30,30},
                {5,30,30},
                {6,30,30}
             })
            .setScrollBar(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
            .defMouseListener(getTableMouseEvent())
            .setScrollSize(300,120);
	}

	private DefaultTableModel getTableModel() {
        return new DefaultTableModel(null, new Vector<String>(Arrays.asList(
            new String[]{ _LANGUAGE.translate("Remote ID:"), "SC:", "FM:", "CH:", "MI:", 
                "SV:", ""}))) {
            	public boolean isCellEditable(int row, int col) {
                	return false;
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
                    .defBackground(Constants.Colors.mercury);

                String column_name = table.getColumnName(column);

                if(!column_name.equals(_LANGUAGE.translate("Remote ID:")))
                    btn.setText("");

                if(column_name.equals("SC:")) {
                	btn.setIcon(Utils.toIcon("images/screen.png"));
                	btn.setToolTipText(_LANGUAGE.translate("Screen View"));
                }
                if(column_name.equals("FM:")) {
                	btn.setIcon(Utils.toIcon("images/filemanager.png"));
                	btn.setToolTipText(_LANGUAGE.translate("File Manager"));
                }
                if(column_name.equals("MI:")) {
                	btn.setIcon(Utils.toIcon("images/mic.png"));
                	btn.setToolTipText(_LANGUAGE.translate("Microphone"));
                }
                if(column_name.equals("SV:")) {
                	btn.setIcon(Utils.toIcon("images/save.png"));
                	btn.setToolTipText(_LANGUAGE.translate("Save Remote ID"));
                }
                if(column_name.equals("CH:")) {
                	btn.setIcon(Utils.toIcon("images/chat.png"));
                	btn.setToolTipText(_LANGUAGE.translate("Chat"));
                }
                if(column_name.equals("")) {
                    btn.setIcon(Utils.toIcon("images/close.png"));
                    btn.setToolTipText(_LANGUAGE.translate("Close connection"));
                }
                    
                if(isSelected)
                    btn.setBackground(Constants.Colors.anakiwa); 
                  
                return btn;
            }
        };
    }

    private MouseAdapter getTableMouseEvent()
    {
        return new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) 
            {
                JTable jtable = (JTable)e.getComponent();

                int column  = jtable.getSelectedColumn();
                int row     = jtable.getSelectedRow();
                String colunm_name = jtable.getColumnName(column);
                String remote_id = jtable.getValueAt(row, column).toString();
                
                if (e.getClickCount() == 1) 
                {
                    if(colunm_name.equals("")) {
                        _SERVER_CONNECTION._OUTCOMING_USER_ACTION.closeRemoteIdConnection(remote_id);
                        removeRow(row);
                    }
                    if(colunm_name.equals("SC:")) {
                        _SERVER_CONNECTION._OUTCOMING_USER_ACTION.getScreenView(
                            remote_id, (Constants.Monitor.width-25));
                    }
                          
                }
            };
        };
    }

    public void addRemoteIdConnection(String remote_id) {
        this.addRow(new Object[]{remote_id,remote_id,remote_id,remote_id,remote_id,
            remote_id, remote_id});
    }

    public void removeRemoteIdConnection(String remote_id) {
        Vector<Vector> vectors = this.getDataVector();

        for(Vector vector : vectors) {
            vector.remove(remote_id);
        }
        this.setDataVector(vectors, true);
    }
}