package global.responses;
import java.io.Serializable;
import server.data.Campus;

public record AddCampusResponse(
    Campus campus
) implements Serializable { }