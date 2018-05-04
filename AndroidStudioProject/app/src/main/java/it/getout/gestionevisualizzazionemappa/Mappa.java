package it.getout.gestionevisualizzazionemappa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created on 01/02/2018.
 */

public class Mappa {
    private static Bitmap immagine;
    private static double width;
    private static double height;

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

    /*public static void setMappa(String base64Image){
        immagine = convertiDaBase64(base64Image);
        width = immagine.getWidth();
        height = immagine.getHeight();
    }*/
    public static void setMappa(Bitmap image) {
        immagine = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public static Bitmap getMappa() {
        return immagine;
    }

    private static Bitmap convertiDaBase64(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
