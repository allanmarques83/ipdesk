package resources;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.Robot;
import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import main.Main;
import traffic_model.TrafficModel;

public class Utils {

    public static String getPath(String file) {
        return Main.class.getResource("").getPath().concat(file);
    }

	public static ImageIcon toIcon(String path) {
		java.net.URL imgURL = Main.class.getResource(path);

		return imgURL != null ? new ImageIcon(imgURL) : null;
	}

	public static String getRandomPassword() {
		return UUID.randomUUID().toString().substring(0,8);
	}

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static byte[] toByteArray(TrafficModel traffic) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(traffic);

        return out.toByteArray();
    }

    public static TrafficModel toTrafficModel(byte[] data) {
        TrafficModel traffic = null;
        
        try
        {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            traffic = (TrafficModel)is.readUnshared();
            
            return traffic;
        }
        catch(ClassNotFoundException ex)
        {
            return null;
        }
        catch(IOException ex2)
        {
            return null;
        }
    }

    public static String getMacAddress() {
        String ipAddress = Utils.getOsSystem().equals("linux") ? 
            Utils.getLinuxIpAddress() : Utils.getWindowsIpAddress();

        try {
            String firstInterface = null;        
            Map<String, String> addressByNetwork = new HashMap<>();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                .getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();

                byte[] bmac = network.getHardwareAddress();
                
                if(bmac != null) 
                {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bmac.length; i++){
                        sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));                   
                    }
                    if(!sb.toString().isEmpty()){
                        addressByNetwork.put(network.getName(), sb.toString());
                    }

                    if(!sb.toString().isEmpty() && firstInterface == null){
                        if(Utils.isIpToInterface( network.getInetAddresses(),ipAddress ))
                            firstInterface = network.getName();
                    }
                }
            }

            if(firstInterface != null) {
                return addressByNetwork.get(firstInterface);
            }
            return null;
        }
        catch(Exception e) {
            return null;
        } 
    }

    public static boolean isIpToInterface(Enumeration<InetAddress> inets, String ip) {
       
       while(inets.hasMoreElements()) {
           InetAddress inet = inets.nextElement();
           String ipx = inet.getHostAddress();
           
           if(ipx.equals(ip))
               return true;
       } 
       return false;
    }

    public static String getOsSystem() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static String getWindowsIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception exception) {
            return null;
        }
    }

    public static String getLinuxIpAddress() {
        try(final DatagramSocket socket = new DatagramSocket())
        {
          socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
          return socket.getLocalAddress().getHostAddress();
        }
        catch(UnknownHostException e)
        {
            return null;
        }
        catch(SocketException e2)
        {
            return null;
        }
    }

    public static void applyNimbusLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean Error(String message) {
        JOptionPane.showMessageDialog(null,message,"Erro!",JOptionPane.ERROR_MESSAGE); 
        return false;
    }

    public static void loopDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] compressBytes(byte[] bytes) {
        try {
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(bytes); 
            deflater.finish();  
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
            
            byte[] buffer = new byte[1024];   

            while (!deflater.finished())  {  
                int count = deflater.deflate(buffer); // returns the generated code... index  
                outputStream.write(buffer, 0, count);   
            }  
            outputStream.close();  
            return outputStream.toByteArray();      
        }
        catch(Exception exception) {
            return null;
        }
    }

    public static byte[] decompressBytes(byte[] bytes) {  
        try {
            Inflater inflater = new Inflater();   
            inflater.setInput(bytes);  

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);  

            byte[] buffer = new byte[1024];  

            while (!inflater.finished()) 
            {  
                int count = inflater.inflate(buffer);  
                outputStream.write(buffer, 0, count);  
            }
            outputStream.close();  

            return outputStream.toByteArray();  
        }
        catch(Exception e) {
            return null;
        }
    }

    public static int getClickMousePosition(
        double click_position, double image_resolution, int monitor
    ) {
        double scale = click_position/image_resolution;
        double real_position = scale*monitor;

        return (int)real_position;
    }

    public static Robot getRobotInstance() {
        try {
            return new Robot();
        }
        catch(AWTException exception) {
            return null;
        }
    }

    public static String getExpression(String regex, String search, int macth_position) {
        Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(search);

		matcher.find();
		
		try {
			return matcher.group(macth_position);
		}
		catch(IllegalStateException e) {	
			return "";
		}
    }
}
