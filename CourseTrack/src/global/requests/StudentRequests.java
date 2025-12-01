package global.requests;
import java.io.Serializable;

public class StudentRequests {

    public record StudentGetScheduleRequest() implements Serializable { }

    public record StudentGetUnitRequest() implements Serializable { }

    public record StudentGetProgressRequest() implements Serializable { }
    

}