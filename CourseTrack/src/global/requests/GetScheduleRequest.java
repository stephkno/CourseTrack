package global.requests;

import global.data.Term;
import java.io.Serializable;

public record GetScheduleRequest(
    Term term
) implements Serializable { }