package global.responses;

import java.io.Serializable;

public record ErrorResponse(
    
    String msg

) implements Serializable { }