package global.responses;
import java.io.Serializable;
import global.data.Campus;
import global.LinkedList;

public record GetCampusesResponse (
    LinkedList<Campus> campuses
) implements Serializable { }