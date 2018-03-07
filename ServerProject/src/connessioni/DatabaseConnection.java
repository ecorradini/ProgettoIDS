package connessioni;

import com.sun.jdi.connect.spi.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static void init(String host) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");   //controllare stringa

        connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:XE","UTE","UTE");

        //rivedere parametro di DriverManager.getConnection

        connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:XE","UTE","UTE");
    }

    public static Connection getConn() {
        return connection;
    }
}
