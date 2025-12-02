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

    public static void main(String[] args) {
		var client = new Client("localhost", 7777);

		if (!client.connect()) {
            Log.Err("Could not connect to server.");
			return;
		}

		var controller = new ClientController(client);
		controller.start();
		
	}
}
