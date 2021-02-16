package client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
import org.json.JSONObject;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import resources.Constants;
import resources.Utils;
import traffic_model.TrafficModel;

public class Client {
    Logger _LOGGER;

    Socket socket;

    private String MAC, IP, ID, SYSTEM_LANGUAGE;
    private int PORT;
    private int MAX_BYTES_SEND = Constants.MAX_BYTES_SEND;
    private double SYSTEM_VERSION;

    private Map<Date, String> LOG_ACTIONS;

    private DefaultListModel<TrafficModel> TRAFFIC_QUEUE;

    private Consumer<String> _REMOVE_CLIENT;

    private ObjectOutputStream OUTPUT_DATA;

    public Client(final Socket cli_socket, Consumer<String> remove_client) throws Exception
    {  
        _LOGGER = Logger.getLogger("com.ipdesk");

        _REMOVE_CLIENT = remove_client;
        socket = cli_socket;

        TRAFFIC_QUEUE = new DefaultListModel<TrafficModel>();
        LOG_ACTIONS = new HashMap<>();

        socket.setSoTimeout(5000);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.setTrafficClass(0x10);
        
        socket.setSendBufferSize(MAX_BYTES_SEND);
        socket.setPerformancePreferences(0,2,1);

        OUTPUT_DATA = new ObjectOutputStream(socket.getOutputStream());
    }

    public Client build(TrafficModel traffic) throws Exception {

        JSONObject client_config = new JSONObject(
            new String(traffic.getMessage())
        );

        setMac(client_config.getString("MAC"));
        setSystemVersion(client_config.getDouble("SYSTEM_VERSION"));
        setSystemLanguage(client_config.getString("SYSTEM_LANGUAGE"));
        setIP(socket.getInetAddress().getHostAddress());
        setPort(socket.getPort());
        setID(MAC);

        addQueueListener();

        _LOGGER.info(String.format(
            "%s - New client connected, MAC: %s, ip: %s port: %d%n", getID(), MAC, IP, PORT)
        );

        return this;
    }

    private void setMac(String mac) {
        MAC = mac;
    }
    
    private void setIP(String ip) {
        IP = ip;
    }
    
    private void setSystemVersion(double version) {
        SYSTEM_VERSION = version; 
    }
    
    private void setSystemLanguage(String language) {
        SYSTEM_LANGUAGE = language;
    }
    
    private void setPort(int port) {
        PORT = port;
    }
    
    private void setID(String MAC) {

        Random r = new Random();
        int n = r.nextInt((99999 - 10000) + 1) + 10000;
        ID = ((n < 0) ? "-" : "") + String.format("%05d", Math.abs(n));
        
  //       String ident = String.format("%05d", MAC.hashCode()%100000);
  //       int i = Integer.parseInt(ident);
        
		// ID = ((i < 0) ? "-" : "") + String.format("%05d", Math.abs(i));
    }
    
    private void addQueueListener() {
        TRAFFIC_QUEUE.addListDataListener(new ListDataListener()
        {
            public void intervalAdded(ListDataEvent e)
            {
                try {
                    writeOnSocket(TRAFFIC_QUEUE.get(0));
                    TRAFFIC_QUEUE.remove(0);
                }
                catch(Exception exception) {
                    _REMOVE_CLIENT.accept(getID());
                }
            }
            public void contentsChanged(ListDataEvent e){}
            public void intervalRemoved(ListDataEvent e){}
        });
    }
    
    public Socket getSocket() {
        return socket;
    }

    public String getMac() {
        return MAC;
    }
    
    public String getIp() {
        return IP;
    }
    
    public int getPort() {
        return PORT;
    }
    
    public String getID() {
        return ID;
    }

    public void closeSocket() {
        try {
            _LOGGER.info(String.format(
                "%s - closing socket", getID()
            ));

            socket.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public TrafficModel sendTraffic(byte[] message, byte[] object) {

        TrafficModel traffic = new TrafficModel()
            .setMessage(message)
                .setObject(object);

        TRAFFIC_QUEUE.addElement(traffic);

        return traffic;
    }

    public void writeOnSocket(TrafficModel traffic) throws Exception {
        OUTPUT_DATA.writeUnshared(traffic);        
    }

    public void sendWelcomeMessage() {
        sendTraffic(
            new JSONObject()
                .put("action", "successfulServerEntry")
                    .put("client_id", getID())
                        .put("limit_bytes_send", MAX_BYTES_SEND)
                        .put("server_version", Constants.SERVER_VERSION)
                            .toString().getBytes(),
                                null);
    }

    public Map<Date, String> getLogActions() {
        return LOG_ACTIONS;
    }

    public void setLogAction(String action) {
        LOG_ACTIONS.put(new Date(), action);
    }

    public void cleanLogClientByTime() {
        Date fifteen_minutes_ago = Utils.getPastDate(new Date(), 15);

        LOG_ACTIONS = LOG_ACTIONS.entrySet()
            .stream()
                .filter(map -> map.getKey().after(fifteen_minutes_ago))
                    .collect(Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue));
        
        ClientValidation.refreshBlackList(fifteen_minutes_ago);
    }

    public List<String> getLogActionList(String action) {
        return LOG_ACTIONS.values()
                .stream()
                    .filter(key -> key.equals(action))
                        .collect(Collectors.toList());
    }
}