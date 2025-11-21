package client.requests;

import java.io.Serializable;
import server.data.*;

public record BrowseSectionResponse(
    
    Section section

) implements Serializable { }