package client.requests;
import java.io.Serializable;

public record Ping (String message) implements Serializable {
    private static final long serialVersionUID = 1L;

    public String toString(){
        return "<PING> msg: " + message;
    }
}