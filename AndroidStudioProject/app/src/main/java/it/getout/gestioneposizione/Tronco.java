package it.getout.gestioneposizione;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe che rappresenta i tronchi.
 * Created by Alessandro on 01/02/2018.
 */

public class Tronco {

    private int id;
    private PointF inizio;
    private PointF fine;
    private float larghezza;
    private float lunghezza;
    private HashMap<String,Beacon> beacons;

    /**
     * Costruttore.
     * @param id identificatore
     * @param inizio coordinate del punto di inizio del tronco sulla mappa
     * @param fine coordinate del punto di fine del tronco sulla mappa
     * @param larghezza larghezza del tronco
     * @param lunghezza lunghezza del tronco
     */
    public Tronco(int id, PointF inizio, PointF fine, float larghezza, float lunghezza){
        this.id = id;
        this.inizio = inizio;
        this.fine = fine;
        this.larghezza = larghezza;
        this.lunghezza = lunghezza;
    }

    /**
     * Ritorna l'id del tronco
     * @return int
     */
    public int getId() { return id; }

    /**
     * Ritorna le coordinate del punto di inizio del tronco.
     * @return PointF
     */
    public PointF getInizio(){
        return inizio;
    }

    /**
     * Ritorna le coordinate del punto di inizio del tronco.
     * @return PointF
     */
    public PointF getFine(){
        return fine;
    }

    /**
     * Ritorna la larghezza del tronco.
     * @return float
     */
    public float getLarghezza(){
        return larghezza;
    }

    /**
     * Ritorna la lunghezza del tronco.
     * @return float
     */
    public float getLunghezza(){
        return lunghezza;
    }

    /**
     * Ritorna il beacon il cui id è passato come parametro
     * @param id
     * @return Beacon
     */
    public Beacon getBeaconByID(String id){
        return beacons.get(id);
    }


    /**
     * Restituisce tutti i beacon del tronco
     * @return HashMap<String,Beacon>
     */
    public HashMap<String,Beacon> getBeacons(){
        return beacons;
    }


    /**
     * Setta i beacon del tronco.
     * @param b
     */
    public void setBeacons(HashMap<String,Beacon> b) { beacons = b; }

    /**
     * Verifica se i punti passati come parametri sono l'uno di inizio e l'altro di fine del tronco.
     * @param i
     * @param f
     * @return boolean
     */
    public boolean equals(PointF i, PointF f) { return inizio==i && fine==f; }

    /**
     * Verifica se il tronco passato come parametro è uguale a quello corrente.
     * @param t
     * @return boolean
     */
    public boolean equals(Tronco t) { return inizio==t.getInizio() && fine==t.getFine(); }

}
