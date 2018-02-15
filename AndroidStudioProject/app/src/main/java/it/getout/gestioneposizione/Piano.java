package it.getout.gestioneposizione;

import java.util.ArrayList;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class Piano {

    private String nome;
    private ArrayList<Aula> aule;
    private ArrayList<Tronco> tronchi;

    public Piano(String nome){//String nome (va inserito come parametro costruttore
        this.nome = nome;
        this.getAuleDB(this.nome);
        this.getTronchiDB(this.nome);
    }

    public ArrayList<Aula> getAule(){
        return aule;
    }

    public Aula getAula(int index){
        return aule.get(index);
    }

    private void getAuleDB(String nome) {
    }

    public ArrayList<Tronco> getTronchi(){
        return tronchi;
    }

    public Tronco getTronco(int index){
        return tronchi.get(index);
    }

    private void getTronchiDB(String nome){

    }

    public String toString(){
        return nome;
    }
}
