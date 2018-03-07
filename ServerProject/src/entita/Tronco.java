package entita;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tronco {
    public static String ID = "ID";
    public static String X = "X";
    public static String Y = "Y";
    public static String XF = "XF";
    public static String YF = "YF";
    public static String LARGHEZZA = "LARGHEZZA";
    public static String PIANO = "PIANO";
    public static String TABLE_TRONCO = "TRONCO";

    public static String selectAllTronchiByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+LARGHEZZA+","+X+","+Y+","+XF+","+YF+
                " FROM "+TABLE_TRONCO+ " WHERE "+PIANO+"="+piano;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"TRONCO\":{\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"XF\":\""+rs.getFloat(XF)+"\",\"YF\":\""+rs.getFloat(YF)+"\",\"LARGHEZZA\":\""+rs.getFloat(LARGHEZZA)+"\"}},";
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
