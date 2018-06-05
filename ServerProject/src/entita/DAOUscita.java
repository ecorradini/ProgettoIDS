package entita;

import connessioni.Database;
import connessioni.Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Data Access Object di uscita
 */
public class DAOUscita {

    /**
     * ritorna i tronchi in cui ci sono le uscite a partire da dove ci si trova
     * @param beacon identificatico del beacon per cui si vuole svolgere l' operazione
     * @param edificio identificatico del edificio per cui si vuole svolgere l' operazione
     * @param piano identificatico del piano per cui si vuole svolgere l' operazione
     * @return ArrayList<Tronco>
     */
    public static ArrayList<Tronco> getTronchiUscita(String beacon,String edificio,String piano) {
        Connection conn = Database.getConn();
        ArrayList<Tronco> tronchiDaAttraversare = new ArrayList<>();

        String query = "SELECT BEACONPIANO."+DAOBeacon.TRONCO+" AS "+DAOBeacon.TRONCO+
                       " FROM "+
                            "( SELECT "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+" AS "+DAOBeacon.TRONCO+","+
                                DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+" AS "+DAOTronco.PIANO+","+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.USCITA+" AS "+DAOBeacon.USCITA+
                            " FROM "+DAOBeacon.TABLE_BEACON+
                            " JOIN "+DAOTronco.TABLE_TRONCO+
                            " ON "+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+"="+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+
                            " WHERE "+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+"= "+
                                    " (SELECT "+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+
                                    " FROM "+DAOBeacon.TABLE_BEACON+
                                    " JOIN "+DAOTronco.TABLE_TRONCO+
                                    " ON "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+
                                    " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+beacon+"\'"+
                                    ")"+
                            ") AS BEACONPIANO "+
                       " WHERE "+DAOBeacon.USCITA+"=1";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            HashMap<Integer,Tronco> tronchiPiano = Server.getGrafiPiani().get(edificio).get(piano).getTronchiPiano();

            while(rs.next()) {
                tronchiDaAttraversare.add(tronchiPiano.get(rs.getInt(DAOBeacon.TRONCO)));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tronchiDaAttraversare;
    }

    /**
     * ritorna i beacon di uscita di un edificio per il funzionamento offline
     * @param edificio identificativo dell' edificio per cui si vogliono i beacon di uscita
     * @return stringa JSON
     */

    public static String getBeaconUscitaEdificio(String edificio) {
        Connection conn = Database.getConn();
        ArrayList<String> beaconUscita = new ArrayList<>();

        String query = "SELECT "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+" AS ID"+
                        " FROM "+DAOBeacon.TABLE_BEACON+","+DAOTronco.TABLE_TRONCO+","+DAOPiano.TABLE_PIANO+
                        " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+
                        " AND "+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+"="+DAOPiano.TABLE_PIANO+"."+DAOPiano.NOME+
                        " AND "+DAOPiano.TABLE_PIANO+"."+DAOPiano.EDIFICIO+"='"+edificio+"'"+
                        " AND "+DAOBeacon.USCITA+"=1";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                beaconUscita.add(rs.getString(DAOBeacon.ID));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String json = "{USCITE:[";
        for(int i=0; i<beaconUscita.size();i++) {
            json = json + "\""+beaconUscita.get(i)+"\",";
        }
        if(json.substring(json.length() - 1,json.length()).equals(",")) {
            json = json.substring(0, json.length() - 1);
        }
        json = json + "]}";

        return json;
    }
}
