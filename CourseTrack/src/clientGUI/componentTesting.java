package clientGUI;

import clientGUI.UIFramework.nFrame;
import clientGUI.UIFramework.nPanelCheckBox;

public class componentTesting {
    public static void main(String[] args) {
        nFrame frame = new nFrame("test", 500, 500);
        //frame.setBackground(new Color());
        nPanelCheckBox cb = new nPanelCheckBox();
        cb.setLocation(10, 10);
        frame.add(cb);

        frame.setVisible(true);
    }
}
