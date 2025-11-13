package server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextArea;

public class Log {
	
    public static boolean debug = false;
    private static JTextArea textArea = null;
    private static int logCount = 0;

    private Log() {}
    
    public static void SetTextArea(JTextArea t) {
        textArea = t;
    }

	public static void Msg(Object...args) {
        log("Msg", args);
        logCount++;
	}
    
	public static void Err(Object...args) {
        log("Err", args);
        logCount++;
	}
	    
    private static void log(String type, Object...args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stackTrace[2];

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedNow = now.format(formatter);

        String out = "\n[" + type + " " + logCount + "](" + e.getClassName() + " @ " + e.getLineNumber() + ")<" + formattedNow + ">: ";

        for(Object arg : args) {
            out += arg;
        }

        if(textArea != null) {
            textArea.append(out);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
        if(debug) System.err.println(out);
    }
}