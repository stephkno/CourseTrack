package global;

public class Notification {
    String message;
    boolean sent = false;

    public Notification(String message) {
        this.message = message;
    }

    public boolean getSent() {
        return sent;
    }

    public void send() {
        this.sent = true;
    }

    public String getMessage() {
        return message;
    }
}
