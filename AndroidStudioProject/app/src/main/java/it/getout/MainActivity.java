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

    }

    public MappaFragment getMappaFragment() { return mappaFragment; }
}
