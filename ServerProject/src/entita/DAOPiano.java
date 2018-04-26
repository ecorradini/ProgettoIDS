package entita;

import connessioni.Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOPiano {
    static final String NOME = "NOME";
    static final String EDIFICIO = "EDIFICIO";
    static final String TABLE_PIANO = "PIANO";

    public static String selectPianoByBeacon(String idBeacon) {
        Database db = new Database();        String nomePiano = "";

        String query =  "SELECT "+TABLE_PIANO+"."+NOME+" AS NOME_PIANO"+
                " FROM "+DAOBeacon.TABLE_BEACON+","+DAOTronco.TABLE_TRONCO+","+TABLE_PIANO+","+DAOEdificio.TABLE_EDIFICIO+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\' AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                TABLE_PIANO+"."+NOME+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+" AND "+TABLE_PIANO+
                "."+EDIFICIO+" = "+DAOEdificio.TABLE_EDIFICIO+"."+DAOEdificio.NOME;

        try {
            Statement stm = db.connetti();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomePiano = rs.getString("NOME_PIANO");
            }

            rs.close();
            stm.close();
            db.chiudiConnessione();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String json = "{"+"\"PIANO_ATTUALE\":\""+nomePiano+"\""+"}";

        System.out.println("RESPONSE: "+json);

        return json;
    }

    public static String selectAllPianiByEdificio(String edificio) {
        Database db = new Database();

        String json="{\""+edificio+"\":[";

        String query =  "SELECT "+NOME+ " FROM "+TABLE_PIANO+ " WHERE "+DAOEdificio.TABLE_EDIFICIO+"=\'"+edificio+"\'";

        try {
            Statement stm = db.connetti();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"PIANO\":\""+rs.getString(NOME)+"\"},";
            }

            rs.close();
            stm.close();
            db.chiudiConnessione();
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