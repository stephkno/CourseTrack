package global.responses;
import global.data.Course;
import java.io.Serializable;
import global.LinkedList;

public class StudentResponses {

    public record StudentGetScheduleResponse() implements Serializable { }

    public record StudentGetUnitResponse(int units) implements Serializable { }

    public record StudentGetProgressResponse(LinkedList<Course> completedCourses, int completedUnits) implements Serializable { }

}