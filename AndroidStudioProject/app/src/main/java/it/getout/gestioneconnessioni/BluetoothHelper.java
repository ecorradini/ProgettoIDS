package it.getout.gestioneconnessioni;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.getout.gestioneposizione.PosizioneUtente.REQUEST_ENABLE_BT;

/**
 * Created by Alessandro on 20/02/2018.
 * La presente classe si occuperà di definire la scansione, connessione e comunicazione con i Beacon tramite bluetooth
 */

public class BluetoothHelper {

    private static Context context;

    //rappresenta il beacon più vicino, con cui si deve effettuare il collegamento
    private BluetoothDevice currentBeacon;
    //rappresenta il beacon trovato dallo scan
    private BluetoothDevice selectedBeacon;

    //uuid dei sensortag utilizati
    private static final String beaconUUID = "0000aa80-0000-1000-8000-00805f9b34fb";

    //maschera di UUID, serve per filtrare i dispositivi bluetooth da analizzare
    private UUID[] uuids;
    //handler utilizzato per lanciare le varie Runnable (start,stop,wait)
    private Handler scanHandler;
    private static IntentFilter intentFilter;
    //elementi necessari per efettuare lo scan
    private ScanFilter scanFilter;
    private ScanSettings scanSettings;
    private List<ScanFilter> scanFilters;


    public BluetoothHelper(BluetoothAdapter btAdapter, Context context){
        this.context = context;
        //insieme di UUID riconosciuti dallo scan e relativa inizializzazione
        uuids = new UUID[1];
        uuids[0] = UUID.fromString(beaconUUID);
        //inizializzati gli elementi per lo scan
        scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(beaconUUID))).build();

        scanFilters = new ArrayList<>();
        scanFilters.add(scanFilter);
        scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
    }

    /**
     * Metodo che restituisce il beacon più vicino, dall'ultimo scan
     * @return beacon più vicino rispetto all'utente
     */
    public BluetoothDevice getSelectedBeacon() {
        return selectedBeacon;
    }

    /**
     * Metodo che restituisce l'ultimo beacon rilevato
     * @return ultimo beacon con cui ci si è collegati
     */
    public BluetoothDevice getCurrentBeacon() {
        return currentBeacon;
    }

    /**
     * Metodo per iniziare lo scan che ricerca i sensortag presenti nel raggio d'azione dell'utente
     */

    private void discoverBLEDevices() {
        //parte il thread deputato allo scan dei bluetooth LE
        startScan.run(); //vogliamo creare thread apposito per lo scan??????????

        Log.e("BLE_Scanner", "DiscoverBLE, in condition");
    }

    /**
     * Metodo all'interno del quale viene richiesta l'attivazione del bluetooth
     */
    public static void activateBluetooth () {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((AppCompatActivity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

}
