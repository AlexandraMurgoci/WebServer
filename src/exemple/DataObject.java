package exemple;

public class DataObject {
    private String data;
    private long timestamp;

    public DataObject(String data, long timestamp) {
        this.data = data;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "data='" + data + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
