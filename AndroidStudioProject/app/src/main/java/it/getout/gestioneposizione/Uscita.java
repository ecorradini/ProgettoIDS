package it.getout.gestioneposizione;

import android.content.Context;

import it.getout.Client;

public class Uscita {

    private static String beaconPrecedente;

    public static boolean checkUscita(){
        return Posizione.getIDBeaconAttuale().equals("") /*&& Posizione.isUscita(beaconPrecedente)*/;
    }

    public static void setBeaconPrecedente(String b){
        beaconPrecedente = b;
    }
}
