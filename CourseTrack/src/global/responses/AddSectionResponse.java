package global.responses;
import global.data.Section;
import java.io.Serializable;

public record AddSectionResponse(
    Section section
) implements Serializable { }