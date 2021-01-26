package main.configuration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import main.resources.Utils;

public class Language {

    private JSONArray available_languages;
    private JSONObject dict;

    private String system_language;

    public Language(String language) {

        system_language = language;

        getAvailableLanguages();

        setSystemLanguage(language);
    }
    
    private void getAvailableLanguages() {
        System.out.println(Utils.getPath("configuration"));
        available_languages = new JSONArray().put("EN");

        File dir = new File(Utils.getPath("configuration"));
        
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".language");
             }
        });

        if(files != null) {
            for(File file : files){
                available_languages.put(file.getName());
            }
        }
    }

    private void setSystemLanguage(String language) {
        dict = new JSONObject();
    }

    public List<Object> getLanguages() {
        return available_languages.toList();
    }
    public String getSystemLanguage() {
        return system_language;
    }
    public String translate(String text) {
        return dict.has(text) ? dict.getString(text) : text;
    }
    
    
}
