package global.requests;

import global.data.Term;
import java.io.Serializable;

public record ReportRequest(Term term) implements Serializable { }