package server.client;

import java.net.*;
import java.io.*;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.JSONArray;

import server.resources.Utils;


public class ClientValidation {
    
    JSONArray black_list;

    public ClientValidation() throws Exception
    {
        black_list = new JSONArray(Utils.getFileContent("BlackList.conf"));
    }

    public void validate(Client client) throws Exception {

        // if(!isMac(client.getMac()))
        //     throw new Exception("Invalid MAC address");
        
        if(!isIp(client.getIp()))
            throw new Exception("Invalid IP address");
        
        if(isMacBanish(client.getMac()))
            throw new Exception("MAC was banish from this server");
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
}
