package entita;

import connessioni.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOAula {
    static final String NOME = "NOME";
    static final String X = "X";
    static final String Y = "Y";
    static final String PIANO = "PIANO";
    static final String ENTRATA = "ENTRATA";
    static final String TABLE_AULA = "AULA";


    //scaricamento di tutte le aule per ottenimento dati offline EDO
    public static String downloadAule(){
        String json="\"AULE\":[";
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM AULA");
            while (rs.next()){
                json = json + "{\"NOME\":\"" + rs.getString(NOME)+ "\",\"X\":\""+ rs.getInt(X)+"\",\"Y\":\""+rs.getInt(Y)+"\",\"PIANO\":\""+rs.getString(PIANO)+"\",\"ENTRATA\":\""+rs.getString(ENTRATA)+"\"},";
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
            e.printStackTrace();
        }
        return aule;
    }

    public static ArrayList<String> selectBeaconAuleEdificio(String edificio) {
        Connection conn = Database.getConn();
        ArrayList<String> beacons = new ArrayList<String>();

        String query =  "SELECT "+ENTRATA+
                " FROM "+TABLE_AULA+","+DAOPiano.TABLE_PIANO+","+DAOEdificio.TABLE_EDIFICIO+
                " WHERE "+TABLE_AULA+"."+PIANO+"="+DAOPiano.TABLE_PIANO+"."+NOME+
                " AND "+DAOPiano.TABLE_PIANO+"."+DAOPiano.EDIFICIO+"="+DAOEdificio.TABLE_EDIFICIO+"."+NOME+
                " AND "+DAOEdificio.NOME+"='"+edificio+"'";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                beacons.add(rs.getString(ENTRATA));
            }
            rs.close();
            stm.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return beacons;
    }

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
            e.printStackTrace();
        }
        return beacon;
    }

    public static void insertAula(String nome,String piano, String X, String Y, String entrata) throws SQLException {
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