package global.responses;

import java.io.Serializable;

import global.data.Section;
import server.data.*;

public record BrowseSectionResponse(
    
    Section section

) implements Serializable { }