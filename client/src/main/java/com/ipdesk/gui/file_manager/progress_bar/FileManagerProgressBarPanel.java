package gui.file_manager.progress_bar;

import java.awt.GridBagConstraints;

import javax.swing.JProgressBar;

import gui.swing.Panel;
import resources.Constants;

public class FileManagerProgressBarPanel extends Panel 
{
    JProgressBar _PROGRESS_BAR;

    public FileManagerProgressBarPanel() {
        super();

        this.defBackground(Constants.Colors.white);

        this.fill(GridBagConstraints.HORIZONTAL)
            .grid(0,0)
            .weight(1,1)
            .insets(5,0,5,0)
            .ipad(0, 20)
	        .anchor(GridBagConstraints.NORTHWEST)
	        .attach(getProgressBar(), "");
    }

    private JProgressBar getProgressBar() {
        _PROGRESS_BAR = new JProgressBar(0, 100);
		_PROGRESS_BAR.setValue(0);
		_PROGRESS_BAR.setString("0%");
		_PROGRESS_BAR.setStringPainted(true);
		_PROGRESS_BAR.setBorderPainted(false);

        return _PROGRESS_BAR;
    }
}
