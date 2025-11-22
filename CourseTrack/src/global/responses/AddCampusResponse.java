package global.responses;
import java.io.Serializable;

import global.data.Campus;

public record AddCampusResponse(
    Campus campus
) implements Serializable { }