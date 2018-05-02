package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Aula {

    private String nome;
    private String entrata;
    private Piano piano;

    public Aula (String nome, String entrata, Piano piano){
        this.nome = nome;
        this.entrata = entrata;
        this.piano = piano;
    }

    //meotodi per ottenere nome, entrata e piano di un'aula

    public String getNome() {
        return this.nome;
    }

    public String getEntrata(){
        return this.entrata;
    }

    public boolean isEntrata(Beacon b) { return b.equals(entrata); }

    public Piano getPiano(){
        return this.piano;
    }


}
