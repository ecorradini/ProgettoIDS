package it.getout.gestioneposizione;

import java.util.ArrayList;

/**
 * Classe che rappresenta un piano.
 * Created by Alessandro on 01/02/2018.
 */

public class Piano {

    private String nome;
    private ArrayList<Aula> aule;
    private ArrayList<Tronco> tronchi;

    /**
     * Costruttore
     * @param nome nome del piano
     */
    public Piano(String nome){
        this.nome = nome;
    }

    /**
     * Ritorna le aule appartenenti ad un piano.
     * @return ArrayList<Aula>
     */
    public ArrayList<Aula> getAule(){
        return aule;
    }

    /**
     * Setta le aule del piano.
     * @param a
     */
    public void setAule(ArrayList<Aula> a) { aule=a; }

    /**
     * Ritorna l'aula passata come parametro.
     * @param index identificativo dell'aula desiderata.
     * @return Aula
     */
    public Aula getAula(int index){
        return aule.get(index);
    }

    /**
     * Restituisce i tronchi del piano.
     * @return ArrayList<Tronco>
     */
    public ArrayList<Tronco> getTronchi(){
        return tronchi;
    }

    /**
     * Setta i tronchi di un piano.
     * @param t
     */
    public void setTronchi(ArrayList<Tronco> t) { tronchi = t; }

    /**
     * Restituisce il tronco passato come parametro.
     * @param index id del tronco voluto.
     * @return Tronco
     */
    public Tronco getTronco(int index){
        return tronchi.get(index);
    }

    /**
     * Restituisce il nome del piano.
     * @return String
     */
    public String toString(){
        return nome;
    }


    /**
     * Controlla se il piano passato equivale a quello corrente.
     * @param p
     * @return boolean
     */
    public boolean equals(Piano p) {
        return nome.equals(p.toString());
    }
}
