package server;

import global.HashMap;
import global.LinkedList;
import global.Log;
import global.data.Campus;
import global.data.Term;
import java.util.Scanner;
import server.data.*;

public class Shell implements Runnable {
    
    class Command{
        
        String name;

    }

    private Scanner scanner = new Scanner(System.in);

    private void PrintData(String[] args) {

        ServerController server = ServerController.Get();
        HashMap<User> users = User.get();
        HashMap<Campus> campuses = Campus.get();
        LinkedList<Term> terms = Term.get();
        
        Log.Println("Users:");
        for(Object user : users) {
            Log.Println(user.toString());
        }

        Log.Println("Campuses:");
        int c = 0;
        
        for(Object campus : campuses) {
            Log.Print(c++ + " " + campus.toString());
        }

        Log.Println("Terms:");
        for(Object term : terms) {
            Log.Print(term.toString());
        }
    
    }
    
    String help = 
        "Available Commands:\n" +
        "\n" +
        "help\n" +
        "    Show this help message.\n" +
        "\n" +
        "show\n" +
        "    Display all current server data (users, campuses, terms).\n" +
        "\n" +
        "summary\n" +
        "    Print a high-level summary of the data model (counts of campuses, departments, courses, etc).\n" +
        "\n" +
        "clients\n" +
        "    List all currently connected clients and their connection state.\n" +
        "\n" +
        "status\n" +
        "    Show server runtime information (uptime, port, message stats, memory usage).\n" +
        "\n" +
        "log <level>\n" +
        "    Change log verbosity. Levels: error, warn, info, debug.\n" +
        "\n" +
        "kick <clientId>\n" +
        "    Disconnect a client by ID/address.\n" +
        "\n" +
        "ban <username>\n" +
        "    Ban a user account from logging in.\n" +
        "\n" +
        "save\n" +
        "    Save all server data to disk for debugging.\n" +
        "\n" +
        "exit\n" +
        "    Shut down the server and disconnect all clients.\n" +
        "-\n";

        
    void PrintSummary() {}

    void PrintClients()
    {
        for(String clientAddress : Server.Get().getClients()) {
            
            ServerConnection client = Server.Get().getClient(clientAddress);
            Log.Print(client.toString());

        }

    }
    
    void PrintStatus() {

        Log.Print(Server.Get().Uptime());
        Log.Print("\nPort: " + String.valueOf(Server.Get().getPort()));
        Log.Print("\nStatus: " + Server.Get().MessageStats());
        Log.Println("\nMemory: " + Server.Get().MemoryUsage());

    }

    String filename = "data.ct";

    @Override
    public void run() {

        while(Server.Running()) {

            System.out.print("\n CourseTrack $ ");
            String line = scanner.nextLine();

            String[] args = line.split(" ");
            String cmd = args[0];

            switch(cmd.toLowerCase())
            {
                case "":{
                    break;
                }
                case "help":{
                    // print command summary
                    Log.Print(help);
                    break;
                }
                case "data":{
                    PrintData(args);
                    break;
                }
                case "exit":{
                    Server server = Server.Get();
                    server.Hangup();
                    break;
                }
                case "save":{
                    ServerController.Serialize(filename, true);
                    // serialize server data
                    break;
                }
                case "status":{
                    PrintStatus();
                    break;
                }
                case "summary":{
                    PrintSummary();
                    break;
                }
                case "clients":{
                    PrintClients();
                    break;
                }
                case "log":{
                    // set log level
                    Log.Msg(args.length);

                    if(args.length < 3 || args[1].equals("level")) {
                        Log.Println("Syntax error!");
                        break;
                    }

                    Log.Msg(args[1]);

                    int level = Integer.parseInt(args[2]);
                    Log.SetLevel(level);
                    break;
                
                }
                case "kick":{
                    if(args.length <= 1) break;
                    User.get(args[1]).socket.Hangup();
                    break;
                }
                case "ban":{
                    if(args.length <= 1) break;
                    User user = User.get(args[1]);
                    user.socket.Hangup();
                    user.socket.Ban();
                    break;
                }
                default:{
                    Log.Print(" '" + cmd + "' is not a valid command.\n");
                }

            }
        }

    }
}
