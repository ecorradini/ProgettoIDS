package it.getout.gestioneconnessioni;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Edificio;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Tronco;

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

}
