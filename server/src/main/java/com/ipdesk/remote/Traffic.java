package remote;

import java.util.*;
import javafx.collections.ObservableMap;
import org.json.JSONObject;

import client.Client;
import client.ClientValidation;
import traffic_model.TrafficModel;

public class Traffic
{
	// key: client_id, value: Client.class
    private ObservableMap<String, Client> _CLIENTS;
    private ObservableMap<String, String> _CONNECTIONS;

	public Traffic(
		ObservableMap<String, Client> clients, ObservableMap<String, String> connections
	) {
		_CLIENTS = clients;
		_CONNECTIONS = connections;
	}

	public void setTraffic(TrafficModel taffic_model, Client client, String action) {
		if(action.equals("showMessageError")) {
			showMessageError(client);
		}
		else if(action.equals("setRemoteConnection")) {
			setRemoteConnection(taffic_model);
		}
		else if(action.equals("responseAttemptConnection")) {
			responseAttemptConnection(taffic_model);
		}
	}

	private void showMessageError(Client client) {
		ClientValidation.addBlackList(client.getMac());
		_CLIENTS.remove(client.getID());
	}

	private void setRemoteConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = message.getString("sender_id");
		String destination_id = message.getString("destination_id");

		Client client = _CLIENTS.get(sender_id);
		client.setLogAction(String.format("setRemoteConnection(%s)", destination_id));
	}

	private boolean responseAttemptConnection(TrafficModel traffic) {
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = message.getString("sender_id");
		String destination_id = message.getString("destination_id");
		boolean response = message.getBoolean("response");

		if(!response) {

			Client client = _CLIENTS.get(destination_id);
			
			if(!sender_id.equals(destination_id)) {
				boolean has_attempt_connection = !client.getLogActionList(
					String.format("setRemoteConnection(%s)", sender_id)).isEmpty();

				if(!has_attempt_connection) {
					ClientValidation.addBlackList(_CLIENTS.get(sender_id).getMac());
					_CLIENTS.remove(sender_id);
					return false;
				}
			}
			
			client.setLogAction("falseAttemptConnection");
			client.cleanLogClientByTime();

			List<String> false_attempt_connection_list = client
				.getLogActionList("falseAttemptConnection");

			if(false_attempt_connection_list.size() > 5) {
				ClientValidation.addBlackList(client.getMac());
				_CLIENTS.remove(destination_id);
			}
			return false;
		}
		System.out.print("ok responseAttemptConnection");
		_CONNECTIONS.put(sender_id, destination_id);

		return true;
	}
}