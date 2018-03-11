package it.getout.gestioneposizione;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Context;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import it.getout.gestioneconnessioni.BluetoothHelper;
import it.getout.gestioneconnessioni.DBHelper;
import it.getout.gestioneconnessioni.ServerHelper;
import it.getout.gestionevisualizzazionemappa.Mappa;

/**
 * Created on 01/02/2018.
 */

public class PosizioneUtente {

    //Coordinate dell'utente
    private static PointF posizione;
    //Istanza dell'edificio in cui si trova l'utente
    private static Edificio edificioAttuale;
    //Istanza del piano in cui si trova l'utente
    private static Piano pianoAttuale;
    //Beacon al quale l'utente è collegato al momento
    private static Beacon beaconAttuale;
    //Istanza dell'attuale percorso calcolato
    private static Percorso percorso;
    //Istanza dell'Adapter Bluetooth
    private static BluetoothAdapter btAdapter;
    //scanner per ricercare i dispositivi beacon
    private static BluetoothHelper btHelper;
    //Istanza del gestore Database SQLite
    private static DBHelper dbReference;
    //Istanza del gestore Server
    private static ServerHelper serverRefence;
    //Istanza del descrittore dei dispositivi bluetooth
    private static BluetoothDevice device;
    //Context
    private static Context context;

    public static void init(Context c) {
        context = c;
        dbReference = new DBHelper(c);
        serverRefence = new ServerHelper(c);
       // initBluetooth(c);
        //Solo per TESTS
        getInfoByBeaconID("prova");
    }

    public static void getInfoByBeaconID(String beaconAttuale) {
        if(!checkInternet()) {
            edificioAttuale = dbReference.initEdificioAttuale(beaconAttuale);
            pianoAttuale = dbReference.initPianoAttuale(beaconAttuale);
        }
        else {
            serverRefence.richiediEdificio(beaconAttuale);
        }

        //Mappa.setMappa(pianoAttuale);
    }
    /**
    *Metodo che inizializza il bluetooth e tutte le sue fasi(scanner)
     */
    private static void initBluetooth(Context c) {
        if (btAdapter == null) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter
        }

        // Is Bluetooth turned on?
        if (!btAdapter.isEnabled()) {
            //attivazione del bluetooth (qualora non sia già funzionante)
            btHelper.activateBluetooth();
        }

        btHelper = new BluetoothHelper(btAdapter, (AppCompatActivity)c);
        device =  scansionaBluetooth();
        //memorizzo beaconAttusle all'interno dell'oggetto Beacon
        beaconAttuale.setId(device.getAddress());

        getInfoByBeaconID(beaconAttuale.getId());
    }
    /**
     *Metodo che si adopera ad effettuare lo scan dei dispositivi bluetooth
     */
    private static BluetoothDevice scansionaBluetooth(){
        btHelper.discoverBLEDevices();
        while(!btHelper.getTerminatedscan()){
            device = btHelper.getCurrentBeacon();
        }
        return device;
    }

    public static String getBeaconId(){
        return beaconAttuale.getId();
    }

    public static void setPosizione(String beaconID){
        posizione = dbReference.getPosizione(beaconID);
    }

    public static PointF getPosizione(){ return posizione; }

    public static Percorso getPercorso(){
        return percorso;
    }

    public static Edificio getEdificioAttuale(){ return edificioAttuale; }
    public static void setEdificioAttuale(Edificio edificio) { edificioAttuale=edificio; }

    public static Piano getPianoAttuale(){
        return pianoAttuale;
    }

    public static DBHelper getDbReference() { return dbReference; }

    public static ServerHelper getServerReference() { return serverRefence; }

    public static boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
