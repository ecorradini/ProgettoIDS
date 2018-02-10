package it.getout.gestioneposizione;

import android.graphics.Point;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Aula {

    private String nome;
    private Point entrata;
    private Piano piano;

    public Aula (String nome, Point entrata, Piano piano){
        this.nome = nome;
        this.entrata = entrata;
        this.piano = piano;
    }

    public Point getEntrata(){
        return entrata;
    }

    public Piano getPiano(){
        return piano;
    }

    public String toString(){
        return nome;
    }
}
