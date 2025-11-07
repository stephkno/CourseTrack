package server;

import global.*;

import java.io.Serializable;

import client.requests.PingRequest;

// facade class to implement all client-server event interactions
public class ServerController {

    // singleton
    static ServerController instance = new ServerController();
    public static ServerController Get() {
        return instance;
    }

    ServerController() {}

    public void HandleMessage(Message<?> msg, ServerConnection client){

        Log.Msg("Got message: " + msg.toString());
        switch(msg.getType()){
            case MessageType.PING:{
                HandlePing(msg, client);
            }
            default:{

            }
        }

    }

    private void HandlePing(Message<Ping> msg, ServerConnection client){
        Log.Msg("Ping: " + msg.toString());
    }

    public void LoadData(String filepath){

    }

}
