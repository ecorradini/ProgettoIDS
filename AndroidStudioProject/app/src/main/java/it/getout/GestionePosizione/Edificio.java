package it.getout.GestionePosizione;

import java.util.ArrayList;

/**
 * Created by Alessandro on 01/02/2018.
 */

class Edificio {

    String nome;
    ArrayList<Piano> piani;

    Edificio(String name){

    }

    Piano getPiano(int index){
        return piani.get(index);
    }

    void getPianiDB(){

    }

    //ArrayList<Piano> getPiani(){

    //}
}
