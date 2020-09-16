package client.remote;

import client.configuration.Config;
import traffic_model.TrafficModel;
import client.language.Language;
import client.gui.dialogs.ConfirmAttemptConnection;

import org.json.JSONObject;

public class AttemptConnection 
{
	Config config;
	Language language;

	public AttemptConnection(Config config) {
		this.config = config;
		this.language = new Language(config.getLanguage());
	}

	public boolean isValid(String controled_id, String sender_id, String password) {

		if(controled_id != null) 
			return false;

		if(!password.equals(config.getPassword()))
			return false;

		if(config.getBlockedsIds().toList().contains(sender_id))
			return false;

		if(config.getTrustedsIds().toList().contains(sender_id))
			return true;

		String confirm_connection = new ConfirmAttemptConnection(
			sender_id, language).build();

		if(confirm_connection.equals("refused"))
			return false;

		if(confirm_connection.equals("block")) {
			
			config.setConfig("blockeds_ids", 
				config.setBlockedsIds(
					config.getBlockedsIds().put(sender_id).toString()
				).toString()
			);

			return false;
		}

		if(confirm_connection.equals("accept"))
			return true;
	
		return false;
	}
}