package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOPiano {
    static final String NOME = "NOME";
    static final String EDIFICIO = "EDIFICIO";
    static final String TABLE_PIANO = "PIANO";

    //scaricamento di tutti gli edifici per ottenimento dati offline EDO
    public static String downloadPiani(){
        String json="\"PIANI\":[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM PIANO");
            while (rs.next()){
                json = json + "{\"NOME\":\""+ rs.getInt(NOME) +"\",\"EDIFICIO\":\"" + rs.getString(EDIFICIO)+"\"},";  //prende un piano + edificio e poi passa sotto 'spero'
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if (json.substring(json.length() - 1, json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]";
        }
        return json;
    }


    public static String selectPianoByBeacon(String idBeacon) {
        Connection conn = Database.getConn();
        String nomePiano = "";

        String query =  "SELECT "+TABLE_PIANO+"."+NOME+" AS NOME_PIANO"+
                " FROM "+DAOBeacon.TABLE_BEACON+","+DAOTronco.TABLE_TRONCO+","+TABLE_PIANO+","+DAOEdificio.TABLE_EDIFICIO+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\' AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                TABLE_PIANO+"."+NOME+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+" AND "+TABLE_PIANO+
                "."+EDIFICIO+" = "+DAOEdificio.TABLE_EDIFICIO+"."+DAOEdificio.NOME;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomePiano = rs.getString("NOME_PIANO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String json = "{"+"\"PIANO_ATTUALE\":\""+nomePiano+"\""+"}";

        return json;
    }

    public static String selectNomePiano(String idBeacon) {
        Connection conn = Database.getConn();
        String nomePiano = "";

        String query =  "SELECT "+TABLE_PIANO+"."+NOME+" AS NOME_PIANO"+
                " FROM "+DAOBeacon.TABLE_BEACON+","+DAOTronco.TABLE_TRONCO+","+TABLE_PIANO+","+DAOEdificio.TABLE_EDIFICIO+
                " WHERE "+DAOBeacon.TABLE_BEACON+"."+DAOBeacon.ID+"=\'"+idBeacon+"\' AND "+
                DAOBeacon.TABLE_BEACON+"."+DAOBeacon.TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+" AND "+
                TABLE_PIANO+"."+NOME+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+" AND "+TABLE_PIANO+
                "."+EDIFICIO+" = "+DAOEdificio.TABLE_EDIFICIO+"."+DAOEdificio.NOME;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nomePiano = rs.getString("NOME_PIANO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nomePiano;
    }

    public static String selectAllPianiByEdificio(String edificio) {
        Connection conn = Database.getConn();
        String json="{\""+edificio+"\":[";

        String query =  "SELECT "+NOME+ " FROM "+TABLE_PIANO+ " WHERE "+DAOEdificio.TABLE_EDIFICIO+"=\'"+edificio+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"PIANO\":\""+rs.getString(NOME)+"\"},";
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

    public static ArrayList<String> selectListaPianiByEdificio(String edificio) {
        Connection conn = Database.getConn();
        ArrayList<String> piani = new ArrayList<>();

        String query =  "SELECT DISTINCT "+NOME+ " FROM "+TABLE_PIANO+ " WHERE "+DAOEdificio.TABLE_EDIFICIO+"=\'"+edificio+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                piani.add(rs.getString(NOME));
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return piani;
    }

    public static int selectCountPiani() {
        Connection conn = Database.getConn();
        int nPiani=0;

        String query = "SELECT COUNT("+NOME+") AS NUMERO FROM "+TABLE_PIANO;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                nPiani = rs.getInt("NUMERO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nPiani;
    }

    public static void insertPiano(String nome,String edificio) throws SQLException {
        Connection conn = Database.getConn();

        String query = "INSERT INTO " + TABLE_PIANO + " VALUES('" + nome + "','"+ edificio + "')";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }
}