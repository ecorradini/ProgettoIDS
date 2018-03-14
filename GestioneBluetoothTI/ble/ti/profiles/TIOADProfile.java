package com.ti.ble.ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.GenericCharacteristicTableRow;
import com.ti.util.TrippleSparkLineView;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TIOADProfile
  extends GenericBluetoothProfile
{
  public static final String ACTION_PREPARE_FOR_OAD = "com.ti.ble.ti.profiles.ACTION_PREPARE_FOR_OAD";
  public static final String ACTION_RESTORE_AFTER_OAD = "com.ti.ble.ti.profiles.ACTION_RESTORE_AFTER_OAD";
  public static final String oadBlockRequest_UUID = "f000ffc2-0451-4000-b000-000000000000";
  public static final String oadImageNotify_UUID = "f000ffc1-0451-4000-b000-000000000000";
  public static final String oadImageStatus_UUID = "f000ffc4-0451-4000-b000-000000000000";
  public static final String oadService_UUID = "f000ffc0-0451-4000-b000-000000000000";
  private BroadcastReceiver brRecv;
  private boolean clickReceiverRegistered = false;
  private String fwRev;
  
  public TIOADProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new TIOADProfileTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals("f000ffc1-0451-4000-b000-000000000000")) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals("f000ffc2-0451-4000-b000-000000000000")) {
        this.configC = paramBluetoothDevice;
      }
    }
    this.tRow.title.setText("TI OAD Service");
    this.tRow.sl1.setVisibility(4);
    this.tRow.value.setVisibility(4);
    this.tRow.setIcon(getIconPrefix(), paramBluetoothGattService.getUuid().toString(), "oad");
    this.brRecv = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if ("TIOADProfileTableRow.ACTION_VIEW_CLICKED".equals(paramAnonymousIntent.getAction()))
        {
          Log.d("TIOADProfile", "SHOW OAD DIALOG !");
          TIOADProfile.this.prepareForOAD();
        }
      }
    };
    this.context.registerReceiver(this.brRecv, makeIntentFilter());
    this.clickReceiverRegistered = true;
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("f000ffc0-0451-4000-b000-000000000000") == 0;
  }
  
  private static IntentFilter makeIntentFilter()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("TIOADProfileTableRow.ACTION_VIEW_CLICKED");
    return localIntentFilter;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void didUpdateFirmwareRevision(String paramString)
  {
    this.fwRev = paramString;
    this.tRow.value.setText("Current FW :" + paramString);
  }
  
  public void disableService() {}
  
  public void enableService() {}
  
  public void onPause()
  {
    super.onPause();
    if (this.clickReceiverRegistered)
    {
      this.context.unregisterReceiver(this.brRecv);
      this.clickReceiverRegistered = false;
    }
  }
  
  public void onResume()
  {
    super.onResume();
    if (!this.clickReceiverRegistered)
    {
      this.brRecv = new BroadcastReceiver()
      {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
          if ("TIOADProfileTableRow.ACTION_VIEW_CLICKED".equals(paramAnonymousIntent.getAction()))
          {
            Log.d("TIOADProfile", "SHOW OAD DIALOG !");
            TIOADProfile.this.prepareForOAD();
          }
        }
      };
      this.context.registerReceiver(this.brRecv, makeIntentFilter());
      this.clickReceiverRegistered = true;
    }
  }
  
  public void periodWasUpdated(int paramInt) {}
  
  public void prepareForOAD()
  {
    Intent localIntent = new Intent("com.ti.ble.ti.profiles.ACTION_PREPARE_FOR_OAD");
    this.context.sendBroadcast(localIntent);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TIOADProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */