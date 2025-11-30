package global.requests;
import java.io.Serializable;
import global.Notification;
import global.LinkedList;

public record NotificationRequest(
    LinkedList<Notification> notifications
) implements Serializable { }