package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOUscita {

    public static ArrayList<String> getAllUscite(String beacon) {
        Connection conn = DatabaseConnection.getConn();
        ArrayList<String> tronchiDaAttraversare = new ArrayList<>();
        String query = "SELECT BEACONPIANO."+DAOBeacon.ID+" AS "+DAOBeacon.ID+
                       " FROM "+
                            "( SELECT "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+" AS "+DAOBeacon.ID+","+
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

            while(rs.next()) {
                tronchiDaAttraversare.add(rs.getString(DAOBeacon.ID));
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
