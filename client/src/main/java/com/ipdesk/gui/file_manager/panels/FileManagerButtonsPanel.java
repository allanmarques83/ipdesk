package gui.file_manager.panels;

import gui.swing.Button;
import gui.swing.Label;
import gui.swing.Panel;
import resources.Constants;
import resources.Utils;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.function.Consumer;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;


public class FileManagerButtonsPanel extends Panel 
{
    Consumer<String> _EVENT;

    Panel _BUTTONS_PANEL;
    
    public FileManagerButtonsPanel(Consumer<String> event, String tree_name) {
        super();

        _EVENT = event;

        this.defBackground(Constants.Colors.white);

        this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,0)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(getTitle(), "");
        
        this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,1)
            .weight(0,0)
            .insets(0,10,0,10)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new JSeparator(SwingConstants.HORIZONTAL), "");
        
        this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,2)
            .weight(1,1)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(getButtons(tree_name), "");
    }

    public Panel getTitle() {
        Panel title_panel = new Panel()
        .defBackground(Constants.Colors.white);

        title_panel.fill(GridBagConstraints.NONE)
            .grid(0,0)
            .weight(0,0)
            .insets(10,10,5,5)
            .gridheight(2)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new Label("", Utils.toIcon("images/pc.jpg")), "");

        title_panel.fill(GridBagConstraints.NONE)
            .grid(1,0)
            .weight(0,0)
            .insets(10,0,5,0)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(new Label("allan pc").defFont(16, Font.BOLD), "");
        
        title_panel.fill(GridBagConstraints.NONE)
            .grid(1,1)
            .weight(1,0)
            .insets(30,0,5,0)
	        .attach(new Label(String.format("ID: %s", "123456"))
                .defFont(12, Font.PLAIN),
            "");

            return title_panel;
    }

    public Panel getButtons(String tree_name) {
        _BUTTONS_PANEL = new Panel()
        .defBackground(Constants.Colors.white);

        _BUTTONS_PANEL.fill(GridBagConstraints.NONE)
        .grid(0,0)
        .weight(0,0)
        .insets(10,0,5,0)
        .anchor(GridBagConstraints.NORTHWEST)
        .attach(new Button("", Utils.toIcon(
            String.format(
                "images/%s.png", tree_name.equals("CONTROLLER") ? "file_upload" : "file_download"
            )))
            .defFocusPainted(false)
            .defBackground(Constants.Colors.white)
            .defToolTipText(
                String.format(
                    "%s File/Directory (Enter)", tree_name.equals("CONTROLLER") ? "Upload" : "Download"
                )
            )
            .defActionListener(e -> _EVENT.accept(
                String.format("<FILE_TRANSFER_%s:>", tree_name)
            )),
        "btn_transfer");
        
        _BUTTONS_PANEL.fill(GridBagConstraints.NONE)
        .grid(1,0)
        .weight(0,0)
        .insets(10,0,5,0)
        .anchor(GridBagConstraints.NORTHWEST)
        .attach(new Button("", Utils.toIcon("images/stop.png"))
            .defFocusPainted(false)
            .defBackground(Constants.Colors.white)
            .defToolTipText("Stop uploading files/folders")
            .defEnabled(false), 
        "btn_stop");
        
        _BUTTONS_PANEL.fill(GridBagConstraints.NONE)
        .grid(2,0)
        .weight(0,0)
        .insets(10,0,5,0)
        .anchor(GridBagConstraints.NORTHWEST)
        .attach(new Button("", Utils.toIcon("images/folder_add.png"))
            .defFocusPainted(false)
            .defBackground(Constants.Colors.white)
            .defToolTipText("Create new folder"), 
        "btn_new_folder");
        
        _BUTTONS_PANEL.fill(GridBagConstraints.NONE)
        .grid(3,0)
        .weight(0,0)
        .insets(10,0,5,0)
        .anchor(GridBagConstraints.NORTHWEST)
        .attach(new Button("", Utils.toIcon("images/text_edit.png"))
            .defFocusPainted(false)
            .defBackground(Constants.Colors.white)
            .defToolTipText("Rename Files/Folders (F2)"), 
        "btn_rename");
        
        _BUTTONS_PANEL.fill(GridBagConstraints.NONE)
        .grid(4,0)
        .weight(0,0)
        .insets(10,0,5,0)
        .anchor(GridBagConstraints.NORTHWEST)
        .attach(new Button("", Utils.toIcon("images/update.png"))
            .defFocusPainted(false)
            .defBackground(Constants.Colors.white)
            .defToolTipText("Reload directories and files (F5)"), 
        "btn_reload");
        
        _BUTTONS_PANEL.fill(GridBagConstraints.NONE)
        .grid(5,0)
        .weight(1,0)
        .insets(10,0,5,0)
        .anchor(GridBagConstraints.NORTHWEST)
        .attach(new Button("", Utils.toIcon("images/del.gif"))
            .defFocusPainted(false)
            .defBackground(Constants.Colors.white)
            .defToolTipText("Really delete?"), 
        "btn_delete_file");

        return _BUTTONS_PANEL;
    }

    public void setEnableButtons(boolean enable) {
        _BUTTONS_PANEL.components.values().stream().forEach(
            component -> ((Button) component).defEnabled(enable)
        );
    }

    public void setEnableButton(boolean enable, String component_name) {
        ((Button)_BUTTONS_PANEL.components.get(component_name)).defEnabled(enable);
    }
}
