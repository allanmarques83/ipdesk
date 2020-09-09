package traffic_model;

public class TrafficModel implements java.io.Serializable {

    private byte[] MESSAGE, IMAGE, FILE, SPEAKER;

    public TrafficModel(){}

    public byte[] getMessage() {
        return MESSAGE;
    }

    public byte[] getImage() {
        return IMAGE;
    }

    public byte[] getFile() {
        return FILE;
    }

    public byte[] getSpeaker() {
        return SPEAKER;
    }

    public TrafficModel setMessage(byte[] message) {
        MESSAGE = message;
        return this;
    }

    public TrafficModel setImage(byte[] image) {
        IMAGE = image;
        return this;
    }
    
    public TrafficModel setFile(byte[] file) {
        FILE = file;
        return this;
    }
    
    public TrafficModel setSpeaker(byte[] speaker) {
        SPEAKER = speaker;
        return this;
    }
    
}