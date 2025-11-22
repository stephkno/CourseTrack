package global.requests;

import global.*;
import global.responses.LoginResponse;
import java.io.Serializable;
import java.util.Map;

public final class MessageTypeRegistry {
    private static final Map<MessageType, Class<? extends Serializable>> TYPE_MAP = Map.of(
        MessageType.USER_LOGIN, LoginResponse.class,
        MessageType.PING_REQUEST, PingRequest.class
    );

    public static Class<? extends Serializable> getClassFor(MessageType type) {
        return TYPE_MAP.get(type);
    }
}