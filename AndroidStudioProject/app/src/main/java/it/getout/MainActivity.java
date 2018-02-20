package it.getout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.getout.gestioneposizione.PosizioneUtente;

public class MainActivity extends AppCompatActivity {

    private PosizioneUtente posUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posUtente.initBluetooth(this);
    }
}
