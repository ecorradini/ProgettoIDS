package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

    public GestoreEntita(Context c) {
        context = c;
        if(checkInternet()) {
            reader = new Server(context);
        }
        else reader = new Database(context);
        downloadFinished = false;
        downloadNecessariFinished = false;
    }

    public void coordinaPopolamentoDati() {
        initBluetooth(context);
    }

    private void scaricaDati() {
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

        downloadNecessariFinished = true;

        ArrayList<Piano> pianiEdificio = new ArrayList<>();
        pianiEdificio.add(pianoAttuale);
        edificioAttuale.setPiani(pianiEdificio);
        //Poi scarico tutto il resto
        scaricaDatiRimanenti(pianiEdificio);

    }

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
            Log.e(e.getCause().toString(),e.getMessage());
            e.printStackTrace();
        } finally {
            pianiEdificio.addAll(pianiEdificioApp);
            downloadFinished = true;
        }
    }

    public ArrayList<Tronco> scaricaPercorso() {
        return reader.richiediPercorsoFuga(Posizione.getIDBeaconAttuale());
    }

    private void initBluetooth(Context c) {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter

        // Is Bluetooth turned on?
        if (!btAdapter.isEnabled()) {
            //attivazione del bluetooth (qualora non sia già funzionante)
            Bluetooth.activateBluetooth((AppCompatActivity)c);
        }

        bluetooth = new Bluetooth(btAdapter, (AppCompatActivity)c);

        try {
            Thread postDelayed = new Thread() {
                public void run () {

                    BluetoothDevice device = null;

                    try {
                        do {
                            bluetooth.discoverBLEDevices();
                            Thread.sleep(1500);
                        }while(!bluetooth.getTerminatedScan());

                        device = bluetooth.getCurrentBeacon();

                        Log.e("Mi sono connesso",device.getAddress());

                        beacon = device.getAddress();

                        scaricaDati();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            postDelayed.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDownloadFinished() { return downloadFinished;}

    public boolean isDownloadNecessariFinished() { return downloadNecessariFinished; }

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
