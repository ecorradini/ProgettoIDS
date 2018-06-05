package it.getout.gestioneposizione;

import android.content.Context;
import android.graphics.PointF;

import java.util.ArrayList;

import it.getout.gestionevisualizzazionemappa.MappaFragment;


/**
 * Classe statica che rappresenta la posizione dell'utente.
 * Created on 01/02/2018.
 */

public class Posizione {

    private static Edificio edificioAttuale; //Istanza dell'edificio in cui si trova l'utente
    private static Piano pianoAttuale; //Istanza del piano in cui si trova l'utente
    private static Beacon beaconAttuale; //Beacon al quale l'utente è collegato al momento
    private static ArrayList<String> uscite;

    /**
     * Ritorna l'edificio attuale.
     * @return Edificio
     */
    public static Edificio getEdificioAttuale(){ return edificioAttuale; }

    /**
     * Setta l'edificio attuale.
     * @param edificio
     */
    public static void setEdificioAttuale(Edificio edificio) { edificioAttuale=edificio; }

    /**
     * Ritorna il piano attuale.
     * @return Piano
     */
    public static Piano getPianoAttuale(){
        return pianoAttuale;
    }

    /**
     * Setta il piano attuale.
     * @param p
     */
    public static void setPianoAttuale(Piano p) { pianoAttuale = p; }

    /**
     * Ritorna le coordinate del beacon attuale.
     * @return PointF
     */
    public static PointF getPosizione() {
        return beaconAttuale.getPosizione();
    }

    /**
     * Ritorna id del beacon attuale.
     * @return String
     */
    public static String getIDBeaconAttuale() { return beaconAttuale.getId(); }

    /**
     * Ritorna il beacon attuale.
     * @return Beacon
     */
    public static Beacon getBeaconAttuale() { return beaconAttuale; }

    /**
     * Setta il beacona attuale.
     * @param b
     */
    public static void setBeaconAttuale(Beacon b) { beaconAttuale = b; }

    /**
     * Setta i beacon taggati come beacon di uscita.
     * @param u
     */
    public static void setUscite(ArrayList<String> u){ uscite = u; }

    /**
     * Controlla se il beacon passato è di uscita.
     * @param beacon
     * @return boolean
     */
    public static boolean isUscita(String beacon){
        return uscite.contains(beacon);
    }
}
