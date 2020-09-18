package server.watch;

import java.util.*;
import java.util.function.Consumer;
import javafx.collections.ObservableMap;
import javafx.collections.FXCollections;
import org.json.JSONObject;

import server.resources.Utils;
import server.client.Client;
import server.client.ClientValidation;
import server.watch.TrafficQueue;
import traffic_model.TrafficModel;

public class TrafficWatch
{
	private TrafficQueue queue;
	private ObservableMap<String, Client> connections;
    private ObservableMap<String, String> sessions;

	Map<String, Consumer<Object[]>> actions_map;

	public TrafficWatch(ObservableMap<String, Client> connections) {
		this.connections = connections;

		this.sessions = FXCollections.observableHashMap();

		queue = new TrafficQueue(params -> process(params));

		actions_map = new HashMap<>();

		actions_map.put("responseAttemptConnection", params -> responseAttemptConnection(params));
		actions_map.put("setRemoteConnection", params -> setRemoteConnection(params));
		actions_map.put("showMessageError", params -> showMessageError(params));
	}

	public void addTraffic(Object[] params) {
		queue.add(params);
	}

	public void process(Object[] params) {
		String action = (String)params[1];

		actions_map.getOrDefault(action, values -> discard(values)).accept(params);
	}

	private void showMessageError(Object[] params) {
		String sender_id = (String)params[2];
		Client client = connections.get(sender_id);
		ClientValidation.addBlackList(client.getMac());
		connections.remove(sender_id);
	}

	private void setRemoteConnection(Object[] params) {
		TrafficModel traffic = (TrafficModel)params[0];
		
		JSONObject message = new JSONObject(new String(traffic.getMessage()));
		String sender_id = (String)params[2];
		String destination_id = message.getString("destination_id");

		Client client = connections.get(sender_id);
		client.setLogAction(String.format("setRemoteConnection(%s)", destination_id));
	}

	private boolean responseAttemptConnection(Object[] params) {
		
		TrafficModel traffic = (TrafficModel)params[0];
		
		JSONObject message = new JSONObject(new String(traffic.getMessage()));

		String sender_id = (String)params[2];
		String destination_id = message.getString("destination_id");
		boolean response = message.getBoolean("response");

		if(!response) {

			Client client = connections.get(destination_id);
			
			if(!sender_id.equals(destination_id)) {
				boolean has_attempt_connection = !client.getLogActionList(
					String.format("setRemoteConnection(%s)", sender_id)).isEmpty();

				if(!has_attempt_connection) {
					ClientValidation.addBlackList(connections.get(sender_id).getMac());
					connections.remove(sender_id);
					return false;
				}
			}
			
			client.setLogAction("falseAttemptConnection");

			Date fifteen_minutes_ago = Utils.getPastDate(new Date(), 15);

			client.cleanLogClientByTime(fifteen_minutes_ago);

			List<String> false_attempt_connection_list = client
				.getLogActionList("falseAttemptConnection");

			ClientValidation.refreshBlackList(fifteen_minutes_ago);
			
			if(false_attempt_connection_list.size() > 5) {
				ClientValidation.addBlackList(client.getMac());
				connections.remove(destination_id);
			}
			return false;
		}

		sessions.put(sender_id, destination_id);

		return true;
	}

	public void client_exit(Client client) {
		if(sessions.containsKey(client.getID())) {
			Client client_destination = connections.get(sessions.get(client.getID()));
			
			client_destination.sendTraffic(new JSONObject()
                                .put("action", "closeRemoteIdControled")
                                    .put("sender_id", client.getID()).toString().getBytes(),
                                        null, null, null);
			
			sessions.remove(client.getID());
		}

		if(sessions.containsValue(client.getID())) {
			sessions.entrySet().removeIf(map -> notifyControledClients(map, client.getID()));
		}
	}

	private boolean notifyControledClients(Map.Entry<String,String> map, String client_id) {
		
		if(!map.getValue().equals(client_id))
			return false;

		Client client_destination = connections.get(map.getKey());

		client_destination.sendTraffic(new JSONObject()
            .put("action", "closeRemoteIdConnection")
                .put("sender_id", client_id).toString().getBytes(),
                    null, null, null);

		return true;
	}

	private void discard(Object[] values) {

	}

}