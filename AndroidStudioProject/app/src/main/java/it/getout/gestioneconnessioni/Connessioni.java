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
    private static Context context;


    public static void init(Context c) {
        context=c;
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
            BluetoothHelper.activateBluetooth((AppCompatActivity)c);
        }

        btHelper = new BluetoothHelper(btAdapter, (AppCompatActivity)c);
        //device = scansionaBluetooth();

        try {
            Thread postDelayed = new Thread() {
                public void run () {
                    try {
                        do {

                            btHelper.discoverBLEDevices();
                            Thread.sleep(1500);

                        }while(!btHelper.getTerminatedScan());
                        device = btHelper.getCurrentBeacon();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Log.e("Mi sono connesso",device.getAddress());
                        PosizioneUtente.setBeaconAttuale(new Beacon(device.getAddress()));
                        PosizioneUtente.init(context);

                    }
                }

            };
            postDelayed.start();
        } catch(Exception e) {
            Log.e("THREAD","VAFFANCULO");
        }
    }
    /**
     *Metodo che si adopera ad effettuare lo scan dei dispositivi bluetooth
     */
  /*  private static BluetoothDevice scansionaBluetooth(){
        btHelper.discoverBLEDevices();
        do {
            device = btHelper.getCurrentBeacon();
        } while(!btHelper.getTerminatedScan());


        return device;
    }
*/

    public static DBHelper getDbReference() { return dbReference; }             ///

    public static ServerHelper getServerReference() { return serverReference; }   ///

}
