package client;

import client.services.IAppGUIService;
import global.requests.UpdateRequest;

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
