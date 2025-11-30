package clientGUI.UIFramework;
import java.awt.GraphicsEnvironment;
import global.Log;

public class checkfontnames {
    public static void main(String[] args) {
        //just for testing
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] fontNames = ge.getAvailableFontFamilyNames();

        Log.Msg("Available font:");
        for (String name : fontNames) {
            Log.Msg(name);
        }
    }
}
