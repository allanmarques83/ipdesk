package client.gui.buttons;

import javax.swing.SwingWorker;

import client.gui.swing.Button;
import client.language.Language;
import client.remote.ClientActions;
import client.resources.Utils;
import client.resources.Constants;

public class ButtonConnect extends Button
{
	Language language;
	ClientActions client_actions;

	public ButtonConnect(Language language, ClientActions client_actions) {
		super(language.translate("Connect"), null);
		
		this.language = language;
		this.client_actions = client_actions;

		this.defEnabled(false)
            .defActionCommand("connect_to");
	}

	public boolean fireActionButton(String remote_id, String password) {
            String command_execution = this.getActionCommand();

            if(command_execution.equals("connect_to")) {
                  return connect_to(remote_id, password);
            }

            this.setActionCommand("connect_to");
            return true;
      }

      private boolean connect_to(String remote_id, String password) {
            boolean is_valid_form = client_actions.connect_to(remote_id, password);

            if(is_valid_form) {
                  this.setActionCommand("cancel_connection");
                  this.setBackground(Constants.Colors.bittersweet);
                  buttonDisplayConnecting();
            }
            return is_valid_form;
      }

      private void buttonDisplayConnecting() {
            SwingWorker<Object, Object> worker = new SwingWorker<>() {
                  @Override
                   public Object doInBackground() {
                        int seconds_response_wait = 30;
                        while(seconds_response_wait > 0) {
                              Utils.loopDelay(1);

                              String command_execution = getActionCommand();
                              
                              if(command_execution.equals("connect_to"))
                                    break;

                              setText(String.format(
                                    language.translate("Connecting... [%d] Cancel"), 
                                          seconds_response_wait));
                              seconds_response_wait--;
                        }
                        restoreButtonConnect();
                        return null;
                   }
            };
            worker.execute();
      }

      public void restoreButtonConnect() {
            this.setActionCommand("connect_to");
            this.setBackground(Constants.Colors.white);
            this.setText(language.translate("Connect"));
      }
}