package global.responses;
import java.io.Serializable;

public record PasswordChangeResponse (String password) implements Serializable { }