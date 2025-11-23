package global.responses;

import global.data.Course;
import java.io.Serializable;

public record AddCourseResponse(
    Course course
) implements Serializable { }