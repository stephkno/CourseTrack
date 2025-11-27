package global.responses;
import java.io.Serializable;
import global.data.Section;

public record GetScheduleResponse(

    Section[] enrolledClasses

) implements Serializable { }