package traffic_model;

public class TrafficModel implements java.io.Serializable {

    private byte[] MESSAGE, OBJECT;

    public TrafficModel(){}

    public byte[] getMessage() {
        return MESSAGE;
    }

    public byte[] getObject() {
        return OBJECT;
    }

    public TrafficModel setMessage(byte[] message) {
        MESSAGE = message;
        return this;
    }

    public TrafficModel setObject(byte[] object) {
        OBJECT = object;
        return this;
    }
    
}