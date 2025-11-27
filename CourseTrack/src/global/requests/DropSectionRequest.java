package global.requests;

import global.data.Term;
import java.io.Serializable;

public record DropSectionRequest(
    int sectionId,
    Term term
) implements Serializable { }