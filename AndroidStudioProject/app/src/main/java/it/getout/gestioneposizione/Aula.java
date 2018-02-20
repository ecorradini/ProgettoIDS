package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Aula {

    private String nome;
    private PointF entrata;
    private Piano piano;

    public Aula (String nome, PointF entrata, Piano piano){
        this.nome = nome;
        this.entrata = entrata;
        this.piano = piano;
    }

    public PointF getEntrata(){
        return entrata;
    }

    public Piano getPiano(){
        return piano;
    }

    public String toString(){
        return nome;
    }
}
