package client.gui.dialogs;

import javax.swing.JOptionPane;

import client.language.Language;

public class Dialogs
{
	public static String confirmConnection(String sender_id, Language language) {
		String message = language.translate(String.format(
			"Connection recieve from remote ID: %s\nAccept to be controlled?", 
				sender_id));

		Object[] options = new Object[]{
			language.translate(String.format("Block %s", sender_id)),
			language.translate("No"),
			language.translate("Yes")};

		int option_chosen = JOptionPane.showOptionDialog(null,message,
			language.translate("Confirm connection?"), 
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                	null, options, null);

		if(option_chosen == JOptionPane.YES_OPTION)
			return "block";
		if(option_chosen == JOptionPane.CANCEL_OPTION)
			return "accepted";

		return "refused";
	}
}