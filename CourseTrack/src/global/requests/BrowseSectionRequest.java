package global.requests;

import java.io.Serializable;

import global.data.Campus;
import global.data.Department;
import global.data.Term;
import server.data.*;

public record BrowseSectionRequest(
    
    String searchQuery,
    Campus campus,
    Department department,
    Term term

) implements Serializable { }