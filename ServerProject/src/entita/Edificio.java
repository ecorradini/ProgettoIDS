package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Edificio {
    //Nome della colonna "NOME"
    public static final String NOME = "NOME";
    //Nome della tabella "EDIFICIO"
    public static final String TABLE_EDIFICIO = "EDIFICIO";

    public static String selectEdificioByBeacon(String idBeacon) {
        Connection conn = DatabaseConnection.getConn();
        String nomeEdificio = "";

        String query = "SELECT "+TABLE_EDIFICIO+"."+NOME+" AS NOME_EDIFICIO"+
                " FROM "+TABLE_EDIFICIO+","+Piano.TABLE_PIANO+","+Tronco.TABLE_TRONCO+","+Beacon.TABLE_BEACON+
                " WHERE "+Beacon.TABLE_BEACON+"."+Beacon.TRONCO+"="+Tronco.TABLE_TRONCO+"."+Tronco.ID+" AND "+
                Tronco.TABLE_TRONCO+"."+Tronco.PIANO+"="+Piano.TABLE_PIANO+"."+Piano.NOME+" AND "+
                Piano.TABLE_PIANO+"."+Piano.EDIFICIO+"="+TABLE_EDIFICIO+"."+NOME+" AND "+
                Beacon.TABLE_BEACON+"."+Beacon.ID+"=\'"+idBeacon+"\'";
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
        return "{"+"\"EDIFICIO_ATTUALE\":\""+nomeEdificio+"\""+"}";
    }
}
