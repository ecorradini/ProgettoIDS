package utilita;

import connessioni.Server;
import entita.DAOParametri;

public class Emergenza {

    private Thread controllo;
    private NotificaServer n;

    public Emergenza() {
        controllo = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {

                    //COSA DEVE FARE QUI:
                    //1. AGGIORNARE TUTTI I PARAMETRI DEI TRONCHI (PESI SU TUTTI I GRAFI)
                    //2. SE DAOParametri.controllaEmergenza==true allora invia notifica
                    NotificaServer notifica = new NotificaServer();
                    if(DAOParametri.controllaEmergenza() && !notifica.isWorking()) {
                        notifica.start();
                        System.out.println("EMERGENZA");
                    }
                    else if(!DAOParametri.controllaEmergenza() && notifica.isWorking()) {
                        System.out.println("FINE EMERGENZA");
                    }

                    try {
                        Thread.sleep(30*100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        controllo.start();
    }
}
