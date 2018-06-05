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

/**
 * Classe avvio iniziale
 */
public class Server {

    //Grafi dei piani mantenuti in memoria
    private static HashMap<String,HashMap<String,GrafoTronchi>> grafiPiani;

    /**
     * Metodo main per l'avvio del Server GetOut!. Avvia la connessione al database, manda un broadcast nella rete con l'indirizzo IP e popola i grafi delle mappe dei piani.
     * Infine avvia la procedura di controllo emergenze, la console di comando e il server JSON per accettare le richieste dai client.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{

        //Avvio la connessione al database
        try {
            System.out.println("Server Start");
            Database.init();

        } catch (ClassNotFoundException e) {
            System.out.println("Eccezione Class not found");
            e.printStackTrace();
        }


        //Mando il broadcast dell'indirizzo IP nella rete
        Thread discoveryThread = new Thread(DiscoveryIP.getInstance());
        discoveryThread.start();
        //Popolo i grafi
        popolaGrafi();

        //Avvio il controllo di emergenze
        new Emergenza();

        //Avvio la console di comando
        new Thread(new ConsoleDiComando()).start();

        //Avvio il server JSON
        new JsonServer();
    }

    /**
     * Metodo per il popolamento dei grafi associati alle mappe.
     */
    private static void popolaGrafi() {
        Thread popolamento = new Thread() {
            public void run() {
                System.out.println("Avvio il caricamento delle mappe. Attendere per favore.");
                //Variabili numeriche per il calcolo della percentuale di completamento
                int nEdifici = DAOEdificio.selectCountEdifici();
                int nPiani = DAOPiano.selectCountPiani();
                int numeroTotaleDati = nEdifici*nPiani;
                int percentualeCompletamento=0;
                //Instanzio l'HashMap dei grafi
                grafiPiani = new HashMap<>();
                ArrayList<String> edifici = DAOEdificio.selectEdifici();
                //Per ogni edificio genero il grafo
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
    }

    public static HashMap<String,HashMap<String,GrafoTronchi>> getGrafiPiani() { return grafiPiani; }
}