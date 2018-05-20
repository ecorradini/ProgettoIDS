package entita;

import java.util.ArrayList;
import java.util.HashMap;


public class Tronco {
    private int id;
    private float x,y,xf,yf,larghezza, lunghezza;
    private final float[] weight={0.2f,0.2f,0.2f,0.2f,0.2f}; // supponendo che i primi tre valori siano legati ai "parametri", gli altri due alla "lunghezza" e "los"

    public Tronco(int id, float x, float y, float xf, float yf, float larghezza, float lunghezza) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xf = xf;
        this.yf = yf;
        this.larghezza = larghezza;
        this.lunghezza = lunghezza;
    }

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




    public int getID() { return id; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getXF() { return xf; }
    public float getYF() { return yf; }
    public float getLarghezza() { return larghezza; }
    public float getLunghezza() { return lunghezza; }
}
