package it.getout.gestioneposizione;

import android.content.Context;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import it.getout.gestioneconnessioni.Connessioni;


/**
 * Created on 01/02/2018.
 */

public class Posizione {

    private static PointF posizione; //Coordinate dell'utente
    private static Edificio edificioAttuale; //Istanza dell'edificio in cui si trova l'utente
    private static Piano pianoAttuale; //Istanza del piano in cui si trova l'utente
    private static Beacon beaconAttuale; //Beacon al quale l'utente è collegato al momento
    private static Percorso percorso; //Istanza dell'attuale percorso calcolato


    //Context
    private static Context context;

    public static void init(Context c) {
        context = c;

        //Solo per TESTS
        try {
            getInfoByBeaconID(beaconAttuale.getId());
        } catch (Exception e){
            e.printStackTrace();
            Log.e("bluetooth error","non funziona");
        }

    }

    public static void getInfoByBeaconID(String beaconAttuale) {
        if(!checkInternet()) {
            edificioAttuale = Connessioni.getDbReference().initEdificioAttuale(beaconAttuale);
            pianoAttuale = Connessioni.getDbReference().initPianoAttuale(beaconAttuale);
        }
        else {
            Connessioni.getServerReference().richiediEdificio(beaconAttuale);
        }
    }

    public static String getBeaconId(){
        return beaconAttuale.getId();
    }

    public static void setPosizione(PointF pos){
        posizione = pos;
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

    public static void setPianoAttuale(Piano p) { pianoAttuale = p; }

    public static Beacon getBeaconAttuale() { return beaconAttuale; }

    public static void setBeaconAttuale(Beacon b) { beaconAttuale = b; }

    public static boolean checkInternet() {                                     ///
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
