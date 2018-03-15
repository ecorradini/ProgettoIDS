package it.getout.gestioneposizione;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import it.getout.MainActivity;
import it.getout.gestioneconnessioni.BluetoothHelper;
import it.getout.gestioneconnessioni.Connessioni;


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


    //Context
    private static Context context;

    public static void init(Context c) {
        context = c;

        //Solo per TESTS
        getInfoByBeaconID(beaconAttuale.getId());
    }

    public static void getInfoByBeaconID(String beaconAttuale) {
        if(!checkInternet()) {
            edificioAttuale = Connessioni.getDbReference().initEdificioAttuale(beaconAttuale);
            pianoAttuale = Connessioni.getDbReference().initPianoAttuale(beaconAttuale);
        }
        else {

            ((MainActivity)context).startLoading();
            Connessioni.getServerReference().richiediEdificio(beaconAttuale);
        }
    }


    public static String getBeaconId(){
        return beaconAttuale.getId();
    }

    public static void setPosizione(String beaconID){
        posizione = Connessioni.getDbReference().getPosizione(beaconID);
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
