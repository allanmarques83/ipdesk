package client.remote;

import org.json.JSONObject;

import client.language.Language;
import client.configuration.Config;
import client.remote.Connection;
import client.resources.Utils;

public class ClientActions
{
	Connection connection;
	Language language;

	public ClientActions(Connection connection, Language language) {
		this.connection = connection;
		this.language = language;
	}

	public boolean connect_to(String remote_id, String password) {
		
		if(remote_id.replace(language.translate("Remote ID"),"").length() < 5) {
			return Utils.Error(language.translate("Invalid remote ID!"));
		}

		if(password.replace(language.translate("Password"),"").length() < 8) {
			return Utils.Error(language.translate("Invalid password!"));
		}

		if(remote_id.equals(connection.getClientId())) {
			return Utils.Error(language.translate("Self connection error!"));
		}

		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "setRemoteConnection")
					.put("destination_id", remote_id)
						.put("password", password).toString().getBytes(),
							null);

		return true;
	}

	public void getScreenView(Object[] params) {
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "getScreenView")
					.put("destination_id", params[0].toString())
						.toString().getBytes(),
							null);
	}


	public void closeControledId() {

		connection.setControledId(null);
		
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "closeRemoteIdControled")
					.put("destination_id", connection.getControledId())
						.toString().getBytes(),
							null);

	}

	public void closeRemoteIdConnection(Object[] params) {
		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "closeRemoteIdConnection")
					.put("destination_id", params[0].toString())
						.toString().getBytes(),
							null);
	}
}