package remote;

import java.net.Socket;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import gui.Gui;
import gui.swing.Label;
import traffic_model.TrafficModel;


// @RunWith(PowerMockRunner.class)
// @PrepareForTest(ServerConnection.class)
public class ServerConnectionTest {
    // ArgumentCaptor<Runnable> runnables = ArgumentCaptor.forClass(Runnable.class);

    // private ServerConnection test;


    // @Test
    // public void shouldChangeStatusWhenTryEstablishConnection() throws Exception {
        
    //     Gui gui = new Gui();
    //     test = Mockito.spy(new ServerConnection(gui));
        
    //     Thread mock = Mockito.mock(Thread.class);
        
    //     PowerMockito.whenNew(Thread.class)
    //         .withParameterTypes(Runnable.class)
    //             .withArguments(runnables.capture())
    //                 .thenReturn(mock);
        
    //     test.establish("test [%ds]", 3);

    //     runnables.getValue().run();

    //     Mockito.verify(test).setSocketConnection(
    //         new Socket("server_ip", 1566)
    //     );
    // }
}
