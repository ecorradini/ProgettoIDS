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
    //scanner per ricercare i dispositivi beacon

    private PosizioneUtente posUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posUtente.init(this);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //creazione del BluetoothHelper
        if(btAdapter!=null) btHelper = new BluetoothHelper(btAdapter, this);
        //attivazione del bluetooth (qualora non sia già funzionante)
        if(!btAdapter.isEnabled()) btHelper.activateBluetooth();

        btHelper.discoverBLEDevices();
    }
}
