package it.getout.gestioneposizione;

import android.provider.ContactsContract;

import connessioni.DatabaseConnection;
import it.getout.gestioneconnessioni.Database;

import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class Percorso extends Thread {

    private String percorso;
    private String beacon;
    private boolean finished;
    private Database reader;

    public Percorso(String beacon, Database reader) {
        percorso = "";
        this.beacon = beacon;
        this.reader = reader;
        finished=false;
        start();
    }

    public void run() {


        String edificio = Posizione.getEdificioAttuale().toString();
        String piano = Posizione.getPianoAttuale().toString();
        GrafoTronchi.Nodo partenza = reader.selectNodoByBeacon(beacon,edificio,piano);
        ArrayList<Tronco> uscite = reader.getTronchiUscita(beacon,edificio,piano);

        ArrayList<GrafoTronchi.Nodo> listaNodi = calcoloPercorso(partenza,uscite);

        finished = true;
    }

    public String getResult() {
        while (!finished) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return percorso;
    }

    private ArrayList<GrafoTronchi.Nodo> calcoloPercorso(GrafoTronchi.Nodo partenza, ArrayList<Tronco> tronchiUscita) {

        class PercorsoConCosto {

            private ArrayList<GrafoTronchi.Nodo> percorso;    //deve partire con una lista vuota
            private float costo;

            private PercorsoConCosto() {
                percorso = new ArrayList<>();
                costo = 0;
            }

            private ArrayList<GrafoTronchi.Nodo> getPercorso () {return percorso;}

            private void aggiungiNodo(GrafoTronchi.Nodo nuovo) {
                percorso.add(nuovo);
            }

            private void sommaCosto(float costo) {
                this.costo += costo;
            }

            private float getCosto() {
                return costo;
            }

            private GrafoTronchi.Nodo getUltimoNodo() { return percorso.get(percorso.size()-1); }

            private boolean finito() {
                boolean finito = false;
                if(tronchiUscita.contains(percorso.get(percorso.size()-1).getTronco())) {
                    finito=true;
                }
                return finito;
            }

            private void aggiungiPadre(PercorsoConCosto padre) {
                percorso.addAll(padre.getPercorso());
                costo += padre.getCosto();
            }
        }

        PercorsoConCosto finale = null;

        ArrayList<PercorsoConCosto> frontiera = new ArrayList<>();
        PercorsoConCosto iniziale = new PercorsoConCosto();
        iniziale.aggiungiNodo(partenza);
        iniziale.sommaCosto(partenza.getPeso());
        frontiera.add(iniziale);

        boolean stop = false;
        while(!stop && frontiera.size()>0) {

            ArrayList<GrafoTronchi.Nodo> adiacenti;
            PercorsoConCosto minore = frontiera.get(0);
            for(int j=0; j<frontiera.size(); j++) {
                if(frontiera.get(j).getCosto() < minore.getCosto()) {
                    minore = frontiera.get(j);
                }
            }
            adiacenti = minore.getUltimoNodo().getAdiacenti();

            for (int i = 0; i < adiacenti.size(); i++) {
                PercorsoConCosto daAggiungere = new PercorsoConCosto();
                daAggiungere.aggiungiPadre(minore);
                daAggiungere.sommaCosto(minore.getCosto());
                daAggiungere.aggiungiNodo(adiacenti.get(i));
                daAggiungere.sommaCosto(adiacenti.get(i).getPeso());
                frontiera.add(daAggiungere);
            }

            frontiera.remove(minore);

            for(int x=0; x<frontiera.size(); x++) {
                if(frontiera.get(x).finito()) {
                    stop=true;
                    finale = frontiera.get(x);
                }
            }
        }
        return finale.getPercorso();
    }
}
