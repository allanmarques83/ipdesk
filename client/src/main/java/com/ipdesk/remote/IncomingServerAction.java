package remote;

import java.util.Set;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

import traffic_model.TrafficModel;
import resources.Utils;
import resources.Constants;
import configuration.Language;
import gui.Gui;


public class IncomingServerAction
{
	ServerConnection _SERVER_CONNECTION;
	Language _LANGUAGE;
	Gui _GUI_COMPONENTS;

	public IncomingServerAction(ServerConnection server_connection, Gui gui_components) {
		_SERVER_CONNECTION = server_connection;
		_LANGUAGE = _SERVER_CONNECTION.getLanguage();
		_GUI_COMPONENTS = gui_components;
	}

	public void successfulServerEntry(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
        
		_SERVER_CONNECTION.removeTimeoutFromSocket();
		_SERVER_CONNECTION.setUserId(message.getString("client_id"));

		_GUI_COMPONENTS.textfield_client_id.setText(message.getString("client_id"));
		_GUI_COMPONENTS.button_connect.setEnabled(true);
		_GUI_COMPONENTS.label_status.setText(
			_LANGUAGE.translate("Connection successful establish.")
		);
		_GUI_COMPONENTS.label_status.setForeground(Constants.Colors.bright_green);
	}

	public void showMessageError(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		Utils.Error(_LANGUAGE.translate(message.getString("error")));
	}

	public boolean setRemoteConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String password = message.getString("password");
		String sender_id = message.getString("sender_id");

		boolean is_valid_connection = new AttemptConnection(
			(UserServer)_SERVER_CONNECTION).isValid(
				_SERVER_CONNECTION.getControledUserId(), sender_id, password);

		if(is_valid_connection) {
			_GUI_COMPONENTS.button_connect.setButtonConnectionAction("disconnect_remote_user", sender_id);
			_SERVER_CONNECTION.setControledUserId(sender_id);
		}

		return _SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", sender_id)
			.put("sender_id", _SERVER_CONNECTION.getUserId())
				.put("action","responseAttemptConnection")
					.put("response", is_valid_connection)
						.toString()
							.getBytes(), null);
	}

	public boolean responseAttemptConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String button_status_action = _GUI_COMPONENTS.button_connect.getActionCommand();

		if(button_status_action.equals("cancel_connection")) {
			boolean response = message.getBoolean("response");

			_GUI_COMPONENTS.button_connect.setButtonConnectionAction("connect_to", "");

			if(!response) {
				return Utils.Error(_LANGUAGE.translate("Invalid remote ID!"));
			}
			_GUI_COMPONENTS.table_remote_users.addRemoteIdConnection(
				message.getString("sender_id")
			);
			_SERVER_CONNECTION.addRemoteUserId(message.getString("sender_id"));

			return response;
		}

		return false;
	}

	public boolean closeRemoteIdControled(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		
		String sender_id = message.getString("sender_id");
		Set<String> remote_ids = _SERVER_CONNECTION.getRemoteUsersIds();

		if(remote_ids.contains(sender_id)) {
			_GUI_COMPONENTS.table_remote_users.removeRemoteIdConnection(sender_id);
			_SERVER_CONNECTION.removeRemoteUserId(sender_id);
		}

		return true;
	}

	public void closeRemoteIdConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = message.getString("sender_id");

		if(_SERVER_CONNECTION.getControledUserId().equals(sender_id)) {
			_GUI_COMPONENTS.button_connect.setButtonConnectionAction(
				"connect_to", ""
			);
			_SERVER_CONNECTION.setControledUserId(null);
		}
	}

	public void getScreenView(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");

		if(sender_id.equals(_SERVER_CONNECTION.getControledUserId())) {
			_GUI_COMPONENTS.screen_frame._SCREEN_VIEW.startSendScreen(
				message.getInt("monitor_resolution")
			);
		}
	}

	public void setScreenView(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");

		if(_SERVER_CONNECTION.getRemoteUsersIds().contains(sender_id)) {
			_GUI_COMPONENTS.screen_frame._SCREEN_TABS.screenHandler(
				message, Utils.decompressBytes(traffic.getObject())
			);

			System.out.printf("length: %s, send: %s, recieve: %s%n", 
				traffic.getObject().length, 
				message.getString("time"),
				new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()));
		}
	}

	public void stopScreen(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = message.getString("sender_id");

		if(sender_id.equals(_SERVER_CONNECTION.getControledUserId())) {
			_GUI_COMPONENTS.screen_frame._SCREEN_VIEW.stopSendScreen();
		}
	}
}