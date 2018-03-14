package com.ti.ble.BluetoothLEController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.ti.ble.BluetoothLEController.Exceptions.BluetoothLEBluetoothEnableTimeoutException;
import com.ti.ble.BluetoothLEController.Exceptions.BluetoothLEPermissionException;
import java.util.ArrayList;
import java.util.List;

public class BluetoothLEManager
{
  public static final int BT_ENABLE_TIMEOUT = 5;
  public static final int SCAN_PERMISSIONS_CODE = 1;
  public static final int SCAN_TERMINATE_TIMEOUT = 10;
  static final String TAG = BluetoothLEManager.class.getSimpleName();
  private static BluetoothLEManager mThis;
  BluetoothAdapter adapter;
  Context c;
  List<BluetoothLEDevice> deviceList;
  BluetoothManager m;
  public BluetoothLEManagerCB managerCB;
  Runnable scanRoutine = new Runnable()
  {
    public void run()
    {
      Log.d(BluetoothLEManager.TAG, "scanRoutine started");
      BluetoothAdapter localBluetoothAdapter = BluetoothLEManager.mThis.m.getAdapter();
      ScanSettings localScanSettings = new ScanSettings.Builder().build();
      ArrayList localArrayList = new ArrayList(2);
      if (!localBluetoothAdapter.isEnabled())
      {
        localBluetoothAdapter.enable();
        int i = 50;
        for (;;)
        {
          if (!localBluetoothAdapter.isEnabled()) {
            try
            {
              Thread.sleep(100L);
              int j = i - 1;
              i = j;
              if (j < 0) {
                return;
              }
            }
            catch (InterruptedException localInterruptedException2)
            {
              for (;;)
              {
                Log.d(BluetoothLEManager.TAG, "scanRoutine: Interrupted while sleeping when waiting for bluetooth adapter enable !");
              }
            }
          }
        }
      }
      BluetoothLeScanner localBluetoothLeScanner = localBluetoothAdapter.getBluetoothLeScanner();
      ScanCallback local1 = new ScanCallback()
      {
        public void onBatchScanResults(List<ScanResult> paramAnonymous2List)
        {
          super.onBatchScanResults(paramAnonymous2List);
          Log.d(BluetoothLEManager.TAG, "onBatchScanResults");
        }
        
        public void onScanFailed(int paramAnonymous2Int)
        {
          super.onScanFailed(paramAnonymous2Int);
          Log.d(BluetoothLEManager.TAG, "onScanFailed");
        }
        
        public void onScanResult(int paramAnonymous2Int, ScanResult paramAnonymous2ScanResult)
        {
          super.onScanResult(paramAnonymous2Int, paramAnonymous2ScanResult);
          if (!BluetoothLEManager.this.deviceInList(paramAnonymous2ScanResult.getDevice()))
          {
            BluetoothLEDevice localBluetoothLEDevice = new BluetoothLEDevice(paramAnonymous2ScanResult.getDevice(), BluetoothLEManager.this.c);
            localBluetoothLEDevice.m = BluetoothLEManager.this.m;
            localBluetoothLEDevice.sR = paramAnonymous2ScanResult;
            BluetoothLEManager.this.deviceList.add(localBluetoothLEDevice);
            Log.d(BluetoothLEManager.TAG, "" + paramAnonymous2ScanResult.getDevice().getAddress().toString() + " - Added to device list");
            BluetoothLEManager.mThis.managerCB.deviceFound(localBluetoothLEDevice);
          }
        }
      };
      localBluetoothLeScanner.startScan(localArrayList, localScanSettings, local1);
      while (!BluetoothLEManager.this.stopScan) {
        try
        {
          Thread.sleep(500L, 0);
        }
        catch (InterruptedException localInterruptedException1)
        {
          Log.d(BluetoothLEManager.TAG, "Interrupted");
        }
      }
      if (localBluetoothAdapter.isEnabled()) {
        localBluetoothLeScanner.stopScan(local1);
      }
      Log.d(BluetoothLEManager.TAG, "scanRoutine stopped");
    }
  };
  Thread scanThread;
  boolean stopScan = false;
  
  public BluetoothLEManager(Context paramContext)
  {
    this.c = paramContext;
    this.deviceList = new ArrayList();
    this.m = ((BluetoothManager)this.c.getSystemService("bluetooth"));
    this.adapter = this.m.getAdapter();
  }
  
  public static BluetoothLEManager getInstance(Context paramContext)
  {
    if (mThis == null) {
      mThis = new BluetoothLEManager(paramContext);
    }
    return mThis;
  }
  
  public int checkPermission()
  {
    return ContextCompat.checkSelfPermission(this.c, "android.permission.ACCESS_FINE_LOCATION");
  }
  
  public BluetoothLEDevice deviceForBluetoothDev(BluetoothDevice paramBluetoothDevice)
  {
    int i = 0;
    while (i < this.deviceList.size())
    {
      localBluetoothLEDevice = (BluetoothLEDevice)this.deviceList.get(i);
      if (localBluetoothLEDevice.d.getAddress().toString().equalsIgnoreCase(paramBluetoothDevice.getAddress().toString())) {
        return localBluetoothLEDevice;
      }
      i += 1;
    }
    BluetoothLEDevice localBluetoothLEDevice = new BluetoothLEDevice(paramBluetoothDevice, this.c);
    localBluetoothLEDevice.sR = new ScanResult(paramBluetoothDevice, null, 0, 0L);
    this.deviceList.add(localBluetoothLEDevice);
    Log.d(TAG, "Did not find deviceForBluetoothDev, but added a new device instead to the list");
    return localBluetoothLEDevice;
  }
  
