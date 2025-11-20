package UIFramework;
import java.awt.GraphicsEnvironment;

public class checkfontnames {
    public static void main(String[] args) {
        //just for testing
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] fontNames = ge.getAvailableFontFamilyNames();

        System.out.println("avvailable font:");
        for (String name : fontNames) {
            System.out.println(name);
        }
    }
}
