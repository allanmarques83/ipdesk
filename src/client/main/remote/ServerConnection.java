package main.remote;

import java.io.*;
import java.net.*;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.Set;

import org.json.JSONObject;

import main.resources.Utils;
import main.gui.Gui;
import main.resources.Constants;
import traffic_model.TrafficModel;

public class ServerConnection extends UserServer
{
    Gui _GUI_COMPONENTS;
    public OutcomingUserAction _OUTCOMING_USER_ACTION;
    public IncomingServerAction _INCOMING_USER_ACTION;

	private Socket socket;
	private ObjectOutputStream buffer_stream;

    private Vector<TrafficModel> OUT_TRAFFIC_QUEUE;


	public ServerConnection(Gui gui_components) {
        _GUI_COMPONENTS = gui_components;
        _INCOMING_USER_ACTION = new IncomingServerAction(this, _GUI_COMPONENTS);
        _OUTCOMING_USER_ACTION = new OutcomingUserAction(this, _GUI_COMPONENTS);

        OUT_TRAFFIC_QUEUE = new Vector<>(1);
	}

	public void establish(String status_message, int seconds_delay) {

		Thread thread = new Thread()
		{
            @Override
			public void run() {
				try {
					int steps = seconds_delay;
					
					while(steps > 0) {
                        Thread.sleep(1000);
                        System.out.println(String.format(getLanguage().translate(status_message), steps));
                        _GUI_COMPONENTS.label_status.setText(
                            String.format(getLanguage().translate(status_message), steps)
                        );
                        _GUI_COMPONENTS.label_status.setForeground(Constants.Colors.red);
						steps--;
					}

					InetAddress server_ip = InetAddress.getByName(getServerIp());
					int server_port = getServerPort();

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
        this.socket.setReceiveBufferSize(getMaxBytesSend());
        this.socket.setSendBufferSize(getMaxBytesSend());

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

                        Method action_method = IncomingServerAction.class.getMethod(
                        	message.getString("action"), TrafficModel.class);

                        action_method.invoke(_INCOMING_USER_ACTION, traffic);
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
            @Override
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
				.put("SYSTEM_VERSION", getSystemVersion())
				.put("SYSTEM_LANGUAGE", getLanguage())
					.toString()
						.getBytes(),
			null);
	}

    public int getOutTrafficQueueSize() {
        return OUT_TRAFFIC_QUEUE.size();
    }

    public void closeSocket() {
        try 
        {
            this.socket.close();
            _GUI_COMPONENTS.button_connect.defEnabled(false);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void serverDownAction() {
        Set<String> remote_ids = getRemoteUsersIds();

        for(String remote_id : remote_ids) {
            _GUI_COMPONENTS.table_remote_users.removeRemoteIdConnection(remote_id);
            removeRemoteUserId(remote_id);
        }
        _GUI_COMPONENTS.button_connect.setButtonConnectionAction(
            "connect_to", "");
        setControledUserId(null);

        establish("Try to establish connection in: [%ds]", 30);

    }
}