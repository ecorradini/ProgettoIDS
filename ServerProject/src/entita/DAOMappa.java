package entita;

import connessioni.DatabaseConnection;

import javax.management.ImmutableDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOMappa {
    static final String PIANO = "PIANO";
    static final String IMMAGINE = "IMMAGINE";
    static final String TABLE_MAPPA = "MAPPA";

    public static String selectMappaByPiano(String piano) {
        Connection conn = DatabaseConnection.getConn();
        String immagineBase64 = "";

        String query =  "SELECT "+ IMMAGINE+
                " FROM "+TABLE_MAPPA+
                " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                immagineBase64 = rs.getString(IMMAGINE);
            }

            rs.close();
            stm.close();
            DatabaseConnection.getConn().commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String json = "{ "+"\"MAPPA\":\""+immagineBase64+"\"}";

        System.out.println("RESPONSE: "+json);

        return json;
    }
}

