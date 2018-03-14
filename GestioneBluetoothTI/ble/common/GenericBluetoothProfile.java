package com.ti.ble.common;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.BluetoothLEController.BluetoothLEManager;
import com.ti.util.GenericCharacteristicTableRow;
import java.util.Map;
import java.util.UUID;

public class GenericBluetoothProfile
{
  protected static final int GATT_TIMEOUT = 250;
  protected BluetoothGattCharacteristic configC;
  protected Context context;
  protected BluetoothGattCharacteristic dataC;
  protected BluetoothLEDevice dev;
  private final BroadcastReceiver guiReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      String str = paramAnonymousIntent.getStringExtra("com.ti.util.EXTRA_SERVICE_UUID");
      if (GenericBluetoothProfile.this.tRow.uuidLabel.getText().toString().compareTo(str) == 0)
      {
        Log.d("GenericBluetoothProfile", "Matched UUID :" + GenericBluetoothProfile.this.tRow.uuidLabel.getText());
        str = paramAnonymousIntent.getStringExtra("com.ti.ble.common.GenericServiceConfigurationDialog.PERIOD");
        boolean bool = paramAnonymousIntent.getBooleanExtra("com.ti.ble.common.GenericServiceConfigurationDialog.SENSOR_STATE", true);
        if (paramAnonymousContext.compareTo("GenericServiceConfigurationDialogFragment.UPDATE") == 0)
        {
          GenericBluetoothProfile.this.onOffWasUpdated(bool);
          paramAnonymousContext = str.replaceAll("[^0-9.]", "");
          GenericBluetoothProfile.this.periodWasUpdated(Integer.parseInt(paramAnonymousContext));
        }
      }
    }
  };
  public boolean isConfigured;
  public boolean isEnabled;
  protected boolean isRegistered;
  protected BluetoothDevice mBTDevice;
  protected BluetoothGattService mBTService;
  protected BluetoothLEManager man;
  protected BluetoothGattCharacteristic periodC;
  protected BroadcastReceiver serviceSettings;
  protected GenericCharacteristicTableRow tRow;
  
  public GenericBluetoothProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    this.mBTDevice = paramBluetoothDevice;
    this.mBTService = paramBluetoothGattService;
    this.tRow = new GenericCharacteristicTableRow(paramContext, 1000, true);
    this.dataC = null;
    this.periodC = null;
    this.configC = null;
    this.context = paramContext;
    this.isRegistered = false;
    this.man = BluetoothLEManager.getInstance(paramContext);
    try
    {
      this.dev = this.man.deviceForBluetoothDev(paramBluetoothDevice);
      return;
    }
    catch (Exception paramContext)
    {
      paramContext.printStackTrace();
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return false;
  }
  
  private static IntentFilter makeFilter()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.ti.util.ACTION_PERIOD_UPDATE");
    localIntentFilter.addAction("com.ti.util.ACTION_ONOFF_UPDATE");
    localIntentFilter.addAction("com.ti.util.ACTION_CALIBRATE");
    localIntentFilter.addAction("GenericServiceConfigurationDialogFragment.UPDATE");
    return localIntentFilter;
  }
  
  protected void calibrationButtonTouched() {}
  
  public void configureService()
  {
    int i = this.dev.setCharacteristicNotificationSync(this.dataC, true);
    if ((i != 0) && (this.dataC != null)) {
      printError("Sensor notification enable failed: ", this.dataC, i);
    }
    this.isConfigured = true;
  }
  
  public void deConfigureService()
  {
    int i = this.dev.setCharacteristicNotificationSync(this.dataC, false);
    if ((i != 0) && (this.dataC != null)) {
      printError("Sensor notification disable failed: ", this.dataC, i);
    }
    this.isConfigured = false;
  }
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void didUpdateFirmwareRevision(String paramString) {}
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void didWriteValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void disableService()
  {
    int i = this.dev.writeCharacteristicAsync(this.configC, (byte)0);
    if ((i != 0) && (this.configC != null)) {
      printError("Sensor disable failed: ", this.configC, i);
    }
    this.isConfigured = false;
  }
  
  public void enableService()
  {
    int i = this.dev.writeCharacteristicAsync(this.configC, (byte)1);
    if ((i != 0) && (this.configC != null)) {
      printError("Sensor enable failed: ", this.configC, i);
    }
    this.dev.readCharacteristicAsync(this.periodC);
    this.isEnabled = true;
  }
  
  public String getIconPrefix()
  {
    String str2 = "";
    String str1 = str2;
    if (this.mBTDevice.getName() != null)
    {
      if ((!this.mBTDevice.getName().equals("CC2650 SensorTag")) && (!this.mBTDevice.getName().equals("CC1350 SensorTag"))) {
        break label50;
      }
      str1 = "sensortag2_";
    }
    label50:
    do
    {
      return str1;
      if (this.mBTDevice.getName().equals("SensorTag")) {
        return "sensortag_";
      }
      if (this.mBTDevice.getName().equals("CC2650 RC")) {
        return "cc2650_rc_";
      }
      if ((this.mBTDevice.getName().equals("CC2650 LaunchPad")) || (this.mBTDevice.getName().equals("CC1350 LaunchPad"))) {
        return "cc2650_launchpad_";
      }
      if (this.mBTDevice.getName().equalsIgnoreCase("ProjectZero")) {
        break;
      }
      str1 = str2;
    } while (!this.mBTDevice.getName().equalsIgnoreCase("Project Zero"));
    return "prz_";
  }
  
  public Map<String, String> getMQTTMap()
  {
    return null;
  }
  
  public TableRow getTableRow()
  {
    return this.tRow;
  }
  
  public void grayOutCell(boolean paramBoolean)
  {
    if (paramBoolean == true)
    {
      this.tRow.setAlpha(0.4F);
      return;
    }
    this.tRow.setAlpha(1.0F);
  }
  
  public boolean isDataC(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if (this.dataC == null) {}
    while (!paramBluetoothGattCharacteristic.equals(this.dataC)) {
      return false;
    }
    return true;
  }
  
  public boolean isEnabledByPrefs(String paramString)
  {
    paramString = "pref_" + paramString;
    return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(paramString, Boolean.valueOf(true).booleanValue());
  }
  
  public void onOffWasUpdated(boolean paramBoolean)
  {
    Log.d("GenericBluetoothProfile", "Config characteristic set to :" + paramBoolean);
    if (paramBoolean)
    {
      configureService();
      enableService();
      this.tRow.grayedOut(false);
      this.tRow.servOn = true;
      return;
    }
    deConfigureService();
    disableService();
    this.tRow.grayedOut(true);
    this.tRow.servOn = false;
  }
  
  public void onPause()
  {
    if (this.isRegistered == true)
    {
      this.context.unregisterReceiver(this.guiReceiver);
      this.isRegistered = false;
    }
  }
  
  public void onResume()
  {
    if (!this.isRegistered)
    {
      this.context.registerReceiver(this.guiReceiver, makeFilter());
      this.isRegistered = true;
    }
  }
  
  public void periodWasUpdated(int paramInt)
  {
    int i = paramInt;
    if (paramInt > 2450) {
      i = 2450;
    }
    paramInt = i;
    if (i < 100) {
      paramInt = 100;
    }
    byte b = (byte)(paramInt / 10);
    Log.d("GenericBluetoothProfile", "Period characteristic set to :" + paramInt);
    this.tRow.period = paramInt;
    if (this.dev.writeCharacteristicSync(this.periodC, b) == 0) {}
    for (;;)
    {
      paramInt = this.dev.writeCharacteristicAsync(this.periodC, b);
      if ((paramInt != 0) && (this.periodC != null)) {
        printError("Sensor period failed: ", this.periodC, paramInt);
      }
      return;
      Log.d("GenericBluetoothProfile", "Sensor period failed: " + this.periodC.getUuid().toString());
    }
  }
  
  public void printError(String paramString, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, int paramInt)
  {
    try
    {
      Log.d("GenericBluetoothProfile", paramString + paramBluetoothGattCharacteristic.getUuid().toString() + " Error: " + paramInt);
      return;
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\GenericBluetoothProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */