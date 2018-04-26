package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOAula {
    static final String NOME = "NOME";
    static final String X = "X";
    static final String Y = "Y";
    static final String PIANO = "PIANO";
    static final String TABLE_AULA = "AULA";

    public static String selectAllAuleByPiano(String piano) {
        Database db = new Database();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+NOME+","+X+","+Y+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = db.connetti();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"AULA\":{\"NOME\":\""+rs.getString(NOME)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\"}},";
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

        System.out.println("RESPONSE: " + json);
        return json;
    }
}