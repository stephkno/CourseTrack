package global.requests;
import java.io.Serializable;
import global.LinkedList;
import global.data.Course;

public record AddCourseRequest(
    String name,
    int number,
    int units,
    String campus,
    String department,
    LinkedList<Course> requirements
) implements Serializable { }