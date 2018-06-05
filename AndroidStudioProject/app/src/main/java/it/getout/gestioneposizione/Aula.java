package it.getout.gestioneposizione;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Alessandro on 01/02/2018.
 *
 * Classe che rappresenta un'aula.
 *
 */

public class Aula {

    private String nome;
    private String entrata;

    public Aula (String nome, String entrata){
        this.nome = nome;
        this.entrata = entrata;
    }


    /**
     * Ritorna il nome dell'aula
     * @return String
     */

    public String getNome() {
        return this.nome;
    }

    /**
     * Rirtorna l'ID del beacon posizionato all'entrata dell'aula.
     * @return String
     */

    public String getEntrata(){
        return this.entrata;
    }

    /**
     * Controlla se un certo beacon è associato ad un'aula.
     * @param b Beacon
     * @return boolean
     */
    public boolean isEntrata(Beacon b) { return b.getId().equals(entrata); }

}
