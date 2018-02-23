package it.getout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.getout.gestioneposizione.PosizioneUtente;
import it.getout.gestionevisualizzazionemappa.MappaFragment;

public class MainActivity extends AppCompatActivity {

    MappaFragment mappaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PosizioneUtente.init(this);

        //Una volta che ho inizializzato tutte le informazioni di partenza con il metodo init di PosizioneUtente
        //istanzio il fragment con la mappa e disegno la posizione
        mappaFragment = MappaFragment.newInstance();
        mappaFragment.disegnaPosizione();

    }

    public MappaFragment getMappaFragment() { return mappaFragment; }
}
