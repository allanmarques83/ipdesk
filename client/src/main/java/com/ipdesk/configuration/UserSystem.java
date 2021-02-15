package configuration;

import org.json.JSONArray;
import org.json.JSONObject;

import configuration.resources.UserSystemUtils;
import resources.Utils;

public class UserSystem {

    JSONObject _configuration;

    Language LANGUAGE;

    private String TITLE, PASSWORD, LOGO_FILE, SERVER_IP;

    private int SERVER_PORT, MAX_BYTES_SEND, MAX_BYTES_RECIEVE;

    private double SYSTEM_VERSION;

    private JSONArray BLOCKEDS_IDS, TRUSTEDS_IDS;
    
    public UserSystem() {
        
        _configuration = UserSystemUtils.getConfiguration();

        setSystemVersion(2.0);

        setTitle(_configuration.optString("system_title", "iPdesk"));
        setLanguage(_configuration.optString("system_language", "EN"));
        setPassword(_configuration.optString("system_password", Utils.getRandomPassword()));
        setLogoFilePath(_configuration.optString("system_logo", "images/ipdesk.jpg"));
        // setServerIp(_configuration.optString("server_ip", "127.0.1.1"));
        setServerIp(_configuration.optString("server_ip", "198.148.118.52"));
        setServerPort(_configuration.optInt("server_port", 1527));
        setBlockedsIds(_configuration.optString("blockeds_ids", "[]"));
        setTrustedsIds(_configuration.optString("trusteds_ids", "[]"));
        
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
        LANGUAGE = new Language(language);
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

    public Language getLanguage() {
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

    public void setConfig(String conf_name, String conf_value) {
        _configuration.put(conf_name, conf_value);
        UserSystemUtils.createConfiguration(_configuration);
    }
}
