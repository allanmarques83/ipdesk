package client.gui.screen.panels;

import java.util.function.Consumer;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

import client.gui.swing.Panel;
import client.language.Language;
import client.resources.Constants;
import client.gui.screen.panels.ScreenTab;

public class ScreenTabsPanel extends Panel
{
	JTabbedPane tab_remote_ids;
	Map<String, ScreenTab> tabs;

	Consumer<Object[]> set_close_tab;

	public ScreenTabsPanel(Language language) {
		super();

		this.defBackground(Constants.Colors.mercury);

		tab_remote_ids = new JTabbedPane();
		tabs = new HashMap<>();
	}

	public Panel getPanel() {

		this
			.fill(GridBagConstraints.BOTH)
			.grid(0,0)
			.weight(1,1)
			.anchor(GridBagConstraints.CENTER)
			.attach(tab_remote_ids, "tab_remote_ids");

		return this;
	}

	public void addTab(String remote_id) {
		ScreenTab screen_tab = new ScreenTab(remote_id);

		this.tab_remote_ids.addTab(remote_id, null,screen_tab, null);

		this.tab_remote_ids.setTabComponentAt((tab_remote_ids.getTabCount()-1),
			new ScreenTabButtonClose(remote_id, params -> closeTab(remote_id)));

		tabs.put(remote_id, screen_tab);
	}

	public void closeTab(String remote_id) {
		int tab_index = this.tab_remote_ids.indexOfTab(remote_id);
		this.tab_remote_ids.removeTabAt(tab_index);
		tabs.remove(remote_id);

		this.set_close_tab.accept(new Object[]{remote_id,
			this.tab_remote_ids.getTabCount() == 0});
	}

	public void closeAllTabs() {

		for(String remote_id : tabs.keySet()) {

			int tab_index = this.tab_remote_ids.indexOfTab(remote_id);
			this.tab_remote_ids.removeTabAt(tab_index);
			tabs.remove(remote_id);

			this.set_close_tab.accept(new Object[]{remote_id,
				this.tab_remote_ids.getTabCount() == 0});
		}
	}

	public void setCloseTab(Consumer<Object[]> set_close_tab) {
		this.set_close_tab = set_close_tab;
	}

	public void screenHandler(Object[] params) {
		JSONObject message = (JSONObject) params[0];
		byte[] image = (byte[]) params[1];

		String remote_id = message.getString("sender_id");

		ScreenTab screen_tab = tabs.getOrDefault(remote_id, null);

		if(screen_tab != null)
			screen_tab.setImage(image);
	}
}