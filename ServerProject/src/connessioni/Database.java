package connessioni;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;

    public Database() {
        try {
            String dbUrl="jdbc:jtds:sqlserver://den1.mssql5.gear.host/getoutdb";
            connection = DriverManager.getConnection(dbUrl, "getoutdb", Server.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connesso al DB");
    }

    public Statement connetti() throws SQLException {
        return connection.createStatement();
    }

    public void chiudiConnessione() throws SQLException {
        connection.close();
    }
}