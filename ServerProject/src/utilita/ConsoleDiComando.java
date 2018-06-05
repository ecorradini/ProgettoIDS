package utilita;

import entita.DAOParametri;
import entita.Tronco;
import gui.ImportaCSV;
import gui.GUIAmministrazione;
import gui.GUIParametri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class ConsoleDiComando implements Runnable {

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try{
                String command = br.readLine();
                if(command.length()>0) {
                    try {
                        if (command.equals("help")) {
                            System.out.println();
                            System.out.println("Comandi eseguibili per il server di GetOut!:\n");
                            System.out.println("\033[1m'e -i <id_tronco>'\033[0m \t--> inizio test emergenza sul tronco id_tronco\n");
                            System.out.println("\033[1m'e -f <id_tronco>'\033[0m \t--> fine test emergenza sul tronco id_tronco\n");
                            System.out.println("\033[1m'amm'\033[0m \t\t\t--> avvio interfaccia di amministrazione dati del server\n");
                            System.out.println("\033[1m'par'\033[0m \t\t\t--> avvio interfaccia di modifica dei parametri dei tronchi\n");
                            System.out.println("\033[1m'pesi <peso_vulnerabilita> <peso_rischiovita> <peso_presenzafumo> <peso_lunghezza> <peso_los>'\033[0m\n \t\t\t\t--> aggiornamento valore dei pesi\n");
                            System.out.println("\033[1m'csv edificio'\033[0m \t\t--> caricamento CSV edifici\n");
                            System.out.println("\033[1m'csv piano'\033[0m \t\t--> caricamento CSV piani\n");
                            System.out.println("\033[1m'csv tronco'\033[0m \t\t--> caricamento CSV tronchi\n");
                            System.out.println("\033[1m'csv aula'\033[0m \t\t--> caricamento CSV aule\n");
                            System.out.println("\033[1m'csv beacon'\033[0m \t\t--> caricamento CSV beacon\n");
                            System.out.println("\033[1m'exit'\033[0m \t\t\t--> spegnimento server");
                            System.out.println();
                        }
                        else if (command.equals("amm")) {
                            new GUIAmministrazione();
                        } else if (command.equals("par")) {
                            new GUIParametri();
                        } else if (command.substring(0, 4).equals("e -i")) {
                            int tronco = Integer.parseInt(command.substring(5));
                            testaEmergenza(tronco);
                        } else if (command.substring(0, 4).equals("e -f")) {
                            int tronco = Integer.parseInt(command.substring(5));
                            fineTestaEmergenza(tronco);
                        }
                        else if(command.substring(0,3).equals("csv")) {
                            System.out.println("Inizio l'importazione");
                            new ImportaCSV(command.substring(4,command.length()).toUpperCase());
                            System.out.println("Ho terminato l'importazione");
                        }
                        else if(command.substring(0,4).equals("pesi")) {
                            String pesi[] = command.substring(5).split(" ");
                            Tronco.setWeight(Float.parseFloat(pesi[0]),Float.parseFloat(pesi[1]),Float.parseFloat(pesi[2]),Float.parseFloat(pesi[3]),Float.parseFloat(pesi[4]));
                        }else if(command.substring(0,4).equals("exit")) {
                            System.exit(0);
                        }
                        else System.out.println("Comando non trovato. Digitare 'help' per ottenere la lista dei comandi disponibli.");
                    } catch(Exception e) {
                        System.out.println("Comando non trovato. Digitare 'help' per ottenere la lista dei comandi disponibli.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Comando non trovato!");
            }
        }
    }

    /**
     * metodo che permette di avviare l'emergenza
     */
    private void testaEmergenza(int tronco) {
        DAOParametri.updateTestaEmergenza(tronco);
    }

    private void fineTestaEmergenza(int tronco) {
        DAOParametri.updateFineTestaEmergenza(tronco);
    }


}
