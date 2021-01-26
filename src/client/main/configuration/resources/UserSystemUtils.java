package main.configuration.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.json.JSONObject;

public class UserSystemUtils {
    public static JSONObject getConfiguration()
    {
        try {
            FileReader reader = new FileReader("config"); 
            BufferedReader leitor = new BufferedReader(reader);
            JSONObject config = new JSONObject(leitor.readLine());
            leitor.close();
            reader.close();

            return config;
        } 
        catch(Exception exception)
        {  
            return createConfiguration(null);
        }
    }

    public static JSONObject createConfiguration(JSONObject config)
    {      
        try
        {
            new File("config").createNewFile();
            FileWriter writer = new FileWriter("config", false);
            PrintWriter saida = new PrintWriter(writer, false); 

            if(config == null)
                config = new JSONObject();

            saida.println(config.toString());
           
            saida.close(); 
            writer.close();
            return config;
        } 
        catch(Exception ex)
        {
            return null;
        }  
    }
}
