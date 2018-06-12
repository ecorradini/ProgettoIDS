package connessioni;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe interfacciamento con database
 */
public class Database {
    //variabile connessione
    private static Connection connection;
    //Url del database
    private static final String dbUrl="jdbc:mysql://localhost/getoutdb";


    /**
     * Metodo di inizializzazione connessione al database
     * @throws ClassNotFoundException
     */
    public static void init() throws ClassNotFoundException {

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

            //Instanzio la connessione al databse
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, "getoutdb", password);
            System.out.println("Connesso al DB");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            init();
        }

    }

    /**
     * Restituisce la connessione al database
     * @return instanza della classe
     */
    public static Connection getConn() {
        return connection;
    }


}