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
                " FROM "+TABLE_BEACON+" WHERE "+TABLE_BEACON+"."+ID+"=\'"+idBeacon+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                xPos = rs.getString(X);
                yPos = rs.getString(Y);
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String json = "{ "+"\"POSIZIONE\":\"{\"X\":\""+xPos+"\",\"Y\":\""+yPos+"\"}}";

        System.out.println("RESPONSE: "+json);

        return json;
    }

    public static String selectAllBeaconsByTronco(String tronco) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+tronco+"\":[";

        String query =  "SELECT "+ID+","+X+","+Y+
                " FROM "+TABLE_BEACON+
                " WHERE "+TRONCO+"=\'"+tronco+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"BEACON\":{\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"ID\":\""+rs.getString(ID)+"\"}},";
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
