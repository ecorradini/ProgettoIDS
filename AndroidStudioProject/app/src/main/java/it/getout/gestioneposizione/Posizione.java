package it.getout.gestioneposizione;

import android.content.Context;
import android.graphics.PointF;

import java.util.ArrayList;

import it.getout.gestionevisualizzazionemappa.MappaFragment;


/**
 * Created on 01/02/2018.
 */

public class Posizione {

    private static Edificio edificioAttuale; //Istanza dell'edificio in cui si trova l'utente
    private static Piano pianoAttuale; //Istanza del piano in cui si trova l'utente
    private static Beacon beaconAttuale; //Beacon al quale l'utente è collegato al momento
    private static ArrayList<String> uscite;

    public static Edificio getEdificioAttuale(){ return edificioAttuale; }

    public static void setEdificioAttuale(Edificio edificio) { edificioAttuale=edificio; }

    public static Piano getPianoAttuale(){
        return pianoAttuale;
    }

    public static void setPianoAttuale(Piano p) { pianoAttuale = p; }

    public static PointF getPosizione() {
        return beaconAttuale.getPosizione();
    }

    public static String getIDBeaconAttuale() { return beaconAttuale.getId(); }

    public static Beacon getBeaconAttuale() { return beaconAttuale; }

    public static void setBeaconAttuale(Beacon b) { beaconAttuale = b; }

    public static void setUscite(ArrayList<String> u){ uscite = u; }

    public static boolean isUscita(String beacon){
        return uscite.contains(beacon);
    }
}
