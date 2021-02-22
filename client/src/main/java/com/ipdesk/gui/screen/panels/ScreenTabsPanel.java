package gui.screen.panels;

import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JTabbedPane;
import org.json.JSONObject;

import gui.swing.Panel;
import remote.ServerConnection;
import resources.Constants;
import services.screen.keyboard.KeyboardEventScreen;

public class ScreenTabsPanel extends Panel
{
	JTabbedPane tab_remote_ids;
	Map<String, ScreenTab> tabs;

	ServerConnection _SERVER_CONNECTION;

	Consumer<Object[]> set_close_tab;

	public ScreenTabsPanel(ServerConnection server_connection) {
		super();

		_SERVER_CONNECTION = server_connection;

		this.defBackground(Constants.Colors.mercury);

		tab_remote_ids = new JTabbedPane();
		tab_remote_ids.addKeyListener(
			new KeyboardEventScreen(params -> keyboardEvent(params))
		);
		tabs = new HashMap<>();
	}

	public Panel getPanel() {

		this
			.fill(GridBagConstraints.BOTH)
			.grid(0,0)
			.weight(1,1)
			.anchor(GridBagConstraints.NORTH)
			.attach(tab_remote_ids, "tab_remote_ids");

		return this;
	}

	public void addTab(String remote_id) {
		ScreenTab screen_tab = new ScreenTab(remote_id, _SERVER_CONNECTION);

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

	public void screenHandler(JSONObject message, byte[] image) {
		String remote_id = message.getString("sender_id");

		ScreenTab screen_tab = tabs.getOrDefault(remote_id, null);

		if(screen_tab != null)
			screen_tab.setImage(image);
	}

	public void keyboardEvent(Object[] args) {
		String remote_id = tab_remote_ids.getTitleAt(
			tab_remote_ids.getSelectedIndex()
		);

		_SERVER_CONNECTION._OUTCOMING_USER_ACTION.keyboardEvent(
			remote_id,
			args[0].toString(),
			(int)args[1]
		);
	}

	public void setTabbetFocus() {
		tab_remote_ids.grabFocus();
	}
}