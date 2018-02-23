package it.getout.gestioneposizione;
import java.util.ArrayList;

import it.getout.gestioneposizione.Piano;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Edificio {
    private String nome;
    private ArrayList<Piano> piani;

    public Edificio(String nome) {
        this.nome = nome;
        getPianiDB();
    }

    private void getPianiDB(){
        piani = PosizioneUtente.getDbReference().initPiani(nome);
    }

    public Piano getPiano(int index) {
        return piani.get(index);
    }

    public String toString(){
        return nome;
    }

    public ArrayList<Piano> getPiani() { return piani; }
}
