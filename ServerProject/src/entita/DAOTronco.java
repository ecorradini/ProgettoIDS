package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOTronco {
    static final String ID = "ID";
    static final String X = "X";
    static final String Y = "Y";
    static final String XF = "XF";
    static final String YF = "YF";
    static final String LARGHEZZA = "LARGHEZZA";
    static final String PIANO = "PIANO";
    static final String TABLE_TRONCO = "TRONCO";

    public static String selectAllTronchiByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+ID+","+LARGHEZZA+","+X+","+Y+","+XF+","+YF+
                " FROM "+TABLE_TRONCO+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"TRONCO\":{\"ID\":\""+rs.getInt(ID)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"XF\":\""+rs.getFloat(XF)+"\",\"YF\":\""+rs.getFloat(YF)+"\",\"LARGHEZZA\":\""+rs.getFloat(LARGHEZZA)+"\"}},";
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

    public static HashMap<Integer,Tronco> selectTronchiDelPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        HashMap<Integer,Tronco> risultato = new HashMap<>();

        String query = "SELECT ID,X,Y,XF,YF" +
                       " FROM TRONCO" +
                       " WHERE PIANO = \'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                risultato.put(rs.getInt(DAOTronco.ID),new Tronco(rs.getInt(DAOTronco.ID),rs.getFloat(DAOTronco.X),rs.getFloat(DAOTronco.Y)
                        ,rs.getFloat(DAOTronco.XF),rs.getFloat(DAOTronco.YF)));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return risultato;
    }

    public static ArrayList<Integer> selectTronchiAdiacenti(Tronco t) {
        Connection conn = DatabaseConnection.getConn();
        ArrayList<Integer> risultato = new ArrayList<>();

        String query = "SELECT TRONCO.ID" +
                       " FROM TRONCO," +
                       "( SELECT TRONCO.ID as IDINIZIO," +
                       " TRONCO.X  as XINIZIO," +
                       " TRONCO.Y  as YINIZIO," +
                       " TRONCO.XF as XFINIZIO," +
                       " TRONCO.YF as YFINIZIO," +
                       " TRONCO.PIANO as PIANOINIZIO "+
                       " FROM TRONCO" +
                       " WHERE TRONCO.ID = "+ t.getID() +
                       ") AS TRONCOINIZIO" +
                       " WHERE ((TRONCO.X=TRONCOINIZIO.XINIZIO" +
                       " AND TRONCO.Y=TRONCOINIZIO.YINIZIO)" +
                       " OR (TRONCO.XF=TRONCOINIZIO.XINIZIO" +
                       " AND TRONCO.YF=TRONCOINIZIO.YINIZIO)" +
                       " OR (TRONCO.X=TRONCOINIZIO.XFINIZIO" +
                       " AND TRONCO.Y=TRONCOINIZIO.YFINIZIO)" +
                       " OR (TRONCO.XF=TRONCOINIZIO.XFINIZIO" +
                       " AND TRONCO.YF=TRONCOINIZIO.YFINIZIO))" +
                       " AND TRONCO.ID <> TRONCOINIZIO.IDINIZIO" +
                       " AND TRONCO.PIANO = TRONCOINIZIO.PIANOINIZIO";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                risultato.add(rs.getInt(DAOTronco.ID));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(risultato.size()>0) {
            return risultato;
        }
        else return null;
    }
}