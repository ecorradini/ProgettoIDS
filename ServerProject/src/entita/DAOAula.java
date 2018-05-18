package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOAula {
    static final String NOME = "NOME";
    static final String X = "X";
    static final String Y = "Y";
    static final String PIANO = "PIANO";
    static final String ENTRATA = "ENTRATA";
    static final String TABLE_AULA = "AULA";

    public static String selectAllAuleByPiano(String piano) {
        Connection conn = Database.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+NOME+","+X+","+Y+","+ENTRATA+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"AULA\":{\"NOME\":\""+rs.getString(NOME)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"ENTRATA\":\""+rs.getString(ENTRATA)+"\"}},";
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

        return json;
    }

    public static ArrayList<String> selectListaAuleByPiano(String piano) {
        Connection conn = Database.getConn();
        ArrayList<String> aule = new ArrayList<String>();
        String query =  "SELECT "+NOME+","+X+","+Y+","+ENTRATA+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                aule.add(rs.getString("NOME"));
            }
            rs.close();
            stm.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return aule;
    }
}