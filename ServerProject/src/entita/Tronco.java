package entita;

import java.util.ArrayList;
import java.util.HashMap;


public class Tronco {
    private int id;
    private float x,y,xf,yf,larghezza, lunghezza;
    private static float[] weight; // supponendo che i primi tre valori siano legati ai "parametri", gli altri due alla "lunghezza" e "los"
    /**
     * costruttore
     */
    public Tronco(int id, float x, float y, float xf, float yf, float larghezza, float lunghezza) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xf = xf;
        this.yf = yf;
        this.larghezza = larghezza;
        this.lunghezza = lunghezza;
        weight = new float[]{0.2f,0.2f,0.2f,0.2f,0.2f};
    }
    /**
     * metodo per impostare i pesi dei parametri che influenzano il calcolo del percorso
     * @param w1
     * @param w2
     * @param w3
     * @param w4
     * @param w5
     */
    public static void setWeight(float w1, float w2, float w3, float w4, float w5) {
        weight = new float[]{w1,w2,w3,w4,w5};
    }
    /**
     * ritorna i tronchi adiacenti a un insieme di tronchi passato in input
     * @param tronchiPiano insieme dei tronchi di cui si vuole trovare il set di tronchi adiacenti
     * @return ArrayList<Tronco>
     */
    public ArrayList<Tronco> richiediAdiacenti(HashMap<Integer,Tronco> tronchiPiano) {
        ArrayList<Integer> adiacentiID = DAOTronco.selectTronchiAdiacenti(this);
        ArrayList<Tronco> adiacenti = new ArrayList<>();
        if( adiacentiID!=null) {
            for (int i = 0; i < adiacentiID.size(); i++) {
                adiacenti.add(tronchiPiano.get(adiacentiID.get(i)));
            }
            return adiacenti;
        }
        else return null;
    }

    /**
     * applica la formula per il calcolo del percorso in base ai pesi e ai valori dei parametri del tronco selezionato poi lo ritorna
     * @return float
     */

    public float calcolaPesoTronco(){
        float peso=0;
        int numeroPersone;

        ArrayList<Float> parametri;  //0.VULNERABILITA  1.RISCHIOVITA  2.PRESENZAFUMO
        parametri = DAOParametri.selectParametri(id);

        //calcolo peso con parametri 1, 2 e 3.
        for(int i=0; i<parametri.size(); i++){
            if(i==1)
                peso += parametri.get(i)*100*weight[i];
            else {
                peso += parametri.get(i) * weight[i];
            }
        }

        numeroPersone = DAOBeacon.getNumeroPersoneInTronco(id);

        // aggiunta al peso le componenti di lunghezza e los (C'È DA NORMALIZZARE: PORTARE TUTTE LE VARIE COMPONENTI A VALORI COMPRESI TRA 0 E 1)
        peso  += lunghezza*weight[3]+(numeroPersone/(lunghezza*larghezza))*weight[4];

        return peso;
    }


    /**
     * ritorna true se il tronco passato è uguale al tronco corrente selezionato
     * @param t
     * @return boolean
     */

    public boolean equals(Tronco t) {
        return t.getID()==id;
    }

    public int getID() { return id; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getXF() { return xf; }
    public float getYF() { return yf; }
    public float getLarghezza() { return larghezza; }
    public float getLunghezza() { return lunghezza; }
}
