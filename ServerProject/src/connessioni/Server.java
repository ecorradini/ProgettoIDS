package connessioni;


import java.io.IOException;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws IOException{

        try {
            System.out.println("Server Start");
            DatabaseConnection.init();

        } catch (ClassNotFoundException e) {
            System.out.println("Eccezione Class not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eccezione SQL");
            e.printStackTrace();
        }

        new JsonServer();

    }
}
