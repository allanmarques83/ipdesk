package client.gui.panels;

import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.Color;

import client.gui.swing.Panel;
import client.gui.tables.ActiveRemoteAccessTable;
import client.gui.tables.SavedRemoteIds;
import client.language.Language;
import client.resources.Constants;
import client.remote.ServerActions;
import client.remote.ClientActions;

public class RemoteAccessTabbet extends Panel
{
	JTabbedPane tab_remote_access;

	ActiveRemoteAccessTable active_remote_access;

	public RemoteAccessTabbet(Language language, ServerActions server_actions,
		ClientActions client_actions) {
		super();

		active_remote_access = new ActiveRemoteAccessTable(language, 
			params -> client_actions.closeRemoteIdConnection(params));

		this.defBackground(Constants.Colors.white);

		tab_remote_access = new JTabbedPane();

		tab_remote_access.addTab(language.translate("Connected IDs:"), null,
               active_remote_access.getTableScroll(), null);

        tab_remote_access.addTab(language.translate("Saved IDs:"), null,
                new SavedRemoteIds(language).getTableScroll(), null);

        server_actions.addAction("addRemoteIdConnection", params -> 
        	active_remote_access.addRemoteIdConnection(params));

        server_actions.addAction("removeRemoteIdConnection", params -> 
        	active_remote_access.removeRemoteIdConnection(params));
        
	}

	public Panel getPanel() {

		this
			.fill(GridBagConstraints.BOTH)
			.grid(0,0)
			.weight(1,1)
			.insets(5,0,0,0)
			.gridwidth(2)
			.anchor(GridBagConstraints.CENTER)
			.attach(tab_remote_access, "tab_remote_access");

		return this;
	}
}