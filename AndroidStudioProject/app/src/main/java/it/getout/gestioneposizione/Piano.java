package it.getout.gestioneposizione;

import java.util.ArrayList;

import it.getout.gestioneconnessioni.Connessioni;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Piano {

    private String nome;
    private ArrayList<Aula> aule;
    private ArrayList<Tronco> tronchi;

    public Piano(String nome){//String nome (va inserito come parametro costruttore
        this.nome = nome;
        downloadAule();
        downloadTronchi();
    }

    private void downloadAule() {
        if(!Posizione.checkInternet()) {
            aule = Connessioni.getDbReference().richiediAulePiano(nome);
        }
        else {
            Connessioni.getServerReference().richiediAulePiano(nome);
        }
    }

    private void downloadTronchi() {
        if(!Posizione.checkInternet()) {
            tronchi = Connessioni.getDbReference().richiediTronchiPiano(nome);
        }
        else {
            Connessioni.getServerReference().richiediTronchiPiano(nome);
        }
    }

    public ArrayList<Aula> getAule(){
        return aule;
    }

    public void setAule(ArrayList<Aula> a) { aule=a; }

    public Aula getAula(int index){
        return aule.get(index);
    }

    public ArrayList<Tronco> getTronchi(){
        return tronchi;
    }

    public void setTronchi(ArrayList<Tronco> t) { tronchi = t; }

    public Tronco getTronco(int index){
        return tronchi.get(index);
    }

    public String toString(){
        return nome;
    }
}
