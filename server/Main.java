import java.rmi.ServerException;

import server.Server;

public class Main 
{
    public static void main(String[] args) {

        boolean is_server_close = false;

		if(args.length > 0) {
			is_server_close = Boolean.valueOf(args[0]);
        }

        new Server(is_server_close);
    }
}