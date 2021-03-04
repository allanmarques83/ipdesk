package remote;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import configuration.Language;
import gui.Gui;

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

	public void setScreenResolution(String remote_id, int resolution) {
		_GUI_COMPONENTS.screen_frame._SCREEN_TABS.setTabbetFocus();

		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
				.put("action", "setScreenResolution")
					.put("resolution", resolution)
						.toString().getBytes(), null);
	}
	
	public void setScreenQuality(String remote_id, String quality) {
		_GUI_COMPONENTS.screen_frame._SCREEN_TABS.setTabbetFocus();

		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
				.put("action", "setScreenQuality")
					.put("quality", quality.equals("Normal") ? 0.4f : 0.7f)
						.toString().getBytes(), null);
	}

	public void mouseScreenEvent(
		String remote_id, 
		String mouse_event_type, 
		int from_x, 
		int from_y, 
		int to_x, 
		int to_y, 
		int button_mask,
		int screen_view_width,
		int screen_view_height
	) {
		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
			.put("action", "mouseScreenEvent")
			.put("mouse_event_type", mouse_event_type)
			.put("from_x", from_x)
			.put("from_y", from_y)
			.put("to_x", to_x)
			.put("to_y", to_y)
			.put("button_mask", button_mask)
			.put("screen_view_width", screen_view_width)
			.put("screen_view_height", screen_view_height)
			.toString().getBytes(), null);
	}

	public void keyboardEvent(String remote_id, String keyboard_event, int key_code) {
		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
				.put("action", "keyboardEvent")
					.put("keyboard_event", keyboard_event)
						.put("key_code", key_code).toString().getBytes(), null);
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

	public void getControledUserDrives(String remote_id) {
		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
				.put("action", "getControledUserDrives")
					.toString().getBytes(), null);
	}

	public void getControledUserDirectory(String remote_id, String path_dir) {
		_SERVER_CONNECTION.sendTraffic(new JSONObject()
			.put("destination_id", remote_id)
				.put("action", "getControledUserDirectory")
					.put("path_dir", path_dir)
						.toString().getBytes(), null);
	}
}