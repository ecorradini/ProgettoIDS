package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.HashMap;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Tronco {

    private PointF inizio;
    private PointF fine;
    private float larghezza;
    private HashMap<String,Beacon> beacons;

    public Tronco(PointF inizio, PointF fine, float larghezza){
        this.inizio = inizio;
        this.fine = fine;
        this.larghezza = larghezza;
        this.getBeaconsDB();
    }

    private void getBeaconsDB(){
        beacons = PosizioneUtente.getDbReference().initBeacons(this);
    }

    public PointF getInizio(){
        return inizio;
    }

    public PointF getFine(){
        return fine;
    }

    public float getLarghezza(){
        return larghezza;
    }

    public Beacon getBeaconByID(String id){
        return beacons.get(id);
    }

    public HashMap<String,Beacon> getBeacons(){
        return beacons;
    }

    public boolean equals(PointF i, PointF f) { return inizio==i && fine==f; }
    public boolean equals(Tronco t) { return inizio==t.getInizio() && fine==t.getFine(); }

}
