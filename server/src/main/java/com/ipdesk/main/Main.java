package main;

import java.net.*;
import java.util.logging.Logger;

import remote.Server;
import resources.Utils;

public class Main 
{
    Logger _LOGGER;
    String _SERVER_IP;

	ServerSocket _SERVER_SOCKET;
    Server _SERVER;

	public Main() 
    {
        try {
            _LOGGER = Logger.getLogger("com.ipdesk");
            _SERVER_IP = Utils.getServerIp();

            _SERVER = new Server();

            _SERVER_SOCKET = new ServerSocket(
                1527, 100, InetAddress.getByName(_SERVER_IP )
            );
        }
        catch (final Exception ex) {
            _LOGGER.warning(ex.getMessage());
        }
    }

    public void start() {
        try {
            _LOGGER.info(String.format(
                "Server is listening on IP: %s and PORT: 1527%n", _SERVER_IP)
            );

            while (true) 
            {
                final Socket socket = _SERVER_SOCKET.accept();
                _SERVER.entryPoint(socket);
            }
        }
        catch (final Exception ex) {
            _LOGGER.warning(ex.getMessage());
        }
	}

    public static void main(String[] args) {
        new Main().start();
    }
}