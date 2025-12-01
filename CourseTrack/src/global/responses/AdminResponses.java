package global.responses;
import java.io.Serializable;
import global.LinkedList;
import server.data.User;
import global.data.*;

public class AdminResponses {

    public record AdminGetUsersResponse(LinkedList<User> users) implements Serializable { }

    public record AdminGetCampusesResponse(LinkedList<Campus> campuses) implements Serializable { }

    public record AdminGetDepartmentsResponse(LinkedList<Department> departments) implements Serializable { }
        
    public record AdminGetCoursesResponse(LinkedList<Course> courses) implements Serializable { }

    public record AdminGetSectionsResponse(LinkedList<Section> sections) implements Serializable { }

    public record AdminRemoveUserResponse() implements Serializable { }

    public record AdminRemoveCampusResponse() implements Serializable { }

    public record AdminRemoveDepartmentResponse() implements Serializable { }
    
    public record AdminRemoveCourseResponse() implements Serializable { }

    public record AdminRemoveSectionResponse() implements Serializable { }
    
    public record DisplayReport(LinkedList<String> reportEntries) implements Serializable { }

}
