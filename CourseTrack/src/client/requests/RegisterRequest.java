package client.requests;
import client.UserType;
import java.io.Serializable;

public record RegisterRequest (String username, String password, UserType type) implements Serializable { }