package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.Client;
import it.getout.gestioneconnessioni.Bluetooth;
import it.getout.gestioneconnessioni.Database;
import it.getout.gestioneconnessioni.GestoreDati;
import it.getout.gestioneconnessioni.Server;
import it.getout.gestionevisualizzazionemappa.Mappa;

public class GestoreEntita {

    private Context context;
    private GestoreDati reader;
    private String beacon;
    private Bluetooth bluetooth;
    private boolean downloadFinished;
    private boolean downloadNecessariFinished;

    private boolean uscito;


    /**
     * Costruttore
     * @param c
     */
    public GestoreEntita(Context c) {
        context = c;
        if(checkInternet()) {
            reader = new Server(context);
            ((Server)reader).downloadIniziale();    //serve per scaricamento iniziale
        }
        else reader = new Database(context);
        downloadFinished = false;
        downloadNecessariFinished = false;
        uscito=false;
    }

    public void coordinaPopolamentoDati() {
        initBluetooth(context);
    }


    /**
     * Funzione che, al primo avvio, scarica e istanzia i dati relativi all'attuale edificio, l'attuale piano e setta
     * la posizione attuale sulla base del MAC address del beacon passato come parametro.
     *
     * @param beacon id del beacon al quale si è collegati
     */
    private void scaricaDati(String beacon) {
        //All'inizio scarico solo i dati che mi servono immediatamente
        Edificio edificioAttuale = reader.richiediEdificioAttuale(beacon);
        Piano pianoAttuale = reader.richiediPianoAttuale(beacon);

        //Popolo il piano attuale
        ArrayList<Tronco> tronchiPiano = reader.richiediTronchiPiano(pianoAttuale.toString());
        pianoAttuale.setAule(reader.richiediAulePiano(pianoAttuale.toString()));
        for(int i=0; i<tronchiPiano.size(); i++) {
            tronchiPiano.get(i).setBeacons(reader.richiediBeaconTronco(tronchiPiano.get(i).getId()));
        }

        pianoAttuale.setTronchi(tronchiPiano);

        Beacon posizione = reader.richiediPosizione(beacon);
        Posizione.setEdificioAttuale(edificioAttuale);
        Posizione.setPianoAttuale(pianoAttuale);
        Posizione.setBeaconAttuale(posizione);

        Mappa.setMappa(reader.richiediMappaPiano(Posizione.getPianoAttuale().toString()));

        //Avvio comunque l'app
        ((Client)context).inizializzaFragment();

        Posizione.setUscite(reader.richiediUsciteEdificio(Posizione.getEdificioAttuale().toString()));

        downloadNecessariFinished = true;

        ArrayList<Piano> pianiEdificio = new ArrayList<>();
        pianiEdificio.add(pianoAttuale);
        edificioAttuale.setPiani(pianiEdificio);
        //Poi scarico tutto il resto
        scaricaDatiRimanenti(pianiEdificio);

    }

    /**
     * Funzione che istanzia i dati non essenziali sin da subito dell'edificio.
     *
     * @param pianiEdificio lista dei piani che costituiscono l'edificio corrente
     */

