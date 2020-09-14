package server.exceptions;

import server.client.Client;
import org.json.JSONObject;

public class ClientValidationException extends Exception
{
	public ClientValidationException(String message, Client client, boolean send_message_error) {
		super(message);

		client.sendTraffic(new JSONObject()
            .put("action", "showMessageError")
                .put("error", message),
                    null,
                        null,
                            null);
	}
}