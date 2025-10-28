package client;

import client.requests.MessageTypeRegistry;
import client.services.IClientListenerService;
import global.*;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientListener implements Runnable {
    private final Client client;
    private final IClientListenerService controller;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public ClientListener(Client client, IClientListenerService controller) {
        this.client = client;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (running.get() && client.isConnected()) {
            Message<?> request = client.receiveResponse();

            if (request == null) 
                continue;
            
            Class<? extends Serializable> tClass = MessageTypeRegistry.getClassFor(request.getType());

            if (tClass == null) {
                System.err.println("Unknown message type received: " + request.getType());
                continue;
            }

            controller.handleServerMessage(request, tClass);
        }
    }

    public void stopListening() {
        running.set(false);
    }
}