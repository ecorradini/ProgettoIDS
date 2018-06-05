package it.getout.gestioneposizione;

import java.util.ArrayList;
import java.util.HashMap;

import it.getout.gestioneconnessioni.Database;


/**
 * Classe che rappresenta il grafo dei tronchi di un piano. Esso viene utilizzato per costruire il percorso
 * migliore verso la destinazione (percorso a peso totale minore) nel caso in cui l'applicazione venga
 * eseguita in modalità offline.
 *
 */


public class GrafoTronchi {


    /**
     * Classe che rappresenta un tronco con i suoi adiacenti ed il peso valutato di quel tronco
     *
     */

    public class Nodo {

        Tronco dato;
        ArrayList<Nodo> adiacenti;
        float peso;
        private final float[] weight={0.2f,0.2f,0.2f,0.2f,0.2f};

        public Nodo(Tronco t, Database reader) {
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

            // aggiunta al peso le componenti di lunghezza e los (C'È DA NORMALIZZARE: PORTARE TUTTE LE VARIE COMPONENTI A VALORI COMPRESI TRA 0 E 1)
            peso  += t.getLunghezza()*weight[3];
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

    /**
     * Costruttore che genera il grafo dei tronchi.
     * @param piano piano
     * @param tronco tronco
     * @param reader gestore della connessione con il database locale.
     */

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


    /**
     * Metodo che ritorna il piano
     * @return String
     */
    public String getPiano() { return piano; }

    /**
     * Metodo che ritorna il nodo(tronco) radice.
     * @return Nodo
     */
    public Nodo getRadice() { return radice; }
}
