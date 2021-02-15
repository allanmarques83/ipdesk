package main;


import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;


import remote.ServerConnection;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class MainTest 
{
    Main main;
    
    @Mock
    ServerConnection serverConnectionMock;

    @Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

        main = new Main();
	}

    @Test
    public void shouldEstablishConnectionToServer()
    {
        // doNothing().when(serverConnectionMock).establish(
        //     "Try to establish connection in: [%ds]", 3);
        main._server_connection = serverConnectionMock;

        main.start();

        Mockito.verify(serverConnectionMock, times(1)).establish(
            "Try to establish connection in: [%ds]", 3
        );
    }
}
