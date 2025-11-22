package global.requests;
import java.io.Serializable;

public record AddCampusRequest (String campusName) implements Serializable { }