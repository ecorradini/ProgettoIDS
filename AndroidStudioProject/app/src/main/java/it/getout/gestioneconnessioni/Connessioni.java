package it.getout.gestioneconnessioni;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import it.getout.gestioneposizione.Beacon;
import it.getout.gestioneposizione.PosizioneUtente;

/**
 * Created by Edoardo on 15/03/2018.
 */

public class Connessioni {
    private static DBHelper dbReference;
    private static ServerHelper serverReference;
    //Istanza dell'Adapter Bluetooth
    private static BluetoothAdapter btAdapter;
    //scanner per ricercare i dispositivi beacon
    private static BluetoothHelper btHelper;
    //Istanza del descrittore dei dispositivi bluetooth
    private static BluetoothDevice device;


    public static void init(Context c) {
        dbReference = new DBHelper(c);
        serverReference = new ServerHelper(c);
        initBluetooth(c);
    }

    /**
     *Metodo che inizializza il bluetooth e tutte le sue fasi(scanner)
     */
    private static void initBluetooth(Context c) {
        if (btAdapter == null) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();  // Local Bluetooth adapter
        }

        // Is Bluetooth turned on?
        if (!btAdapter.isEnabled()) {
            //attivazione del bluetooth (qualora non sia già funzionante)
            btHelper.activateBluetooth();
        }

        btHelper = new BluetoothHelper(btAdapter, (AppCompatActivity)c);
        device =  scansionaBluetooth();
        Thread attesa = new Thread() {
            public void run() {
                while(device==null) {
                    try {
                        Log.e("DEVICE","NON ISTANZIATO");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        attesa.start();
        try {
            attesa.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //memorizzo beaconAttusle all'interno dell'oggetto Beacon
        PosizioneUtente.setBeaconAttuale(new Beacon(device.getAddress()));
    }
    /**
     *Metodo che si adopera ad effettuare lo scan dei dispositivi bluetooth
     */
    private static BluetoothDevice scansionaBluetooth(){
        btHelper.discoverBLEDevices();
        do {
            device = btHelper.getCurrentBeacon();
            if (device == null) Log.i("DEVICE", "NULL");
            try {
                Thread.sleep(5500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //} while(!btHelper.getTerminatedscan());
        } while(device==null);

        return device;
    }


    public static DBHelper getDbReference() { return dbReference; }             ///

    public static ServerHelper getServerReference() { return serverReference; }   ///

}
