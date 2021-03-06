package it.getout.gestioneconnessioni;

/**
    Classe utilizzata per la gestione e l'interpretazione dei dispositivi rintracciati dallo scan
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

// Adapter per gestire i dispositivi identificati durante lo scanner.
public class LeDeviceListAdapter {

    private BluetoothDevice currentBeacon; //beacon più vicino all'utente
    private static HashMap<String, BluetoothDevice> sensors;
    private String TAG2 = "LeDeviceAdapter";

    private TreeMap<Integer,BluetoothDevice> mLeDevices; //Hashmap di dispositivi estratti dallo scan (K: potenza del segnali RSSI, dispositivo trovato dallo scan)
    private Context context;

    /**
     * Costruisce l'adapter per identificare i dispositivi estratti dallo scan
     */
    public LeDeviceListAdapter(Context context) {
        super();
        mLeDevices = new TreeMap<>(Collections.<Integer>reverseOrder());
        sensors = new HashMap<>();
        this.context = context;
    }
    /**
     * Metodo per aggiungere un dispositivo alla lista di quelli trovati (solamente nel caso in cui non sia già presente)
     */
    public void addDevice(BluetoothDevice device, int rssi) {
        if (!mLeDevices.containsValue(device)) {
            mLeDevices.put(rssi,device);
            sensors.put(device.getAddress(), device);
            Log.i(TAG2,"device aggiunto: " + device.getAddress() + " rssi: " + rssi);
        }
        else {
            Log.i("addDevice", "non aggiunto");
        }
    }


    /**
     * Metodo che calcola e restituisce il beacon più vicino fra quelli trovati
     */
    public BluetoothDevice selectedDevice() {

        BluetoothDevice b = null;

        Iterator it = mLeDevices.entrySet().iterator();
        //scandisce la lista in base alla distanza rispetto all'utente, finchè non trova un beacon presente nel CSV o finchè
        //non termina la lista
        while (b == null && it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            BluetoothDevice dev = (BluetoothDevice) entry.getValue();
            //valuta se il beacon trovato è presente nella lista di quelli salvati. Se si allora si è trovato il
            //dispositivo più vicino
            if(sensors.containsKey(dev.getAddress())) {
                b = dev;
            }
            else {
                Toast.makeText(context,
                        " Si è individuato un sensore non presente nel documento, ", Toast.LENGTH_SHORT).show();

                sensors.put(dev.getAddress(), dev);
            }
        }

        currentBeacon = b;
        return b;
    }

    /**
     * Metodo che restituisce il beacon più vicino rispetto all'utente
     * @return beacon più vicino rispetto all'utente
     */
    public BluetoothDevice getCurrentBeacon() {
        return currentBeacon;
    }
    /**
     * Metodo che setta il dispositivo più vicino
     * @param b, dispositivo bluetooth da impostare con currentBeacon
     */
    public void setCurrentBeacon(BluetoothDevice b) {
        currentBeacon = b;
    }
    /**
     * Metodo cancella tutti i dispositivi trovati dalla lista
     */
    public void clear() {
        mLeDevices.clear();
    }

    /**
     * Metodo che restituisce il numero di dispositivi trovati
     * @return numero di dispositivi trovati
     */
    public int getCount() {
        return mLeDevices.size();
    }

}