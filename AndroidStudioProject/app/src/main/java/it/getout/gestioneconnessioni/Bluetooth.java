package it.getout.gestioneconnessioni;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by Alessandro on 20/02/2018.
 * La presente classe si occuperà di definire la scansione, connessione e comunicazione con i Beacon tramite bluetooth
 */

public class Bluetooth {

    public static final int REQUEST_ENABLE_BT = 1;


    private BluetoothAdapter bluetoothAdapter; //istanza dell'adapter relativo al bluetooth

    private BluetoothDevice currentBeacon; //rappresenta il beacon più vicino, con cui si deve effettuare il collegamento

    private BluetoothDevice selectedBeacon; //rappresenta il beacon trovato dallo scan

    private LeDeviceListAdapter mLeDeviceListAdapter; //adapter per interpretare ciò che viene trovato nello scan

    private static final String beaconUUID = "0000aa80-0000-1000-8000-00805f9b34fb"; //uuid dei sensortag utilizati

    //maschera di UUID, serve per filtrare i dispositivi bluetooth da analizzare
    private UUID[] uuids;
    //handler utilizzato per lanciare le varie Runnable (start,stop,wait)
    private Handler scanHandler;
    //private static IntentFilter intentFilter;
    //elementi necessari per efettuare lo scan
    private ScanFilter scanFilter;
    private ScanSettings scanSettings;
    private ArrayList<ScanFilter> scanFilters;

    //flag per verificare che lo scan sia concluso
    public boolean terminatedScan;
    //activity corrente
    private static Activity activity;

    //numero massimo di scan senza che venga mandata la posizione al server
    private static final int maxNoUpdate = 5;
    //conta quante volte consecutive non si invia la propria posizione al server
    private int cont;

    public Bluetooth(BluetoothAdapter btAdapter, AppCompatActivity a){

        activity = a;
        bluetoothAdapter = btAdapter;
        terminatedScan = false;
        mLeDeviceListAdapter = new LeDeviceListAdapter(a.getBaseContext()); //inizializzati i componenti del bluetooth
        //insieme di UUID riconosciuti dallo scan e relativa inizializzazione
        uuids = new UUID[1];
        uuids[0] = UUID.fromString(beaconUUID);

        //inizializzati gli elementi per lo scan
        scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(beaconUUID))).build();
        scanFilters = new ArrayList<>();
        scanFilters.add(scanFilter);
        scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

        scanHandler = new Handler(); //viene inizializzato l'handler
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

    public void discoverBLEDevices() {

        startScan.run(); //parte il thread deputato allo scan dei bluetooth LE

        Log.i("BLE_Scanner", "DiscoverBLE, in condition");
    }

    /**
     * Metodo all'interno del quale viene richiesta l'attivazione del bluetooth
     */
    public static void activateBluetooth (AppCompatActivity c) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        c.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    //thread che si occupa di far partire lo scan in cerca dei beacon
    private Runnable startScan = new Runnable() {
        @Override
        public void run() {

            mLeDeviceListAdapter.clear(); //cancella la lista di sensortag precedentemente trovati
            mLeDeviceListAdapter.setCurrentBeacon(null);

            currentBeacon = null;

            Log.i(TAG, "Start Scan");
            //parte effettivamente la ricerca dei sensortag
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (bluetoothAdapter != null) {
                    try {
                        bluetoothAdapter.getBluetoothLeScanner().startScan(scanFilters, scanSettings, mScanCallback);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.e("bluetooth error", "accendi il bluetooth");
                    }

                }
            }

            //attende per la durata dello scan e poi lancia la runnable per stopparlo
            //scanHandler.postDelayed(stopScan, 1000L);
            Thread postDelayed = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopScan.run();
                }
            };
            postDelayed.start();
        }
    };

    //thread per mettere in pausa lo scan ed eventualmente elaborare i dati
    private Runnable stopScan = new Runnable() {
        @Override
        public void run() {

            Log.i(TAG, "Stop Scan");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    bluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("bluetooth error","accendi il bluetooth");
                }
            }

            Log.i(TAG,"numero: " + mLeDeviceListAdapter.getCount());

            if(mLeDeviceListAdapter.getCount() == 0)
                terminatedScan = false;
            else {

                //trova il beacon più vicino
                selectedBeacon = mLeDeviceListAdapter.selectedDevice();

                if (selectedBeacon != null) {
                    if (currentBeacon == null || !currentBeacon.getAddress().equals(mLeDeviceListAdapter.getCurrentBeacon().getAddress())) {
                        currentBeacon = mLeDeviceListAdapter.getCurrentBeacon();
                    }
                    //nel caso per n cicli non venga aggiornato
                    else {
                        cont++;
                        if (cont >= maxNoUpdate) {
                            currentBeacon = selectedBeacon;
                            cont = 0;
                        }
                    }
                }
                terminatedScan = true;
            }
        }
    };


    //callback utilizzata per trovare dispositivi nel raggio d'azione
    private ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d("result", result.toString());
            Log.i("ONSCANRESULT", "qua dentro ci sono");
            BluetoothDevice btDevice = result.getDevice();
            mLeDeviceListAdapter.addDevice(btDevice,result.getRssi());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            System.out.println("BLE// onBatchScanResults");
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            System.out.println("BLE// onScanFailed");
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    public boolean getTerminatedScan(){ return terminatedScan; }
}