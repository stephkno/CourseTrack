package global.responses;

import java.io.Serializable;
import server.data.*;

public record Error(
    
    String msg

) implements Serializable { }