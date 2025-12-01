package global.requests;
import global.data.Term;
import java.io.Serializable;

public class StudentRequests {

    public record StudentGetScheduleRequest() implements Serializable { }

    public record StudentGetUnitRequest(Term term) implements Serializable { }

    public record StudentGetProgressRequest() implements Serializable { }
    

}