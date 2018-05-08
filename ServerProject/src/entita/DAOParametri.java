package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOParametri {
    static final String TRONCO = "TRONCO";
    static final String VULNERABILITA = "VULN";
    static final String RISCHIOSITA = "RV";
    static final String PRESENZAFUMO = "PF";
    static final String TABLE_PARAMETRI = "PARAMETRI";

    public static final String selectParametri(int tronco){
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+tronco+"\":[";

        String query =  "SELECT "+VULNERABILITA+","+RISCHIOSITA+","+PRESENZAFUMO+","+
                " FROM "+TABLE_PARAMETRI+ " WHERE "+TRONCO+"=\'"+tronco+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"PARAMETRI\":{\"VULN\":\""+rs.getInt(VULNERABILITA)+"\",\"RV\":\""+rs.getFloat(RISCHIOSITA)+"\",\"PF\":\""+rs.getFloat(PRESENZAFUMO)+"\",}},";
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

    public static final boolean controllaEmergenza(){
        boolean emergenza = false;

        Connection conn = DatabaseConnection.getConn();

        String query =  "SELECT "+RISCHIOSITA+","+
                " FROM "+TABLE_PARAMETRI;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next() || emergenza!= true) {
                if(rs.getFloat(RISCHIOSITA)==1){
                    emergenza = true;
                }
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return emergenza;
    }
}
