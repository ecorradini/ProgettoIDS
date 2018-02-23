package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import it.getout.gestioneconnessioni.BluetoothHelper;
import it.getout.gestioneconnessioni.DBHelper;
import it.getout.gestioneconnessioni.ServerHelper;
import it.getout.gestionevisualizzazionemappa.Mappa;

import static android.provider.Settings.Global.BLUETOOTH_ON;

/**
 * Created by Alessandro on 01/02/2018.
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
    private static ArrayList<BluetoothDevice> btdevice;

    public static final int REQUEST_ENABLE_BT = 1;

    public static void init(Context context) {
        dbReference = new DBHelper(context);
        serverRefence = new ServerHelper(context);
        initBluetooth(context);
    }

    public static void getInfoByBeaconID(String beaconAttuale) {
        edificioAttuale = dbReference.initEdificioAttuale(beaconAttuale);
        pianoAttuale = dbReference.initPianoAttuale(edificioAttuale,beaconAttuale);
        Mappa.setMappa(pianoAttuale);
    }
    /**
    *Metodo che inizializza il bluetooth e tutte le sue fasi(scanner)
     */
    private static void initBluetooth(Context context) {

        if (btAdapter == null) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter
        }

        // Is Bluetooth turned on?
        if (!btAdapter.isEnabled()) {
            //attivazione del bluetooth (qualora non sia già funzionante)
            btHelper.activateBluetooth();
        }

        btHelper = new BluetoothHelper(btAdapter, ((AppCompatActivity) context));
        btHelper.discoverBLEDevices();
    }
    /**
     *Metodo che si adopera ad effettuare lo scan dei dispositivi bluetooth
     */
    private static ArrayList<BluetoothDevice> scansionaBluetooth(){
        ArrayList<BluetoothDevice> array = null;
        btHelper.discoverBLEDevices();
        return array;
    }

    private static String getBeaconId(ArrayList<BluetoothDevice> Array){
        return null;
    }

    public static void setPosizione(String beaconID){
        posizione = dbReference.getPosizione(beaconID);
    }

    public static PointF getPosizione(){ return posizione; }

    public static Percorso getPercorso(){
        return percorso;
    }

    public static Edificio getEdificioAttuale(){ return edificioAttuale; }

    public static Piano getPianoAttuale(){
        return pianoAttuale;
    }

    public static DBHelper getDbReference() { return dbReference; }

    public static ServerHelper getServerReference() { return serverRefence; }
}
