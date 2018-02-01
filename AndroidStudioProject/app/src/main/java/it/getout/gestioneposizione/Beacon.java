package it.getout.gestioneposizione;

import android.graphics.Point;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Beacon {
    private String id;
    private Point posizione;

    public Beacon(String id, Point posizione){

    }

    public Point getPosizione(){
        return posizione;
    }

    public String getId(){
        return id;
    }
}
