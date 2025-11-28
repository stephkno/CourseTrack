package client;

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
            Message<?> msg = client.internalReceive();

            if (msg == null) 
                continue;

            if (!client.routeIncoming(msg))
                controller.handleServerMessage(msg, Serializable.class);
        }
    }

    public void stopListening() {
        running.set(false);
    }
}