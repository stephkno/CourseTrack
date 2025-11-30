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
    static ClientController controller = new ClientController(client);

    public static void main(String[] args) {
        
        controller.start();

        if(!client.connect()){
            Log.Err("Could not connect to server!");
            System.exit(1);
        }

    }
}
