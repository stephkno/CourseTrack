package global.requests;
import java.io.Serializable;
import java.util.LinkedList;

import global.data.Term;

public class AdminRequests {

    public record AdminGetUsersRequest() implements Serializable { }

    public record AdminGetCampusesRequest() implements Serializable { }

    public record AdminGetDepartmentsRequest() implements Serializable { }

    public record AdminGetCoursesRequest() implements Serializable { }

    public record AdminGetSectionsRequest() implements Serializable { }

    public record AdminRemoveUserRequest(String userName) implements Serializable { }

    public record AdminRemoveCampusRequest(String campus) implements Serializable { }

    public record AdminRemoveDepartmentRequest(String campus, String department) implements Serializable { }

    public record AdminRemoveCourseRequest(int courseId) implements Serializable { }

    public record AdminRemoveSectionRequest(int sectionId) implements Serializable { }

    public record ReportRequest(Term term) implements Serializable { }
}