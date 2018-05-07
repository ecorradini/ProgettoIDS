package entita;

import java.util.ArrayList;
import java.util.HashMap;

public class GrafoTronchi {

    class Nodo {
        Tronco dato;
        ArrayList<Nodo> adiacenti;

        Nodo(Tronco t) {
            dato = t;
        }

        public void addAdiacente(Tronco t) {
            if(adiacenti==null) adiacenti = new ArrayList<>();
            adiacenti.add(new Nodo(t));
        }

        public Tronco getTronco() { return dato; }
    }

    private HashMap<Integer,Tronco> tronchiPiano;
    private String piano;
    private Nodo radice;

    public GrafoTronchi(String piano) {
        this.piano = piano;
        tronchiPiano = DAOTronco.selectTronchiDelPiano(piano);

        ArrayList<Nodo> bfs = new ArrayList<>();
        Tronco rad = DAOTronco.selectPrimoTroncoPiano(piano);
        if(rad!=null) {
            radice = new Nodo(rad);
            bfs.add(radice);
        }

        while(bfs.size()>0) {
            ArrayList<Tronco> adiacenti = bfs.get(0).getTronco().richiediAdiacenti(tronchiPiano);
            ArrayList<Nodo> nodiAdiacenti = null;
            if(adiacenti!=null) {
                nodiAdiacenti = new ArrayList<>();
                for (int i = 0; i < adiacenti.size(); i++) {
                    bfs.get(0).addAdiacente(adiacenti.get(i));
                    Nodo attuale = new Nodo(adiacenti.get(i));
                    nodiAdiacenti.add(attuale);
                }
            }
            if(nodiAdiacenti!=null) {
                bfs.addAll(nodiAdiacenti);
            }
            bfs.remove(0);
        }
    }

    public String getPiano() { return piano; }
}
