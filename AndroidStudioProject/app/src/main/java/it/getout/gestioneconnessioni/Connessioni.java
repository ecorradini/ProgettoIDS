package it.getout.gestioneconnessioni;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.Posizione;

/**
 * Created by Edoardo on 15/03/2018.
 */

public class Connessioni {
    private static Database dbReference;
    private static Server serverReference;
    //scanner per ricercare i dispositivi beacon
    private static BluetoothHelper btHelper;
    //Istanza del descrittore dei dispositivi bluetooth
    private static BluetoothDevice device;
    private static Context context;


    public static void init(Context c) {
        context=c;
        dbReference = new Database(c);
        serverReference = new Server(c);
        initBluetooth(c);

    }

    /**
     *Metodo che inizializza il bluetooth e tutte le sue fasi(scanner)
     */
    private static void initBluetooth(Context c) {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter

        // Is Bluetooth turned on?
        if (!btAdapter.isEnabled()) {
            //attivazione del bluetooth (qualora non sia già funzionante)
            BluetoothHelper.activateBluetooth((AppCompatActivity)c);
        }

        btHelper = new BluetoothHelper(btAdapter, (AppCompatActivity)c);

        try {
            Thread postDelayed = new Thread() {
                public void run () {

                    BluetoothDevice device = null;

                    try {
                        do {
                            btHelper.discoverBLEDevices();
                            Thread.sleep(1500);
                        }while(!btHelper.getTerminatedScan());

                        device = btHelper.getCurrentBeacon();

                        Log.e("Mi sono connesso",device.getAddress());
                        Posizione.setBeaconAttuale(new Beacon(device.getAddress()));
                        Posizione.init(context);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            postDelayed.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getDbReference() { return dbReference; }             ///

    public static Server getServerReference() { return serverReference; }   ///

}
