package it.getout.gestionevisualizzazionemappa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Alessandro on 01/02/2018.
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

    public static void setMappa (String string) {
    }

    public static void getMappaDB(String string){
    }

    public static Bitmap getMappa() {
        return immagine;            //ritorno cosa? messo a caso
    }

    private static void convertiDaBase64(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        immagine = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
