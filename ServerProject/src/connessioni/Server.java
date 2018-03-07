package connessioni;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws IOException{
        PlugConnection.init(args[0],9600);

        try {
            DatabaseConnection.init(args[1]);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JsonServer server = JsonServer();

    }
}
