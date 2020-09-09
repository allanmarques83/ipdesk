import java.io.*;
import java.net.*;
import javax.net.ssl.*;

import traffic_model.TrafficModel;
import org.json.JSONObject;

public class MainClient {
    
    Socket SOCK;
    BufferedOutputStream OUTPUT;

    public MainClient()
    {
        connect();
    }

    public void connect()
	{
		Thread th = new Thread()
		{
			public void run() 
            {
                try {
                    SOCK = new Socket(InetAddress.getByName("127.0.1.1"), 1527);
                        
                    SOCK.setKeepAlive(true);
                    SOCK.setTcpNoDelay(true);
                    SOCK.setTrafficClass(0x10);
                    SOCK.setSoTimeout(7000);
                    SOCK.setPerformancePreferences(0,1,1);
                    SOCK.setReceiveBufferSize(20480);
                    SOCK.setSendBufferSize(20480);

                    OUTPUT = new BufferedOutputStream(SOCK.getOutputStream(),20480);

                    send("ola");

                    listener();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        };
        th.start();
    }

    public void send(String message)
    {
        System.out.println(message);
		try
		{

            JSONObject json = new JSONObject();
            json.put("MAC", "1234");
            json.put("SYSTEM_VERSION", 2.0);
            json.put("SYSTEM_LANGUAGE", "PT");
            json.put("message", message);

            TrafficModel traffic = new TrafficModel();
            traffic.setMessage(json.toString().getBytes());

            byte[] traffic_bytes = this.serialize(traffic);
            
			OUTPUT.write(traffic_bytes,0,traffic_bytes.length); 
			//OUTPUT.writeUnshared(msg); 
			OUTPUT.flush();
		}
		catch (Exception ex) 
		{			
			System.out.println("sendert IOException: " + ex.getMessage());
			ex.printStackTrace();  
		}
    }

    public void listener()
    {
        Thread th = new Thread()
		{
			public void run() 
            {
				try 
                {
                    SOCK.setSoTimeout(0);

                    BufferedInputStream bis = new BufferedInputStream(SOCK.getInputStream(),20480);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    int bytesRead = 0;

                    while(true)
                    {
						byte[] buffer = new byte[20480];
						bytesRead = bis.read(buffer,0,20480);

						baos.write(buffer,0,bytesRead);
                        TrafficModel pck = deserialize(baos.toByteArray());

                        JSONObject message = new JSONObject(
                            new String(pck.getMessage()));

                        System.out.println(message.getString("message"));
                        send("valeu");
                    }
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        th.start();
    }

    public byte[] serialize(Object obj) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        
		return out.toByteArray();
    }
    
    public TrafficModel deserialize(byte[] data)  
	{
		TrafficModel pck = null;
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			pck = (TrafficModel)is.readObject();
			return pck;
		}
		catch(ClassNotFoundException ex)
		{
			return pck;
		}
		catch(IOException ex2)
		{
			return pck;
		}
	}

    public static void main(String[] args) {
        new MainClient();
    }
}
