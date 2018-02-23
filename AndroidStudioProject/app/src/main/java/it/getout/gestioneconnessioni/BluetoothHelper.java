package it.getout.gestioneconnessioni;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
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

import static android.content.ContentValues.TAG;
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
    //adapter per interpretare ciò che viene trovato nello scan
    private LeDeviceListAdapter mLeDeviceListAdapter;
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
    private BluetoothAdapter bluetoothAdapter;

    //identificativo del messaggio che si può ricevere
    public boolean terminatedScan = false;

    private static Activity activity;

    public BluetoothHelper(BluetoothAdapter btAdapter, Context context){

        context = context;
        bluetoothAdapter = btAdapter;
        //inizializzati i componenti del bluetooth
        mLeDeviceListAdapter = new LeDeviceListAdapter(context);
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

    public void discoverBLEDevices() {
        //parte il thread deputato allo scan dei bluetooth LE
        startScan.run();

        Log.e("BLE_Scanner", "DiscoverBLE, in condition");
    }

    /**
     * Metodo all'interno del quale viene richiesta l'attivazione del bluetooth
     */
    public static void activateBluetooth () {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((AppCompatActivity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    //thread che si occupa di far partire lo scan in cerca dei beacon
    private Runnable startScan = new Runnable() {
        @Override
        public void run() {
            //cancella la lista di sensortag precedentemente trovati
            mLeDeviceListAdapter.clear();
            mLeDeviceListAdapter.setCurrentBeacon(null);

            Log.e(TAG, "Start Scan");
            //parte effettivamente la ricerca dei sensortag
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (bluetoothAdapter != null) {
                    try {
                        bluetoothAdapter.getBluetoothLeScanner()
                                .startScan(scanFilters, scanSettings, mScanCallback);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.e("bluetooth error","accendi il bluetooth");
                    }

                }
            }
            else {
                //parte effettivamente la ricerca dei sensortag
                bluetoothAdapter.startLeScan(uuids, mLeScanCallback);
            }

            //attende per la durata dello scan e poi lancia la runnable per stopparlo
            scanHandler.postDelayed(stopScan, 1000L);
            terminatedScan = false;
        }
    };

    //thread per mettere in pausa lo scan ed eventualmente elaborare i dati
    private Runnable stopScan = new Runnable() {
        @Override
        public void run() {

            Log.e(TAG, "Stop Scan");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    bluetoothAdapter.getBluetoothLeScanner()
                            .stopScan(mScanCallback);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("bluetooth error","accendi il bluetooth");
                }
            }
            else {
                bluetoothAdapter.stopLeScan(mLeScanCallback);
            }
            Log.i(TAG,"numero: " + mLeDeviceListAdapter.getCount());

            //trova il beacon più vicino
            selectedBeacon = mLeDeviceListAdapter.selectedDevice();

            if(selectedBeacon!=null){
                if (currentBeacon==null || !currentBeacon.getAddress().equals(mLeDeviceListAdapter.getCurrentBeacon().getAddress())) {
                    currentBeacon = mLeDeviceListAdapter.getCurrentBeacon();
                }
                //nel caso per n cicli non venga aggiornato
                else {
                    /*cont++;
                    if(cont>=maxNoUpdate) {
                        currentBeacon = selectedBeacon;
                        update();
                        cont = 0;
                    }*/
                }
            }
            terminatedScan = true;
        }
    };

    /**
     * Metodo che cancella la registrazione del broadcast receiver
     */
   /*public void closeScan() {
        if(broadcastReceiver!=null) context.unregisterReceiver(broadcastReceiver);
    }*/

    //callback utilizzata per trovare dispositivi nel raggio d'azione
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                     activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device,rssi);
                        }
                    });
                }
            };


    //callback utilizzata per trovare dispositivi nel raggio d'azione
    private ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("result", result.toString());
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

    public Context getContext(){
        return context;
    }

    public boolean getTerminatedscan(){ return terminatedScan; }
}
