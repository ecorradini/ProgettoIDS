package entita;

import java.util.ArrayList;
import java.util.HashMap;

public class Tronco {
    private int id;
    private float x,y,xf,yf;

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

    public int getID() { return id; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getXF() { return xf; }
    public float getYF() { return yf; }
}
