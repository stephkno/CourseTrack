package global.requests;
import java.io.Serializable;

public record AddCourseRequest(
    String name,
    int number,
    int units,
    String campus,
    String department
) implements Serializable { }