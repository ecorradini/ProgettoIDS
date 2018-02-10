package it.getout.gestioneposizione;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Percorso {

    private ArrayList<Tronco> tronchi;

    public Percorso(Point point){

    }

    public Percorso(Point point, Aula aula){

    }

    public ArrayList<Tronco> getTronchi(){
        return  tronchi;
    }

    public Point getDestinazione(){ //destinazione non dovrebbe essere un attributo?
        Point destinazione = null;
        return destinazione;
    }
}
