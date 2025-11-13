package global;

public enum MessageType {
    PingRequest,
    // Client to server requests
    USER_REGISTER,
    USER_LOGIN,
    USER_LOGOUT,
    USER_CHANGE_PASSWORD,
    USER_SEARCH_COURSE,
    USER_ENROLL,
    USER_DROP,
    USER_WAS_WAITLISTED,
    USER_WAS_ENROLLED,
    ADMIN_ADD_UNIVERSITY,
    ADMIN_ADD_DEPARTMENT,
    ADMIN_ADD_COURSE,
    ADMIN_REMOVE_COURSE,
    // Server to client responses
    REGISTER_SUCCESS,
    REGISTER_FAILURE,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    // Server to client notifications
    USER_CONNECTED,
    USER_DISCONNECTED,
    MESSAGE_RECEIVED,
    ERROR,
    UPDATE,
};