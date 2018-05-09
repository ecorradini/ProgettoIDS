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
    public static final String dbUrl="jdbc:jtds:sqlserver://den1.mssql5.gear.host/getoutdb";

    public static void init() throws ClassNotFoundException, SQLException {

        try {

            //Chiedo la password del Server
            Console console = System.console();
            if (console == null) {
                System.out.println("Istanza della console nulla");
                System.exit(0);
            }
            console.printf("%n");
            char passwordArray[] = console.readPassword("Password del database: ");
            String password = new String(passwordArray);


            //String password = "getout2018@";

            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, "getoutdb", password);
            System.out.println("Connesso al DB");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            init();
        }

    }

    public static Connection getConn() {
        return connection;
    }
}