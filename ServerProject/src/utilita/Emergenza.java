package utilita;

import connessioni.Server;
import entita.DAOParametri;

public class Emergenza {

    private Thread controllo;

    public Emergenza() {
        controllo = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {

                    //COSA DEVE FARE QUI:
                    //1. AGGIORNARE TUTTI I PARAMETRI DEI TRONCHI (PESI SU TUTTI I GRAFI)
                    //2. SE DAOParametri.controllaEmergenza==true allora invia notifica

                    if(DAOParametri.controllaEmergenza() && !Notifica.isWorking()) {
                        Notifica.startThread("EMERGENZA AIUTO AIUTO CIAOCIAO");
                        System.out.println("EMERGENZA");
                    }
                    else if(!DAOParametri.controllaEmergenza() && Notifica.isWorking()) {
                        Notifica.stopThread();
                        System.out.println("FINE EMERGENZA");
                    }

                    try {
                        Thread.sleep(30*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        controllo.start();
    }
}
