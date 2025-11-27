package global.requests;

import global.data.Term;
import java.io.Serializable;

public record EnrollSectionRequest(
    int sectionId,
    Term term
) implements Serializable { }