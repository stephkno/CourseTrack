package global;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public final class Message<TObjMessage extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong counter = new AtomicLong(0);

    private final long id;
    private final MessageType msgType;
    private final MessageStatus msgStatus;
    private final TObjMessage[] arguments;

    private static final String[] messageTypes = {
        "PING",
        "USER_REGISTER",
        "USER_LOGIN",
        "USER_LOGIN_SUCCESS",
        "USER_LOGIN_FAILURE",
        "USER_UPDATE",
        "USER_LOGOUT",
        "STUDENT_SEARCH_COURSE",
        "STUDENT_ENROLL",
        "STUDENT_DROP",
        "STUDENT_WAS_WAITLISTED",
        "STUDENT_WAS_ENROLLED",
        "STUDENT_WAITLIST_PROMOTION",
        "STUDENT_GET_ENROLLED_COURSES",
        "STUDENT_GENEREATE_SCHEDULE",
        "STUDENT_GET_UNITS",
        "STUDENT_GET_CLASSES_TAKEN",
        "STUDENT_NOT_MET_REQUIREMENTS",
        "STUDENT_SCHEDULE_CONFLICT",
        "STUDENT_MAX_CREDITS_EXCEEDED",
        "STUDENT_COURSE_FULL",
        "STUDENT_COURSE_NOT_FOUND",
        "STUDENT_GET_WAITLIST_PLACE",
        "ADMIN_GET_SCHEDULE_REPORT",
        "ADMIN_GET_ENROLLMENT_REPORT",
        "ADMIN_ADD_UNIVERSITY",
        "ADMIN_REMOVE_UNIVERSITY",
        "ADMIN_ADD_DEPARTMENT",
        "ADMIN_REMOVE_DEPARTMENT",
        "ADMIN_ADD_TERM",
        "ADMIN_REMOVE_TERM",
        "ADMIN_ADD_COURSE",
        "ADMIN_REMOVE_COURSE",
        "ADMIN_GET_USERS",
        "ADMIN_REMOVE_USER",
        "ADMIN_GET_CAMPUSES",
        "ADMIN_GET_DEPARTMENTS",
        "ADMIN_GET_TERMS",
        "ADMIN_GET_COURSES",
        "REGISTER_SUCCESS",
        "REGISTER_FAILURE",
        "USER_CONNECTED",
        "USER_DISCONNECTED",
        "MESSAGE_RECEIVED",
        "CLIENT_UPDATE",
        "ERROR"
    };

    private static final String[] messageStatus = {
        "REQUEST",
        "RESPONSE",
        "SUCCESS",
        "FAILURE"
    };

    public Message(MessageType type, MessageStatus status, TObjMessage[] arguments) {
        this.id = counter.incrementAndGet();
        this.msgType = type;
        this.msgStatus = status;
        this.arguments = arguments;
    }

    public long getId() { return id; }
    public MessageType getType() { return msgType; }
    public MessageStatus getStatus() { return msgStatus; }
    public TObjMessage[] getArguments() { return arguments; }
    
    public String getTypeString(){
        return messageTypes[msgType.ordinal()];
    }

    public String getStatusString(){
        return messageStatus[msgStatus.ordinal()];
    }

    public String toString(){

        String out = "\n+ Message +";

        out += "\n - Type: " + getTypeString();
        out += "\n - Status: " + getStatusString();
        out += "\n - Args: ";

        int i = 0;
        if(arguments == null)
            out += "0";
        else
            for(Object argument : arguments) out += "\n - - " + i++ + ": " + argument.toString();

        return out;
    }
}
