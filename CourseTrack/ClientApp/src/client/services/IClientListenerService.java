package client.services;

import java.io.Serializable;

import client.Message;

public interface IClientListenerService {
    void handleServerMessage(Message<?> request, Class<? extends Serializable> tClass);
}
