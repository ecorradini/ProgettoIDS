package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.Client;
import it.getout.gestioneconnessioni.Bluetooth;
import it.getout.gestioneconnessioni.Database;
import it.getout.gestioneconnessioni.GestoreDati;
import it.getout.gestioneconnessioni.Server;

public class GestoreEntita {

    private Context context;
    private GestoreDati reader;
    private String beacon;
    private Bluetooth bluetooth;

    public GestoreEntita(Context c) {
        context = c;
        if(checkInternet()) {
            reader = new Server(context);
        }
        else reader = new Database(context);
    }

    public void coordinaPopolamentoDati() {
        initBluetooth(context);
    }

    private void scaricaDati() {
        Edificio edificioAttuale = reader.richiediEdificioAttuale(beacon);
        ArrayList<Piano> pianiEdificio = reader.richiediPianiEdificio(edificioAttuale.toString());
        for(int i=0; i<pianiEdificio.size(); i++) {
            ArrayList<Aula> aulePiano = reader.richiediAulePiano(pianiEdificio.get(i).toString());
            pianiEdificio.get(i).setAule(aulePiano);
            ArrayList<Tronco> tronchiPiano = reader.richiediTronchiPiano(pianiEdificio.get(i).toString());
            Log.e("TRONCHI "+pianiEdificio.get(i).toString(),tronchiPiano.size()+"");
            for(int j=0; j<tronchiPiano.size(); j++) {
                Log.e("TRONCO",tronchiPiano.get(j).getId()+"");
                HashMap<String,Beacon> beaconsTronco = reader.richiediBeaconTronco(tronchiPiano.get(j).getId());
                tronchiPiano.get(j).setBeacons(beaconsTronco);
            }
            pianiEdificio.get(i).setTronchi(tronchiPiano);
        }
        edificioAttuale.setPiani(pianiEdificio);

        Posizione.setEdificioAttuale(edificioAttuale);
        Posizione.setPianoAttuale(reader.richiediPianoAttuale(beacon));

        ((Client)context).inizializzaFragment();
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

                        Posizione.setBeaconAttuale(device.getAddress());
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

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
