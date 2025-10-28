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
}
