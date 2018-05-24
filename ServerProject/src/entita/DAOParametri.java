package entita;

import connessioni.Database;

import java.sql.*;
import java.util.ArrayList;

public class DAOParametri {
    static final String TRONCO = "TRONCO";  //id del tronco di riferimento
    static final String VULNERABILITA = "VULN"; //parametro reale (probabilità di incendio per quel tronco)
    static final String RISCHIOVITA = "RV"; // parametro reale
    static final String PRESENZAFUMO = "PF"; // parametro di tipo boolean
    static final String TABLE_PARAMETRI = "PARAMETRI";

    public static ArrayList<Float> selectParametri(int tronco){
        Connection conn = Database.getConn();

        ArrayList<Float> tronchi;

        String query =  "SELECT "+VULNERABILITA+","+RISCHIOVITA+","+PRESENZAFUMO+
                " FROM "+TABLE_PARAMETRI+" WHERE "+TRONCO+"="+tronco;

        try {
            tronchi = new ArrayList<>();

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {

                tronchi.add(rs.getFloat(VULNERABILITA));
                tronchi.add(rs.getFloat(RISCHIOVITA));
                tronchi.add(rs.getFloat(PRESENZAFUMO));

            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            tronchi=null;
            e.printStackTrace();
        }

        return tronchi;
    }

    public static ArrayList<String[]> selectAllParametri(){
        Connection conn = Database.getConn();

        ArrayList<String[]> parametriTronchi;

        String query =  "SELECT "+DAOEdificio.TABLE_EDIFICIO+"."+DAOEdificio.NOME+" AS EDIFICIO,"+DAOPiano.TABLE_PIANO+"."+DAOPiano.NOME+" as PIANO,"+TABLE_PARAMETRI+"."+TRONCO+" AS TRONCO,"+VULNERABILITA+","+RISCHIOVITA+","+PRESENZAFUMO+
                " FROM "+DAOEdificio.TABLE_EDIFICIO+","+DAOPiano.TABLE_PIANO+","+DAOTronco.TABLE_TRONCO+","+TABLE_PARAMETRI+
                " WHERE "+TABLE_PARAMETRI+"."+TRONCO+"="+DAOTronco.TABLE_TRONCO+"."+DAOTronco.ID+
                " AND "+DAOTronco.TABLE_TRONCO+"."+DAOTronco.PIANO+"="+DAOPiano.TABLE_PIANO+"."+DAOPiano.NOME+
                " AND "+DAOPiano.TABLE_PIANO+"."+DAOPiano.EDIFICIO+"="+DAOEdificio.TABLE_EDIFICIO+"."+DAOEdificio.NOME;

        try {
            parametriTronchi = new ArrayList<>();

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while(rs.next()) {

                String[] result = new String[] {
                    rs.getString("EDIFICIO"),
                        rs.getString("PIANO"),
                        String.valueOf(rs.getInt("TRONCO")),
                        String.valueOf(rs.getFloat(VULNERABILITA)),
                        String.valueOf(rs.getFloat(RISCHIOVITA)),
                        String.valueOf(rs.getFloat(PRESENZAFUMO))
                };

                parametriTronchi.add(result);

            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            parametriTronchi=null;
            e.printStackTrace();
        }

        return parametriTronchi;
    }

    public static void updateTronco(int id, float vulnerabilita, float rischiovita, float presenzafumo) {
        Connection conn = Database.getConn();

        String query = "UPDATE "+TABLE_PARAMETRI+" SET " + VULNERABILITA + "=" + vulnerabilita + ","+ RISCHIOVITA + "=" + rischiovita +"," + PRESENZAFUMO + "=" + presenzafumo + " WHERE "+TRONCO+"="+id;
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(query);

            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean controllaEmergenza(){
        boolean emergenza = false;

        Connection conn = Database.getConn();

        String query =  "SELECT COUNT("+RISCHIOVITA+") AS EMERGENZE"+
                " FROM "+TABLE_PARAMETRI+" WHERE "+RISCHIOVITA+"=1";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                int countRischio = rs.getInt("EMERGENZE");
                if (countRischio > 0) emergenza = true;
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return emergenza;
    }

    public static String selectEdificioParametro(){
        Connection conn = Database.getConn();
        String edificio = null;
        String query2 = "select distinct EDIFICIO from PIANO" +
                        " where NOME = (" +
                        "select TRONCO.PIANO" +
                        " from PARAMETRI" +
                        " JOIN TRONCO" +
                        " ON PARAMETRI.TRONCO = TRONCO.ID" +
                        " where RV=1)";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);

            while(rs.next()) {
                edificio = rs.getString("EDIFICIO");
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return edificio;

    }

    public static void updateTestaEmergenza(int tronco) {
        Connection conn = Database.getConn();

        String query = "update PARAMETRI set RV=1 where TRONCO="+tronco;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
        }
    }

    public static void updateFineTestaEmergenza(int tronco) {
        Connection conn = Database.getConn();

        String query = "update PARAMETRI set RV=0 where TRONCO="+tronco;

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
        }
    }

}
