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
import client.language.Language;
import client.resources.Utils;
import client.resources.Constants;
import client.configuration.Config;
import client.remote.AttemptConnection;
import client.remote.Connection;
import client.services.screen.ScreenView;

public class ServerActions
{
	Config config;
	Connection connection;
	Language language;

	ScreenView screen_view;

	private Map<String, Consumer<Object[]>> actions;
	private Map<String, Supplier<Object[]>> data;

	public ServerActions(Connection connection, Language language, Config config) {
		this.language = language;
		this.connection = connection;
		this.config = config;

		this.actions = new HashMap<>();
		this.data = new HashMap<>();
		screen_view = new ScreenView(connection);
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

	public Object[] getData(String data_name) {
		return data.get(data_name).get();
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

		boolean is_valid_connection = new AttemptConnection(config)
			.isValid(connection.getControledId(), sender_id, password);

		if(is_valid_connection) {
			getAction("setButtonConnectionAction").accept(
				new Object[]{"remote_controled", sender_id});

			connection.setControledId(sender_id);
		}

		return connection.sendTraffic(new JSONObject()
			.put("destination_id", sender_id)
			.put("sender_id", connection.getClientId())
				.put("action","responseAttemptConnection")
					.put("response", is_valid_connection)
						.toString()
							.getBytes(), null);
	}

	public boolean responseAttemptConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String button_status_action = getData("getButtonConnectAcion")[0]
			.toString();

		if(button_status_action.equals("cancel_connection")) {
			boolean response = message.getBoolean("response");

			getAction("setButtonConnectionAction").accept(new Object[]{"connect_to"});

			if(!response) {
				return Utils.Error(language.translate("Invalid remote ID!"));
			}

			getAction("addRemoteIdConnection").accept(new Object[]{
				message.getString("sender_id")});
			connection.addRemoteId(message.getString("sender_id"));

			return response;
		}

		return false;
	}

	public boolean closeRemoteIdControled(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		
		String sender_id = message.getString("sender_id");
		List<Object> remote_ids = connection.getRemoteIds().toList();

		if(remote_ids.contains(sender_id)) {
			getAction("removeRemoteIdConnection").accept(new Object[]{sender_id});
			connection.removeRemoteId(remote_ids.indexOf(sender_id));
		}

		return true;
	}

	public void closeRemoteIdConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = message.getString("sender_id");

		if(connection.getControledId().equals(sender_id)) {
			getAction("setButtonConnectionAction").accept(new Object[]{"connect_to"});
			connection.setControledId(null);
		}
	}

	public void getScreenView(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");

		if(sender_id.equals(connection.getControledId())) {
			screen_view.startSendScreen();
		}
	}

	public void setScreenView(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");

		if(connection.getRemoteIds().toList().contains(sender_id)) {
			System.out.println(traffic.getObject().length);
		}
	}
}