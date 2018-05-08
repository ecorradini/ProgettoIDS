package entita;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class Tronco {
    private int id;
    private float x,y,xf,yf,larghezza;
    private final float[] weight={0.2f,0.2f,0.2f,0.2f,0.2f}; // supponiamo che i primi tre valori siano legati ai "parametri", gli altri due alla "lunghezza" e "los"

    public Tronco(int id, float x, float y, float xf, float yf) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xf = xf;
        this.yf = yf;
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
        float lunghezza;
        int numeroPersone;

        ArrayList<Float> parametri;  //1.VULNERABILITA  2.RISCHIOVITA  3.PRESENZAFUMO
        parametri = new ArrayList<>(DAOParametri.selectParametri(id));

        for(int i=0; i<parametri.size(); i++){
            peso += parametri.get(i)*(weight[i]);
        }

        numeroPersone = DAOBeacon.getNumeroPersoneByTroncoId(id);
        lunghezza = calcolaLunghezzaTronco();
        peso  += lunghezza*weight[4]+(numeroPersone/(lunghezza*larghezza))*weight[5];

        return peso;
    }

    private float calcolaLunghezzaTronco(){

        float dx,dy;
        if (x==xf)
            return Math.abs(yf-y);
        else if(y==yf)
            return Math.abs(xf-x);
        else{
            dx = Math.abs(xf-x);
            dy = Math.abs(yf-y);
            return (float)Math.sqrt(dx*dx+dy*dy);
        }

    }


    public int getID() { return id; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getXF() { return xf; }
    public float getYF() { return yf; }
}
