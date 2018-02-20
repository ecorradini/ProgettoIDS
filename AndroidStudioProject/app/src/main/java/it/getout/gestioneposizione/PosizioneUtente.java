package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Point;
import android.widget.Toast;

import java.util.ArrayList;

import it.getout.gestioneconnessioni.DBHelper;

import static android.provider.Settings.Global.BLUETOOTH_ON;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class PosizioneUtente {

    //Coordinate dell'utente
    private Point posizione;
    //Istanza dell'edificio in cui si trova l'utente
    private Edificio edificioAttuale;
    //Istanza del piano in cui si trova l'utente
    private Piano pianoAttuale;
    //Istanza dell'attuale percorso calcolato
    private Percorso percorso;
    //Istanza dell'Adapter Bluetooth
    private BluetoothAdapter btAdapter;
    //Istanza del gestore Database SQLite
    private DBHelper dbReference;

    public void init(){

    }

    public void initBluetooth(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter
    }

    private ArrayList<BluetoothDevice> scansionaBluetooth(){
return null;
    }

    private String getBeaconId(ArrayList<BluetoothDevice> Array){
        return null;
    }

    public void setPosizione(String string){

    }

    public void getPosizione(){

    }

    public Percorso getPercorso(Point point){
        return percorso;
    }

    public Edificio getEdificioAttuale(){
        return edificioAttuale;
    }

    public Piano getPianoAttuale(){
        return pianoAttuale;
    }

}
