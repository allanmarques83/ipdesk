package client.remote;

import org.json.JSONObject;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

import client.language.Language;
import client.configuration.Config;
import client.remote.Connection;
import client.remote.SystemActions;
import client.resources.Utils;

public class ClientActions
{
	Connection connection;
	SystemActions system_actions;
	Language language;
	
	private Map<String, Consumer<Object[]>> actions;

	public ClientActions(Connection connection, Language language, 
SystemActions system_actions) {
		this.connection = connection;
		this.language = language;
		this.system_actions = system_actions;

		this.system_actions
			.addAction("connect_to", params -> connect_to(params))
			.addAction("getScreenView", params -> getScreenView(params))
			.addAction("closeControledId", params -> closeControledId())
			.addAction("closeRemoteIdConnection", params -> closeRemoteIdConnection(params))
			.addAction("sendScreen", params -> sendScreen(params))
			.addAction("stopScreen", params -> stopScreen(params));
	}

	public boolean connect_to(Object[] params) {

		String remote_id = (String) params[0];
		String password = (String) params[1];
		
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "setRemoteConnection")
					.put("destination_id", remote_id)
						.put("password", password).toString().getBytes(),
							null);

		return true;
	}

	public void getScreenView(Object[] params) {
		this.system_actions.getAction("Screen.newScreen").accept(new Object[]{
			params[0].toString()
		});
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "getScreenView")
					.put("destination_id", params[0].toString())
						.put("monitor_resolution", (int) params[1])
							.toString().getBytes(),
								null);
	}

	public void sendScreen(Object[] params) {
		this.connection.sendTraffic(new JSONObject()
			.put("destination_id", connection.getControledId())
				.put("action", "setScreenView")
					.put("time", new Date().toString())
						.toString().getBytes(), (byte[])params[0]);
	}

	public void stopScreen(Object[] params) {
		this.connection.sendTraffic(new JSONObject()
			.put("destination_id", params[0].toString())
				.put("action", "stopScreen")
					.toString().getBytes(), null);
	}

	public void closeControledId() {

		String controled_id = connection.getControledId();
		connection.setControledId(null);
		
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "closeRemoteIdControled")
					.put("destination_id", controled_id)
						.toString().getBytes(),
							null);

	}

	public void closeRemoteIdConnection(Object[] params) {
		this.connection.removeRemoteId((String) params[0]);
		
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "closeRemoteIdConnection")
					.put("destination_id", params[0].toString())
						.toString().getBytes(),
							null);
	}
}