package entita;

import connessioni.DatabaseConnection;

import javax.management.ImmutableDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOMappa {
    static final String PIANO = "PIANO";
    static final String LINK = "LINK";
    static final String TABLE_MAPPA = "MAPPA";

    public static String selectMappaByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
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
}

