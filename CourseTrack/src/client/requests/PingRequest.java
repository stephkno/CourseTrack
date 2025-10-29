package client.requests;
import java.io.Serializable;

public record PingRequest (String message) implements Serializable { }