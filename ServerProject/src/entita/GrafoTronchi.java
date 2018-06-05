package entita;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * classe per gestire i pesi dei tronchi nel calcolo del percorso
 */

public class GrafoTronchi {

    class Nodo {

        Tronco dato;
        ArrayList<Nodo> adiacenti;
        float peso;

        Nodo(Tronco t) {
            dato = t;
            peso = t.calcolaPesoTronco();
        }

        void addAdiacente(Nodo n) {
            if(adiacenti==null) adiacenti = new ArrayList<>();
            adiacenti.add(n);
        }
        /**
         * ritorna il tronco del nodo
         * @return Tronco
         */
        Tronco getTronco() { return dato; }
        /**
         * ritorna i nodi adiacenti
         * @return ArrayList<Nodo>
         */
        ArrayList<Nodo> getAdiacenti() { return adiacenti; }
        /**
         * ritorna il peso di un tronco
         * @return float
         */
        public float getPeso() { return peso; }
    }

    private HashMap<Integer,Tronco> tronchiPiano;
    private String piano;
    private Nodo radice;

    /**
     * costruttore
     */

    public GrafoTronchi(String piano) {
        this.piano = piano;
        tronchiPiano = DAOTronco.selectTronchiDelPiano(piano);

        ArrayList<Nodo> bfs = new ArrayList<>();
        HashMap<Tronco,Nodo> fatti = new HashMap<>();

        Tronco rad = null;
        if(tronchiPiano.size()>0) {
            rad = tronchiPiano.entrySet().iterator().next().getValue();
        }

        if(rad!=null) {
            radice = new Nodo(rad);
            bfs.add(radice);
            fatti.put(radice.getTronco(),radice);
        }

        while(bfs.size()>0) {
            ArrayList<Tronco> adiacenti = bfs.get(0).getTronco().richiediAdiacenti(tronchiPiano);
            ArrayList<Nodo> nodiAdiacenti = null;
            if(adiacenti!=null) {
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
    /**
     * ritorna il piano in cui si trova il nodo
     * @return String
     */
    public String getPiano() { return piano; }
    /**
     * ritorna i tronchi del piano cui si riferisce il grafo
     * @return HashMap<Integer,Tronco>
     */
    public HashMap<Integer,Tronco> getTronchiPiano() { return tronchiPiano; }

    /**
     * ritorna la radice del grafo
     * @return Nodo
     */

    public Nodo getRadice() { return radice; }
}
