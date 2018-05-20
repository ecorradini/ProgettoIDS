package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Aula {

    private String nome;
    private String entrata;

    public Aula (String nome, String entrata){
        this.nome = nome;
        this.entrata = entrata;
    }

    //meotodi per ottenere nome, entrata e piano di un'aula

    public String getNome() {
        return this.nome;
    }

    public String getEntrata(){
        return this.entrata;
    }

    public boolean isEntrata(Beacon b) { return b.getId().equals(entrata); }

}
