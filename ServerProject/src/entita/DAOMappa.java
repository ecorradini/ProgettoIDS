package entita;

import connessioni.Database;
import connessioni.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class DAOMappa {
    static final String PIANO = "PIANO";
    static final String LINK = "LINK";
    static final String TABLE_MAPPA = "MAPPA";


    //scaricamento di tutte le mappe per ottenimento dati offline EDO
    public static String downloadMappe() {
        String json = "MAPPE:[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM MAPPA");
            while (rs.next()) {
                json = json + rs.getString(1) + " " + rs.getString(2) + ",";
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (json.substring(json.length() - 1, json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]";
        }
        return json;
    }


    public static String selectMappaByPiano(String piano) {
        Connection conn = Database.getConn();
        String link = "";

        String query = "SELECT " + LINK +
                " FROM " + TABLE_MAPPA +
                " WHERE " + PIANO + "=\'" + piano + "\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
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

    public static String getBase64Mappe() {
        String json = "MAPPA:[";
        String path = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/ServerProject.jar","");
        //String link = DAOMappa.selectMappaByPiano(arg0.getRequestURI().getQuery());
        Connection conn = Database.getConn();
        String query = "SELECT * FROM "+TABLE_MAPPA;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                String link = rs.getString(LINK);
                String piano = rs.getString(PIANO);

                File file = new File(path+link);
                String encodedMap = null;
                try {
                    FileInputStream fileInputStreamReader = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStreamReader.read(bytes);
                    encodedMap = Base64.getEncoder().encodeToString(bytes);

                    json = json + "\""+piano+"\":\""+encodedMap+"\",";

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(json.substring(json.length() - 1,json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]";
        }

        return json;


    }

}

