package global.responses;
import global.data.Section;
import java.io.Serializable;

public record DropSectionResponse(
    Section section
) implements Serializable { }