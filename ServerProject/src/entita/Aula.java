package entita;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Aula {
    public static String NOME = "NOME";
    public static String X = "X";
    public static String Y = "Y";
    public static String PIANO = "PIANO";
    public static String TABLE_AULA = "AULA";

    public static String selectAllAuleByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+NOME+","+X+","+Y+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"="+piano;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"AULA\":{\"NOME\":\""+rs.getString(NOME)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\"}},";
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
