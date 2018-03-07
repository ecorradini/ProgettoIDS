package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Beacon {
    public static final String ID = "ID";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String TRONCO = "TRONCO";
    public static final String TABLE_BEACON = "BEACON";

    public static String selectPosizioneById(String idBeacon) {
        Connection conn = DatabaseConnection.getConn();
        String xPos = "", yPos = "";

        String query =  "SELECT "+X+","+Y+
                " FROM "+TABLE_BEACON+" WHERE "+TABLE_BEACON+"."+ID+"="+idBeacon;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                xPos = rs.getString(X);
                yPos = rs.getString(Y);
            }

            rs.close();
            stm.close();
            DatabaseConnection.getConn().commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "{ "+"\"POSIZIONE\":\"{\"X\":\""+xPos+"\",\"Y\":\""+yPos+"\"}}";
    }

    public static String selectAllBeaconsByTronco(String tronco) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+tronco+"\":[";

        String query =  "SELECT "+ID+","+X+","+Y+
                " FROM "+TABLE_BEACON+
                " WHERE "+TRONCO+"="+tronco;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"BEACON\":{\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"ID\":\""+rs.getString(ID)+"\"}},";
            }

            rs.close();
            stm.close();
            DatabaseConnection.getConn().commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Eliminare la virgola finale
        json = json.substring(0,json.length()-2);
        json = json + "]}";

        return json;
    }
}
