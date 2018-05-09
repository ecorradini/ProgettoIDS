package entita;

import connessioni.DatabaseConnection;

import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class Percorso extends Thread {

    private String percorso;
    private String beacon;

    public Percorso(String beacon) {
        percorso = "";
        this.beacon = beacon;
        start();
    }

    public void run() {
        Connection conn = DatabaseConnection.getConn();
        String json = "{PERCORSO:[";


    }

    public String getResult() {
        while(percorso.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return percorso;
    }

    private ArrayList<GrafoTronchi.Nodo> UCS(GrafoTronchi.Nodo partenza, ArrayList<Tronco> tronchiPiano, ArrayList<Tronco> tronchiUscita){

        class PercorsoConCosto {

            private ArrayList<Tronco> percorso;    //deve partire con una lista vuota
            private float costo;

            private PercorsoConCosto(Tronco primo) {
                percorso = new ArrayList<>();
                costo = 0;
            }

            private ArrayList<Tronco> getPercorso () {return percorso;}

            private void aggiungiTronco(Tronco nuovo) {
                percorso.add(nuovo);
            }

            private void setCosto(float costo) {
                this.costo += costo;
            }

            private float getCosto() {
                return costo;
            }
        }

        ArrayList<GrafoTronchi.Nodo> adiacenti = partenza.getAdiacenti();

        GrafoTronchi.Nodo costoBasso = adiacenti.get(0);
        for(int i=1; i < adiacenti.size(); i++) {
            if(adiacenti.get(i).get)
        }

        ArrayList<PercorsoConCosto> frontiera = new ArrayList<>();

        boolean guardia=true;
        for (int i = 0; i < adiacenti.size(); i++)    //ciclo di inizializzazione della frontiera
        {
            frontiera.get(i).setPercorso(adiacenti.get(i).getId());
            frontiera.get(i).setCosto(pesiTronchi.get(adiacenti.get(i).getId()));
        }
        while (guardia)
        {
            for (int j=0;j<tronchiUscita.size();j++)        //ciclo di arresto
            {
                for (int k=0; k<frontiera.size() && guardia==true;k++ )
                    if (tronchiUscita.get(j).getId()==frontiera.get(k).getPercorso().get(frontiera.get(k).getPercorso().size()-1))
                    {
                        guardia=false;
                    }
            }

            float paragone = 2;       //lo inizializzo a due perché i pesi e la formula in generale dovrebbe dar valori in [0;1] : così non dovesse essere basta cambiare
            int scelta = 0;           //il valore a un opportuno valore sempre maggiore del peso massimo possibile
            for (int i = 0; i < frontiera.size() && guardia==true; i++)       //ciclo di ricerca del tronco da espandere
            {
                if (frontiera.get(i).getCosto() < paragone)
                {
                    scelta = i;
                    paragone = frontiera.get(i).getCosto();
                }
            }

            frontiera.get(scelta).setPercorso(); = /*getTroncoByID */(frontiera.get(scelta).getPercorso().get(frontiera.get(scelta).getPercorso().size() - 1)).getTronchiAdiacenti();
        }
    }
}
