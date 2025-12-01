package global.requests;
import java.io.Serializable;
import java.util.LinkedList;

import global.data.Term;
import global.data.Course;
import global.data.Section;

public class AdminRequests {

    public record AdminGetUsersRequest() implements Serializable { }

    public record AdminGetCampusesRequest() implements Serializable { }

    public record AdminGetDepartmentsRequest() implements Serializable { }

    public record AdminGetCoursesRequest() implements Serializable { }

    public record AdminGetSectionsRequest() implements Serializable { }

    public record AdminRemoveUserRequest(String userName) implements Serializable { }

    public record AdminRemoveCampusRequest(String campus) implements Serializable { }

    public record AdminRemoveDepartmentRequest(String campus, String department) implements Serializable { }

    public record AdminRemoveCourseRequest(String campus, String department, Course course) implements Serializable { }

    public record AdminRemoveSectionRequest(Course course, Term term, Section section) implements Serializable { }

    public record ReportRequest(Term term) implements Serializable { }
}