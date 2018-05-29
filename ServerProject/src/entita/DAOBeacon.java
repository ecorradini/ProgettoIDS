package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOBeacon {
    static final String ID = "ID";
    static final String X = "X";
    static final String Y = "Y";
    static final String TRONCO = "TRONCO";
    static final String UTENTI = "UTENTI";
    static final String USCITA = "USCITA";
    static final String TABLE_BEACON = "BEACON";


    //scaricamento di tutti i beacon per ottenimento dati offline EDO
    public static String downloadBeacons(){
        String json="BEACON:[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM BEACON");
            while (rs.next()){
                json = json+"\""+rs.getString(ID)+"\":{\"X\":\""+rs.getInt(X)+"\",\"Y\":\""+rs.getInt(Y)+"\",\"TRONCO\":\""+rs.getString(TRONCO)+"\",\"UTENTI\":\""+rs.getInt(UTENTI)+"\",\"USCITA\":\""+rs.getInt(USCITA)+"\"},";
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if(json.substring(json.length() - 1,json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]";
        }
        return json;
    }


    public static String selectPosizioneById(String idBeacon) {
        Connection conn = Database.getConn();
        String json="";

        String query =  "SELECT "+ID+","+X+","+Y+
                " FROM "+TABLE_BEACON+
                " WHERE "+ID+"=\'"+idBeacon+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = "{\"BEACON\":{\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"ID\":\""+rs.getString(ID)+"\"}}";
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String selectAllBeaconsByTronco(String tronco) {
        Connection conn = Database.getConn();
        String json="{\""+tronco+"\":[";

        String query =  "SELECT "+ID+","+X+","+Y+
                " FROM "+TABLE_BEACON+
                " WHERE "+TRONCO+"=\'"+tronco+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"BEACON\":{\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"ID\":\""+rs.getString(ID)+"\"}},";
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

    public static ArrayList<String> selectListaBeaconsByTronco(int tronco) {
        Connection conn = Database.getConn();
        ArrayList<String> beacons = new ArrayList<>();
        String query =  "SELECT "+ID+","+X+","+Y+
                " FROM "+TABLE_BEACON+
                " WHERE "+TRONCO+"=\'"+tronco+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                beacons.add(rs.getString("ID"));
            }
            rs.close();
            stm.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return beacons;
    }

    public static boolean sumUser(String beaconID) {
        Connection conn = Database.getConn();
        String query = "UPDATE "+TABLE_BEACON+" SET "+UTENTI+" = (SELECT "+UTENTI+" FROM"+ TABLE_BEACON+" WHERE "+ID+" =\'"+beaconID+"\')+1 WHERE "+ID+" = \'"+beaconID+"\'";

        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(query);

            stm.close();

            return true;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeUser(String beaconID) {
        Connection conn = Database.getConn();
        String query = "UPDATE "+TABLE_BEACON+" SET "+UTENTI+" = (SELECT "+UTENTI+" FROM"+ TABLE_BEACON+" WHERE "+ID+" =\'"+beaconID+"\')-1 WHERE "+ID+" = \'"+beaconID+"\'";

        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(query);

            stm.close();

            return true;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getNumeroPersoneInTronco(int tronco){
        int persone=0;

        Connection conn = Database.getConn();
        String query = "SELECT SUM("+UTENTI+") FROM "+TABLE_BEACON+" WHERE "+TRONCO+" = "+tronco;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            persone = rs.getRow();

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return persone;

    }

    public static void insertBeacon(String id,String tronco, String X, String Y, String uscita) throws SQLException {
        Connection conn = Database.getConn();

        String query = "INSERT INTO " + TABLE_BEACON + " VALUES('" + id + "','"+ X + "','" + Y +"','" + tronco + "','" + "0" +"','" + uscita + "')";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }

}