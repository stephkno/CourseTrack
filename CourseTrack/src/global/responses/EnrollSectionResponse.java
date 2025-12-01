package global.responses;

import global.data.Section;
import java.io.Serializable;

// if waitlist_position == 0 then student is not waitlisted
public record EnrollSectionResponse(
    
    Section section,
    int waitlist_position,
    EnrollStatus status

) implements Serializable { }