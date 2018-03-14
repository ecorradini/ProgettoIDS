package com.ti.ble.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.BluetoothLEController.EddystoneBeaconDecoder;
import com.ti.device_selector.BTDeviceWithAdvData;
import com.ti.util.bleUtility;
import java.util.Map;

public class BroadcastActivity
  extends AppCompatActivity
{
  public static final String TAG = "BroadCastActivity";
  public String btAddr;
  public BTDeviceWithAdvData mDev;
  public BroadcastActivity mThis;
  ScanCallback scanCB = new ScanCallback()
  {
    public void onScanResult(int paramAnonymousInt, final ScanResult paramAnonymousScanResult)
    {
      super.onScanResult(paramAnonymousInt, paramAnonymousScanResult);
      if (paramAnonymousScanResult.getDevice().getAddress().equalsIgnoreCase(BroadcastActivity.this.btAddr))
      {
        Log.d("BroadCastActivity", "Found correct device !");
        if (BroadcastActivity.this.mDev == null)
        {
          BroadcastActivity.this.mDev = new BTDeviceWithAdvData(paramAnonymousScanResult.getDevice());
          BroadcastActivity.this.mDev.beaconDecoder = new EddystoneBeaconDecoder();
        }
        final ScanRecord localScanRecord = paramAnonymousScanResult.getScanRecord();
        Map localMap = localScanRecord.getServiceData();
        if (BroadcastActivity.this.mDev.beaconDecoder.updateData(localMap)) {
          BroadcastActivity.this.runOnUiThread(new Runnable()
          {
            public void run()
            {
              ((TextView)BroadcastActivity.this.findViewById(2131230737)).setText("Eddystone beacon Decoder");
              ((TextView)BroadcastActivity.this.findViewById(2131230736)).setText("Eddystone parameters");
              ((TextView)BroadcastActivity.this.findViewById(2131230734)).setText(BroadcastActivity.this.mDev.btDevice.getAddress().toString());
              ((TextView)BroadcastActivity.this.findViewById(2131230735)).setText(paramAnonymousScanResult.getRssi() + "dB");
              TableLayout localTableLayout1 = (TableLayout)BroadcastActivity.this.findViewById(2131230733);
              localTableLayout1.removeAllViews();
              TableLayout localTableLayout2 = (TableLayout)BroadcastActivity.this.findViewById(2131231070);
              localTableLayout2.removeAllViews();
              SparseArray localSparseArray = localScanRecord.getManufacturerSpecificData();
              int i = 0;
              while (i < localSparseArray.size())
              {
                int j = localSparseArray.keyAt(i);
                byte[] arrayOfByte = (byte[])localSparseArray.get(j);
                TableRow localTableRow = (TableRow)((LayoutInflater)BroadcastActivity.this.getApplicationContext().getSystemService("layout_inflater")).inflate(2131361833, null);
                ((TextView)localTableRow.findViewById(2131230765)).setText((CharSequence)BluetoothGATTDefines.manufacturerIDStrings.get(Integer.valueOf(j)));
                ((TextView)localTableRow.findViewById(2131230766)).setText(bleUtility.getStringFromByteVector(arrayOfByte));
                localTableLayout1.addView(localTableRow);
                i += 1;
              }
              BroadcastActivity.this.mDev.beaconDecoder.updateTableWithParameters(localTableLayout2, BroadcastActivity.this.getApplicationContext());
            }
          });
        }
      }
    }
  };
  
  protected void onCreate(@Nullable Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2131361818);
    this.btAddr = getIntent().getExtras().getString("TopLevel.BROADCAST_DEVICE_ADDR");
    BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(this.scanCB);
    this.mThis = this;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\BroadcastActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */