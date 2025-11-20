package client;

import client.requests.UpdateRequest;
import client.services.IAppGUIService;

public class AppGUI {
    private final IAppGUIService appGUIService;

    public AppGUI(IAppGUIService appGUIService) {
        this.appGUIService = appGUIService;
    }

    public void start() {
        start();
    }

    public Boolean updateData(UpdateRequest updateRequest) {
        return false;
    }
}
