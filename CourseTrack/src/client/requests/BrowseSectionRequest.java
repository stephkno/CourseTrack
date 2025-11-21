package client.requests;

import java.io.Serializable;
import server.data.*;

public record BrowseSectionRequest(
    
    String searchQuery,
    Campus campus,
    Department department,
    Term term

) implements Serializable { }