package client.requests;

import java.io.Serializable;
import server.data.Department;
import server.data.Term;

public record AddCourseRequest(
    
    String name,
    int number,
    int units,
    Term term,
    Department department

) implements Serializable { }