package utilita;

import connessioni.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsoleDiComando implements Runnable {

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try{
                String command = br.readLine();
                if(command.equals("e")) {
                    System.out.println("INIZIO TEST EMERGENZA");
                    testaEmergenza();
                }
                if(command.equals("!e")) {
                    fineTestaEmergenza();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void testaEmergenza() {
        Connection conn = DatabaseConnection.getConn();

        String query = "update PARAMETRI set RV=1 where TRONCO=3";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
            //L'utente è già stato inserito, non mi interessa
        }
    }

    private void fineTestaEmergenza() {
        Connection conn = DatabaseConnection.getConn();

        String query = "update PARAMETRI set RV=0 where TRONCO=3";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
            //L'utente è già stato inserito, non mi interessa
        }
    }
}
