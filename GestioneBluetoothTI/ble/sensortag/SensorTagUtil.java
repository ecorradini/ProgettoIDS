package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;

public class SensorTagUtil
{
  public static boolean isSensorTag2(BluetoothDevice paramBluetoothDevice)
  {
    if (paramBluetoothDevice != null)
    {
      paramBluetoothDevice = paramBluetoothDevice.getName();
      if (paramBluetoothDevice.compareTo("SensorTag2") == 0) {}
      while ((paramBluetoothDevice.compareTo("SensorTag2.0") == 0) || (paramBluetoothDevice.compareTo("CC2650 SensorTag") == 0) || (paramBluetoothDevice.compareTo("CC2650 SensorTag LED") == 0)) {
        return true;
      }
    }
    return false;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */