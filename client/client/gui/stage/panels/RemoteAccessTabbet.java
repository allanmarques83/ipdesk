package client.gui.stage.panels;

import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.Color;

import client.gui.swing.Panel;
import client.gui.stage.tables.ActiveRemoteAccessTable;
import client.gui.stage.tables.SavedRemoteIds;
import client.language.Language;
import client.resources.Constants;
import client.remote.SystemActions;

public class RemoteAccessTabbet extends Panel
{
	JTabbedPane tab_remote_access;

	ActiveRemoteAccessTable active_remote_access;

	public RemoteAccessTabbet(Language language, SystemActions system_actions) {
		super();

		active_remote_access = new ActiveRemoteAccessTable(language, 
			system_actions.getAction("closeRemoteIdConnection"),
			system_actions.getAction("getScreenView"));

		this.defBackground(Constants.Colors.white);

		tab_remote_access = new JTabbedPane();

		tab_remote_access.addTab(language.translate("Connected IDs:"), null,
               active_remote_access.getTableScroll(), null);

        tab_remote_access.addTab(language.translate("Saved IDs:"), null,
                new SavedRemoteIds(language).getTableScroll(), null);

        system_actions.addAction("addRemoteIdConnection", params -> 
        	active_remote_access.addRemoteIdConnection(params));

        system_actions.addAction("removeRemoteIdConnection", params -> 
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