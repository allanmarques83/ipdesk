package client.configuration;

import java.io.*;

import org.json.JSONObject;
import org.json.JSONArray;

import client.resources.Utils;

public class Config {

    JSONObject configuration;

    private String TITLE, LANGUAGE, PASSWORD, LOGO_FILE, SERVER_IP;

    private int SERVER_PORT, MAX_BYTES_SEND, MAX_BYTES_RECIEVE;

    private double SYSTEM_VERSION;

    private JSONArray BLOCKEDS_IDS, TRUSTEDS_IDS;
    
    public Config() {
        
        this.configuration = getConf();

        setSystemVersion(2.0);

        setTitle(configuration.optString("system_title", "iPdesk"));
        setLanguage(configuration.optString("system_language", "EN"));
        setPassword(configuration.optString("system_password", Utils.getRandomPassword()));
        setLogoFilePath(configuration.optString("system_logo", "images/ipdesk.jpg"));
        // setServerIp(configuration.optString("server_ip", "127.0.1.1"));
        setServerIp(configuration.optString("server_ip", "198.148.118.52"));
        setServerPort(configuration.optInt("server_port", 1527));
        setBlockedsIds(configuration.optString("blockeds_ids", "[]"));
        setTrustedsIds(configuration.optString("trusteds_ids", "[]"));
        
        setMaxBytesSend(65536);
        setMaxBytesRecieve(131072);
    }

    public void setSystemVersion(double version) {
        SYSTEM_VERSION = version;
    }
    
    public void setTitle(String title) {
        TITLE = title;
    }

    public void setLanguage(String language) {
        LANGUAGE = language;
    }

    public void setPassword(String password) {
        PASSWORD = password;
    }

    public void setLogoFilePath(String file_path) {
        LOGO_FILE = file_path;
    }

    public void setServerIp(String server_ip) {
        SERVER_IP = server_ip;
    }
    
    public void setServerPort(int server_port) {
            SERVER_PORT = server_port;
    }
    
    public void setMaxBytesSend(int max_bytes) {
            MAX_BYTES_SEND = max_bytes;
    }

    public void setMaxBytesRecieve(int max_bytes) {
            MAX_BYTES_RECIEVE = max_bytes;
    }

    public JSONArray setBlockedsIds(String blockeds_ids) {
        BLOCKEDS_IDS = new JSONArray(blockeds_ids);
        return BLOCKEDS_IDS;
    }

    public void setTrustedsIds(String trusteds_ids) {
        TRUSTEDS_IDS = new JSONArray(trusteds_ids);
    }

    public double getSystemVersion() {
        return SYSTEM_VERSION;
    }
    
    public String getTitle() {
        return TITLE;
    }

    public String getLanguage() {
        return LANGUAGE;
    }
    
    public String getPassword() {
        return PASSWORD;
    }

    public String getLogoFilePath() {
        return LOGO_FILE;
    }

    public String getServerIp() {
        return SERVER_IP;
    }
    
    public int getServerPort() {
        return SERVER_PORT;
    }

    public int getMaxBytesSend() {
        return MAX_BYTES_SEND;
    }

    public int getMaxBytesRecieve() {
        return MAX_BYTES_RECIEVE;
    }

    public JSONArray getBlockedsIds() {
        return BLOCKEDS_IDS;
    }

    public JSONArray getTrustedsIds() {
        return TRUSTEDS_IDS;
    }

    private JSONObject getConf()
    {   
        try
        {
            FileReader reader = new FileReader("config"); 
            BufferedReader leitor = new BufferedReader(reader);
            JSONObject config = new JSONObject(leitor.readLine());
            leitor.close();
            reader.close();

            return config;
        } 
        catch(Exception exception)
        {  
            return createConfig(null);
        }
    }

    private JSONObject createConfig(JSONObject config)
    {      
        try
        {
            File file = new File("config");
            file.createNewFile();
            FileWriter writer = new FileWriter("config",false);
            PrintWriter saida = new PrintWriter(writer,false); 

            if(config == null)
                config = new JSONObject();

            saida.println(config.toString());
           
            saida.close(); 
            writer.close();
            return config;
        } 
        catch(Exception ex)
        {
            // Utils.Error(ex, "Exception", "setPropretiesSys", "");
            return null;
        }  
    }

    public void setConfig(String conf_name, String conf_value) {
        configuration.put(conf_name, conf_value);
        createConfig(configuration);
    }
}
