package entita;

import connessioni.DatabaseConnection;

import java.sql.Connection;
import java.util.ArrayList;

public class Percorso extends Thread {

    private String percorso;
    private String beacon;

    public Percorso(String beacon) {
        percorso = "";
        this.beacon = beacon;
        start();
    }

    public void run() {
        Connection conn = DatabaseConnection.getConn();
        String json = "{PERCORSO:[";


    }

    public String getResult() {
        while(percorso.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return percorso;
    }
}
