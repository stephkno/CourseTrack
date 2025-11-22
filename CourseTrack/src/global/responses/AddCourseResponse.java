package global.responses;

import java.io.Serializable;
import server.data.Course;

public record AddCourseResponse(
    Course course
) implements Serializable { }