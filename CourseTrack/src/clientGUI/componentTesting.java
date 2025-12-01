package clientGUI;

import java.awt.Dimension;

import clientGUI.UIFramework.nButton;
import clientGUI.UIFramework.nFrame;
import clientGUI.UIFramework.nPanelCheckBox;
import clientGUI.UIFramework.nPanelDropDown;
import clientGUI.UIFramework.nScrollableList;

public class componentTesting {
    public static void main(String[] args) {
        nFrame frame = new nFrame("test", 500, 500);

        nButton button1 = new nButton();
        button1.setText("Option 1");
        button1.setPreferredSize(new Dimension(150, 20));
        nButton button2 = new nButton();
        button2.setText("Option 2");
        button2.setPreferredSize(new Dimension(150, 20));
        nButton button3 = new nButton();
        button3.setText("Option 3");
        button3.setPreferredSize(new Dimension(150, 20));
        nButton button4 = new nButton();
        button4.setText("Option 4");
        button4.setPreferredSize(new Dimension(150, 20));
        nPanelDropDown dropdown = new nPanelDropDown(150, 20);
        dropdown.setText("Select option");
        dropdown.setBounds(50, 50, 150, 40);  // collapsed size

        dropdown.addOption(button1);
        dropdown.addOption(button2);
        dropdown.addOption(button3);
        dropdown.addOption(button4);



        frame.add(dropdown);
        frame.setVisible(true);
    }
}
