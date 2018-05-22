package utilita;

import connessioni.Server;
import entita.DAOParametri;

public class Emergenza {

    private Thread controllo;
    private NotificaServer n;
    private boolean emergenza = false;

    public Emergenza() {
        controllo = new Thread(new Runnable() {
            @Override
            public void run() {
                NotificaServer notifica = null;
                while(true) {

                    //COSA DEVE FARE QUI:
                    //1. AGGIORNARE TUTTI I PARAMETRI DEI TRONCHI (PESI SU TUTTI I GRAFI)
                    //2. SE DAOParametri.controllaEmergenza==true allora invia notifica

                    if(DAOParametri.controllaEmergenza() && notifica==null) {
                        notifica = new NotificaServer(emergenza);
                        notifica.start();
                        emergenza = true;
                    }
                    else if(!DAOParametri.controllaEmergenza() && emergenza == true) {
                        notifica = new NotificaServer(emergenza);
                        notifica.start();
                        emergenza = false;
                    }else if(!DAOParametri.controllaEmergenza()){
                        notifica = null;
                    }

                    try {
                        Thread.sleep(15*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        controllo.start();
    }
}