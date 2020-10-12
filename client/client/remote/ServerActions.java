package client.remote;

import java.util.List;
import java.util.Set;
import java.util.Date;
import java.awt.Color;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

import traffic_model.TrafficModel;
import client.language.Language;
import client.resources.Utils;
import client.resources.Constants;
import client.configuration.Config;
import client.remote.AttemptConnection;
import client.remote.Connection;
import client.remote.SystemActions;

public class ServerActions
{
	Config config;
	Connection connection;
	Language language;
	SystemActions actions;

	public ServerActions(Connection connection, Language language, Config config, 
SystemActions actions) {
		this.language = language;
		this.connection = connection;
		this.config = config;
		this.actions = actions;
	}

	public void successfulServerEntry(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
        
		this.connection.removeTimeoutFromSocket();
		this.connection.setClientId(message.getString("client_id"));

		actions.getAction("Stage.setRemoteId").accept(new Object[]{
			message.getString("client_id")
		});
		
		actions.getAction("setEnabledButtonConnect").accept(new Object[]{true});

        actions.getAction("setStatusSystem").accept(new Object[]{
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
			actions.getAction("setButtonConnectionAction").accept(
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

		String button_status_action = actions.getData("ButtonConnect")
			.getString("getActionCommand");

		if(button_status_action.equals("cancel_connection")) {
			boolean response = message.getBoolean("response");

			actions.getAction("setButtonConnectionAction").accept(new Object[]{"connect_to"});

			if(!response) {
				return Utils.Error(language.translate("Invalid remote ID!"));
			}

			actions.getAction("addRemoteIdConnection").accept(new Object[]{
				message.getString("sender_id")});
			connection.addRemoteId(message.getString("sender_id"));

			return response;
		}

		return false;
	}

	public boolean closeRemoteIdControled(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		
		String sender_id = message.getString("sender_id");
		Set<String> remote_ids = connection.getRemoteIds();

		if(remote_ids.contains(sender_id)) {
			actions.getAction("removeRemoteIdConnection").accept(new Object[]{sender_id});
			connection.removeRemoteId(sender_id);
		}

		return true;
	}

	public void closeRemoteIdConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = message.getString("sender_id");

		if(connection.getControledId().equals(sender_id)) {
			actions.getAction("setButtonConnectionAction").accept(new Object[]{"connect_to"});
			connection.setControledId(null);
		}
	}

	public void getScreenView(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");

		if(sender_id.equals(connection.getControledId())) {
			actions.getAction("ScreenView.startSendScreen").accept(
				new Object[]{message.getInt("monitor_resolution")});
		}
	}

	public void setScreenView(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");

		if(connection.getRemoteIds().contains(sender_id)) {
			actions.getAction("Screen.screenHandler").accept(new Object[]{
				message,
				Utils.decompressBytes(traffic.getObject())
			});
			System.out.printf("length: %s, send: %s, recieve: %s%n", 
				traffic.getObject().length, 
				message.getString("time"),
				new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()));
		}
	}

	public void stopScreen(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = message.getString("sender_id");

		if(sender_id.equals(connection.getControledId())) {
			actions.getAction("ScreenView.stopSendScreen").accept(null);
		}
		
	}
}