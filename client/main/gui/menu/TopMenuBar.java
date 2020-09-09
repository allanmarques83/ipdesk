package gui.menu;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.util.List;

import language.Language;
import resources.Utils;

public class TopMenuBar extends JMenuBar
{
    Language  language;

    public TopMenuBar(Language language) {
        super();

        this.language = language;
    }

    public JMenuBar build()
    {
        JMenu menu_system = new JMenu(language.translate("System"));
            
            JMenuItem configuration = new JMenuItem(language.translate(
                "Configuration"));

        menu_system.add(configuration);

        JMenu menu_language = new JMenu(language.translate("Language"));

        List<Object> system_languages = language.getLanguages();

        for(Object language_name : system_languages) {
            boolean is_system_language = language_name.equals(
                language.getSystemLanguage());

            menu_language.add(new JMenuItem(language_name.toString(), 
                is_system_language ? Utils.toIcon("images/1.png") : null));
        }

        this.add(menu_system);
        this.add(menu_language);

        return this;
    }
    
}
