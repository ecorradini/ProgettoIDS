package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Aula {
    public static String NOME;
    public static String X;
    public static String Y;
    public static String PIANO;
    public static String TABLE_AULA;

    public static String selectAllAuleByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+NOME+","+X+","+Y+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"AULA\":{\"NOME\":\""+rs.getString(NOME)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\"}},";
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

        System.out.println("RESPONSE: " + json);
        return json;
    }
}
