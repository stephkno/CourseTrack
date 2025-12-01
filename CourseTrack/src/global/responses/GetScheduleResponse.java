package global.responses;
import java.io.Serializable;
import global.data.Section;
import global.LinkedList;

public record GetScheduleResponse(

    LinkedList<Section> enrolledSections,
    LinkedList<Section> waitlistedSections

) implements Serializable { }