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
        downloadPiani();
    }

    private void downloadPiani() {
        if(!PosizioneUtente.checkInternet()) {
            piani = PosizioneUtente.getDbReference().initPiani(nome);
        }
        else {
            PosizioneUtente.getServerReference().richiediPianibyEdificio(this);
        }
    }

    public Piano getPiano(int index) {
        return piani.get(index);
    }

    public String toString(){
        return nome;
    }

    public ArrayList<Piano> getPiani() { return piani; }
    public void setPiani(ArrayList<Piano> p) { piani = p; }
}
