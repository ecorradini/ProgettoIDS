package it.getout;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.getout.gestioneconnessioni.BluetoothHelper;
import it.getout.gestioneposizione.PosizioneUtente;

public class MainActivity extends AppCompatActivity {

    //struttura utilizzata per interfacciarsi con il bluetooth
    private static BluetoothAdapter btAdapter;
    //scanner per ricercare i dispositivi beacon
    private static BluetoothHelper btHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PosizioneUtente.init(this);

    }
}
