package it.getout.gestionevisualizzazionemappa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.PosizioneUtente;

/**
 * Created on 01/02/2018.
 */

public class Mappa {
    private static Bitmap immagine;
    private static double witdh;
    private static double height;

    public static double getWitdh() {
        return witdh;
    }

    public static double getHeight() {
        return height;
    }

    public static void setMappa (Piano piano) {
        immagine = convertiDaBase64(getMappaDB(piano));
    }

    private static String getMappaDB(Piano piano){
        return PosizioneUtente.getDbReference().queryMappa(piano);
    }

    public static Bitmap getMappa() {
        return immagine;
    }

    private static Bitmap convertiDaBase64(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
