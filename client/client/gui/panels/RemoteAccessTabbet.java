package client.gui.panels;

import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.Color;

import client.gui.swing.Panel;
import client.gui.tables.ActiveRemoteAccessTable;
import client.gui.tables.SavedRemoteIds;
import client.language.Language;


public class RemoteAccessTabbet extends Panel
{
	JTabbedPane tab_remote_access;

	public RemoteAccessTabbet(Language language) {

		super();

		this.defBackground(Color.decode("#FFFFFF"));

		tab_remote_access = new JTabbedPane();

		tab_remote_access.addTab(language.translate("Connected IDs:"), null,
               new ActiveRemoteAccessTable(language).getTableScroll(), null);

        tab_remote_access.addTab(language.translate("Saved IDs:"), null,
                new SavedRemoteIds(language).getTableScroll(), null);
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