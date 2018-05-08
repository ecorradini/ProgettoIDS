package utilita;

import connessioni.Server;

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

                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        controllo.start();
    }
}
