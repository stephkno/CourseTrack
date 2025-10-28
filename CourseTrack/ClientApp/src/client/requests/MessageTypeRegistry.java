package client.requests;

import client.MessageType;
import client.responses.LoginResponse;
import java.io.Serializable;
import java.util.Map;

public final class MessageTypeRegistry {
    private static final Map<MessageType, Class<? extends Serializable>> TYPE_MAP = Map.of(
        MessageType.USER_LOGIN, LoginResponse.class
    );

    public static Class<? extends Serializable> getClassFor(MessageType type) {
        return TYPE_MAP.get(type);
    }
}