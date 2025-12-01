package global.responses;
import java.io.Serializable;
import global.data.StudentScheduleItem;
import global.LinkedList;

public record GetScheduleResponse(

    LinkedList<StudentScheduleItem> schedule

) implements Serializable { }