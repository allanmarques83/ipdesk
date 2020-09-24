package client.gui.stage.buttons;

import javax.swing.SwingWorker;

import client.gui.swing.Button;
import client.language.Language;
import client.remote.SystemActions;
import client.resources.Utils;
import client.resources.Constants;

public class ButtonConnect extends Button
{
	Language language;
	SystemActions system_actions;

	public ButtonConnect(Language language, SystemActions system_actions) {
		super(language.translate("Connect"), null);
		
		this.language = language;
		this.system_actions = system_actions;

		this.defEnabled(false)
            .defActionCommand("connect_to");
	}

	public boolean fireActionButton(String remote_id, String password) {
            String command_execution = this.getActionCommand();

            if(command_execution.equals("connect_to")) {
                  return connect_to(remote_id, password);
            }

            if(command_execution.equals("remote_controled")) {
                  system_actions.getAction("closeControledId").accept(new Object[]{});
                  setButtonConnectionAction(new Object[]{"connect_to"});
                  return true;
            }

            this.setActionCommand("connect_to");
            return true;
      }

      private boolean connect_to(String remote_id, String password) {

            if(remote_id.replace(language.translate("Remote ID"),"").length() < 5) {
                  return Utils.Error(language.translate("Invalid remote ID!"));
            }

            if(password.replace(language.translate("Password"),"").length() < 8) {
                  return Utils.Error(language.translate("Invalid password!"));
            }

            if(remote_id.equals(system_actions.getData("Connection").getString("getClientId"))) {
                  return Utils.Error(language.translate("Self connection error!"));
            }

            system_actions.getAction("connect_to").accept(new Object[]{remote_id, password});

            setButtonConnectionAction(new Object[]{"cancel_connection"});
            
            return true;
      }

      private void startCountDownConfirm() {
            SwingWorker<Object, Object> worker = new SwingWorker<>() {
                  @Override
                   public Object doInBackground() {
                        int seconds_response_wait = 30;
                        while(seconds_response_wait > 0) {
                              Utils.loopDelay(1000);

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