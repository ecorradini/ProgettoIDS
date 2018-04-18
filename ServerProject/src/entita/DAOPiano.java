package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOPiano {
    public static final String NOME = "NOME";
    public static final String EDIFICIO = "EDIFICIO";
    public static final String TABLE_PIANO = "PIANO";

    public static String selectPianoByBeacon(String idBeacon) {
        Connection conn = DatabaseConnection.getConn();
        String nomePiano = "";

        String query =  "SELECT "+TABLE_PIANO+"."+NOME+" AS NOME_PIANO"+
                " FROM "+DAOBeacon.TABLE_BEACON+","+DAOTronco.TABLE_TRONCO+","+TABLE_PIANO+","+DAOEdificio.TABLE_EDIFICIO+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\' AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                TABLE_PIANO+"."+NOME+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+" AND "+TABLE_PIANO+
                "."+EDIFICIO+" = "+DAOEdificio.TABLE_EDIFICIO+"."+DAOEdificio.NOME;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomePiano = rs.getString("NOME_PIANO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String json = "{"+"\"PIANO_ATTUALE\":\""+nomePiano+"\""+"}";

        System.out.println("RESPONSE: "+json);

        return json;
    }

    public static String selectAllPianiByEdificio(String edificio) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+edificio+"\":[";

        String query =  "SELECT "+NOME+ " FROM "+TABLE_PIANO+ " WHERE "+DAOEdificio.TABLE_EDIFICIO+"=\'"+edificio+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"PIANO\":\""+rs.getString(NOME)+"\"},";
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            //Eliminare la virgola finale
            if(json.substring(json.length() - 1,json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]}";
        }

        System.out.println("RESPONSE: "+json);

        return json;
    }
}