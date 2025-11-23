package global.responses;

import java.io.Serializable;

public record AddDepartmentResponse(
    String msg
) implements Serializable { }