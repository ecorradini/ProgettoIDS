package it.getout.gestioneposizione;

import android.graphics.Point;

import java.util.HashMap;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Tronco {

    private Point inizio;
    private Point fine;
    private double larghezza;
    private HashMap<String,Beacon> beacons;

    public Tronco(Point inizio, Point fine, double larghezza){
        this.inizio = inizio;
        this.fine = fine;
        this.larghezza = larghezza;
        this.getBeaconsDB();
    }

    public Point getInizio(){
        return inizio;
    }

    public Point getFine(){
        return fine;
    }

    public double getLarghezza(){
        return larghezza;
    }

    public Beacon getBeaconByID(String id){
        return beacons.get(id);
    }

    public HashMap<String,Beacon> getBeacons(){
        return beacons;
    }

    private void getBeaconsDB(){

    }


}
