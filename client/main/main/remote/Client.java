package main.remote;

import java.io.*;
import java.net.*;
import java.awt.Color;
import java.util.function.Consumer;

public class Client
{
	private Socket socket;

	public Client() {
	
	}

	private void getServerConnection() {

	}

	public void addConsumers(Consumer<Object[]> status_system_connection) {
		status_system_connection.accept(new Object[]{"test", Color.decode("#FF0000")});
	}
}