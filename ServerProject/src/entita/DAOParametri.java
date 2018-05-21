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

    public static void updateTestaEmergenza() {
        Connection conn = Database.getConn();

        String query = "update PARAMETRI set RV=1 where TRONCO=3";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
            //L'utente è già stato inserito, non mi interessa
        }
    }

    public static void updateFineTestaEmergenza() {
        Connection conn = Database.getConn();

        String query = "update PARAMETRI set RV=0 where TRONCO=3";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.close();
            stm.close();

        } catch (SQLException e) {
            //L'utente è già stato inserito, non mi interessa
        }
    }

}
