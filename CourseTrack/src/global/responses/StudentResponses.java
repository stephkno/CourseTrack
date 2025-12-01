package global.responses;
import java.io.Serializable;

public class StudentResponses {

    public record StudentGetScheduleResponse() implements Serializable { }

    public record StudentGetUnitResponse() implements Serializable { }

    public record StudentGetProgressResponse() implements Serializable { }

}