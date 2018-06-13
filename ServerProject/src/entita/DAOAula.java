package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe Data Access Object per Aula
 */

public class DAOAula {

    //Nomi delle colonne della tabella
    static final String NOME = "NOME";
    static final String X = "X";
    static final String Y = "Y";
    static final String PIANO = "PIANO";
    static final String ENTRATA = "ENTRATA";
    static final String TABLE_AULA = "AULA";


    /**
     * scaricamento di tutte le aule per ottenimento dati offline
     * @return stringa JSON
     */
    public static String downloadAule(){
        String json="\"AULE\":[";
        try {
            //connessione al database
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            //query
            ResultSet rs = stm.executeQuery("SELECT * FROM AULA");
            //ciclo il risultato
            while (rs.next()){
                json = json + "{\"NOME\":\"" + rs.getString(NOME)+ "\",\"X\":\""+ rs.getInt(X)+"\",\"Y\":\""+rs.getInt(Y)+"\",\"PIANO\":\""+rs.getString(PIANO)+"\",\"ENTRATA\":\""+rs.getString(ENTRATA)+"\"},";
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block

        }
        //Alla fine rimuovo l'ultima virgola
        finally {
            if(json.substring(json.length() - 1,json.length()).equals(",")) {
                json = json.substring(0, json.length() - 1);
            }
            json = json + "]";
        }
        return json;
    }


    /**
     * Leggo tutte le aule del piano
     * @param piano piano di cui si vogliono conoscere le aule
     * @return stringa JSON
     */
    public static String selectAllAuleByPiano(String piano) {
        Connection conn = Database.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+NOME+","+X+","+Y+","+ENTRATA+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"AULA\":{\"NOME\":\""+rs.getString(NOME)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"ENTRATA\":\""+rs.getString(ENTRATA)+"\"}},";
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
     * Leggere tutte le aule del piano
     * @param piano piano di cui si vogliono conoscere le aule
     * @return lista delle aule del piano
     */
    public static ArrayList<String> selectListaAuleByPiano(String piano) {
        Connection conn = Database.getConn();
        ArrayList<String> aule = new ArrayList<String>();
        String query =  "SELECT "+NOME+","+X+","+Y+","+ENTRATA+
                " FROM "+TABLE_AULA+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                aule.add(rs.getString("NOME"));
            }
            rs.close();
            stm.close();
        }
        catch (SQLException e) {

        }
        return aule;
    }

    /**
     * Restituire l'id del beacon posto all'entrata dell'aula
     * @param aula di cui si vuole conoscere il beacon d'entrata
     * @return id del beacon
     */
    public static String selectBeaconEntrata(String aula) {
        Connection conn = Database.getConn();
        String beacon = "";

        String query =  "SELECT "+ENTRATA+
                " FROM "+TABLE_AULA+
                " WHERE "+NOME+"='"+aula+"'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                beacon = rs.getString(ENTRATA);
            }
            rs.close();
            stm.close();
        }
        catch (SQLException e) {

        }
        return beacon;
    }

    /**
     * Inserimento di un'aula nel database
     * @param nome nome dell'aula
     * @param piano piano dove si trova l'aula
     * @param X coordinata X di entrata dell'aula
     * @param Y coordinata Y di entrata dell'autla
     * @param entrata id del beacon di entrata dell'aula
     * @throws SQLException
     */
    public static void insertAula(String nome, String X, String Y, String piano, String entrata) throws SQLException {
        Connection conn = Database.getConn();

        String query="";
        if(entrata!=null) {
            query = "INSERT INTO " + TABLE_AULA + " VALUES('" + nome + "','" + X + "','" + Y + "','" + piano + "','" + entrata + "')";
        }
        else {
            query = "INSERT INTO " + TABLE_AULA + "(NOME,X,Y,PIANO) VALUES('" + nome + "','" + X + "','" + Y + "','" + piano + "')";
        }
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);

        stm.close();
    }
}