package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Point;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.Settings.Global.BLUETOOTH_ON;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class PosizioneUtente {

    private Point posizione;
    private Edificio edificioAttuale;
    private Piano pianoAttuale;
    private Percorso percorso;

    public void init(){

    }

    public void initBluetooth(BluetoothAdapter btAdapter){
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
