package client.gui.dialogs;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.SwingWorker;
import java.awt.Component;
import java.awt.Container;

import client.resources.Utils;
import client.language.Language;

public class ConfirmAttemptConnection
{
	Language language;
	JOptionPane option_pane;

	public ConfirmAttemptConnection(String sender_id, Language language) {
		this.language = language;

		String message = String.format(language.translate(
			"Connection recieve from remote ID: %s\nAccept to be controlled?"), 
				sender_id);


		Object[] options = new Object[]{
			String.format(language.translate("Block %s"),sender_id),
			language.translate("No"),
			language.translate("Yes")};


		option_pane = new JOptionPane(
			message, 
				JOptionPane.QUESTION_MESSAGE,
					JOptionPane.YES_NO_CANCEL_OPTION, 
						null, 
							options, 
								null); 
	}

	public String build() {
		JButton yes_btn = getYesButton(option_pane);

		JDialog dlg = option_pane.createDialog(
			language.translate("Confirm connection?"));
		dlg.addComponentListener(addTimerVisible(yes_btn));

		dlg.setVisible(true);

		if(option_pane.getValue() == null)
			return "refused";

		String option_chosen = option_pane.getValue().toString();

		if(option_chosen.equals(language.translate("No")))
			return "refused";
		if(option_chosen.equals(language.translate("uninitializedValue")))
			return "refused";
		if(option_chosen.equals(language.translate("Yes")))
			return "accept";
		
		return "block";
	}

	private JButton getYesButton(Container container) {
		for(Component component : container.getComponents()) {
			if( component instanceof JButton ) {
				JButton button = (JButton)component;
				
				if( button.getText().equals("Yes")) {
					return button;
				}
			}
			else if( component instanceof Container ) {
				JButton button = getYesButton( (Container)component );
				if( button != null ) return button;
			}
		}
		return null;
	}

	private ComponentAdapter addTimerVisible(JButton yes_btn) {
		return new ComponentAdapter() {
	        @Override
	        public void componentShown(ComponentEvent e) {
	            super.componentShown(e);
	            JDialog dlg = (JDialog)e.getSource();
	            SwingWorker<Object, Object> worker = new SwingWorker<>() {
                	@Override
                   	public Object doInBackground() {
                   		int seconds_delay = 30;
                   		while(seconds_delay > 0)
                   		{
                   			Utils.loopDelay(1);
                   			yes_btn.setText(String.format(language.translate(
                   				"Yes [%d]"),seconds_delay));
                   			
                   			seconds_delay--;
                   		}
                   		dlg.setVisible(false);
                   		return null;
                   	}
                };
                worker.execute();
	        }
	    };
	}
}