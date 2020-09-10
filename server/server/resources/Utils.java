package server.resources;

import java.net.*;
import java.io.*;
import java.util.Arrays;

import traffic_model.TrafficModel;

public class Utils
{
    public static String getEnv(String env_name) {
        
        return System.getenv().get(env_name);
    }

    public static TrafficModel toTrafficModel(byte[] data) {
        TrafficModel traffic = null;
        
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			traffic = (TrafficModel)is.readObject();
            
            return traffic;
		}
		catch(Exception ex2)
		{
			ex2.printStackTrace();
			return null;
		}
	}

	public static byte[] toByteArray(TrafficModel traffic) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(traffic);

		return out.toByteArray();
	}

	public static String getServerIp() throws Exception {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static byte[] getBufferSocket(Socket socket, int max_bytes) throws Exception {
		InputStream stream = socket.getInputStream();
		stream.mark(max_bytes);
		BufferedInputStream buffered_stream = new BufferedInputStream(stream, max_bytes);

		byte[] buffer = new byte[max_bytes];
		int bytesRead = buffered_stream.read(buffer,0,max_bytes);

		return Arrays.copyOf(buffer, bytesRead);
	}

	public static String getFileContent(String nameOfFile) throws Exception {
		
		File file = new File(nameOfFile); 
  
  		BufferedReader br = new BufferedReader(new FileReader(file)); 
  
		StringBuilder str_builder = new StringBuilder(); 
		String line;
		while ((line = br.readLine()) != null) {
			str_builder.append(line);
		}
		br.close();
		return str_builder.toString();
	
	} 

	public static void closeSocket(Socket socket) {
        try {
            String ip = socket.getInetAddress().getHostAddress();
            System.out.printf("closing socket ip: %s port: %d%n", ip, socket.getPort());

            socket.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            // close(socket);
        }
    }
}