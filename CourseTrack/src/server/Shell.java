package server;
import java.util.Scanner;

public class Shell implements Runnable {
    
    class Command{
        
        String name;

    }

    private Scanner scanner = new Scanner(System.in);
    
    private boolean run;

    @Override
    public void run(){

        while(ServerApp.run){

            System.out.print(" CourseTrack $ ");
            String line = scanner.nextLine();

            String[] args = line.split(" ");
            String cmd = args[0];

            System.out.println(cmd);

            switch(cmd.toLowerCase())
            {

                case "":{

                    break;
                }
                case "exit":{
                    run = false;
                    break;
                }

            }
        }

    }
}
