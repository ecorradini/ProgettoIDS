package entita;

import connessioni.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class DAOUtente {

    private static final String IP = "IP";
    private static final String TABLE_UTENTE = "UTENTE";

    public static void insertUtente(String ip) {
        Connection conn = DatabaseConnection.getConn();

        String query = "INSERT INTO "+TABLE_UTENTE+" VALUES(\'"+ip+"\')";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
            //L'utente è già stato inserito, non mi interessa
        }
    }

    public static ArrayList<String> getAllUtenti() {
        Connection conn = DatabaseConnection.getConn();
        ArrayList<String> utenti = new ArrayList<>();
        String query = "SELECT "+IP+" FROM "+TABLE_UTENTE;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                utenti.add(rs.getString(IP));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utenti;
    }
}