  public boolean deviceInList(BluetoothDevice paramBluetoothDevice)
  {
    int i = 0;
    while (i < this.deviceList.size())
    {
      if (((BluetoothLEDevice)this.deviceList.get(i)).d.getAddress().equalsIgnoreCase(paramBluetoothDevice.getAddress())) {
        return true;
      }
      i += 1;
    }
    return false;
  }
  
  public void prepareForScanForDevices(Activity paramActivity)
  {
    BluetoothAdapter localBluetoothAdapter = this.m.getAdapter();
    if (localBluetoothAdapter == null) {}
    do
    {
      return;
      if (!localBluetoothAdapter.isEnabled())
      {
        localBluetoothAdapter.enable();
        int i = 50;
        for (;;)
        {
          if (!localBluetoothAdapter.isEnabled()) {
            try
            {
              Thread.sleep(100L);
              int j = i - 1;
              i = j;
              if (j < 0) {
                return;
              }
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;)
              {
                Log.d(TAG, "prepareForScanForDevices: Interrupted while sleeping when waiting for bluetooth adapter enable !");
              }
            }
          }
        }
      }
    } while ((ContextCompat.checkSelfPermission(paramActivity, "android.permission.ACCESS_FINE_LOCATION") == 0) || (ActivityCompat.shouldShowRequestPermissionRationale(paramActivity, "android.permission.ACCESS_FINE_LOCATION")));
    ActivityCompat.requestPermissions(paramActivity, new String[] { "android.permission.ACCESS_FINE_LOCATION" }, 1);
  }
  
  public void restartBluetooth(final Context paramContext, final Activity paramActivity)
  {
    final BluetoothAdapter localBluetoothAdapter = this.m.getAdapter();
    paramContext = new ProgressDialog(paramContext);
    paramContext.setTitle("Restarting Bluetooth");
    paramContext.setMax(100);
    paramContext.setProgressStyle(1);
    paramContext.show();
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          if (localBluetoothAdapter.isEnabled())
          {
            localBluetoothAdapter.disable();
            i = 0;
            while (i < 10)
            {
              Thread.sleep(200L, 0);
              paramActivity.runOnUiThread(new Runnable()
              {
                public void run()
                {
                  BluetoothLEManager.1.this.val$dialog.setProgress(i * 5);
                }
              });
              i += 1;
            }
          }
          localBluetoothAdapter.enable();
          final int i = 0;
          while (i < 10)
          {
            Thread.sleep(250L, 0);
            paramActivity.runOnUiThread(new Runnable()
            {
              public void run()
              {
                BluetoothLEManager.1.this.val$dialog.setProgress(i * 5 + 50);
              }
            });
            i += 1;
          }
          paramActivity.runOnUiThread(new Runnable()
          {
            public void run() {}
          });
          paramContext.dismiss();
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    }).start();
  }
  
  public void scanForDevices()
  {
    BluetoothAdapter localBluetoothAdapter = this.m.getAdapter();
    if (localBluetoothAdapter == null) {
      return;
    }
    if (!localBluetoothAdapter.isEnabled())
    {
      localBluetoothAdapter.enable();
      int i = 50;
      for (;;)
      {
        if (!localBluetoothAdapter.isEnabled()) {
          try
          {
            Thread.sleep(100L);
            int j = i - 1;
            i = j;
            if (j < 0) {
              throw new BluetoothLEBluetoothEnableTimeoutException("Timed out after waiting for 5 seconds for bluetooth enable");
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            for (;;)
            {
              Log.d(TAG, "prepareForScanForDevices: Interrupted while sleeping when waiting for bluetooth adapter enable !");
            }
          }
        }
      }
    }
    if (ContextCompat.checkSelfPermission(this.c, "android.permission.ACCESS_FINE_LOCATION") != 0) {
      throw new BluetoothLEPermissionException("Permission denied");
    }
    scanForDevices(0);
  }
  
  public void scanForDevices(int paramInt)
  {
    StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
    if (arrayOfStackTraceElement.length > 3) {
      Log.d(TAG, "scanForDevices from : " + arrayOfStackTraceElement[3].getClassName() + " " + arrayOfStackTraceElement[3].getMethodName());
    }
    if (paramInt == -1) {
      throw new BluetoothLEPermissionException("Permission denied");
    }
    if (this.scanThread != null)
    {
      if (this.scanThread.getState() == Thread.State.TERMINATED)
      {
        this.stopScan = false;
        this.scanThread = new Thread(this.scanRoutine);
        this.scanThread.start();
        return;
      }
      stopScan();
      paramInt = 10;
    }
    for (;;)
    {
      if (this.scanThread.getState() != Thread.State.TERMINATED) {}
      try
      {
        Thread.sleep(50L, 0);
        int i = paramInt - 1;
        paramInt = i;
        if (i >= 0) {
          continue;
        }
        Log.d(TAG, "Timeout while waiting for scanThread to die ...");
        return;
        Log.d(TAG, "Scan thread stopped, restarting");
        this.deviceList = new ArrayList();
        this.stopScan = false;
        this.scanThread = new Thread(this.scanRoutine);
        this.scanThread.start();
        return;
        this.scanThread = new Thread(this.scanRoutine);
        this.scanThread.start();
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        for (;;) {}
      }
    }
  }
  
  public void stopScan()
  {
    this.stopScan = true;
  }
  
  public static abstract interface BluetoothLEManagerCB
  {
    public abstract void deviceFound(BluetoothLEDevice paramBluetoothLEDevice);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\BluetoothLEController\BluetoothLEManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */