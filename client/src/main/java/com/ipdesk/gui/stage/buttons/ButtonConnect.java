package gui.stage.buttons;

import javax.swing.SwingWorker;

import configuration.Language;
import gui.swing.Button;
import remote.ServerConnection;
import resources.Constants;
import resources.Utils;

public class ButtonConnect extends Button
{
      Language _LANGUAGE;
      ServerConnection _SERVER_CONNECTION;

	public ButtonConnect(ServerConnection server_connection) {
		super("Connect", null);
            
            _SERVER_CONNECTION = server_connection;
		_LANGUAGE = server_connection.getLanguage();

		this.defEnabled(false)
            .defActionCommand("connect_to");
	}

	public boolean fireActionButton(String remote_id, String password) {
            String command_execution = this.getActionCommand();

            if(command_execution.equals("connect_to")) {
                  return connectTo(remote_id, password);
            }

            if(command_execution.equals("disconnect_controled_user")) {
                  _SERVER_CONNECTION._OUTCOMING_USER_ACTION.closeControledId();
                  return true;
            }

            this.setActionCommand("connect_to");
            return true;
      }

      private boolean connectTo(String remote_id, String password) {

            if(remote_id.replace(_LANGUAGE.translate("Remote ID"),"").length() < 5) {
                  return Utils.Error(_LANGUAGE.translate("Invalid remote ID!"));
            }

            if(password.replace(_LANGUAGE.translate("Password"),"").length() < 8) {
                  return Utils.Error(_LANGUAGE.translate("Invalid password!"));
            }

            if(remote_id.equals(_SERVER_CONNECTION.getUserId())) {
                  return Utils.Error(_LANGUAGE.translate("Self connection error!"));
            }
            
            _SERVER_CONNECTION._OUTCOMING_USER_ACTION.connectTo(remote_id, password);
            
            setButtonConnectionAction("cancel_connection", "");
            
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
                                    _LANGUAGE.translate("Connecting... [%d] Cancel"), 
                                          seconds_response_wait));
                              seconds_response_wait--;
                        }
                        setButtonConnectionAction("connect_to", "");
                        return null;
                   }
            };
            worker.execute();
      }

      public void setButtonConnectionAction(String command, String controled_id) {
            
            if(command.equals("connect_to")) {
                  this.setActionCommand("connect_to");
                  this.setBackground(Constants.Colors.white);
                  this.setText(_LANGUAGE.translate("Connect"));
            }

            if(command.equals("cancel_connection")) {
                  this.setActionCommand("cancel_connection");
                  this.setBackground(Constants.Colors.bittersweet);
                  startCountDownConfirm();
            }

            if(command.equals("disconnect_controled_user")) {
                  this.setActionCommand("disconnect_controled_user");
                  this.setBackground(Constants.Colors.bittersweet);
                  this.setText(String.format(_LANGUAGE.translate(
                        "Close [%s] control"), controled_id));
            }
      }
}