package utilita;

import entita.DAOParametri;
import gui.CSVImport;
import gui.GUIAmministrazione;
import gui.GUIParametri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                            System.out.println("Comandi eseguibili per il server di GetOut!:");
                            System.out.println("'e -i id_tronco' --> inizio test emergenza sul tronco id_tronco");
                            System.out.println("'e -f id_tronco' --> fine test emergenza sul tronco id_tronco");
                            System.out.println("'amm' --> avvio interfaccia di amministrazione dati del server");
                            System.out.println("'par' --> avvio interfaccia di modifica dei parametri dei tronchi");
                            System.out.println("'csv edificio' --> caricamento CSV edifici");
                            System.out.println("'csv piano' --> caricamento CSV piani");
                            System.out.println("'csv tronco' --> caricamento CSV tronchi");
                            System.out.println("'csv aula' --> caricamento CSV aule");
                            System.out.println("'csv beacon' --> caricamento CSV beacon");
                            System.out.println();
                        }
                        if (command.equals("amm")) {
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
                            new CSVImport(command.substring(4,command.length()).toUpperCase());
                            System.out.println("Ho terminato l'importazione");
                        }
                    } catch(Exception e) {
                        System.out.println("Comando non trovato!");
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
