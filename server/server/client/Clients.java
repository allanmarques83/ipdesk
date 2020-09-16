package server.client;

import java.io.*;
import java.net.Socket;
import javafx.collections.ObservableMap;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;

import org.json.JSONArray;
import org.json.JSONObject;

import server.resources.Constants;
import server.resources.Utils;
import server.exceptions.ClientValidationException;
import server.watch.TrafficQueue;

import traffic_model.TrafficModel;

public class Clients {

    private ObservableMap<String, Client> connections;

    private ClientValidation client_validation;
    private TrafficQueue traffic_queue;

    int LIMIT_BYTES_RECIEVE = Constants.LIMIT_BYTES_RECIEVE;

    public Clients() 
    {
        try
        {
            connections = FXCollections.observableHashMap();
            
            client_validation = new ClientValidation(connections);
            traffic_queue = new TrafficQueue(connections);

            addConnectionsListener();
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
                    
                    byte[] buffer_data = Utils.getBufferSocket(socket, 
                        Constants.MAX_BYTES_SEND);

                    TrafficModel traffic = Utils.toTrafficModel(buffer_data);
        
                    client = new Client(socket).build(traffic);

                    client_validation.process(client);
                    
                    connections.put(client.getID(), client);

                    client.sendWelcomeMessage();
                    
                    addClientListener(client);
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

    public void addClientListener(Client client) {
        Thread thread = new Thread()
		{
			public void run() 
            {
				try 
                {
                    BufferedInputStream stream = new BufferedInputStream(
                        client.getSocket().getInputStream(), 20480);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    while(!client.getSocket().isClosed())
                    {
                        TrafficModel traffic = getTraffic(stream, baos);
                        
                        if(traffic == null && baos.size() <= LIMIT_BYTES_RECIEVE)
                            continue;
                        if(traffic == null && baos.size() > LIMIT_BYTES_RECIEVE)
                            throw new Exception("Traffic bytes exceeded!");

                        baos.reset();

                        String traffic_action = processTraffic(traffic);
                        
                        if(traffic_action == null) {
                            traffic = client.sendTraffic(new JSONObject()
                                .put("action", "responseAttemptConnection")
                                    .put("response", false).toString().getBytes(),
                                        null, null, null);

                            traffic_action = "responseAttemptConnection";
                        }

                        traffic_queue.add(new Object[]{
                            client.getID(), 
                                traffic_action, 
                                    traffic});
                    }
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                    connections.remove(client.getID());
                }
            }
        };
        thread.start();
    }

    public void addConnectionsListener() {
        
        connections.addListener(
            (MapChangeListener<String, Client>) CHANGE -> 
            {
                if(CHANGE.wasRemoved()) {
                    Client client = (Client)CHANGE.getValueRemoved();
                    client.closeSocket();
                }
            }
        );
    }

    private TrafficModel getTraffic(BufferedInputStream stream, 
        ByteArrayOutputStream baos) throws Exception {
            
        byte[] buffer_stream = new byte[20480];
        
        int bytesRead = stream.read(buffer_stream,0,20480);
         
        baos.write(buffer_stream, 0, bytesRead);
                        
        return Utils.toTrafficModel(baos.toByteArray());
    }

    private String processTraffic(TrafficModel traffic) throws Exception {
        
        JSONObject message = new JSONObject(new String(traffic.getMessage()));
        System.out.println(message.getString("action"));
        String destination_id = message.getString("destination_id");

        if(connections.containsKey(destination_id)) {
            Client client_destination = connections.get(destination_id);

            client_destination.writeOnSocket(traffic);
            return message.getString("action");
        }
        return null;
    }
}