package client.requests;
import java.io.Serializable;

public class Ping implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public Ping(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString(){
        return "<PING> msg: " + message;
    }
}