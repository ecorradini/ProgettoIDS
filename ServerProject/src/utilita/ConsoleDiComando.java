package utilita;

import entita.DAOParametri;
import gui.GUIAmministrazione;

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
                if(command.equals("e")) {
                    System.out.println("INIZIO TEST EMERGENZA");
                    testaEmergenza();
                }
                if(command.equals("!e")) {
                    fineTestaEmergenza();
                }
                if(command.equals("ui")) {
                    new GUIAmministrazione();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * metodo che permette di avviare l'emergenza
     */
    private void testaEmergenza() {
        DAOParametri.updateTestaEmergenza();
    }

    private void fineTestaEmergenza() {
        DAOParametri.updateFineTestaEmergenza();
    }
}
