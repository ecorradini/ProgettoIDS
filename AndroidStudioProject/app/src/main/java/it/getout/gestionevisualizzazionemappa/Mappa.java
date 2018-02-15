package it.getout.gestionevisualizzazionemappa;

import android.graphics.Bitmap;

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

    public static String convertiDaBase64(String string) {
        return string;              //ritorno cosa? messo a caso
    }

}
