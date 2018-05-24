package utilita;

import entita.DAOParametri;
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
                if(command.equals("amm")) {
                    new GUIAmministrazione();
                }
                else if(command.equals("par")) {
                    new GUIParametri();
                }
                else if(command.substring(0,4).equals("e -i")) {
                    int tronco = Integer.parseInt(command.substring(5));
                    testaEmergenza(tronco);
                }
                else if(command.substring(0,4).equals("e -f")) {
                    int tronco = Integer.parseInt(command.substring(5));
                    fineTestaEmergenza(tronco);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
