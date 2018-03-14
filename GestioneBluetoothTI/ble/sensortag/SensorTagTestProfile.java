package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import com.ti.ble.common.GenericBluetoothProfile;
import java.util.UUID;

public class SensorTagTestProfile
  extends GenericBluetoothProfile
{
  public SensorTagTestProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_TST_SERV.toString()) == 0;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void disableService() {}
  
  public void enableService() {}
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagTestProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */