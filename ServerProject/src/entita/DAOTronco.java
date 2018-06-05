package entita;

import com.mysql.jdbc.log.Log;
import connessioni.Database;
import connessioni.Server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOTronco {
    static final String ID = "ID";
    static final String X = "X";
    static final String Y = "Y";
    static final String XF = "XF";
    static final String YF = "YF";
    static final String LARGHEZZA = "LARGHEZZA";
    static final String LUNGHEZZA = "LUNGHEZZA";
    static final String PIANO = "PIANO";
    static final String TABLE_TRONCO = "TRONCO";

    /**
     * scaricamento di tutti i tronchi per ottenimento dati offline
     * @return stringa JSON
     */
    public static String downloadTronchi(){
        String json="\"TRONCHI\":[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM TRONCO");
            while (rs.next()){
                json = json+"{\"ID\":\""+rs.getInt(ID)+"\",\"X\":\""+rs.getInt(X)+"\",\"Y\":\""+rs.getInt(Y)+"\",\"XF\":\""+rs.getInt(XF)+"\",\"YF\":\""+rs.getInt(YF)+"\",\"LARGHEZZA\":\""+rs.getInt(LARGHEZZA)+"\",\"PIANO\":\""+rs.getString(PIANO)+"\",\"LUNGHEZZA\":\""+rs.getInt(LUNGHEZZA)+"\"},";
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

    /**
     * scaricamento di tutti i tronchi relativi a un piano per ottenimento dati offline
     * @param piano identificativo del piano per cui si vogliono i tronchi
     * @return stringa JSON
     */

    public static String selectAllTronchiByPiano(String piano) {
        Connection conn = Database.getConn();
        String json="{\""+piano+"\":[";

        String query =  "SELECT "+ID+","+LARGHEZZA+","+LUNGHEZZA+","+X+","+Y+","+XF+","+YF+
                " FROM "+TABLE_TRONCO+ " WHERE "+PIANO+"=\'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                json = json + "{\"TRONCO\":{\"ID\":\""+rs.getInt(ID)+"\",\"X\":\""+rs.getFloat(X)+"\",\"Y\":\""+rs.getFloat(Y)+"\",\"XF\":\""+rs.getFloat(XF)+"\",\"YF\":\""+rs.getFloat(YF)+"\",\"LARGHEZZA\":\""+rs.getFloat(LARGHEZZA)+"\",\"LUNGHEZZA\":\""+rs.getFloat(LUNGHEZZA)+"\"}},";
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
    /**
     * costruisce un' hashmap tra gli id dei tronchi e i tronchi stessi di un piano specificato
     * @param piano identificatico del piano per cui si vuole svolgere l' operazione
     * @return HashMap<Integer,Tronco>
     */
    public static HashMap<Integer,Tronco> selectTronchiDelPiano(String piano) {
        Connection conn = Database.getConn();
        HashMap<Integer,Tronco> risultato = new HashMap<>();

        String query = "SELECT ID,X,Y,XF,YF,LARGHEZZA,LUNGHEZZA" +
                       " FROM TRONCO" +
                       " WHERE PIANO = \'"+piano+"\'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                risultato.put(rs.getInt(DAOTronco.ID),new Tronco(rs.getInt(DAOTronco.ID),rs.getFloat(DAOTronco.X),rs.getFloat(DAOTronco.Y)
                        ,rs.getFloat(DAOTronco.XF),rs.getFloat(DAOTronco.YF), rs.getFloat(LARGHEZZA), rs.getFloat(LUNGHEZZA)));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return risultato;
    }
    /**
     * ritorna le coordinate del tronco
     * @param troncoID id del tronco per cui si vogliono le coordinate
     * @return Hint[]
     */

    public static int[] selectCoordinateTronco(int troncoID) {
        Connection conn = Database.getConn();
        int[] result = new int[4];

        String query = "SELECT "+X+","+Y+","+XF+","+YF+" FROM "+TABLE_TRONCO+" WHERE "+ID+"="+troncoID;

        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                result[0] = rs.getInt(X);
                result[1] = rs.getInt(Y);
                result[2] = rs.getInt(XF);
                result[3] = rs.getInt(YF);
            }

            rs.close();
            stm.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * ritorna l' array degli identificativi dei tronchi adiacenti a un tronco specificato
     * @param t tronco per cui si vogliono calcolare gli adiacenti
     * @return Array<Integer>
     */
    public static ArrayList<Integer> selectTronchiAdiacenti(Tronco t) {
        Connection conn = Database.getConn();
        ArrayList<Integer> risultato = new ArrayList<>();

        String query = "SELECT TRONCO.ID" +
                       " FROM TRONCO," +
                       "( SELECT TRONCO.ID as IDINIZIO," +
                       " TRONCO.X  as XINIZIO," +
                       " TRONCO.Y  as YINIZIO," +
                       " TRONCO.XF as XFINIZIO," +
                       " TRONCO.YF as YFINIZIO," +
                       " TRONCO.PIANO as PIANOINIZIO"+
                       " FROM TRONCO" +
                       " WHERE TRONCO.ID = "+ t.getID() +
                       ") AS TRONCOINIZIO" +
                       " WHERE ((TRONCO.X=TRONCOINIZIO.XINIZIO" +
                       " AND TRONCO.Y=TRONCOINIZIO.YINIZIO)" +
                       " OR (TRONCO.XF=TRONCOINIZIO.XINIZIO" +
                       " AND TRONCO.YF=TRONCOINIZIO.YINIZIO)" +
                       " OR (TRONCO.X=TRONCOINIZIO.XFINIZIO" +
                       " AND TRONCO.Y=TRONCOINIZIO.YFINIZIO)" +
                       " OR (TRONCO.XF=TRONCOINIZIO.XFINIZIO" +
                       " AND TRONCO.YF=TRONCOINIZIO.YFINIZIO))" +
                       " AND TRONCO.ID <> TRONCOINIZIO.IDINIZIO" +
                       " AND TRONCO.PIANO=TRONCOINIZIO.PIANOINIZIO";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                risultato.add(rs.getInt(DAOTronco.ID));
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(risultato.size()>0) {
            return risultato;
        }
        else return null;
    }

    /**
     * seleziona un nodo del grafo in base al beacon, all'edificio e al piano specificato
     * @param beacon identificatico del beacon per cui si vuole svolgere l' operazione
     * @param edificio identificatico del edificio per cui si vuole svolgere l' operazione
     * @param piano identificatico del piano per cui si vuole svolgere l' operazione
     * @return GrafoTronchi.Nodo
     */

    public static GrafoTronchi.Nodo selectNodoByBeacon(String beacon, String edificio, String piano) {
        Connection conn = Database.getConn();
        String query = "SELECT TRONCO" +
                " FROM BEACON" +
                " WHERE BEACON.ID = \'"+beacon+"\'";

        int tronco=0;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {
                tronco = rs.getInt("TRONCO");
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<GrafoTronchi.Nodo> visitati = new ArrayList<>();
        ArrayList<GrafoTronchi.Nodo> daVisitare = new ArrayList<>();
        System.out.println(piano);
        daVisitare.add(Server.getGrafiPiani().get(edificio).get(piano).getRadice());

        GrafoTronchi.Nodo risultato = null;

        while(daVisitare.size()>0 && risultato==null) {
            if(daVisitare.get(0).getTronco().getID()==tronco) {
                risultato = daVisitare.get(0);
            }
            else {
                if(!visitati.contains(daVisitare.get(0))) {
                    daVisitare.addAll(daVisitare.get(0).getAdiacenti());
                    visitati.add(daVisitare.get(0));
                }
                daVisitare.remove(0);
            }
        }

        return risultato;
    }

    /**
     * inserisce un nuovo tronco
     * @param X cooordinata X di inizio del tronco
     * @param Y cooordinata Y di inizio del tronco
     * @param XF cooordinata X di fine del tronco
     * @param YF cooordinata Y di fine del tronco
     * @param lunghezza lunghezza del tronco
     * @param larghezza larghezza del tronco
     * @param piano identificatico del piano a cui si trova l' edificio
     * @throws SQLException
     */

    public static int insertTronco(String X, String Y, String XF, String YF, String lunghezza, String larghezza, String piano) throws SQLException {
        Connection conn = Database.getConn();

        String query = "INSERT INTO " + TABLE_TRONCO + "(X,Y,XF,YF,LARGHEZZA,PIANO,LUNGHEZZA) VALUES(" + X + ","+ Y + "," + XF +"," + YF + "," + larghezza + ",'" + piano + "'," + lunghezza +")";
        Statement stm = conn.createStatement();
        stm.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
        String queryID = "SELECT MAX("+ID+") AS LASTID FROM "+TABLE_TRONCO;
        ResultSet rs = stm.executeQuery(queryID);
        int id=0;
        while(rs.next()) {
            id = rs.getInt("LASTID");
        }

        stm.close();

        return id;
    }
}