package global.requests;

import java.io.Serializable;

public record LoginRequest(String username, String password) implements Serializable { }