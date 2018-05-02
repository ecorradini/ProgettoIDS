package it.getout.gestioneposizione;
import java.util.ArrayList;

import it.getout.gestioneconnessioni.Connessioni;

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
        if(!Posizione.checkInternet()) {
            piani = Connessioni.getDbReference().initPiani(nome);
        }
        else {
            Connessioni.getServerReference().richiediPianibyEdificio(this);
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
