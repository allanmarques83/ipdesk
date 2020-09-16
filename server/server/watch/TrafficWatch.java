package server.watch;

import java.util.*;
import java.util.function.Consumer;
import javafx.collections.ObservableMap;
import org.json.JSONObject;

import server.resources.Utils;
import server.client.Client;
import traffic_model.TrafficModel;

public class TrafficWatch
{
	ObservableMap<String, Client> connections;
	Map<String, Consumer<Object[]>> actions_map;

	public TrafficWatch(ObservableMap<String, Client> connections) {
		this.connections = connections;

		actions_map = new HashMap<>();

		actions_map.put("responseAttemptConnection", params -> responseAttemptConnection(params));
	}

	public void process(Object[] params) {
		String action = (String)params[1];
		TrafficModel traffic = (TrafficModel)params[2];

		actions_map.getOrDefault(action, null).accept(params);
	}

	private void responseAttemptConnection(Object[] params) {
		String client_id = (String)params[0];
		TrafficModel traffic = (TrafficModel)params[2];
		
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		boolean response = message.getBoolean("response");

		if(!response) {
			Client client = connections.get(client_id);
			client.setLogAction("falseAttemptConnection");

			Date fifteen_minutes_ago = Utils.getPastDate(new Date(), 15);

			client.cleanLogClientByTime(fifteen_minutes_ago);

			List<String> list_false_attempt_connection = client
				.getLogActionList("falseAttemptConnection");

			if(list_false_attempt_connection.size() > 5) {}

			// Map<Date, String> client_log = client.getLogActions();		
		}
	}

}