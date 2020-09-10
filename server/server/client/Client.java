package server.client;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import server.resources.Constants;
import server.resources.Utils;
import traffic_model.TrafficModel;

public class Client {

    Socket socket;

    private String MAC, IP, SYSTEM_LANGUAGE, ID;
    private int PORT;
    private int MAX_BYTES_SEND = Constants.MAX_BYTES_SEND;
    private double SYSTEM_VERSION;

    private DefaultListModel<TrafficModel> TRAFFIC_QUEUE;

    private BufferedOutputStream OUTPUT_DATA;

    public Client(final Socket cli_socket) throws Exception
    {   
        socket = cli_socket;

        TRAFFIC_QUEUE = new DefaultListModel<TrafficModel>();

        socket.setSoTimeout(0);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.setTrafficClass(0x10);
        
        socket.setSendBufferSize(MAX_BYTES_SEND);
        socket.setPerformancePreferences(0,1,1);

        OUTPUT_DATA = new BufferedOutputStream(socket.getOutputStream(),
            MAX_BYTES_SEND);
    }

    public Client build(TrafficModel traffic) throws Exception {

        System.out.println(traffic.getMessage().length);

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

        System.out.printf(
            "New client connected, MAC: %s, ip: %s port: %d%n", MAC, IP, PORT);

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
    
    private void setID(String MAC){
        
        String ident = String.format("%05d", MAC.hashCode()%100000);
        int i = Integer.parseInt(ident);
        
		ID = ((i < 0) ? "-" : "") + String.format("%05d", Math.abs(i));
    }
    
    private void addQueueListener() {
        TRAFFIC_QUEUE.addListDataListener(new ListDataListener()
        {
            public void intervalAdded(ListDataEvent e)
            {
                writeOnSocket(TRAFFIC_QUEUE.get(0));
                TRAFFIC_QUEUE.remove(0);
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
            System.out.printf("closing socket ip: %s port: %d%n", getIp(), getPort());

            socket.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendTraffic(JSONObject message,byte[] screen,byte[] file,
        byte[] speaker) 
    {
        TRAFFIC_QUEUE.addElement(
            new TrafficModel()
            .setMessage(message.toString().getBytes())
            .setImage(screen)
            .setFile(file)
            .setSpeaker(speaker));
    }

    public void writeOnSocket(TrafficModel traffic) {
        try
        {
            byte[] buffer = Utils.toByteArray(traffic);

            OUTPUT_DATA.write(buffer,0,buffer.length);
            OUTPUT_DATA.flush();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void sendWelcomeMessage() {
        sendTraffic(
            new JSONObject()
                .put("instruction", "welcome")
                .put("id", getID())
                .put("limit_bytes_send", MAX_BYTES_SEND)
                .put("server_version", Constants.SERVER_VERSION),
            null,
            null,
            null
        );
    }
}