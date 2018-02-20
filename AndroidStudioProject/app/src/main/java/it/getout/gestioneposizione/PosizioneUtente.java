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

import it.getout.gestioneconnessioni.DBHelper;

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
    //Istanza del gestore Database SQLite
    private static DBHelper dbReference;

    public static final int REQUEST_ENABLE_BT = 1;

    public static void init(Context context) {
        dbReference = new DBHelper(context);
        initBluetooth(context);
    }

    public static void getInfoByBeaconID(String beaconAttuale) {
        edificioAttuale = dbReference.initEdificioAttuale(beaconAttuale);
    }

    private static void initBluetooth(Context context) {

        if (btAdapter == null) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter
        }
        // Is Bluetooth turned on?
        if (!btAdapter.isEnabled()) {
            // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((AppCompatActivity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    private static ArrayList<BluetoothDevice> scansionaBluetooth(){
        //ArrayAdapter adapter = null;
        ArrayList<BluetoothDevice> array = null;
        ArrayList<String> btst = null;
        Set<BluetoothDevice> dispositivi;
        dispositivi = btAdapter.getBondedDevices();
        array.clear();
        //BluetoothDevice b = (BluetoothDevice) dispositivi;
        for(BluetoothDevice bt : dispositivi){
            btst.add(bt.getName());
        }
        return array;
    }

    private static String getBeaconId(ArrayList<BluetoothDevice> Array){
        return null;
    }

    public static void setPosizione(String beaconID){
        posizione = dbReference.getPosizione(beaconID);
    }

    public static PointF getPosizione(){ return posizione; }

    public static Percorso getPercorso(PointF point){
        return percorso;
    }

    public static Edificio getEdificioAttuale(){ return edificioAttuale; }

    public static Piano getPianoAttuale(){
        return pianoAttuale;
    }

}
