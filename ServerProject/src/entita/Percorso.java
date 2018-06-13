package entita;

import java.util.ArrayList;

public class Percorso extends Thread {

    private String percorso;
    private String beacon;
    private String destinazione;
    private boolean finished;
    /**
     * costruttore per fuga
     */
    public Percorso(String beacon) {
        percorso = "";
        this.beacon = beacon;
        this.destinazione="";
        finished=false;
        start();
    }
    /**
     * costruttore per richiesta destinazione
     */
    public Percorso(String beacon, String destinazione) {
        percorso = "";
        this.beacon = beacon;
        this.destinazione = destinazione;
        finished=false;
        start();
    }
    /**
     * metodo run del runnable per il calcolo del percorso
     */
    public void run() {

        if(beacon!=null) {

            if(destinazione.isEmpty()) {
                String json = "{PERCORSO:[";

                String edificio = DAOEdificio.selectNomeEdificio(beacon);
                String piano = DAOPiano.selectNomePiano(beacon);
                GrafoTronchi.Nodo partenza = DAOTronco.selectNodoByBeacon(beacon, edificio, piano);
                ArrayList<Tronco> uscite = DAOUscita.getTronchiUscita(beacon, edificio, piano);

                if (uscite.contains(partenza.getTronco())) {
                    json += "\"" + partenza.getTronco().getID() + "\"";
                    json += "]}";
                    percorso = json;
                } else {

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
            }
            else {
                String json = "{PERCORSO:[";

                String edificio = DAOEdificio.selectNomeEdificio(beacon);
                String piano = DAOPiano.selectNomePiano(beacon);
                String beaconAula = DAOAula.selectBeaconEntrata(destinazione);
                String edificioArrivo = DAOEdificio.selectNomeEdificio(beaconAula);
                String pianoArrivo = DAOPiano.selectNomePiano(beaconAula);
                GrafoTronchi.Nodo partenza = DAOTronco.selectNodoByBeacon(beacon, edificio, piano);
                GrafoTronchi.Nodo arrivo = DAOTronco.selectNodoByBeacon(beaconAula, edificioArrivo, pianoArrivo);

                ArrayList<GrafoTronchi.Nodo> listaNodi = calcoloPercorso(partenza, arrivo);

                for (int i = 0; i < listaNodi.size(); i++) {
                    json = json + "\"" + listaNodi.get(i).getTronco().getID() + "\",";
                }
                if (json.charAt(json.length() - 1) == ',') {
                    json = json.substring(0, json.length() - 1);
                }

                json = json + "]}";

                percorso = json;
            }

        }
        else{
            percorso = "";
        }

        finished = true;
    }
    /**
     * evita che si avviino più thread di calcolo contemporaneamente
     * @return stringa JSON
     */
    public String getResult() {
        while (!finished) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        return percorso;
    }
    /**
     * metodo che calcola il percorso sul grafo tramite UCS ritornando i nodi da attraversare
     * @param partenza nodo di partenza
     * @param tronchiUscita possibili tronchi di uscita dall' edificio
     * @return ArrayList<GrafoTronchi.Nodo>
     */
    private ArrayList<GrafoTronchi.Nodo> calcoloPercorso(GrafoTronchi.Nodo partenza, ArrayList<Tronco> tronchiUscita) {

        class PercorsoConCosto {

            private ArrayList<GrafoTronchi.Nodo> percorso;    //deve partire con una lista vuota
            private float costo;

            private PercorsoConCosto() {
                percorso = new ArrayList<>();
                costo = 0;
            }
            /**
             * ritorna il percorso costruito
             * @return ArrayList<GrafoTronchi.Nodo>
             */
            private ArrayList<GrafoTronchi.Nodo> getPercorso () {return percorso;}
            /**
             * aggiunge il  nodo passato al percorso
             * @param nuovo
             */
            private void aggiungiNodo(GrafoTronchi.Nodo nuovo) {
                percorso.add(nuovo);
            }
            /**
             * calcola il peso del percorso per espandere su un certo nodo
             * @param costo costo del nuovo nodo da espandere
             */
            private void sommaCosto(float costo) {
                this.costo += costo;
            }
            /**
             * ritorna il costo del percorso
             * @param costo costo del nuovo nodo da espandere
             */
            private float getCosto() {
                return costo;
            }
            /**
             * ritorna l'ultimo nodo di un percorso
             * @return GrafoTronchi.Nodo
             */
            private GrafoTronchi.Nodo getUltimoNodo() { return percorso.get(percorso.size()-1); }
            /**
             * controlla se si è arrivati a trovare la via di fuga più vicina, e ritorna TRUE nel caso
             * @return boolean
             */
            private boolean finito() {
                boolean finito = false;
                if(tronchiUscita.contains(percorso.get(percorso.size()-1).getTronco())) {
                    finito=true;
                }
                return finito;
            }
            /**
             * costruisce il percorso aggiungendo al percorso fino al nodo padre il nodo figlio
             * @param padre
             */
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
    /**
     * metodo che calcola il percorso sul grafo tramite UCS identicamente a sopra ma verso una destinazione interna all' aula e senza condizione di emergenza
     * @param partenza nodo di partenza
     * @param destinazione  nodo di destinazione
     * @return ArrayList<GrafoTronchi.Nodo>
     */
    private ArrayList<GrafoTronchi.Nodo> calcoloPercorso(GrafoTronchi.Nodo partenza, GrafoTronchi.Nodo destinazione) {

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
