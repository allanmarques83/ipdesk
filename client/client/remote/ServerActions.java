package client.remote;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.awt.Color;

import org.json.JSONObject;
import org.json.JSONArray;

import traffic_model.TrafficModel;
import client.remote.Connection;
import client.remote.AttemptConnection;
import client.language.Language;
import client.resources.Utils;
import client.resources.Constants;
import client.configuration.Config;

public class ServerActions
{
	Config config;
	Connection connection;
	Language language;

	private Map<String, Consumer<Object[]>> actions;
	private Map<String, Supplier<Object[]>> data;

	public ServerActions(Connection connection, Language language, Config config) {
		this.language = language;
		this.connection = connection;
		this.config = config;

		this.actions = new HashMap<>();
		this.data = new HashMap<>();
	}

	public void addAction(String action_name, Consumer<Object[]> action) {
		actions.put(action_name, action);
	}

	public Consumer<Object[]> getAction(String action_name) {
		return actions.get(action_name);
	}

	public void addData(String data_name, Supplier<Object[]> data) {
		this.data.put(data_name, data);
	}

	public Supplier<Object[]> getData(String data_name) {
		return data.get(data_name);
	}

	public void successfulServerEntry(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
        
		this.connection.removeTimeoutFromSocket();
		this.connection.setClientId(message.getString("client_id"));

		getAction("setRemoteClientId").accept(new Object[]{
			message.getString("client_id")
		});
		
		getAction("setEnabledButtonConnect").accept(new Object[]{true});

        getAction("setStatusSystem").accept(new Object[]{
        	language.translate("Connection successful establish."),
        	Constants.Colors.bright_green
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

		JSONObject message_response = new JSONObject()
			.put("destination_id", sender_id)
				.put("action","responseAttemptConnection")
					.put("response", false);

		boolean is_valid = new AttemptConnection(config)
			.isValid(sender_id, password);

		return connection.sendTraffic(message_response
			.put("response", is_valid)
				.toString()
					.getBytes(), null, null, null);
	}

	public boolean responseAttemptConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String button_status_action = getData("isWaitingForResponse").get()[0].toString();

		if(button_status_action.equals("cancel_connection")) {
			boolean response = message.getBoolean("response");

			getAction("restoreButtonConnect").accept(null);

			if(!response) {
				return Utils.Error(language.translate("Invalid remote ID!"));
			}

			return response;
		}

		return false;
	}

	public void closeServerSocket() {
		this.connection.closeSocket();
		actions.get("setEnabledButtonConnect").accept(new Object[]{false});
	}
}