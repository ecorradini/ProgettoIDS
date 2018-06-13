package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Data Access Object per edificio
 */

public class DAOEdificio {
    //Nome della colonna "NOME"
    static final String NOME = "NOME";
    //Nome della tabella "EDIFICIO"
    static final String TABLE_EDIFICIO = "EDIFICIO";


    /**
     * Scaricamento di tutti i edifici per ottenimento dati offline
     * @return stringa JSON
     */

    public static String downloadEdicifi(){
        String json="\"EDIFICI\":[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM EDIFICIO");
            while (rs.next()){
                json = json + "\"" + rs.getString(NOME)+"\",";
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block

        }
        finally {
            if(json.substring(json.length() - 1,json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]";
        }
        return json;
    }


    /**
     * scarica l' edificio in base al beacon cui si è connessi per ottenimento dati
     * @param idBeacon id del beacon  di cui si è interessati a scaricare l' edificio
     * @return stringa JSON
     */

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

        }

        String json = "{"+"\"EDIFICIO_ATTUALE\":\""+nomeEdificio+"\""+"}";


        return json;
    }

    /**
     * seleziona l' edificio in base al beacon cui si è connessi per ottenimento offline
     * @param idBeacon id del beacon  di cui si è interessati a selezionare l'edificio
     * @return stringa JSON
     */

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

        }


        return nomeEdificio;
    }

    /**
     * seleziona gli edifici
     * @return ArrayList<String>
     */

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

        }

        return edifici;
    }

    /**
     * conta gli edifici
     * @return int
     */

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

        }

        return nEdifici;
    }
    /**
     * Inserimento di un'aula nel database
     * @param nome nome che funge anche da identificativo dell' edificio
     * @throws SQLException
     */
    public static void insertEdificio(String nome) throws SQLException {
        Connection conn = Database.getConn();

        String query = "INSERT INTO " + TABLE_EDIFICIO + " VALUES('" + nome + "')";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }




}

