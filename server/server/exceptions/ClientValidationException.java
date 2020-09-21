package server.exceptions;

import server.client.Client;
import org.json.JSONObject;

public class ClientValidationException extends Exception
{
	public ClientValidationException(String message, Client client, boolean send_message_error) {
		super(message);

		if(send_message_error) {
			client.sendTraffic(new JSONObject()
	            .put("action", "showMessageError")
	                .put("error", message).toString().getBytes(),
	                    null);
		}
	}
}