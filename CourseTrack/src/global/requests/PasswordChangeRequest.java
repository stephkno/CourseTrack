package global.requests;
import java.io.Serializable;

public record PasswordChangeRequest (String password) implements Serializable { }