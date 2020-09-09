package server;

import java.io.*;
import java.net.*;
import javax.net.ssl.SSLServerSocket;

import resources.Constants;
import resources.Utils;

import client.Clients;

public class Server
{
    ServerSocket server_socket;

    Clients clients;

    public Server(final boolean is_server_on) {
        
        try 
        {
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

        } catch (final Exception ex) 
        {
            ex.printStackTrace();
        }
    }
}