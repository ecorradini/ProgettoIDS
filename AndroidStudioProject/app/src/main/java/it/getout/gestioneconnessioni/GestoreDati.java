package it.getout.gestioneconnessioni;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Tronco;


/**
 * Classe astratta per la gestione dell'elaborazione dei dati: se il client è connesso alla rete,
 * i metodi di tale classe saranno gli analoghi di quelli definiti nella classe Server, altrimenti
 * saranno quelli della classe Database.
 */
public abstract class GestoreDati {

    public abstract Edificio richiediEdificioAttuale(String beacon);

    public abstract Piano richiediPianoAttuale(String beacon);

    public abstract Bitmap richiediMappaPiano(String piano);

    public abstract HashMap<String,Beacon> richiediBeaconTronco(int Tronco);

    public abstract ArrayList<Tronco> richiediTronchiPiano(String piano);

    public abstract ArrayList<Aula> richiediAulePiano(String piano);

    public abstract ArrayList<Piano> richiediPianiEdificio(String edificio);

    public abstract Beacon richiediPosizione(String beacon);

    public abstract ArrayList<Tronco> richiediPercorsoFuga(String beacon);

    public abstract ArrayList<String> richiediUsciteEdificio(String edificio);

}
