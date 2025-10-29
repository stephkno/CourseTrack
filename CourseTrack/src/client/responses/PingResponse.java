package client.responses;
import java.io.Serializable;

public record PingResponse (String message) implements Serializable { }