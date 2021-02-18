package remote;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;  

class TestTmp {
//     public void doSomthingAsynchrone() {
//        new Thread(() -> {
//           doSomthing();
//        }).start();
//     }
 
//     void doSomthing() {
//     }
// }

// @RunWith(PowerMockRunner.class)
// @PrepareForTest(TestTmp.class)
// public class TestTmpTest {
//     ArgumentCaptor<Runnable> runnables = ArgumentCaptor.forClass(Runnable.class);
//     private TestTmp myClass;

//     @Test
//     public void shouldDoSomthingAsynchrone() throws Exception {
//         myClass = Mockito.spy(new TestTmp());
//         // create a mock for Thread.class
//         Thread mock = Mockito.mock(Thread.class);

//         // mock the 'new Thread', return the mock and capture the given runnable
//         PowerMockito.whenNew(Thread.class).withParameterTypes(Runnable.class).withArguments(runnables.capture())
//                 .thenReturn(mock);

//         myClass.doSomthingAsynchrone();

//         runnables.getValue().run();

//         /**
//          * instead of 'runnables.getValue().run();' you can use a real thread.start
//          *
//          * MockRepository.remove(Thread.class); Thread thread = new
//          * Thread(runnables.getValue()); thread.start(); thread.join();
//          **/

//         verify(myClass, times(1)).doSomthing();
    // }
 }