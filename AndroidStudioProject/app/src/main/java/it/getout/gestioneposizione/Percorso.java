package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Percorso {

    private ArrayList<Tronco> tronchi;
    private PointF Destinazione;

    public Percorso(PointF destinazione, PointF partenza){
        this.Destinazione = destinazione;
    }

    public ArrayList<Tronco> getTronchi(){
        return  tronchi;
    }

    public PointF getDestinazione(){
        return Destinazione;
    }
}
