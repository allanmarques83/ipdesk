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

            if(command_execution.equals("remote_controled")) {
                  client_actions.closeControledId();
                  setButtonConnectionAction(new Object[]{"connect_to"});
                  return true;
            }

            this.setActionCommand("connect_to");
            return true;
      }

      private boolean connect_to(String remote_id, String password) {
            boolean is_valid_form = client_actions.connect_to(remote_id, password);

            if(is_valid_form) {
                  setButtonConnectionAction(new Object[]{
                        "cancel_connection",
                  });
            }
            return is_valid_form;
      }

      private void startCountDownConfirm() {
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
                        setButtonConnectionAction(new Object[]{"connect_to"});
                        return null;
                   }
            };
            worker.execute();
      }

      public void setButtonConnectionAction(Object[] params) {
            String command = params[0].toString();
            
            if(command.equals("connect_to")) {
                  this.setActionCommand("connect_to");
                  this.setBackground(Constants.Colors.white);
                  this.setText(language.translate("Connect"));
            }

            if(command.equals("cancel_connection")) {
                  this.setActionCommand("cancel_connection");
                  this.setBackground(Constants.Colors.bittersweet);
                  startCountDownConfirm();
            }

            if(command.equals("remote_controled")) {
                  String controled_id = params[1].toString();

                  this.setActionCommand("remote_controled");
                  this.setBackground(Constants.Colors.bittersweet);
                  this.setText(String.format(language.translate(
                        "Close [%s] control"), controled_id));
            }
      }
}