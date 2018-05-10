package entita;

import connessioni.DatabaseConnection;

import javax.print.attribute.standard.PresentationDirection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOParametri {
    static final String TRONCO = "TRONCO";  //id del tronco di riferimento
    static final String VULNERABILITA = "VULN"; //parametro reale (probabilità di incendio per quel tronco)
    static final String RISCHIOVITA = "RV"; // parametro reale
    static final String PRESENZAFUMO = "PF"; // parametro di tipo boolean
    static final String TABLE_PARAMETRI = "PARAMETRI";

    public static ArrayList<Float> selectParametri(int tronco){
        Connection conn = DatabaseConnection.getConn();

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

        Connection conn = DatabaseConnection.getConn();

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

    public static ArrayList<Integer> selectTroncoEmergenza(){
        ArrayList<Integer> tronchi = new ArrayList<Integer>();
        Connection conn = DatabaseConnection.getConn();

        String query =  "SELECT "+TRONCO+
                " FROM "+TABLE_PARAMETRI+" WHERE "+RISCHIOVITA+"=1";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                tronchi.add(rs.getInt(TRONCO));
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tronchi;
    }

    public static String selectEdificioParametro(){
        Connection conn = DatabaseConnection.getConn();
        String edificio = null;
        String query2 = "select distinct EDIFICIO from PIANO\n" +
                        "where NOME = (\n" +
                        "      select TRONCO.PIANO\n" +
                        "      from PARAMETRI\n" +
                        "            JOIN TRONCO\n" +
                        "            ON PARAMETRI.TRONCO = TRONCO.ID\n" +
                        "      where RV=1\n" +
                        ");";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);

            edificio = rs.getString("EDIFICIO");

            rs.close();
            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return edificio;
    }
}
