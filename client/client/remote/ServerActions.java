package client.remote;

import java.util.function.Consumer;
import java.util.Map;
import java.util.HashMap;
import java.awt.Color;

import org.json.JSONObject;

import traffic_model.TrafficModel;
import client.remote.Connection;
import client.language.Language;
import client.resources.Utils;
import client.configuration.Config;
import client.gui.dialogs.Dialogs;

public class ServerActions
{
	Config config;
	Connection connection;
	Language language;

	private Map<String, Consumer<Object[]>> actions;

	public ServerActions(Connection connection, Language language, Config config) {
		this.language = language;
		this.connection = connection;
		this.config = config;

		this.actions = new HashMap<>();
	}

	public void add(String action_name, Consumer<Object[]> action) {
		actions.put(action_name, action);
	}

	public Consumer<Object[]> get(String action_name) {
		return actions.get(action_name);
	}

	public void successfulServerEntry(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
        
		this.connection.removeTimeoutFromSocket();
		this.connection.setClientId(message.getString("client_id"));

		actions.get("setRemoteClientId").accept(new Object[]{
			message.getString("client_id")
		});
		
		actions.get("setEnabledButtonConnect").accept(new Object[]{true});

        actions.get("setStatusSystem").accept(new Object[]{
        	language.translate("Connection successful establish."),
        	Color.decode("#66FF00")
        });
	}

	public void showMessageError(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		Utils.Error(language.translate(message.getString("error")));
	}

	public boolean setRemoteConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String password = message.getString("password");
		String sender_id = message.getString("sender_id");

		byte[] message_refused = new JSONObject()
			.put("destination_id", sender_id)
				.put("action","showMessageError")
					.put("error", "Invalid remote ID!")
						.toString()
							.getBytes();

		if(!password.equals(config.getPassword())) {
			return connection.sendTraffic(message_refused, null, null, null);
		}

		String confirm_connection = Dialogs.confirmConnection(sender_id, language);

		if(confirm_connection.equals("refused")) {
			return connection.sendTraffic(message_refused, null, null, null);
		}
		return true;

	}

	public void closeServerSocket() {
		this.connection.closeSocket();
		actions.get("setEnabledButtonConnect").accept(new Object[]{false});
	}
}