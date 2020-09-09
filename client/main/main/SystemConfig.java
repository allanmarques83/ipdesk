package main;

import java.io.*;

import org.json.JSONObject;

import resources.Utils;

public class SystemConfig {

    private String TITLE, LANGUAGE, PASSWORD, LOGO_FILE;
    
    public SystemConfig() {
        
        JSONObject configuration = getConf();

        setTitle(configuration.optString("system_title", "iPdesk"));
        setLanguage(configuration.optString("system_language", "EN"));
        setPassword(configuration.optString("system_password", Utils.getRandomPassword()));
        setLogoFilePath(configuration.optString("system_logo", "images/ipdesk.jpg"));

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
            return setConf(null);
        }
    }

    private JSONObject setConf(JSONObject config)
    {      
        try
        {
            File file = new File("config");
            file.createNewFile();
            FileWriter writer = new FileWriter("config",false);
            PrintWriter saida = new PrintWriter(writer,false); 

            if(config == null)
            {
                config = new JSONObject()
                    .put("system_title", "iPdesk")
                    .put("language", "EN");
                
            }

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
}
