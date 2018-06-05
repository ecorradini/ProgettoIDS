package it.getout.gestioneposizione;

/**
 * Classe per gestire il controllo dell'uscita dell'utente dall'edificio.
 */

public class Uscita {

    private static String beaconPrecedente="";

    /**
     * Controlla se l'utente è uscito: in particolare verificando che l'attuale beacon sia quello
     * fittizio (a null) e quello precedente sia taggato come beacon di uscita.
     * @return boolean
     */
    public static boolean checkUscita(){
        return Posizione.getIDBeaconAttuale().equals("") && Posizione.isUscita(beaconPrecedente);
    }

    /**
     * Setta il beacon precedente.
     * @param b
     */
    public static void setBeaconPrecedente(String b){
        beaconPrecedente = b;
    }

    /**
     * Ritorna l'id del beacon precedente.
     * @return String
     */
    public static String getBeaconPrecedente(){
        return beaconPrecedente;
    }
}
