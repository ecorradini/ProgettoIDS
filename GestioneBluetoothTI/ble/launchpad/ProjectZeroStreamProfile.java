package com.ti.ble.launchpad;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.TableRow;
import com.ti.ble.common.GenericBluetoothProfile;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ProjectZeroStreamProfile
  extends GenericBluetoothProfile
{
  static final String PRZ_DATA_SERVICE_UUID = "f0001130-0451-4000-b000-000000000000";
  static final String PRZ_STREAM_CHARACTERISIC_UUID = "f0001132-0451-4000-b000-000000000000";
  static final String PRZ_STRING_RW_CHARACTERISIC_UUID = "f0001131-0451-4000-b000-000000000000";
  ProjectZeroLEDTableRow pTr;
  BluetoothGattCharacteristic streamC;
  BluetoothGattCharacteristic stringRWC;
  
  public ProjectZeroStreamProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.pTr = new ProjectZeroLEDTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equalsIgnoreCase("f0001131-0451-4000-b000-000000000000")) {
        this.stringRWC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equalsIgnoreCase("f0001132-0451-4000-b000-000000000000")) {
        this.streamC = paramBluetoothDevice;
      }
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareToIgnoreCase("f0001130-0451-4000-b000-000000000000") == 0;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void disableService() {}
  
  public void enableService() {}
  
  public TableRow getTableRow()
  {
    return this.pTr;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\launchpad\ProjectZeroStreamProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */