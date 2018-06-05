package it.getout.gestioneposizione;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneconnessioni.Database;

public class GrafoTronchi {

    public class Nodo {

        Tronco dato;
        ArrayList<Nodo> adiacenti;
        float peso;

        public Nodo(Tronco t) {
            dato = t;
            adiacenti=new ArrayList<>();
        }

        public void addAdiacente(Nodo n) {
            if(adiacenti==null) adiacenti = new ArrayList<>();
            adiacenti.add(n);
        }

        public Tronco getTronco() { return dato; }

        public ArrayList<Nodo> getAdiacenti() { return adiacenti; }

        public float getPeso() { return peso; }
    }

    private String piano;
    private Nodo radice;

    public GrafoTronchi(String piano, int tronco, Database reader) {
        HashMap<Integer,Tronco> tronchiPiano = new HashMap<>();
        this.piano = piano;

        Piano p = null;
        for(int i=0; i<Posizione.getEdificioAttuale().getPiani().size() && p==null; i++) {
            if(Posizione.getEdificioAttuale().getPiani().get(i).toString().equals(piano)) {
                p = Posizione.getEdificioAttuale().getPiani().get(i);
            }
        }

        for(int i=0; i<p.getTronchi().size(); i++) {
            tronchiPiano.put(Posizione.getPianoAttuale().getTronchi().get(i).getId(),Posizione.getPianoAttuale().getTronco(i));
        }

        ArrayList<Nodo> bfs = new ArrayList<>();
        HashMap<Tronco,Nodo> fatti = new HashMap<>();

        Tronco rad = null;
        if(tronchiPiano.size()>0) {
            rad = tronchiPiano.get(tronco);
        }

        if(rad!=null) {
            radice = new Nodo(rad);
            bfs.add(radice);
            fatti.put(radice.getTronco(),radice);
        }

        while(bfs.size()>0) {
            ArrayList<Tronco> adiacenti = new ArrayList<>();
            ArrayList<Integer> adiacentiID = reader.richiediTronchiAdiacenti(bfs.get(0).getTronco());
            for(int i=0; i<adiacentiID.size(); i++) {
                Tronco corrente = null;
                for(int j=0; j<Posizione.getPianoAttuale().getTronchi().size() && corrente==null; j++) {
                    if(adiacentiID.get(i)==Posizione.getPianoAttuale().getTronchi().get(j).getId()) {
                        corrente = Posizione.getPianoAttuale().getTronchi().get(j);
                    }
                }
                if(corrente==null) {
                    for(int x=0; x<Posizione.getEdificioAttuale().getPiani().size() && corrente==null; x++) {
                        for(int t=0; t<Posizione.getEdificioAttuale().getPiani().get(t).getTronchi().size() && corrente==null; t++) {
                            if(adiacentiID.get(t)==Posizione.getEdificioAttuale().getPiani().get(t).getTronchi().get(t).getId()) {
                                corrente = Posizione.getEdificioAttuale().getPiani().get(t).getTronchi().get(t);
                            }
                        }
                    }
                }
                if(corrente!=null) {
                    adiacenti.add(corrente);
                }
            }
            ArrayList<Nodo> nodiAdiacenti = null;
            if(adiacenti.size()>0) {
                nodiAdiacenti = new ArrayList<>();
                for (int i = 0; i < adiacenti.size(); i++) {
                    Nodo attuale;
                    if(fatti.containsKey(adiacenti.get(i))) {
                        attuale = fatti.get(adiacenti.get(i));
                    }
                    else attuale = new Nodo(adiacenti.get(i));
                    bfs.get(0).addAdiacente(attuale);
                    nodiAdiacenti.add(attuale);
                }
            }
            if(nodiAdiacenti!=null) {
                for(int i=0; i<nodiAdiacenti.size(); i++) {
                    if(!fatti.containsKey(nodiAdiacenti.get(i).getTronco())) {
                        bfs.add(nodiAdiacenti.get(i));
                        fatti.put(nodiAdiacenti.get(i).getTronco(),nodiAdiacenti.get(i));
                    }
                }
            }
            bfs.remove(0);
        }
    }

    public String getPiano() { return piano; }

    public Nodo getRadice() { return radice; }
}
