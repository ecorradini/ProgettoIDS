package connessioni;

import java.io.Console;
import java.sql.*;

/**
 * Classe interfacciamento con database
 */
public class Database {
    //variabile connessione
    private static Connection connection;
    //Url del database
    private static final String dbUrl="jdbc:jtds:sqlserver://den1.mssql5.gear.host/getoutdb";
    //private static final String dbUrl="jdbc:mysql://localhost/getoutdb";


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
            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, "getoutdb", password);
            System.out.println("Connesso al DB");
        } catch(SQLException e) {
            e.printStackTrace();
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