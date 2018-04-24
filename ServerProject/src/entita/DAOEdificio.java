package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOEdificio {
    //Nome della colonna "NOME"
    static final String NOME = "NOME";
    //Nome della tabella "EDIFICIO"
    static final String TABLE_EDIFICIO = "EDIFICIO";

    public static String selectEdificioByBeacon(String idBeacon) {
        Connection conn = DatabaseConnection.getConn();
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
            e.printStackTrace();
        }

        String json = "{"+"\"EDIFICIO_ATTUALE\":\""+nomeEdificio+"\""+"}";

        System.out.println("RESPONSE: "+json);

        return json;
    }
}

