package server;

import client.requests.PingRequest;
import global.*;

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
            case MessageType.PingRequest:{
                HandlePing((Message<PingRequest>) msg, client);
            }
            default:{

            }
        }

    }

    private void HandlePing(Message<PingRequest> msg, ServerConnection client){
        Log.Msg("PingRequest: " + msg.toString());
    }

    public void LoadData(String filepath){

    }

}
