package gui.stage.panels;

import java.awt.GridBagConstraints;

import javax.swing.JTabbedPane;

import configuration.Language;
import gui.Gui;
import gui.stage.tables.ActiveRemoteAccessTable;
import gui.stage.tables.SavedRemoteIds;
import gui.swing.Panel;
import remote.ServerConnection;
import resources.Constants;

public class RemoteAccessTabbet extends Panel
{
	JTabbedPane _REMOTE_USERS_TAB;

	ActiveRemoteAccessTable _TABLE_REMOTE_USERS;

	Language _LANGUAGE;

	public RemoteAccessTabbet(ServerConnection server_connection, Gui gui_components) {
		super();

		gui_components.table_remote_users = new ActiveRemoteAccessTable(server_connection);
		_LANGUAGE = server_connection.getLanguage();

		_TABLE_REMOTE_USERS = gui_components.table_remote_users;

		this.defBackground(Constants.Colors.white);

		_REMOTE_USERS_TAB = new JTabbedPane();

		_REMOTE_USERS_TAB.addTab(_LANGUAGE.translate("Connected IDs:"), null,
               _TABLE_REMOTE_USERS.getTableScroll(), null);

        _REMOTE_USERS_TAB.addTab(_LANGUAGE.translate("Saved IDs:"), null,
                new SavedRemoteIds(_LANGUAGE).getTableScroll(), null);  
	}

	public Panel getPanel() {

		this
			.fill(GridBagConstraints.BOTH)
			.grid(0,0)
			.weight(1,1)
			.insets(5,0,0,0)
			.gridwidth(2)
			.anchor(GridBagConstraints.CENTER)
			.attach(_REMOTE_USERS_TAB, "tab_remote_access");

		return this;
	}
}