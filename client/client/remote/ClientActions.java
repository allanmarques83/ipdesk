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

		// if(remote_id.equals(connection.getClientId())) {
		// 	return Utils.Error(language.translate("Self connection error!"));
		// }

		this.connection.sendTraffic(
			new JSONObject()
				.put("action", "setRemoteConnection")
				.put("destination_id", remote_id)
				.put("password", password)
				.put("sender_id", connection.getClientId()).toString().getBytes(),
				null, null, null);

		return true;
	}
}