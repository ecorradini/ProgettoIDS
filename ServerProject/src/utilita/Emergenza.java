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
                        System.out.println("inizioemergenza");
                        emergenza = true;
                        notifica = new NotificaServer(emergenza);
                        notifica.start();

                    }
                    else if(!DAOParametri.controllaEmergenza() && emergenza == true) {
                        System.out.println("fineemergenza");
                        emergenza = false;
                        notifica = new NotificaServer(emergenza);
                        notifica.start();

                    }else if(!DAOParametri.controllaEmergenza() && emergenza == false ){
                        //System.out.println("birillo");
                        notifica = null;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        controllo.start();
    }
}
