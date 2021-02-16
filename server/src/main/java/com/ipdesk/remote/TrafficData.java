package remote;

import java.util.logging.Logger;

import org.json.JSONObject;

import javafx.collections.ObservableMap;

import traffic_model.TrafficModel;
import client.Client;

public class TrafficData extends Traffic
{
    Logger _LOGGER;

    // key: client_id, value: Client.class
    private ObservableMap<String, Client> _CLIENTS;

    public TrafficData(
        ObservableMap<String, Client> clients, ObservableMap<String, String> connections
    )
    {
        super(clients, connections);

        _LOGGER = Logger.getLogger("com.ipdesk");
            
        _CLIENTS = clients;
    }

    public void process(
        TrafficModel traffic, Client client
    ) throws Exception 
    {
        JSONObject message = new JSONObject(new String(traffic.getMessage()));
        message.put("sender_id", client.getID());

        _LOGGER.info(String.format(
            "%s - ", message.toString())
        );

        String destination_id = message.optString("destination_id", "");
        String action = message.getString("action");

        traffic.setMessage(message.toString().getBytes());

        if(_CLIENTS.containsKey(destination_id)) {
            Client client_destination = _CLIENTS.get(destination_id);
            client_destination.sendTraffic(
                traffic.getMessage(), traffic.getObject()
            );
        }
        else
            action = action.equals("setRemoteConnection") ? null : action;

         if(action == null) {
            traffic = client.sendTraffic(new JSONObject()
                .put("action", "responseAttemptConnection")
                    .put("destination_id", client.getID())
                        .put("sender_id", client.getID())
                            .put("response", false).toString().getBytes(),
                                null);

            action = "responseAttemptConnection";
        }
        setTraffic(traffic, client, action);
    }
}
