package main.gui.stage.menu;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import main.configuration.Language;
import main.resources.Utils;

public class TopMenuBar extends JMenuBar
{
    Language _language;

    public TopMenuBar(Language language) {
        super();

        this._language = language;
    }

    public JMenuBar build()
    {
        JMenu menu_system = new JMenu(_language.translate("System"));
            
            JMenuItem configuration = new JMenuItem(_language.translate(
                "Configuration"));

        menu_system.add(configuration);

        JMenu menu_language = new JMenu(_language.translate("Language"));

        List<Object> system_languages = _language.getLanguages();

        for(Object language_name : system_languages) {
            boolean is_system_language = language_name.equals(
                _language.getSystemLanguage());

            menu_language.add(new JMenuItem(language_name.toString(), 
                is_system_language ? Utils.toIcon("images/1.png") : null));
        }

        this.add(menu_system);
        this.add(menu_language);

        return this;
    }
    
}
