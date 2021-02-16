package remote;

import client.Client;
import client.ClientValidation;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import resources.Constants;
import resources.Utils;
import traffic_model.TrafficModel;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;


public class Server {
    Logger _LOGGER;

    // key: client_id, value: Client.class
    private ObservableMap<String, Client> _CLIENTS;

    // key: client_controlled_id, value: client_controller_id 
    private ObservableMap<String, String> _CONNECTIONS;

    private TrafficData _TRAFFIC_DATA;

    public Server() {
        try {
            _LOGGER = Logger.getLogger("com.ipdesk");
            
            _CLIENTS = FXCollections.observableHashMap();
            _CONNECTIONS = FXCollections.observableHashMap();

            _TRAFFIC_DATA = new TrafficData(_CLIENTS, _CONNECTIONS);

            addRemovedClientsListener();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void entryPoint(Socket socket) {
       new Thread(() -> 
       {
            try {
                socket.setSoTimeout(7000);
                socket.setReceiveBufferSize(Constants.MAX_BYTES_SEND);

                ObjectInputStream client_stream = new ObjectInputStream(
                    socket.getInputStream());
                
                TrafficModel traffic = Utils.buildTrafficModel(client_stream);

                Client client = new Client(
                    socket, client_id -> _CLIENTS.remove(client_id)
                ).build(traffic);

                new ClientValidation(_CLIENTS, client);
                
                _CLIENTS.put(client.getID(), client);

                client.sendWelcomeMessage();
                
                addClientListener(client_stream, client);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                Utils.closeSocket(socket);
            }
        }).start();
    }

    public void addClientListener(ObjectInputStream client_stream, Client client) {
        new Thread(() -> {
            try 
            {
                while(!client.getSocket().isClosed())
                {
                    TrafficModel traffic = Utils.buildTrafficModel(client_stream);

                    if(traffic == null)
                        continue;

                    _TRAFFIC_DATA.process(traffic, client);
                }                    
                if(_CLIENTS.containsKey(client.getID())) {
                    _LOGGER.info("caiu");
                    throw new Exception(String.format(
                        "%s - client socket is close!", client.getID()
                    ));
                }
            }
            catch(Exception exception) {
                exception.printStackTrace();
                _LOGGER.info("%s - Null traffic model or socket close.");
                _CLIENTS.remove(client.getID());
            }
        }).start();
    }
    
    public void addRemovedClientsListener() {
        _CLIENTS.addListener((MapChangeListener<? super String, ? super Client>) CHANGE -> 
            {
                if(CHANGE.wasRemoved()) {
                    Client client = (Client)CHANGE.getValueRemoved();
                    exitClient(client);
                    client.closeSocket();
                }
            }
        );
    }

    public void exitClient(Client client) {
		_LOGGER.info(String.format("%s - exit client", client.getID()));
		
        if(_CONNECTIONS.containsKey(client.getID())) {
			Client client_destination = _CLIENTS.get(
                _CONNECTIONS.get(client.getID())
            );
			
			client_destination.sendTraffic(new JSONObject()
                .put("action", "closeRemoteIdControled")
                    .put("sender_id", client.getID()).toString().getBytes(),
                        null);
			
            _CONNECTIONS.remove(client.getID());
		}

		if(_CONNECTIONS.containsValue(client.getID())) {
			_CONNECTIONS.entrySet().removeIf(
                map -> closeControledConnection(map, client.getID())
            );
		}
	}

	private boolean closeControledConnection(
        Map.Entry<String,String> map, String client_id) {
		
		if(!map.getValue().equals(client_id))
			return false;

		Client client_destination = _CLIENTS.get(map.getKey());

		client_destination.sendTraffic(new JSONObject()
            .put("action", "closeRemoteIdConnection")
                .put("sender_id", client_id).toString().getBytes(),
                    null);

		return true;
	}
}
