package global;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public final class Message<TObjMessage extends Serializable> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final AtomicLong counter = new AtomicLong(0);

    public static String[] messageStatus = {
        "REQUEST",
        "RESPONSE",
        "SUCCESS",
        "FAILURE"
    };

    public static String[] messageTypes = {
        "PING_REQUEST",
        "USER_REGISTER",
        "USER_LOGIN",
        "USER_LOGOUT",
        "USER_CHANGE_PASSWORD",
        "USER_UPDATE",
        "STUDENT_SEARCH_COURSE",
        "STUDENT_ENROLL",
        "STUDENT_DROP",
        "STUDENT_WAS_WAITLISTED",
        "STUDENT_WAS_ENROLLED",
        "STUDENT_WAITLIST_PROMOTION",
        "STUDENT_GET_SCHEDULE",
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
        "ADMIN_ADD_CAMPUS",
        "ADMIN_REMOVE_UNIVERSITY",
        "ADMIN_ADD_DEPARTMENT",
        "ADMIN_REMOVE_DEPARTMENT",
        "ADMIN_ADD_TERM",
        "ADMIN_REMOVE_TERM",
        "ADMIN_ADD_COURSE",
        "ADMIN_REMOVE_COURSE",
        "ADMIN_ADD_SECTION",
        "ADMIN_REMOVE_SECTION",
        "ADMIN_GET_USERS",
        "ADMIN_REMOVE_USER",
        "ADMIN_GET_CAMPUSES",
        "ADMIN_GET_DEPARTMENTS",
        "ADMIN_GET_TERMS",
        "ADMIN_GET_COURSES",
        "ADMIN_GET_SECTIONS",
        "ADMIN_GET_REPORT",
        "REGISTER_SUCCESS",
        "REGISTER_FAILURE",
        "USER_CONNECTED",
        "USER_DISCONNECTED",
        "MESSAGE_RECEIVED",
        "CLIENT_UPDATE",
        "NOTIFICATION",
        "ERROR"
    };

    private final long id;
    private final MessageType msgType;
    private final MessageStatus msgStatus;
    private final TObjMessage messageObject;

    public Message(MessageType type, MessageStatus status, TObjMessage messageObject) {
        this.id = counter.incrementAndGet();
        this.msgType = type;
        this.msgStatus = status;
        this.messageObject = messageObject;
    }

    public long getId() { return id; }
    
    public MessageType getType() { return msgType; }

    public MessageStatus getStatus() { return msgStatus; }

    public TObjMessage get() { 
        return messageObject; 
    }
    
    public String getTypeString() {
        return messageTypes[msgType.ordinal()];
    }

    public String getStatusString() {
        return messageStatus[msgStatus.ordinal()];
    }

    public String toString() {

        String out = "\n+ Message +";

        out += "\n - Type: " + getTypeString();
        out += "\n - Status: " + getStatusString();
        out += "\n - Content: " + messageObject.toString();

        return out;
    }

}
