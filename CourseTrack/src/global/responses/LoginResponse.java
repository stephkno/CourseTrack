package global.responses;

import client.User;
import java.io.Serializable;

public record LoginResponse(User user) implements Serializable { }