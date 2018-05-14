package it.getout.gestioneposizione;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneconnessioni.Database;

public class GrafoTronchi {

    public class Nodo {

        Tronco dato;
        ArrayList<Nodo> adiacenti;
        float peso;
        private final float[] weight={0.2f,0.2f,0.2f,0.2f,0.2f};

        Nodo(Tronco t, Database reader) {
            dato = t;

            ArrayList<Float> parametri;  //0.VULNERABILITA  1.RISCHIOVITA  2.PRESENZAFUMO
            parametri = reader.richiediParametri(t.getId());

            //calcolo peso con parametri 1, 2 e 3.
            for(int i=0; i<parametri.size(); i++){
                if(i==1)
                    peso += parametri.get(i)*100*weight[i];
                else {
                    peso += parametri.get(i) * weight[i];
                }
            }

            int numeroPersone = reader.richiediNumeroPersoneInTronco(t.getId());

            // aggiunta al peso le componenti di lunghezza e los (C'È DA NORMALIZZARE: PORTARE TUTTE LE VARIE COMPONENTI A VALORI COMPRESI TRA 0 E 1)
            peso  += t.getLunghezza()*weight[3]+(numeroPersone/(t.getLunghezza()*t.getLarghezza()))*weight[4];
        }

        void addAdiacente(Nodo n) {
            if(adiacenti==null) adiacenti = new ArrayList<>();
            adiacenti.add(n);
        }

        Tronco getTronco() { return dato; }

        ArrayList<Nodo> getAdiacenti() { return adiacenti; }

        public float getPeso() { return peso; }
    }

    private HashMap<Integer,Tronco> tronchiPiano;
    private String piano;
    private Nodo radice;

    public GrafoTronchi(String piano, Database reader) {
        this.piano = piano;
        for(int i=0; i<Posizione.getPianoAttuale().getTronchi().size(); i++) {
            tronchiPiano.put(Posizione.getPianoAttuale().getTronchi().get(i).getId(),Posizione.getPianoAttuale().getTronco(i));
        }

        ArrayList<Nodo> bfs = new ArrayList<>();
        HashMap<Tronco,Nodo> fatti = new HashMap<>();

        Tronco rad = null;
        if(tronchiPiano.size()>0) {
            rad = tronchiPiano.entrySet().iterator().next().getValue();
        }

        if(rad!=null) {
            radice = new Nodo(rad,reader);
            bfs.add(radice);
            fatti.put(radice.getTronco(),radice);
        }

        while(bfs.size()>0) {
            ArrayList<Tronco> adiacenti = new ArrayList<>();
            ArrayList<Integer> adiacentiID = reader.richiediTronchiAdiacenti(bfs.get(0).getTronco());
            for(int i=0; i<adiacentiID.size(); i++) {
                Tronco corrente = null;
                for(int j=0; j<Posizione.getPianoAttuale().getTronchi().size() && corrente==null; j++) {
                    if(adiacentiID.get(i)==Posizione.getPianoAttuale().getTronchi().get(i).getId()) {
                        corrente = Posizione.getPianoAttuale().getTronchi().get(i);
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
                    else attuale = new Nodo(adiacenti.get(i),reader);
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

    public HashMap<Integer,Tronco> getTronchiPiano() { return tronchiPiano; }

    public Nodo getRadice() { return radice; }
}
