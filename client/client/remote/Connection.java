package client.remote;

import java.io.*;
import java.net.*;
import java.lang.reflect.Method;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import org.json.JSONObject;
import org.json.JSONArray;

import client.configuration.Config;
import client.language.Language;
import client.remote.ServerActions;
import client.remote.ClientActions;
import client.resources.Utils;
import client.resources.Constants;
import traffic_model.TrafficModel;

public class Connection
{
	private Socket socket;
	private BufferedOutputStream buffer_stream;

	private Config config;
	private Language language;
    private ServerActions server_actions;
    private ClientActions client_actions;

	private String CLIENT_ID, CONTROLED_ID;
    private JSONArray REMOTE_IDS;
    private DefaultListModel<TrafficModel> TRAFFIC_QUEUE;


	public Connection(Config config, Language language) {
		this.config = config;
		this.language = language;

        REMOTE_IDS = new JSONArray();
	}

	public void setActions(ServerActions server_actions,
			ClientActions client_actions) {
		this.server_actions = server_actions;
		this.client_actions = client_actions;
	}

	public void establish(String status_message, int seconds_delay) {

		Thread thread = new Thread()
		{
			public void run() {
				try {
					int steps = seconds_delay;
					
					while(steps > 0) {
						Thread.sleep(1000);
						server_actions.getAction("setStatusSystem").accept(new Object[]{ 
							String.format(language.translate(status_message), steps), 
								Constants.Colors.red});
						steps--;
					}

					InetAddress server_ip = InetAddress.getByName(config.getServerIp());
					int server_port = config.getServerPort();

					setSocketConnection(new Socket(server_ip, server_port));
					
					setServerListener();

					sendClientIdentification();
				}
				catch(Exception exception) {
					// exception.printStackTrace();
					establish("Error to connect! Try to establish connection in: [%ds]", 30);
				}
			}
		};
		thread.start();
	}

	private void setServerListener() {
		Thread thread = new Thread()
		{
			public void run() 
            {
				try 
                {
                    BufferedInputStream stream = new BufferedInputStream(
                        socket.getInputStream(), config.getMaxBytesSend());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    while(!socket.isClosed())
                    {
                        TrafficModel traffic = getTraffic(stream, baos);
                        
                        if(traffic == null && baos.size() <= config.getMaxBytesRecieve())
                            continue;
                        if(traffic == null && baos.size() > config.getMaxBytesRecieve())
                            throw new Exception("Traffic bytes exceeded!");
        				
        				baos.reset();

                        JSONObject message = new JSONObject(
                        	new String(traffic.getMessage()));

        				System.out.println(message.getString("action"));

                        Method action_method = ServerActions.class.getMethod(
                        	message.getString("action"), TrafficModel.class);

                        action_method.invoke(server_actions, traffic);
                    }
                }
                catch(Exception exception) {
                	// exception.printStackTrace();
                	closeSocket();
                    serverDownAction();
                }
            }
        };
        thread.start();
	}

	private TrafficModel getTraffic(BufferedInputStream stream, 
        ByteArrayOutputStream baos) throws Exception {
            
        byte[] buffer_stream = new byte[config.getMaxBytesSend()];
        
        int bytesRead = stream.read(buffer_stream,0,config.getMaxBytesSend());
         
        baos.write(buffer_stream, 0, bytesRead);
                        
        return Utils.toTrafficModel(baos.toByteArray());
    }

    public void setSocketConnection(Socket client_socket) throws Exception
	{
		this.socket = client_socket;

		this.socket.setKeepAlive(true);
		this.socket.setTcpNoDelay(true);
		this.socket.setTrafficClass(0x10);
		this.socket.setSoTimeout(7000);
		this.socket.setPerformancePreferences(0,1,1);
        this.socket.setReceiveBufferSize(this.config.getMaxBytesSend());
        this.socket.setSendBufferSize(this.config.getMaxBytesSend());

        this.buffer_stream = new BufferedOutputStream(this.socket.getOutputStream(),
         	this.config.getMaxBytesSend());

        TRAFFIC_QUEUE = new DefaultListModel<TrafficModel>();
        
        this.addQueueListener();
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

    public boolean sendTraffic(byte[] message, byte[] screen, byte[] file, 
    	byte[] speaker) 
    {
        TRAFFIC_QUEUE.addElement(
            new TrafficModel()
            	.setMessage(message)
            	.setImage(screen)
            	.setFile(file)
            	.setSpeaker(speaker));

        return true;
    }

    public void removeTimeoutFromSocket() {
        try {
            this.socket.setSoTimeout(0);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

	private void sendClientIdentification() {
		this.sendTraffic(
			new JSONObject()
				.put("MAC", Utils.getMacAddress())
				.put("SYSTEM_VERSION", config.getSystemVersion())
				.put("SYSTEM_LANGUAGE", config.getLanguage())
					.toString()
						.getBytes(),
			null,
			null,
			null);
	}

	public void setControledId(String remote_controled_id) {
		CONTROLED_ID = remote_controled_id;
	}

	public String getControledId() {
		return CONTROLED_ID;
	}

    public void setClientId(String remote_client_id) {
        CLIENT_ID = remote_client_id;
    }

    public String getClientId() {
        return CLIENT_ID;
    }

    public JSONArray getRemoteIds() {
        return REMOTE_IDS;
    }

    public void addRemoteId(String remote_id) {
        REMOTE_IDS.put(remote_id);
    }

    public void removeRemoteId(int index) {
        REMOTE_IDS.remove(index);
    }

    public void closeSocket() {
        try 
        {
            this.socket.close();
            server_actions.getAction("setEnabledButtonConnect")
                .accept(new Object[]{false});

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void serverDownAction() {
        List<Object> remote_ids = getRemoteIds().toList();

        for(Object remote_id : remote_ids) {
            server_actions.getAction("removeRemoteIdConnection").accept(
                new Object[]{(String)remote_id});
            removeRemoteId(remote_ids.indexOf((String)remote_id));
        }
        server_actions.getAction("setButtonConnectionAction").accept(
            new Object[]{"connect_to"});
        setControledId(null);

        establish("Try to establish connection in: [%ds]", 30);

    }
}