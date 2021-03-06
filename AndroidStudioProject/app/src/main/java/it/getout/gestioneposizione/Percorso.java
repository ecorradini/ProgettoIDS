package it.getout.gestioneposizione;

import android.util.Log;

import it.getout.gestioneconnessioni.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class Percorso extends Thread {

    private ArrayList<Tronco> percorso;
    private String beacon;
    private boolean finished;
    private String destinazione;
    private Database reader;

    public Percorso(String beacon, Database reader) {
        percorso = new ArrayList<>();
        this.beacon = beacon;
        this.reader = reader;
        this.destinazione="";
        finished=false;
        start();
    }

    /**
     * Costruttore
     * @param beacon ID beacon di partenza;
     * @param destinazione Uscita (null) o ID beacon di destinazione.
     * @param reader Gestore connessione con database locale.
     */
    public Percorso(String beacon, String destinazione, Database reader) {
        percorso = new ArrayList<>();
        this.beacon = beacon;
        this.destinazione = destinazione;
        this.reader = reader;
        finished=false;
        start();
    }

    /**
     * Metodo che implementa l'algoritmo di calcolo del percorso.
     */
    public void run() {
        GrafoTronchi partenza;

        ArrayList<Piano> piani = Posizione.getEdificioAttuale().getPiani();
        int tronco = reader.richiediTroncoByBeacon(beacon);
        HashMap<String,GrafoTronchi> grafi = new HashMap<>();

        for(int i = 0; i < piani.size(); i++){
            grafi.put(piani.get(i).toString(), new GrafoTronchi(piani.get(i).toString(),tronco,reader));
        }

        partenza = grafi.get(Posizione.getPianoAttuale().toString());

        ArrayList<GrafoTronchi.Nodo> listaNodi = new ArrayList<>();

        if(destinazione.isEmpty()) {
            ArrayList<Tronco> uscite = reader.richiediTronchiUscita(beacon);

            listaNodi = calcoloPercorso(partenza.getRadice(), uscite);
        }
        else {
            Aula corrente = null;
            for(int i=0; i<Posizione.getPianoAttuale().getAule().size() && corrente==null; i++) {
                if(Posizione.getPianoAttuale().getAule().get(i).getNome().equals(destinazione)) {
                    corrente = Posizione.getPianoAttuale().getAule().get(i);
                }
            }
            Log.e("BEACON AULA",corrente.getEntrata());
            int troncoAula = reader.richiediTroncoByBeacon(corrente.getEntrata());
            Tronco troncoDestinazione = null;
            for(int i=0; i<Posizione.getPianoAttuale().getTronchi().size() && troncoDestinazione==null; i++) {
                if(Posizione.getPianoAttuale().getTronchi().get(i).getId()==troncoAula) {
                    troncoDestinazione = Posizione.getPianoAttuale().getTronchi().get(i);
                }
            }
            GrafoTronchi grafo = grafi.get(Posizione.getPianoAttuale().toString());
            GrafoTronchi.Nodo destinazione = null;
            ArrayList<GrafoTronchi.Nodo> daVisitare = new ArrayList<>();
            daVisitare.add(grafo.getRadice());
            while(daVisitare.size()> 0 && destinazione==null) {
                if(daVisitare.get(0).getTronco().equals(troncoDestinazione)) {
                    destinazione = daVisitare.get(0);
                }
                else {
                    ArrayList<GrafoTronchi.Nodo> adiacenti = daVisitare.get(0).getAdiacenti();
                    daVisitare.remove(0);
                    daVisitare.addAll(adiacenti);
                }

            }
            listaNodi = calcoloPercorso(partenza.getRadice(),destinazione);

        }

        for(int i = 0; i < listaNodi.size(); i++){
            percorso.add(listaNodi.get(i).getTronco());
        }

        finished = true;
    }

    /**
     * Metodo che restituisce il percorso calcolato.
     * @return ArrayList<Tronco>
     */
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

    /**
     * Calcolo del percorso verso l'uscita.
     * @param partenza
     * @param tronchiUscita
     * @return ArrayList<GrafoTronchi.Nodo>
     */
    private ArrayList<GrafoTronchi.Nodo> calcoloPercorso(GrafoTronchi.Nodo partenza, ArrayList<Tronco> tronchiUscita) {

        class PercorsoConCosto {

            private ArrayList<GrafoTronchi.Nodo> percorso;    //deve partire con una lista vuota
            private float costo;
            private ArrayList<Integer> tronchiUscita;

            private PercorsoConCosto(ArrayList<Tronco> tronchiUscita) {
                percorso = new ArrayList<>();
                costo = 0;
                this.tronchiUscita = new ArrayList<>();
                for(int i=0; i<tronchiUscita.size(); i++) {
                    Log.e("ID",tronchiUscita.get(i).getId()+"");
                    this.tronchiUscita.add(tronchiUscita.get(i).getId());
                }
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
                if(tronchiUscita.contains(percorso.get(percorso.size()-1).getTronco().getId())) {
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

    /**
     * Calcolo del percorso verso una destinazione scelta.
     * @param partenza
     * @param destinazione
     * @return ArrayList<GrafoTronchi.Nodo>
     */
    private ArrayList<GrafoTronchi.Nodo> calcoloPercorso(GrafoTronchi.Nodo partenza, GrafoTronchi.Nodo destinazione) {

        class PercorsoConCosto {

            private ArrayList<GrafoTronchi.Nodo> percorso;    //deve partire con una lista vuota
            private float costo;
            private GrafoTronchi.Nodo destinazione;

            private PercorsoConCosto(GrafoTronchi.Nodo destinazione) {
                percorso = new ArrayList<>();
                costo = 0;
                this.destinazione = destinazione;
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
                if(percorso.get(percorso.size()-1).getTronco().equals(destinazione.getTronco())) {
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
        PercorsoConCosto iniziale = new PercorsoConCosto(destinazione);
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
                PercorsoConCosto daAggiungere = new PercorsoConCosto(destinazione);
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
