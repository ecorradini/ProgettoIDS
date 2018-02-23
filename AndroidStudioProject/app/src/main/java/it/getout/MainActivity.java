package it.getout;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.getout.gestioneconnessioni.BluetoothHelper;
import it.getout.gestioneposizione.PosizioneUtente;
import it.getout.gestionevisualizzazionemappa.MappaFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PosizioneUtente.init(this);

    }
}
