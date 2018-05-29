package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOEdificio {
    //Nome della colonna "NOME"
    static final String NOME = "NOME";
    //Nome della tabella "EDIFICIO"
    static final String TABLE_EDIFICIO = "EDIFICIO";


    //scaricamento di tutti gli edifici per ottenimento dati offline EDO
    public static String downloadEdicifi(){
        String json="EDIFICI:[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM EDIFICIO");
            while (rs.next()){
                json = json + rs.getString(1)+",";
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


    public static String selectEdificioByBeacon(String idBeacon) {
        Connection conn = Database.getConn();
        String nomeEdificio = "";

        String query = "SELECT "+TABLE_EDIFICIO+"."+NOME+" AS NOME_EDIFICIO"+
                " FROM "+TABLE_EDIFICIO+","+DAOPiano.TABLE_PIANO+","+DAOTronco.TABLE_TRONCO+","+DAOBeacon.TABLE_BEACON+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+"="+DAOPiano.TABLE_PIANO+"."+DAOPiano.NOME+" AND "+
                DAOPiano.TABLE_PIANO+"."+DAOPiano.EDIFICIO+"="+TABLE_EDIFICIO+"."+NOME+" AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\'";
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomeEdificio = rs.getString("NOME_EDIFICIO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String json = "{"+"\"EDIFICIO_ATTUALE\":\""+nomeEdificio+"\""+"}";


        return json;
    }

    public static String selectNomeEdificio(String idBeacon) {
        Connection conn = Database.getConn();
        String nomeEdificio = "";

        String query = "SELECT "+TABLE_EDIFICIO+"."+NOME+" AS NOME_EDIFICIO"+
                " FROM "+TABLE_EDIFICIO+","+DAOPiano.TABLE_PIANO+","+DAOTronco.TABLE_TRONCO+","+DAOBeacon.TABLE_BEACON+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+"="+DAOPiano.TABLE_PIANO+"."+DAOPiano.NOME+" AND "+
                DAOPiano.TABLE_PIANO+"."+DAOPiano.EDIFICIO+"="+TABLE_EDIFICIO+"."+NOME+" AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomeEdificio = rs.getString("NOME_EDIFICIO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return nomeEdificio;
    }

    public static ArrayList<String> selectEdifici() {
        Connection conn = Database.getConn();
        ArrayList<String> edifici = new ArrayList<>();

        String query = "SELECT "+NOME+" FROM "+TABLE_EDIFICIO;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                edifici.add(rs.getString(NOME));
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return edifici;
    }

    public static int selectCountEdifici() {
        Connection conn = Database.getConn();
        int nEdifici=0;

        String query = "SELECT COUNT("+NOME+") AS NUMERO FROM "+TABLE_EDIFICIO;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nEdifici = rs.getInt("NUMERO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nEdifici;
    }

    public static void insertEdificio(String nome) throws SQLException {
        Connection conn = Database.getConn();

        String query = "INSERT INTO " + TABLE_EDIFICIO + " VALUES('" + nome + "')";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }




}

