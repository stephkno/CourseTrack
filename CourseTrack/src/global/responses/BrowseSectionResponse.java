package global.responses;

import global.data.Course;
import global.data.Section;

import global.LinkedList;
import java.io.Serializable;

public record BrowseSectionResponse(
    
    LinkedList<Section> sections

) implements Serializable { }