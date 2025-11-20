package clientGUI.UIFramework;
import java.awt.*;

public final class UITheme {
    private UITheme() {
    }

    public static final Color BG_APP = new Color(68, 67, 73);
    public static final Color BG_ELEVATED = new Color(37, 36, 39);// new Color(23, 26, 45);
    public static final Color BG_ELEVATED2 = new Color(37, 36, 39, 230);
    public static final Color BG_ELEVATED_ELEVATED = new Color(103, 100, 110);
    public static final Color BG_ELEVATED_ELEVATED_ELEVATED = new Color(178, 173, 191);
    public static final Color ACCENT = new Color(71, 73, 67); // new Color(38, 39, 36);//new Color(177, 112, 255);
    public static final Color TEXT_PRIMARY = new Color(245, 246, 250);
    public static final Color TEXT_MUTED = new Color(210, 215, 235);

    public static final Color INFO = new Color(50, 103, 181);
    public static final Color FAIL = new Color(133, 29, 34);
    public static final Color SUCCESS = new Color(22, 117, 51);

    public static final int RADIUS_LG = 24;
    public static final int RADIUS_MD = 16;
    public static final int RADIUS_SM = 10;

    public static final Font FONT_H1 = new Font("Consolas", Font.BOLD, 28);
    public static final Font FONT_H2 = new Font("Consolas", Font.BOLD, 20);
    public static final Font FONT_H3 = new Font("Consolas", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Consolas", Font.PLAIN, 14);
}
