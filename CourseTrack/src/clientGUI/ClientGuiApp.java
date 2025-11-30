package clientGUI;

import client.*;
import client.services.IAppGUIService;
import clientGUI.Pages.LoginPage;
import clientGUI.UIFramework.ButtonInterface;
import clientGUI.UIInformations.LoginInformation;
import clientGUI.UIFramework.*;
import global.Log;

@SuppressWarnings("unused")
public class ClientGuiApp {

    static LoginInformation lInfo = new LoginInformation();

    static Client client = new Client("localhost", 7777);
    static ClientController controller;

    public static void main(String[] args) {
        
        ClientController.start(client);
        controller = ClientController.get();

        if(!client.connect()){
            Log.Err("Could not connect to server!");
            System.exit(1);
        }
		controller.start();

    }
}
