package it.getout.gestioneposizione;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Percorso {

    private ArrayList<Tronco> tronchi;
    private Point Destinazione;

    public Percorso(Point destinazione, Point partenza){
        this.Destinazione = destinazione;
    }

    public ArrayList<Tronco> getTronchi(){
        return  tronchi;
    }

    public Point getDestinazione(){
        return Destinazione;
    }
}
