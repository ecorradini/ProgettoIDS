package connessioni;

import entita.*;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseConnection {

    private static Connection connection;
    private static final String dbUrl="jdbc:jtds:sqlserver://den1.mssql5.gear.host/getoutdb";
    public static DatabaseMetaData databaseMetaData;

    static void init() throws SQLException {
        //Chiedo la password del Server
        Console console = System.console();
        if (console == null) {
            System.out.println("Istanza della console nulla");
            //System.exit(0);
        }
        //console.printf("%n");
        //char passwordArray[] = console.readPassword("Password del database: ");
        //String password = new String(passwordArray);
        String password = "getout2018@";

        //Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(dbUrl, "getoutdb", password);
        System.out.println("Connesso al DB");

        getTableNames();
    }

    private static void getTableNames() throws SQLException {

        databaseMetaData = connection.getMetaData();

        ResultSet tabelleR = databaseMetaData.getTables(null, "dbo", null, new String[] {"TABLE"});
        HashMap<String,ArrayList<String>> tabelle = new HashMap<>();

        while(tabelleR.next()) {
            String tableName = tabelleR.getString(3);
            ResultSet colonneR = databaseMetaData.getColumns(null, null, tableName,null);
            ArrayList<String> colonne = new ArrayList<>();
            while(colonneR.next()) {
                colonne.add(colonneR.getString(4));
            }
            colonne.sort(String::compareToIgnoreCase);
            tabelle.put(tableName,colonne);
        }

        HashMap<String,ArrayList<String>> tabelleS = sortHashMap(tabelle);

        Aula.TABLE_AULA = (String)tabelleS.keySet().toArray()[0];
        System.out.println(tabelleS.get(Aula.TABLE_AULA).get(0));
        Aula.NOME = tabelleS.get(Aula.TABLE_AULA).get(0);
        Aula.PIANO = tabelleS.get(Aula.TABLE_AULA).get(1);
        Aula.X = tabelleS.get(Aula.TABLE_AULA).get(2);
        Aula.Y = tabelleS.get(Aula.TABLE_AULA).get(3);

        Beacon.TABLE_BEACON = (String)tabelleS.keySet().toArray()[1];
        Beacon.ID = tabelleS.get(Beacon.TABLE_BEACON).get(0);
        Beacon.TRONCO = tabelleS.get(Beacon.TABLE_BEACON).get(1);
        Beacon.X = tabelleS.get(Beacon.TABLE_BEACON).get(2);
        Beacon.Y = tabelleS.get(Beacon.TABLE_BEACON).get(3);

        Edificio.TABLE_EDIFICIO = (String)tabelleS.keySet().toArray()[2];
        Edificio.NOME = tabelleS.get(Beacon.TABLE_BEACON).get(0);

        Mappa.TABLE_MAPPA = (String)tabelleS.keySet().toArray()[3];
        Mappa.IMMAGINE = tabelleS.get(Mappa.TABLE_MAPPA).get(0);
        Mappa.PIANO = tabelleS.get(Mappa.TABLE_MAPPA).get(1);

        Piano.TABLE_PIANO = (String)tabelleS.keySet().toArray()[4];
        Piano.EDIFICIO = tabelleS.get(Piano.TABLE_PIANO).get(0);
        Piano.NOME = tabelleS.get(Piano.TABLE_PIANO).get(1);

        Tronco.TABLE_TRONCO = (String)tabelleS.keySet().toArray()[5];
        Tronco.ID = tabelleS.get(Tronco.TABLE_TRONCO).get(0);
        Tronco.LARGHEZZA = tabelleS.get(Tronco.TABLE_TRONCO).get(1);
        Tronco.PIANO = tabelleS.get(Tronco.TABLE_TRONCO).get(2);
        Tronco.X = tabelleS.get(Tronco.TABLE_TRONCO).get(3);
        Tronco.XF = tabelleS.get(Tronco.TABLE_TRONCO).get(4);
        Tronco.Y = tabelleS.get(Tronco.TABLE_TRONCO).get(5);
        Tronco.YF = tabelleS.get(Tronco.TABLE_TRONCO).get(6);
    }

    public static Connection getConn() {
        return connection;
    }

    private static HashMap<String,ArrayList<String>> sortHashMap(HashMap<String,ArrayList<String>> hm) {
        HashMap<String,ArrayList<String>> app = new HashMap<>();
        ArrayList<String> keys = new ArrayList<>(hm.keySet());
        keys.sort(String::compareToIgnoreCase);
        for(String chiave : keys) {
            app.put(chiave,hm.get(chiave));
            System.out.println(chiave+" "+hm.get(chiave).size());
        }
        return new HashMap<>(app);
    }
}
