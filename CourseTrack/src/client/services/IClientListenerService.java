package client.services;

import global.*;
import java.io.Serializable;

public interface IClientListenerService {
    void handleServerMessage(Message<?> request, Class<? extends Serializable> tClass);
}
