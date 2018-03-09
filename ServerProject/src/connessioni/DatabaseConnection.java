package connessioni;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    public static final String dbUrl="jdbc:mysql://localhost:3306/getout";

    public static void init() throws ClassNotFoundException, SQLException {
        //Chiedo la password del Server
        Console console = System.console();
        if (console == null) {
            System.out.println("Istanza della console nulla");
            System.exit(0);
        }
        console.printf("%n");
        char passwordArray[] = console.readPassword("Password del database: ");
        String password = new String(passwordArray);

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(dbUrl, "getout_user", password);
        System.out.println("Connesso al DB");

    }

    public static Connection getConn() {
        return connection;
    }
}
