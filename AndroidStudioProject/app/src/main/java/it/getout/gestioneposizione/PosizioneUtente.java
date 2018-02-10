package it.getout.gestioneposizione;

import android.bluetooth.BluetoothDevice;
import android.graphics.Point;

import java.util.ArrayList;

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

    private void initBluetooth(){

    }

    /*private ArrayList<BluetoothDevice> scansionaBluetooth(){

    }*/

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
