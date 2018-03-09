package connessioni;

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

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(dbUrl, "root", "getout2018");
        System.out.println("Connesso al DB");

    }

    public static Connection getConn() {
        return connection;
    }
}
