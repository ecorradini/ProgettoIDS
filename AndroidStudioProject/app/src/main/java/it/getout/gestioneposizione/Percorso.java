package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;
import java.util.ArrayList;

import it.getout.gestioneconnessioni.Connessioni;
import it.getout.gestioneconnessioni.ServerHelper;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Percorso {

    private ArrayList<Tronco> tronchi;
    private PointF destinazione;

    public Percorso(PointF destinazione) {
        this.destinazione = destinazione;
        Connessioni.getServerReference().richiediPercorso(destinazione);
    }

    public PointF getDestinazione(){
        return destinazione;
    }

    public ArrayList<Tronco> getTronchi(){
        return tronchi;

    }

    public void setTronchi(ArrayList<Tronco> t) { tronchi = t; }


}
