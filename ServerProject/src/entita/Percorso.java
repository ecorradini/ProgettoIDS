package entita;

import java.util.ArrayList;

public class Percorso extends Thread {

    private String percorso;
    private String beacon;
    private boolean finished;

    public Percorso(String beacon) {
        percorso = "";
        this.beacon = beacon;
        finished=false;
        start();
    }

    public void run() {

        if(!beacon.equals("")) {

            String json = "{PERCORSO:[";

            String edificio = DAOEdificio.selectNomeEdificio(beacon);
            String piano = DAOPiano.selectNomePiano(beacon);
            GrafoTronchi.Nodo partenza = DAOTronco.selectNodoByBeacon(beacon, edificio, piano);
            ArrayList<Tronco> uscite = DAOUscita.getTronchiUscita(beacon, edificio, piano);

            ArrayList<GrafoTronchi.Nodo> listaNodi = calcoloPercorso(partenza, uscite);

            for (int i = 0; i < listaNodi.size(); i++) {
                json = json + "\"" + listaNodi.get(i).getTronco().getID() + "\",";
            }
            if (json.charAt(json.length() - 1) == ',') {
                json = json.substring(0, json.length() - 1);
            }

            json = json + "]}";

            percorso = json;
        }
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
