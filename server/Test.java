import java.lang.reflect.Method;
import java.lang.reflect.*;

import traffic_model.TrafficModel;

public class Test {

    public static void main(String[] args) {
        try {

            Method sumInstanceMethod = Test0.class.getMethod(
                "oi", TrafficModel.class);

            Test0 operationsInstance = new Test0("jh");
            int result = (Integer) sumInstanceMethod.invoke(operationsInstance,new TrafficModel());

            System.out.println(result);
            // Class cls = Class.forName("Test0");
            // Constructor ct = cls.getConstructor();
            // ct.newInstance(new Object[]{"as"});


            // Class partypes[] = new Class[2];
            // partypes[0] = Integer.TYPE;
            // partypes[1] = Integer.TYPE;

            // Method met = cls.getMethod("oi", partypes);
            // met.invoke();
        
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
