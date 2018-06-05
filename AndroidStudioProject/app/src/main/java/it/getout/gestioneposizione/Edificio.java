package it.getout.gestioneposizione;
import java.util.ArrayList;

/**
 * Created by Alessandro on 01/02/2018.
 *
 * Classe che rappresenta l'edificio.
 */

public class Edificio {

    private String nome;
    private ArrayList<Piano> piani;

    /**
     * Costruttore
     * @param nome nome edificio
     */

    public Edificio(String nome) {
        this.nome = nome;
    }


    /**
     * Ritorna il piano che ha un certo id passato come parametro
     * @param index id del piano desiderato
     * @return Piano
     */
    public Piano getPiano(int index) {
        return piani.get(index);
    }


    /**
     * Ritorna il nome del piano.
     * @return String
     */
    public String toString(){
        return nome;
    }


    /**
     * Ritorna la lista di tutti i piani dell'edificio.
     * @return ArrayList<Piano>
     */
    public ArrayList<Piano> getPiani() { return piani; }

    /**
     * Setta i piani dell'edificio
     * @param p ArrayList<Piano>
     */
    public void setPiani(ArrayList<Piano> p) { piani = p; }
}
