package global.responses;
import global.data.Campus;
import java.io.Serializable;

public record AddCampusResponse(
    Campus campus
) implements Serializable { }