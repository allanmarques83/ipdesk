package client.resources;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.Font;
import java.awt.Color;
import java.util.UUID;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import client.Main;
import traffic_model.TrafficModel;

public class Utils {

    public static String getPath(String file) {
        return Main.class.getResource("").getPath().concat("/").concat(file);
    }

	public static ImageIcon toIcon(String path) {
		java.net.URL imgURL = Main.class.getResource(path);
		if (imgURL != null) 
			return new ImageIcon(imgURL);
		else 
		{
			System.out.println(path);
			return null;
		}
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
                    if(sb.toString().isEmpty()==false){
                        addressByNetwork.put(network.getName(), sb.toString());
                    }

                    if(sb.toString().isEmpty()==false && firstInterface == null){
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
}
