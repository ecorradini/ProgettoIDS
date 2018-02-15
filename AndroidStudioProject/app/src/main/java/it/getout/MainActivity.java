package it.getout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.getout.gestioneposizione.PosizioneUtente;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter = null;
    private PosizioneUtente posUtente;

    public static final ParcelUuid Service_UUID = ParcelUuid.fromString("0000b81d-0000-1000-8000-00805f9b34fb");
    public static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {

                // Is Bluetooth turned on?
                if (!mBluetoothAdapter.isEnabled()) {
                    // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

            } else {

                // Bluetooth is not supported.
                //showErrorText(R.string.bt_not_supported);
            }
        }
    }
}
