package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOMappa {
    static final String PIANO = "PIANO";
    static final String LINK = "LINK";
    static final String TABLE_MAPPA = "MAPPA";


    //scaricamento di tutte le mappe per ottenimento dati offline EDO
    public static String downloadMappe(){
        String tmp = "";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM MAPPA");
            while (rs.next()){
                tmp = rs.getString(1)+" "+rs.getString(2);
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tmp;
    }


    public static String selectMappaByPiano(String piano) {
        Connection conn = Database.getConn();
        String link="";

        String query =  "SELECT "+ LINK+
                " FROM "+TABLE_MAPPA+
                " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                link = rs.getString(LINK);
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return link;
    }

    public static void insertMappaPiano(String piano) throws SQLException {
        Connection conn = Database.getConn();
        String link = "/Mappe/q" + piano + ".jpg";

        String query = "INSERT INTO " + TABLE_MAPPA + " VALUES('" + piano + "','" + link + "')";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }
}

