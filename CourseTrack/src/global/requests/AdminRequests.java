package global.requests;
import java.io.Serializable;
import java.util.LinkedList;

public class AdminRequests {
    public record AdminGetUsersRequest() implements Serializable { }

    public record AdminGetCampusesRequest() implements Serializable { }

    public record AdminGetDepartmentsRequest() implements Serializable { }

    public record AdminGetCoursesRequest() implements Serializable { }

    public record AdminGetSectionsRequest() implements Serializable { }

    public record AdminRemoveUserRequest(int userId) implements Serializable { }

    public record AdminRemoveCampusRequest(int campusId) implements Serializable { }

    public record AdminRemoveDepartmentRequest(int departmentId) implements Serializable { }

    public record AdminRemoveCourseRequest(int courseId) implements Serializable { }

    public record AdminRemoveSectionRequest(int sectionId) implements Serializable { }

    public record AdminGetUsersResponse(LinkedList<?> users) implements Serializable { }

    public record AdminGetCampusesResponse(LinkedList<?> campuses) implements Serializable { }

    public record AdminGetDepartmentsResponse(LinkedList<?> departments) implements Serializable { }

    public record AdminGetCoursesResponse(LinkedList<?> courses) implements Serializable { }

    public record AdminGetSectionsResponse(LinkedList<?> sections) implements Serializable { }

    public record AdminRemoveUserResponse() implements Serializable { }

    public record AdminRemoveCampusResponse() implements Serializable { }

    public record AdminRemoveDepartmentResponse() implements Serializable { }

    public record AdminRemoveCourseResponse() implements Serializable { }

    public record AdminRemoveSectionResponse() implements Serializable { }


    }