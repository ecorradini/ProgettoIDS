package com.ti.ble.sensortag;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SensorTagApplicationClass
  extends Application
{
  private static final int REQ_ENABLE_BT = 0;
  public static BluetoothManager mBluetoothManager;
  public Activity currentActivity;
  public boolean mBleSupported = true;
  public BluetoothAdapter mBtAdapter = null;
  public boolean mBtAdapterEnabled = false;
  private IntentFilter mFilter;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(paramAnonymousIntent.getAction())) {
        switch (SensorTagApplicationClass.this.mBtAdapter.getState())
        {
        }
      }
    }
  };
  
  public void onCreate()
  {
    super.onCreate();
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagApplicationClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */