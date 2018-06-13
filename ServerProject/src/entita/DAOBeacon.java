package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe Data Access Object per Beacon
 */

public class DAOBeacon {

    //Nomi delle colonne della tabella
    static final String ID = "ID";
    static final String X = "X";
    static final String Y = "Y";
    static final String TRONCO = "TRONCO";
    static final String UTENTI = "UTENTI";
    static final String USCITA = "USCITA";
    static final String TABLE_BEACON = "BEACON";

    /**
     * Scaricamento di tutti i beacon per ottenimento dati offline
     * @return stringa JSON
     */
    public static String downloadBeacons(){
        String json="\"BEACON\":[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM BEACON");
            while (rs.next()){
                json = json+"{\"ID\":\""+rs.getString(ID)+"\",\"X\":\""+rs.getInt(X)+"\",\"Y\":\""+rs.getInt(Y)+"\",\"TRONCO\":\""+rs.getString(TRONCO)+"\",\"UTENTI\":\""+rs.getInt(UTENTI)+"\",\"USCITA\":\""+rs.getInt(USCITA)+"\"},";
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
     * Scaricamento di tutti i beacon per ottenimento dati offline
     * @return stringa JSON
     */
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

        }

        return json;
    }

    /**
     * Scaricamento di tutti i beacon per ottenimento dati offline
     * @param tronco id convertito a string del tronco di cui si è interessati a selezionare i beacon
     * @return stringa JSON
     */

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
    /**
     *  Seleziona tutti i beacon a partire dal tronco
     *  @param tronco id tronco per cui si vuole determinare la lista dei beacon che lo compongono
     * @return ArrayList<String>
     */
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

        }
        return beacons;
    }
    /**
     *  Somma tutti gli utenti agganciati a il beacon specificato e aggiorna coseguentemente il DB, ritorna un boolean per sapere se l' operazione è andata a buon fine
     * @param beaconID id del beacon per cui si vuole effettuare la somma
     * @return boolean
     */
    public static boolean sumUser(String beaconID) {
        Connection conn = Database.getConn();
        String query = "UPDATE "+TABLE_BEACON+" SET "+UTENTI+" = (SELECT "+UTENTI+" FROM"+ TABLE_BEACON+" WHERE "+ID+" =\'"+beaconID+"\')+1 WHERE "+ID+" = \'"+beaconID+"\'";

        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(query);

            stm.close();

            return true;

        } catch(SQLException e) {

            return false;
        }
    }
    /**
     *  Rimuove l' utente dal beacon specificato ritorna un boolean per sapere se l' operazione è andata a buon fine
     *  @param beaconID id del beacon da cui si vuole sottrarre un utente
     * @return boolean
     */
    public static boolean removeUser(String beaconID) {
        Connection conn = Database.getConn();
        String query = "UPDATE "+TABLE_BEACON+" SET "+UTENTI+" = (SELECT "+UTENTI+" FROM"+ TABLE_BEACON+" WHERE "+ID+" =\'"+beaconID+"\')-1 WHERE "+ID+" = \'"+beaconID+"\'";

        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(query);

            stm.close();

            return true;

        } catch(SQLException e) {

            return false;
        }
    }
    /**
     *  Calcola il numero di persone dentro il tronco specificato
     * @param tronco tronco per cui si vuole calcolare il numero di persone
     * @return int
     */

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

        }

        return persone;

    }
    /**
     * Inserimento di un'aula nel database
     * @param id identificativo del beacon
     * @param tronco tronco di afferenza del beacon
     * @param X coordinata X in cui si trova il beacon
     * @param Y coordinata Y in cui si trova il beacon
     * @param uscita specifica sel beacon è un beacon di uscita o meno
     * @throws SQLException
     */
    public static void insertBeacon(String id, String X, String Y, String tronco, String uscita) throws SQLException {
        Connection conn = Database.getConn();

        String query = "INSERT INTO " + TABLE_BEACON + " VALUES('" + id + "','"+ X + "','" + Y +"','" + tronco + "','" + "0" +"','" + uscita + "')";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }

}