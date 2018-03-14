package com.ti.ble.launchpad;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.sensortag.SensorTagSimpleKeysTableRow;
import com.ti.util.TrippleSparkLineView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProjectZeroSwitchProfile
  extends GenericBluetoothProfile
{
  static final String PRZ_SW0_STATE_CHARACTERISIC_UUID = "f0001121-0451-4000-b000-000000000000";
  static final String PRZ_SW1_STATE_CHARACTERISIC_UUID = "f0001122-0451-4000-b000-000000000000";
  static final String PRZ_SW_SERVICE_UUID = "f0001120-0451-4000-b000-000000000000";
  ImageView leftSW;
  boolean leftSWOn;
  SensorTagSimpleKeysTableRow myRow;
  ImageView rightSW;
  boolean rightSWOn;
  BluetoothGattCharacteristic sw0C;
  BluetoothGattCharacteristic sw1C;
  
  public ProjectZeroSwitchProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.myRow = new SensorTagSimpleKeysTableRow(paramContext);
    ((ImageView)this.myRow.getChildAt(0).findViewById(2131231046)).setImageResource(2131492906);
    ((TrippleSparkLineView)this.myRow.getChildAt(0).findViewById(2131231050)).setColor(0, 0, 0, 0, 2);
    this.rightSW = ((ImageView)this.myRow.getChildAt(0).findViewById(2131231048));
    this.leftSW = ((ImageView)this.myRow.getChildAt(0).findViewById(2131231049));
    ((ImageView)this.myRow.getChildAt(0).findViewById(2131231047)).setVisibility(4);
    this.rightSW.setImageResource(2131492901);
    this.leftSW.setImageResource(2131492901);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equalsIgnoreCase("f0001121-0451-4000-b000-000000000000")) {
        this.sw0C = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equalsIgnoreCase("f0001122-0451-4000-b000-000000000000")) {
        this.sw1C = paramBluetoothDevice;
      }
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareToIgnoreCase("f0001120-0451-4000-b000-000000000000") == 0;
  }
  
  public void configureService()
  {
    if (this.sw0C != null) {
      this.dev.setCharacteristicNotificationSync(this.sw0C, true);
    }
    if (this.sw1C != null) {
      this.dev.setCharacteristicNotificationSync(this.sw1C, true);
    }
  }
  
  public void deConfigureService()
  {
    if (this.sw0C != null) {
      this.dev.setCharacteristicNotificationSync(this.sw0C, false);
    }
    if (this.sw1C != null) {
      this.dev.setCharacteristicNotificationSync(this.sw1C, false);
    }
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    boolean bool2 = true;
    boolean bool1 = true;
    if (paramBluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase("f0001121-0451-4000-b000-000000000000")) {
      if (paramBluetoothGattCharacteristic.getValue()[0] != 0) {
        this.leftSWOn = bool1;
      }
    }
    while (!paramBluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase("f0001122-0451-4000-b000-000000000000")) {
      for (;;)
      {
        updateGUI();
        return;
        bool1 = false;
      }
    }
    if (paramBluetoothGattCharacteristic.getValue()[0] != 0) {}
    for (bool1 = bool2;; bool1 = false)
    {
      this.rightSWOn = bool1;
      break;
    }
  }
  
  public void disableService() {}
  
  public void enableService() {}
  
  public Map<String, String> getMQTTMap()
  {
    int j = 1;
    HashMap localHashMap = new HashMap();
    if (this.leftSWOn)
    {
      i = 1;
      localHashMap.put("prz_sw_0", String.format("%d", new Object[] { Integer.valueOf(i) }));
      if (!this.rightSWOn) {
        break label85;
      }
    }
    label85:
    for (int i = j;; i = 0)
    {
      localHashMap.put("prz_sw_1", String.format("%d", new Object[] { Integer.valueOf(i) }));
      return localHashMap;
      i = 0;
      break;
    }
  }
  
  public TableRow getTableRow()
  {
    return this.myRow;
  }
  
  public void updateGUI()
  {
    this.myRow.lastKeys = 0;
    SensorTagSimpleKeysTableRow localSensorTagSimpleKeysTableRow;
    if (this.rightSWOn)
    {
      localSensorTagSimpleKeysTableRow = this.myRow;
      localSensorTagSimpleKeysTableRow.lastKeys = ((byte)(localSensorTagSimpleKeysTableRow.lastKeys | 0x1));
      this.rightSW.setImageResource(2131492902);
    }
    while (this.leftSWOn)
    {
      localSensorTagSimpleKeysTableRow = this.myRow;
      localSensorTagSimpleKeysTableRow.lastKeys = ((byte)(localSensorTagSimpleKeysTableRow.lastKeys | 0x2));
      this.leftSW.setImageResource(2131492902);
      return;
      this.rightSW.setImageResource(2131492901);
    }
    this.leftSW.setImageResource(2131492901);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\launchpad\ProjectZeroSwitchProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */