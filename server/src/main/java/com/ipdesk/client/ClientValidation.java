package client;

import java.net.*;
import java.io.*;
import javax.json.*;
import java.util.Map;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.collections.ObservableMap;
import java.sql.Timestamp;

import resources.Utils;
import exceptions.ClientValidationException;

public class ClientValidation {
    
    JsonObject black_list;

    ObservableMap<String, Client> connections;

    public ClientValidation(
        ObservableMap<String, Client> clients, Client client
    ) throws ClientValidationException {
        
        ClientValidation.refreshBlackList(
            Utils.getPastDate(new Date(), 15));

        if(!isMac(client.getMac())) {
            throw new ClientValidationException("Invalid MAC address", client, false);
        }
        
        if(!isIp(client.getIp())) {
            throw new ClientValidationException("Invalid IP address", client, false);
        }
        
        if(isMacBanish(client.getMac())) {
            throw new ClientValidationException("MAC was banish from this server", client, false);
        }

        if(clients.containsKey(client.getID())) {
            throw new ClientValidationException("You are already connected in another PC", client, true);
        }
    }

    private boolean isMac(String mac) {
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
        return ClientValidation.getBlackList().containsKey(mac);
    }

    public static JsonObject getBlackList() {
        try {
            String black_file = Utils.getFileContent("BlackList.conf", "{}");
            JsonReader reader = Json.createReader(new StringReader(black_file));
            JsonObject black_list = reader.readObject();
            reader.close();
            return black_list;
        }
        catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static void addBlackList(String mac) 
    {
        JsonObject black_list = ClientValidation.getBlackList();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(mac, now.toString());

        black_list.entrySet().
                forEach(e -> builder.add(e.getKey(), e.getValue()));
        
        black_list = builder.build();

        Utils.saveFile("BlackList.conf", black_list.toString());
    }

    public static boolean refreshBlackList(Date minutes) {
        JsonObject black_list = ClientValidation.getBlackList();
        Timestamp minutes_ago = new Timestamp(minutes.getTime());

        Map<String, JsonValue> filter_map = black_list.entrySet()
            .stream()
                .filter(map -> Timestamp.valueOf(
                    map.getValue().toString().replaceAll("\"", ""))
                        .after(minutes_ago))
                .collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue));

        if(filter_map.size() == black_list.size())
            return false;

        JsonObjectBuilder builder = Json.createObjectBuilder();

        filter_map.entrySet().
                forEach(e -> builder.add(e.getKey(), e.getValue()));

        black_list = builder.build();

        return Utils.saveFile("BlackList.conf", black_list.toString());
    }
}
