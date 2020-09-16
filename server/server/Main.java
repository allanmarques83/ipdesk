package server;

import java.io.*;
import java.net.*;
import javax.net.ssl.SSLServerSocket;

import server.resources.Constants;
import server.resources.Utils;

import server.client.Clients;

public class Main 
{
	ServerSocket server_socket;
    Clients clients;

	public Main(boolean is_server_on) {
		
		try {
            clients = new Clients();
            
            String server_ip = Utils.getServerIp();

            server_socket = new ServerSocket(1527, 100, InetAddress.getByName(
                server_ip));
            
            System.out.printf(
                "Server is listening on IP: %s and PORT: 1527%n", server_ip);

            while (true) {
                final Socket socket = server_socket.accept();

                if (is_server_on)
                    clients.entryPoint(socket);
                // else {
                // closeSocket(socket,null,null);
                // Thread.sleep((30*1000));
                // }
            }

        } 
        catch (final Exception ex) {
            ex.printStackTrace();
        }
	}

    public static void main(String[] args) {
        boolean is_server_on = false;

		if(args.length > 0) {
			is_server_on = Boolean.valueOf(args[0]);
        }
        new Main(is_server_on);
    }
}