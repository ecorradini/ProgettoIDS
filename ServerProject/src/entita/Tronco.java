package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tronco {
    public static final String ID = "ID";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String XF = "XF";
    public static final String YF = "YF";
    public static final String LARGHEZZA = "LARGHEZZA";
    public static final String PIANO = "PIANO";
    public static final String TABLE_TRONCO = "TRONCO";

    public static String selectAllTronchiByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+LARGHEZZA+","+X+","+Y+","+XF+","+YF+
                " FROM "+TABLE_TRONCO+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"TRONCO\":{\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"XF\":\""+rs.getFloat(XF)+"\",\"YF\":\""+rs.getFloat(YF)+"\",\"LARGHEZZA\":\""+rs.getFloat(LARGHEZZA)+"\"}},";
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            //Eliminare la virgola finale
            json = json.substring(0, json.length() - 1);
            json = json + "]}";

            System.out.println("RESPONSE: " + json);
        }
        return json;
    }
}
