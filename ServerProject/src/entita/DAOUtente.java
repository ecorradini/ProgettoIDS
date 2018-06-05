package entita;

import connessioni.Database;

import java.sql.*;
import java.util.ArrayList;
/**
 * Data Access Object per utente
 */
public class DAOUtente {

    private static final String IP = "IP";
    private static final String TABLE_UTENTE = "UTENTE";
    /**
     * inserisce un utente
     * @param ip indirizzo identificativo dell' utente nella rete
     */
    public static void insertUtente(String ip) {
        Connection conn = Database.getConn();

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
    /**
     * ritorna tutti gli utenti connessi
     * @return ArrayList<String>
     */
    public static ArrayList<String> getAllUtenti() {
        Connection conn = Database.getConn();
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
