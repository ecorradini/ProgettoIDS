package it.getout.gestioneposizione;


import android.graphics.PointF;

/**
 * Created by Alessandro on 01/02/2018.
 *
 * Classe che rappresenta un beacon.
 * In particolare le informazioni rilevanti sono il MAC address (id) e le coordinate
 * della mappa che rappresenta il piano dell'edificio in cui si trova.
 */

public class Beacon {
    private String id;
    private PointF posizione;


    /**
     * Costruttore.
     * @param id MAC address del beacon
     * @param posizione Coordinate del beacon sulla mappa
     */
    public Beacon(String id, PointF posizione){
        this.id = id;
        this.posizione = posizione;
    }

    /**
     * Ritorna l'id del beacon
     * @return String
     */
    public String getId(){
        return this.id;
    }


    /**
     * Ritorna le coordinate del beacon sulla mappa.
     * @return PointF
     */
    public PointF getPosizione(){
        return this.posizione;
    }

}
