package it.getout.gestioneposizione;

import it.getout.gestioneconnessioni.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class Percorso extends Thread {

    private ArrayList<Tronco> percorso;
    private String beacon;
    private boolean finished;
    private Database reader;

    public Percorso(String beacon, Database reader) {
        percorso = new ArrayList<>();
        this.beacon = beacon;
        this.reader = reader;
        finished=false;
        start();
    }

    public void run() {
        GrafoTronchi partenza;

        ArrayList<Piano> piani = Posizione.getEdificioAttuale().getPiani();
        int tronco = reader.richiediTroncoByBeacon(beacon);
        HashMap<String,GrafoTronchi> grafi = new HashMap<>();

        for(int i = 0; i < piani.size(); i++){
            grafi.put(piani.get(i).toString(), new GrafoTronchi(piani.get(i).toString(),tronco,reader));
        }

        partenza = grafi.get(Posizione.getPianoAttuale().toString());

        ArrayList<Tronco> uscite = reader.richiediTronchiUscita(beacon);

        ArrayList<GrafoTronchi.Nodo> listaNodi = calcoloPercorso(partenza.getRadice(),uscite);

        for(int i = 0; i < listaNodi.size(); i++){
            percorso.add(listaNodi.get(i).getTronco());
        }

        finished = true;
    }

    public ArrayList<Tronco> getResult() {
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
            private ArrayList<Tronco> tronchiUscita;

            private PercorsoConCosto(ArrayList<Tronco> tronchiUscita) {
                percorso = new ArrayList<>();
                costo = 0;
                this.tronchiUscita = tronchiUscita;
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
        PercorsoConCosto iniziale = new PercorsoConCosto(tronchiUscita);
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
                PercorsoConCosto daAggiungere = new PercorsoConCosto(tronchiUscita);
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
