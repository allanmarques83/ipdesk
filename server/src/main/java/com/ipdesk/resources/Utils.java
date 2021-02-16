package resources;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

import main.Main;
import traffic_model.TrafficModel;

public class Utils
{
    public static String getPath(String file) {
        return Main.class.getResource("").getPath().concat(file);
    }

	public static String getServerIp() throws Exception {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static String getFileContent(String name_file, 
			String default_content) throws Exception {
        
		File file = new File(name_file);
		file.createNewFile();
  
  		BufferedReader br = new BufferedReader(new FileReader(file)); 
  
		StringBuilder str_builder = new StringBuilder(); 
		String line;
		while ((line = br.readLine()) != null) {
			str_builder.append(line);
		}
		br.close();

		if(str_builder.length() > 0)
			return str_builder.toString();

		return default_content;
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

    public static void loopDelay(int seconds) {
        try {
            Thread.sleep((seconds*1000));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Date getPastDate(Date date, int minutes_past) {
        long minutes = minutes_past * 60L * 1000L;
        long timeDate = date.getTime() - minutes;

        return new Date(timeDate);
    }

    public static boolean saveFile(String name_file, String content) {    
        try {
        	String path_file = Utils.getPath(name_file);
            File file = new File(path_file);
            file.createNewFile();

            FileWriter file_writer = new FileWriter(path_file,false);
            PrintWriter writer = new PrintWriter(file_writer,false); 

            writer.println(content);
           
            writer.close(); 
            file_writer.close();
            return true;
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }  
    }

    public static TrafficModel buildTrafficModel(ObjectInputStream stream) throws Exception {
        try {   
            return (TrafficModel)stream.readUnshared();
        }
        catch (SocketTimeoutException exception) {
            return null;
        }
        catch(Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
}