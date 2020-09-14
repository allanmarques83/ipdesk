package server.client;

import java.net.*;
import java.io.*;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.collections.ObservableMap;

import org.json.JSONArray;

import server.resources.Utils;
import server.exceptions.ClientValidationException;

public class ClientValidation {
    
    JSONArray black_list;

    ObservableMap<String, Client> connections;

    public ClientValidation(ObservableMap<String, Client> connections) throws Exception {
        this.connections = connections;
        black_list = new JSONArray(Utils.getFileContent("BlackList.conf"));
    }

    public void process(Client client) throws ClientValidationException {

        if(!isMac(client.getMac())) {
            throw new ClientValidationException("Invalid MAC address", client, false);
        }
        
        if(!isIp(client.getIp())) {
            throw new ClientValidationException("Invalid IP address", client, false);
        }
        
        if(isMacBanish(client.getMac())) {
            throw new ClientValidationException("MAC was banish from this server", client, true);
        }

        if(containsId(client.getID())) {
            throw new ClientValidationException("You are already connected in another PC", client, true);
        }
    }


    private boolean isMac(String mac) {
        System.out.println(mac);
		Pattern p = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
		Matcher m = p.matcher(mac);
		
		return m.find();
    }
    
    private boolean isIp(String ip) {
		try 
		{
			return Inet4Address.getByName(ip).getHostAddress().equals(ip);
		}
		catch (UnknownHostException ex) 
		{
			return false;
		}
	}

    private boolean isMacBanish(String mac) {
        return black_list.toList().contains(mac);
    }

    private boolean containsId(String id) {
        return connections.containsKey(id);
    }
}
