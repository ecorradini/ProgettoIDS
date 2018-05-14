package entita;

import connessioni.Database;
import connessioni.Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOUscita {

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
}
