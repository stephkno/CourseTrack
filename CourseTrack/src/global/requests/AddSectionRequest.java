package global.requests;
import global.data.Term;
import java.io.Serializable;

public record AddSectionRequest(
    
    int courseId,
    String campus,
    String department,
    Term term,
    String instructor,
    int capacity

) implements Serializable { }