package it.getout.GestioneVisualizzazioneMappa;

import android.graphics.Bitmap;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Mappa {
    private Bitmap immagine;
    private double witdh;
    private double height;

    public double getWitdh() {
        return witdh;
    }

    public double getHeight() {
        return height;
    }

    public void setMappa (String string) {
    }

    public void getMappaDB(String string){
    }

    public Bitmap getMappa() {
        return immagine;            //ritorno cosa? messo a caso
    }

    public String convertiDaBase64(String string) {
        return string;              //ritorno cosa? messo a caso
    }

}
