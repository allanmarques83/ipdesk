package server.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javafx.collections.ObservableMap;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;

import org.json.JSONArray;
import org.json.JSONObject;

import server.resources.Constants;
import server.resources.Utils;
import server.exceptions.ClientValidationException;
import server.watch.TrafficWatch;

import traffic_model.TrafficModel;

public class Clients {

    private ObservableMap<String, Client> connections;

    private ClientValidation client_validation;
    private TrafficWatch traffic_watch;

    public Clients() 
    {
        try
        {
            connections = FXCollections.observableHashMap();
            
            client_validation = new ClientValidation(connections);
            traffic_watch = new TrafficWatch(connections);

            addRemovedConnectionsListener();
        }
        catch (Exception exception)
		{
            exception.printStackTrace();
        }
    }  

    public void entryPoint(Socket socket) {
        
        Thread thread = new Thread()
        {
            public void run() 
            {
                Client client = null;

				try
				{
                    socket.setSoTimeout(7000);
                    socket.setReceiveBufferSize(Constants.MAX_BYTES_SEND);

                    ObjectInputStream client_stream = new ObjectInputStream(
                        socket.getInputStream());
                    
                    TrafficModel traffic = buildTrafficModel(client_stream, null);

                    if(traffic == null)
                        throw new Exception("null traffic!");
        
                    client = new Client(socket, client_id -> connections.remove(
                        client_id)).build(traffic);

                    client_validation.process(client);
                    
                    connections.put(client.getID(), client);

                    client.sendWelcomeMessage();
                    
                    addClientListener(client_stream, client);
                }
                catch (Exception exception)
				{
                    exception.printStackTrace();
                    Utils.closeSocket(socket);
				}
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void addClientListener(ObjectInputStream client_stream, Client client) {
        Thread thread = new Thread()
		{
			public void run() 
            {
				try 
                {
                    while(!client.getSocket().isClosed())
                    {
                        TrafficModel traffic = buildTrafficModel(client_stream, client.getID());

                        if(traffic == null) 
                            continue;

                        processTraffic(traffic, client);
                    }                    
                    if(connections.containsKey(client.getID()))
                        connections.remove(client.getID());
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                    connections.remove(client.getID());
                }
            }
        };
        thread.start();
    }

    public void addRemovedConnectionsListener() {
        
        connections.addListener(
            (MapChangeListener<String, Client>) CHANGE -> 
            {
                if(CHANGE.wasRemoved()) {
                    Client client = (Client)CHANGE.getValueRemoved();
                    traffic_watch.client_exit(client);
                    client.closeSocket();
                }
            }
        );
    }

    private TrafficModel buildTrafficModel(ObjectInputStream stream, String client_id) {
        try {   
            return (TrafficModel)stream.readUnshared();
        }
        catch (SocketTimeoutException exception) {
            return null;
        }
        catch(Exception exception) {
            connections.remove(client_id);
            return null;
        }
    }

    private void processTraffic(TrafficModel traffic, Client client) {
        
        JSONObject message = new JSONObject(new String(traffic.getMessage()));
        message.put("sender_id", client.getID());
        System.out.println(message.toString());

        String destination_id = message.optString("destination_id", "");
        String action = message.getString("action");

        traffic.setMessage(message.toString().getBytes());

        try {

            if(connections.containsKey(destination_id)) {
                Client client_destination = connections.get(destination_id);
                client_destination.sendTraffic(traffic.getMessage(), traffic.getObject());
            }
            else
                action = action.equals("setRemoteConnection") ? null : action;
        }
        catch (Exception exception) {

        }

         if(action == null) {
            traffic = client.sendTraffic(new JSONObject()
                .put("action", "responseAttemptConnection")
                    .put("destination_id", client.getID())
                        .put("response", false).toString().getBytes(),
                            null);

            action = "responseAttemptConnection";
        }

        traffic_watch.addTraffic(new Object[]{
            traffic,
                action,
                    client.getID()});

    }
}