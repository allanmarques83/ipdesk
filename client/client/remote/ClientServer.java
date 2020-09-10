package client.remote;

import java.io.*;
import java.net.*;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import org.json.JSONObject;

import client.configuration.Config;
import client.resources.Utils;
import traffic_model.TrafficModel;

public class ClientServer
{
	private Socket socket;
	private BufferedOutputStream buffer_stream;

    private DefaultListModel<TrafficModel> TRAFFIC_QUEUE;

	private String MAC_ADDRESS, IP_ADDRESS, OS_SYSTEM;

	public ClientServer(Socket client_socket, Config config) throws Exception
	{
		this.socket = client_socket;

		this.socket.setKeepAlive(true);
		this.socket.setTcpNoDelay(true);
		this.socket.setTrafficClass(0x10);
		this.socket.setSoTimeout(7000);
		this.socket.setPerformancePreferences(0,1,1);
        this.socket.setReceiveBufferSize(config.getMaxBytesSend());
        this.socket.setSendBufferSize(config.getMaxBytesSend());

        this.buffer_stream = new BufferedOutputStream(this.socket.getOutputStream(),
         	config.getMaxBytesSend());

        TRAFFIC_QUEUE = new DefaultListModel<TrafficModel>();
        addQueueListener();

        setOsSystem(Utils.getOsSystem());
        
        setIpAddress(OS_SYSTEM.equals("linux") ? Utils.getLinuxIpAddress() : 
        	Utils.getWindowsIpAddress());
        
        setClientMac(Utils.getMacAddress(IP_ADDRESS));
	}

	private void addQueueListener() {
        TRAFFIC_QUEUE.addListDataListener(new ListDataListener()
        {
            public void intervalAdded(ListDataEvent e)
            {
            	System.out.println(TRAFFIC_QUEUE.get(0).getMessage().length);
                writeOnSocket(TRAFFIC_QUEUE.get(0));
                TRAFFIC_QUEUE.remove(0);
            }
            public void contentsChanged(ListDataEvent e){}
            public void intervalRemoved(ListDataEvent e){}
        });
    }

    private void writeOnSocket(TrafficModel traffic) {
        try
        {
            byte[] buffer = Utils.toByteArray(traffic);

            this.buffer_stream.write(buffer,0,buffer.length);
            this.buffer_stream.flush();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void sendTraffic(JSONObject message,byte[] screen,byte[] file,
        byte[] speaker) 
    {
        TRAFFIC_QUEUE.addElement(
            new TrafficModel()
            	.setMessage(message.toString().getBytes()));
    }

	private void setOsSystem(String os) {
		OS_SYSTEM = os;
	}

	private void setIpAddress(String ipAddress) {
		IP_ADDRESS = ipAddress;
	}

	private void setClientMac(String mac) {
		MAC_ADDRESS= mac;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public String getClientMac() {
		return MAC_ADDRESS;
	}

	public void closeSocket() {
        try 
        {
            this.socket.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}