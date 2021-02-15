package remote;

import gui.stage.dialogs.ConfirmAttemptConnection;


public class AttemptConnection 
{
	UserServer _USER_SERVER;

	public AttemptConnection(UserServer user_server) {
		_USER_SERVER = user_server;
	}

	public boolean isValid(String controled_id, String sender_id, String password) {

		if(controled_id != null) 
			return false;

		if(!password.equals(_USER_SERVER.getPassword()))
			return false;

		if(_USER_SERVER.getBlockedsIds().toList().contains(sender_id))
			return false;

		if(_USER_SERVER.getTrustedsIds().toList().contains(sender_id))
			return true;

		String confirm_connection = new ConfirmAttemptConnection(
			sender_id, _USER_SERVER.getLanguage()).build();

		if(confirm_connection.equals("refused"))
			return false;

		if(confirm_connection.equals("block")) {
			
			_USER_SERVER.setConfig("blockeds_ids", 
			_USER_SERVER.setBlockedsIds(
				_USER_SERVER.getBlockedsIds().put(sender_id).toString()
				).toString()
			);

			return false;
		}

		if(confirm_connection.equals("accept")) {
			return true;
		}
	
		return false;
	}
}