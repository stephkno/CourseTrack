package global.requests;
import java.io.Serializable;

public record AddDepartmentRequest(
    
    String campus,
    String name

) implements Serializable { }