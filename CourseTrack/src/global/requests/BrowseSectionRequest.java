package global.requests;

import global.data.Term;
import java.io.Serializable;

public record BrowseSectionRequest(
    
    String searchQuery,
    String campus,
    String department,
    Term term,
    int max_results

) implements Serializable { }