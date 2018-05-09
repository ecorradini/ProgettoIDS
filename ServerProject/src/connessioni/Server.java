package connessioni;


import entita.DAOEdificio;
import entita.DAOPiano;
import entita.GrafoTronchi;
import utilita.ConsoleDiComando;
import utilita.Emergenza;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    private static HashMap<String,HashMap<String,GrafoTronchi>> grafiPiani;

    public static void main(String[] args) throws IOException{

        try {
            System.out.println("Server Start");
            DatabaseConnection.init();

        } catch (ClassNotFoundException e) {
            System.out.println("Eccezione Class not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eccezione SQL");
            e.printStackTrace();
        }

        Thread discoveryThread = new Thread(DiscoveryIP.getInstance());
        discoveryThread.start();
        popolaGrafi();

        new JsonServer();
    }

    private static void popolaGrafi() {
        Thread popolamento = new Thread() {
            public void run() {
                System.out.println("Avvio il caricamento delle mappe. Attendere per favore.");
                int nEdifici = DAOEdificio.selectCountEdifici();
                int nPiani = DAOPiano.selectCountPiani();
                int numeroTotaleDati = nEdifici*nPiani;
                int percentualeCompletamento=0;
                grafiPiani = new HashMap<>();
                ArrayList<String> edifici = DAOEdificio.selectEdifici();
                for (int i = 0; i < edifici.size(); i++) {
                    ArrayList<String> piani = DAOPiano.selectListaPianiByEdificio(edifici.get(i));
                    HashMap<String, GrafoTronchi> value = new HashMap<>();
                    for (int j = 0; j < piani.size(); j++) {
                        value.put(piani.get(j), new GrafoTronchi(piani.get(j)));
                        percentualeCompletamento += (100/(numeroTotaleDati));
                        System.out.print("\rPercentuale di completamento: "+percentualeCompletamento+"%\r");
                    }
                    grafiPiani.put(edifici.get(i), value);
                    if(percentualeCompletamento<100) {
                        System.out.print("\rPercentuale di completamento: 100%\r");
                    }
                }
            }
        };
        popolamento.start();
        try {
            popolamento.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TEST
        new Emergenza();

        new Thread(new ConsoleDiComando()).start();
    }

    public static HashMap<String,HashMap<String,GrafoTronchi>> getGrafiPiani() { return grafiPiani; }
}