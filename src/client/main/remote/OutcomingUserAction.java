package main.remote;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import main.configuration.Language;
import main.gui.Gui;

public class OutcomingUserAction
{
	ServerConnection _SERVER_CONNECTION;
	Gui _GUI_COMPONENTS;
	Language _LANGUAGE;
	
	public OutcomingUserAction(ServerConnection server_connection, Gui gui_components) {
		_SERVER_CONNECTION = server_connection;
		_GUI_COMPONENTS = gui_components;
		_LANGUAGE = server_connection.getLanguage();
	}

	public boolean connectTo(String remote_id, String password) {

		_SERVER_CONNECTION.sendTraffic(
			new JSONObject()
				.put("action", "setRemoteConnection")
					.put("destination_id", remote_id)
						.put("password", password).toString().getBytes(),
							null);

		return true;
	}

	public void getScreenView(String remote_id, int monitor_resolution) {
		_GUI_COMPONENTS.screen_frame.newScreen(remote_id);
		_SERVER_CONNECTION.sendTraffic(
			new JSONObject()
				.put("action", "getScreenView")
					.put("destination_id", remote_id)
						.put("monitor_resolution", monitor_resolution)
							.toString().getBytes(),
								null);
	}

	public void sendScreen(byte[] screen) {
		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", _SERVER_CONNECTION.getControledUserId())
				.put("action", "setScreenView")
					.put("time", new SimpleDateFormat("hh:mm:ss.SSS").format(new Date()))
						.toString().getBytes(), screen);
	}

	public void stopScreen(String remote_id) {
		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
				.put("action", "stopScreen")
					.toString().getBytes(), null);
	}

	public void closeControledId() {

		String controled_id = _SERVER_CONNECTION.getControledUserId();
		_SERVER_CONNECTION.setControledUserId(null);
		
		_SERVER_CONNECTION.sendTraffic(
			new JSONObject()
				.put("action", "closeRemoteIdControled")
					.put("destination_id", controled_id)
						.toString().getBytes(),
							null);

	}

	public void closeRemoteIdConnection(String remote_id) {
		_SERVER_CONNECTION.removeRemoteUserId(remote_id);
		
		_SERVER_CONNECTION.sendTraffic(
			new JSONObject()
				.put("action", "closeRemoteIdConnection")
					.put("destination_id", remote_id)
						.toString().getBytes(),
							null);
	}
}