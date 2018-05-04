package it.getout.gestioneposizione;

import android.content.Context;
import android.graphics.PointF;

import it.getout.gestionevisualizzazionemappa.MappaFragment;


/**
 * Created on 01/02/2018.
 */

public class Posizione {

    private static Edificio edificioAttuale; //Istanza dell'edificio in cui si trova l'utente
    private static Piano pianoAttuale; //Istanza del piano in cui si trova l'utente
    private static String beaconAttuale; //Beacon al quale l'utente è collegato al momento

    public static Edificio getEdificioAttuale(){ return edificioAttuale; }

    public static void setEdificioAttuale(Edificio edificio) { edificioAttuale=edificio; }

    public static Piano getPianoAttuale(){
        return pianoAttuale;
    }

    public static void setPianoAttuale(Piano p) { pianoAttuale = p; }

    public static PointF getPosizione() {
        Beacon res = null;
        for(int i=0; i<pianoAttuale.getTronchi().size(); i++) {
            if(pianoAttuale.getTronchi().get(i).getBeacons().containsKey(beaconAttuale)) {
                res = pianoAttuale.getTronchi().get(i).getBeaconByID(beaconAttuale);
            }
        }
        return res.getPosizione();
    }

    public static String getIDBeaconAttuale() { return beaconAttuale; }

    public static void setBeaconAttuale(String b) { beaconAttuale = b; }
}
