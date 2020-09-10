package client.remote;

import java.io.*;
import java.net.*;
import java.awt.Color;
import java.util.function.Consumer;

import org.json.JSONObject;

import client.configuration.Config;
import client.language.Language;
import client.remote.ClientServer;
import client.resources.Utils;
import traffic_model.TrafficModel;

public class Connection
{
	private Socket socket;
	private BufferedOutputStream buffer_stream;

	private Config config;
	private Language language;
	private ClientServer client;

	public Consumer<Object[]> setStatusSystem;

	public Connection(Config config, Language language) {
		this.config = config;
		this.language = language;

		establish(3, "Try to establish connection in: [%ds]");
	}

	private void establish(int seconds_delay, String status_message) {

		Thread thread = new Thread()
		{
			public void run() {
				try {
					int steps = seconds_delay;
					
					while(steps > 0) {
						Thread.sleep(1000);
						setStatusSystem.accept(new Object[]{ 
							String.format(language.translate(status_message), steps), 
								Color.decode("#FF0000")});
						steps--;
					}

					InetAddress server_ip = InetAddress.getByName(config.getServerIp());
					int server_port = config.getServerPort();

					client = new ClientServer(new Socket(server_ip, server_port), config);

					setServerListener();

					sendClientIdentity();
				}
				catch(Exception exception) {
					// exception.printStackTrace();
					establish(30, "Error to connect! Try to establish connection in: [%ds]");
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
                        client.getSocket().getInputStream(), config.getMaxBytesSend());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    while(true)
                    {
                        TrafficModel traffic = getTraffic(stream, baos);
                        
                        if(traffic == null && baos.size() <= config.getMaxBytesRecieve())
                            continue;
                        if(traffic == null && baos.size() > config.getMaxBytesRecieve())
                            throw new Exception("Traffic bytes exceeded!");

                        JSONObject tmp = new JSONObject(new String(traffic.getMessage()));
                        System.out.println(tmp.getString("instruction"));
                    }
                }
                catch(Exception exception) {
                	exception.printStackTrace();
                	client.closeSocket();
                	establish(30, "Try to establish connection in: [%ds]");
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

	private void sendClientIdentity() {
		client.sendTraffic(
			new JSONObject()
				.put("MAC", client.getClientMac())
				.put("SYSTEM_VERSION", config.getSystemVersion())
				.put("SYSTEM_LANGUAGE", config.getLanguage()),
			null,
			null,
			null);
	}

	public void setStatusSystem(Consumer<Object[]> status_system) {
		setStatusSystem = status_system;
	}
}