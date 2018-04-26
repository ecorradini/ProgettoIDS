package connessioni;


import java.io.Console;
import java.io.IOException;
import java.sql.SQLException;

public class Server {
    public static String password;
    public static void main(String[] args) throws IOException {


        System.out.println("Server Start");
        //Chiedo la password del Server
        Console console = System.console();
        if (console == null) {
            System.out.println("Istanza della console nulla");
            System.exit(0);
        }
        console.printf("%n");
        char passwordArray[] = console.readPassword("Password del database: ");
        password = new String(passwordArray);

        new JsonServer();

    }
}