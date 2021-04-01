package services.file_manager;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import remote.ServerConnection;

public class FileManagerSender {

    ServerConnection _SERVER_CONNECTION;

    int MAX_BYTES_TO_SEND;
    
    public FileManagerSender(ServerConnection server_connection) {
        _SERVER_CONNECTION = server_connection;

        MAX_BYTES_TO_SEND = 16384;
    }

    public void sendZipContent(
        byte[] zip_file, String destination_path, String remote_id, String action
    ) {

        // setTotalBytesToSend(zip_file.length);

        new Thread(() -> 
        {
            try {
                ByteArrayInputStream input_stream = new ByteArrayInputStream(zip_file);
                
                while(true) {
                    byte[] buffer_to_send = new byte[MAX_BYTES_TO_SEND];

                    int bytes_to_send = input_stream.read(
                        buffer_to_send, 0, MAX_BYTES_TO_SEND
                    );

                    if(bytes_to_send < 0) {
                        _SERVER_CONNECTION._OUTCOMING_USER_ACTION.sendBytesFile(
                            remote_id, destination_path, null, "transferFileToControlled"
                        );
                        break;
                    }

                    buffer_to_send = Arrays.copyOf(buffer_to_send, bytes_to_send);
                    
                    _SERVER_CONNECTION._OUTCOMING_USER_ACTION.sendBytesFile(
                        remote_id, destination_path, buffer_to_send, action
                    );
                }
            }
            catch(Exception exception) {
                exception.printStackTrace();
            }
		}).start();
    }
}
