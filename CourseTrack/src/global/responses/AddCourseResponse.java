package global.responses;

import java.io.Serializable;

import global.data.Course;

public record AddCourseResponse(
    Course course
) implements Serializable { }