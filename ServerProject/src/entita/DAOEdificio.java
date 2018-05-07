package entita;

import connessioni.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOEdificio {
    //Nome della colonna "NOME"
    static final String NOME = "NOME";
    //Nome della tabella "EDIFICIO"
    static final String TABLE_EDIFICIO = "EDIFICIO";

    public static String selectEdificioByBeacon(String idBeacon) {
        Connection conn = DatabaseConnection.getConn();
        String nomeEdificio = "";

        String query = "SELECT "+TABLE_EDIFICIO+"."+NOME+" AS NOME_EDIFICIO"+
                " FROM "+TABLE_EDIFICIO+","+DAOPiano.TABLE_PIANO+","+DAOTronco.TABLE_TRONCO+","+DAOBeacon.TABLE_BEACON+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+"="+DAOPiano.TABLE_PIANO+"."+DAOPiano.NOME+" AND "+
                DAOPiano.TABLE_PIANO+"."+DAOPiano.EDIFICIO+"="+TABLE_EDIFICIO+"."+NOME+" AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\'";
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomeEdificio = rs.getString("NOME_EDIFICIO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String json = "{"+"\"EDIFICIO_ATTUALE\":\""+nomeEdificio+"\""+"}";

        System.out.println("RESPONSE: "+json);

        return json;
    }

    public static ArrayList<String> selectEdifici() {
        Connection conn = DatabaseConnection.getConn();
        ArrayList<String> edifici = new ArrayList<>();

        String query = "SELECT "+NOME+" FROM "+TABLE_EDIFICIO;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                edifici.add(rs.getString(NOME));
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return edifici;
    }

    public static int selectCountEdifici() {
        Connection conn = DatabaseConnection.getConn();
        int nEdifici=0;

        String query = "SELECT COUNT("+NOME+") AS NUMERO FROM "+TABLE_EDIFICIO;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nEdifici = rs.getInt("NUMERO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nEdifici;
    }
}

