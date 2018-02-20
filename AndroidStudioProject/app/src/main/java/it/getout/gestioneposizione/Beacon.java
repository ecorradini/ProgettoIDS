package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Beacon {
    private String id;
    private PointF posizione;

    public Beacon(String id, PointF posizione){
        this.id = id;
        this.posizione = posizione;
    }

    public PointF getPosizione(){
        return posizione;
    }

    public String getId(){
        return id;
    }
}
