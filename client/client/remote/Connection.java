package client.remote;

import java.io.*;
import java.net.*;
import java.lang.reflect.Method;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.Vector;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.json.JSONObject;

import client.configuration.Config;
import client.language.Language;
import client.remote.ServerActions;
import client.remote.SystemActions;
import client.resources.Utils;
import client.resources.Constants;
import traffic_model.TrafficModel;

public class Connection
{
	private Socket socket;
	private ObjectOutputStream buffer_stream;

	private Config config;
	private Language language;
    private ServerActions server_actions;
    private SystemActions system_actions;

	private String CLIENT_ID, CONTROLED_ID;
    private Set<String> REMOTE_IDS;
    private Vector<TrafficModel> OUT_TRAFFIC_QUEUE;


	public Connection(Config config, Language language) {
		this.config = config;
		this.language = language;

        REMOTE_IDS = new HashSet<>();
        OUT_TRAFFIC_QUEUE = new Vector<>(1);
	}

	public void setActions(ServerActions server_actions, SystemActions system_actions) {
		this.server_actions = server_actions;
        this.system_actions = system_actions;

        this.system_actions
            .addData("Connection", () -> new JSONObject()
                .put("getClientId",getClientId())
                    .put("getControledId", getControledId())
                        .put("getOutTrafficQueueSize", getOutTrafficQueueSize()));
	}

	public void establish(String status_message, int seconds_delay) {

		Thread thread = new Thread()
		{
			public void run() {
				try {
					int steps = seconds_delay;
					
					while(steps > 0) {
						Thread.sleep(1000);
						system_actions.getAction("setStatusSystem").accept(new Object[]{ 
							String.format(language.translate(status_message), steps), 
								Constants.Colors.red});
						steps--;
					}

					InetAddress server_ip = InetAddress.getByName(config.getServerIp());
					int server_port = config.getServerPort();

					setSocketConnection(new Socket(server_ip, server_port));
				    
                    sendClientIdentification();
                	
					setServerListener();

                    setOutTrafficQueue();
                }
				catch(Exception exception) {
					exception.printStackTrace();
					establish("Error to connect! Try to establish connection in: [%ds]", 30);
				}
			}
		};
		thread.start();
	}

    public void setSocketConnection(Socket client_socket) throws Exception
    {
        this.socket = client_socket;

        this.socket.setKeepAlive(true);
        this.socket.setTcpNoDelay(true);
        this.socket.setTrafficClass(0x10);
        this.socket.setSoTimeout(7000);
        this.socket.setPerformancePreferences(0,2,1);
        this.socket.setReceiveBufferSize(this.config.getMaxBytesSend());
        this.socket.setSendBufferSize(this.config.getMaxBytesSend());

        this.buffer_stream = new ObjectOutputStream(this.socket.getOutputStream());
    }

	private void setServerListener() {
		Thread thread = new Thread()
		{
			public void run() 
            {
				try {
                    ObjectInputStream stream = new ObjectInputStream(
                        socket.getInputStream());

                    while(!socket.isClosed())
                    {
                        TrafficModel traffic = getTrafficModel(stream);

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

	private TrafficModel getTrafficModel(ObjectInputStream stream) throws Exception {

        return (TrafficModel)stream.readUnshared();
    }   

	private void setOutTrafficQueue() {
        Thread thread = new Thread()
        {
            public void run() 
            {
                try {
                    while(!socket.isClosed()) {

                        if(OUT_TRAFFIC_QUEUE.isEmpty()) {
                            Utils.loopDelay(1000);
                            continue;
                        }
                        writeOnSocket(OUT_TRAFFIC_QUEUE.get(0));
                        OUT_TRAFFIC_QUEUE.remove(0);
                    }
                }
                catch (Exception exception) {
                    closeSocket();
                    serverDownAction();        
                }
            }
        };
        thread.start();
    }

    private void writeOnSocket(TrafficModel traffic) throws Exception {

        this.buffer_stream.writeUnshared(traffic);
        this.buffer_stream.flush();
    }

    public boolean sendTraffic(byte[] message, byte[] object) 
    {
        OUT_TRAFFIC_QUEUE.add(new TrafficModel()
            .setMessage(message)
            	.setObject(object));

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

    public Set<String> getRemoteIds() {
        return REMOTE_IDS;
    }

    public void addRemoteId(String remote_id) {
        REMOTE_IDS.add(remote_id);
    }

    public void removeRemoteId(String remote_id) {
        REMOTE_IDS.remove(remote_id);
    }

    public int getOutTrafficQueueSize() {
        return OUT_TRAFFIC_QUEUE.size();
    }

    public void closeSocket() {
        try 
        {
            this.socket.close();
            system_actions.getAction("setEnabledButtonConnect")
                .accept(new Object[]{false});

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void serverDownAction() {
        Set<String> remote_ids = getRemoteIds();

        for(String remote_id : remote_ids) {
            system_actions.getAction("removeRemoteIdConnection").accept(
                new Object[]{remote_id});
            removeRemoteId(remote_id);
        }
        system_actions.getAction("setButtonConnectionAction").accept(
            new Object[]{"connect_to"});
        setControledId(null);

        establish("Try to establish connection in: [%ds]", 30);

    }
}