    private void scaricaDatiRimanenti(ArrayList<Piano> pianiEdificio) {
        ArrayList<Piano> pianiEdificioApp = new ArrayList<>(pianiEdificio);
        pianiEdificioApp.remove(0);

        try {

            ArrayList<Piano> altriPiani = reader.richiediPianiEdificio(Posizione.getEdificioAttuale().toString());
            altriPiani.remove(pianiEdificio.get(0));
            pianiEdificio.addAll(altriPiani);

            final ArrayList<Thread> threadsParalleli = new ArrayList<>();

            class ThreadParallelo extends Thread {

                final ArrayList<Thread> threadsParalleliTronco = new ArrayList<>();

                class ThreadParalleloTronco extends Thread {
                    private Tronco troncoAttuale;

                    private ThreadParalleloTronco(Tronco troncoAttuale) {
                        this.troncoAttuale = troncoAttuale;
                        threadsParalleliTronco.add(this);
                        start();
                    }

                    public void run() {
                        HashMap<String, Beacon> beaconsTronco = reader.richiediBeaconTronco(troncoAttuale.getId());
                        troncoAttuale.setBeacons(beaconsTronco);

                        threadsParalleliTronco.remove(this);
                    }
                }

                private Piano pianoAttuale;

                private ThreadParallelo(Piano pianoAttuale) {
                    this.pianoAttuale = pianoAttuale;
                    threadsParalleli.add(this);
                    start();
                }

                public void run() {
                    ArrayList<Aula> aulePiano = reader.richiediAulePiano(pianoAttuale.toString());
                    pianoAttuale.setAule(aulePiano);
                    ArrayList<Tronco> tronchiPiano = reader.richiediTronchiPiano(pianoAttuale.toString());
                    for (int j = 0; j < tronchiPiano.size(); j++) {
                        new ThreadParalleloTronco(tronchiPiano.get(j));
                    }
                    Thread attesa = new Thread() {
                        public void run() {
                            while (threadsParalleliTronco.size() > 0) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    attesa.start();
                    try {
                        attesa.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pianoAttuale.setTronchi(tronchiPiano);

                    threadsParalleli.remove(this);
                }
            }

            for (int i = 0; i < pianiEdificio.size(); i++) {
                new ThreadParallelo(pianiEdificio.get(i));
            }

            Thread attesa = new Thread() {
                public void run() {
                    while (threadsParalleli.size() > 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            attesa.start();
            try {
                attesa.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            boolean done = false;
            for (int i = 0; i < Posizione.getEdificioAttuale().getPiani().size(); i++) {
                Piano pianoAttuale = Posizione.getEdificioAttuale().getPiano(i);
                for (int j = 0; j < pianoAttuale.getTronchi().size(); j++) {
                    if (pianoAttuale.getTronchi().get(j).getBeacons().containsKey(Posizione.getIDBeaconAttuale())) {
                        Posizione.setBeaconAttuale(pianoAttuale.getTronchi().get(j).getBeacons().get(Posizione.getIDBeaconAttuale()));
                        done = true;
                        break;
                    }
                }
                if (done) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pianiEdificio.addAll(pianiEdificioApp);
            downloadFinished = true;
        }
    }

    /**
     * Funzione che richiede il percorso verso l'uscita o una destinazione scelta
     * @param destinazione uscita o id del beacon che si trova presso l'aula desiderata.
     * @return ArrayList<Tronco>
     */


    public ArrayList<Tronco> scaricaPercorso(String destinazione) {
        if(destinazione.isEmpty()) {
            return reader.richiediPercorsoFuga(Posizione.getIDBeaconAttuale());
        }
        else {
            return reader.richiediPercorsoFuga(Posizione.getIDBeaconAttuale()+","+destinazione);
        }
    }

    /**
     * Funzione che aggiorna i dati sulla posizione corrente in base al MAC address del beacon passato.
     * Controlla infine se è avvenuta l'uscita dell'utente dall'edificio.
     *
     * @param beacon id del beacon al quale ci si è collegati per ultimo
     */

    private void aggiornaDati(String beacon) {

        if (!beacon.equals(this.beacon)) {

            Piano pianoAttuale = reader.richiediPianoAttuale(beacon);

            if (pianoAttuale.equals(Posizione.getPianoAttuale())) {
                boolean done = false;

                //Ciclo i tronchi del piano alla ricerca del beacon
                for (int i = 0; i < Posizione.getPianoAttuale().getTronchi().size() && !done; i++) {

                    // Se sul tronco i-esimo è contenuto il beacon al quale l'app si è collegata
                    // per ultimo, setta la nuova posizione e la ridisegna su mappa
                    if (Posizione.getPianoAttuale().getTronchi().get(i).getBeacons().containsKey(beacon)) {

                        Posizione.setBeaconAttuale(Posizione.getPianoAttuale().getTronchi().get(i).getBeacons().get(beacon));
                        new Thread() {
                            public void run() {
                                ((Client) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((Client) context).getMappaFragment().disegnaPosizione();

                                    }
                                });
                            }
                        }.start();
                        done = true;
                    }
                }
            } else {
                boolean done = false;

                //Ciclo fra tutti i tronchi di tutti i piani alla ricerca del beacon e
                // disegno della nuova mappa e della nuova posizione su tale mappa.
                for (int i = 0; i < Posizione.getEdificioAttuale().getPiani().size() && !done; i++) {
                    Piano corrente = Posizione.getEdificioAttuale().getPiani().get(i);
                    for (int j = 0; j < corrente.getTronchi().size(); j++) {
                        if (corrente.getTronchi().get(i).getBeacons().containsKey(beacon)) {
                            Posizione.setBeaconAttuale(corrente.getTronchi().get(i).getBeacons().get(beacon));
                            Posizione.setPianoAttuale(corrente);
                            Mappa.setMappa(reader.richiediMappaPiano(Posizione.getPianoAttuale().toString()));
                            new Thread() {
                                public void run() {
                                    ((Client) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((Client) context).getMappaFragment().disegnaPosizione();

                                        }
                                    });
                                }
                            }.start();
                            done = true;
                        }
                    }
                }

            }
        }

        // Verifica se è avvenuta l'USCITA: se il beacon attuale è il beacon fittizio e l'ultimo
        // beacon reale al quale l'app si è collegata è taggato come "beacon di uscita" allora
        // considera l'utente come fuori dall'edificio e quindi fuori pericolo.
        // Viene quindi terminata chiusa l'applicazione.
        if(Uscita.checkUscita()) {
            Toast.makeText(context,"Bravo! Sei correttamente uscito dall'edificio.",Toast.LENGTH_SHORT).show();

            new Thread() {
                public void run() {
                    ((Client) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (btAdapter.isEnabled())
                                btAdapter.disable();

                            uscito=true;

                            ((Client)context).finishAffinity();

                        }
                    });
                }
            }.start();
        }
    }


    /**
     * Funzione per la scansione Bluetooth continua dei beacons. Se non viene rilevato alcun beacon
     * dopo 5 tentativi, la posizione corrente è settata a null.
     * @param c Context
     */
    private void initBluetooth(Context c) {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter

        if (btAdapter == null){
            Toast.makeText(c,"Bluetooth NOT support",Toast.LENGTH_SHORT).show();
        }else {

            // Is Bluetooth turned on?
            if (!btAdapter.isEnabled()) {
                //attivazione del bluetooth (qualora non sia già funzionante)
                Bluetooth.activateBluetooth((AppCompatActivity) c);
            }

            bluetooth = new Bluetooth(btAdapter, (AppCompatActivity) c);

            try {
                final Thread postDelayed = new Thread() {
                    public void run() {

                        Looper.prepare();

                        boolean primaScansione = true;
                        int contatore = 0;
                        BluetoothDevice device;

                        while (!uscito) {
                            try {
                                if (primaScansione) {
                                    do {
                                        bluetooth.discoverBLEDevices();
                                        Thread.sleep(1500);
                                    } while (!bluetooth.getTerminatedScan());
                                } else {
                                    contatore = 0;
                                    do {
                                        bluetooth.discoverBLEDevices();
                                        Thread.sleep(1500);
                                        contatore++;
                                    } while (!bluetooth.getTerminatedScan() && contatore < 5);
                                }

                                primaScansione = false;

                                Log.e("VALORE DEL CONTATORE", "CONTATORE = " + contatore);

                                // se sono uscito dal precedente while perché il contatore ha raggiunto il
                                // valore 5, allora setta il beacon attuale a null.
                                if (contatore >= 5) {

                                    Log.e("ERRORE ", "Setto il beacon a STRINGA VUOTA");
                                    Uscita.setBeaconPrecedente(Posizione.getIDBeaconAttuale());
                                    beacon = "";
                                    Posizione.setBeaconAttuale(new Beacon(beacon, null));
                                }

                                device = bluetooth.getCurrentBeacon();

                                if (device != null) {
                                    Log.e("Mi sono connesso", device.getAddress());

                                    if (beacon == null) {
                                        scaricaDati(device.getAddress());
                                        //new ThreadBluetooth(context);
                                    } else {
                                        aggiornaDati(device.getAddress());
                                    }

                                    beacon = device.getAddress();
                                } else {

                                    aggiornaDati(beacon);
                                }
                                Thread.sleep(7000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                };
                postDelayed.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Restituisce il valore della variabile per controllare se il download è terminato.
     * @return boolean
     */
    public boolean isDownloadFinished() { return downloadFinished;}


    /**
     * Restituisce il valore della variabile per controllare se il download è terminato.
     * @return boolean
     */
    public boolean isDownloadNecessariFinished() { return downloadNecessariFinished; }


    /**
     * Funzione che controlla è presente la connessione alla rete.
     * @return boolean
     */

    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
