package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TableRow;
import com.ti.ble.common.GenericBluetoothProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SensorTagSimpleKeysProfile
  extends GenericBluetoothProfile
{
  SensorTagSimpleKeysTableRow myRow;
  
  public SensorTagSimpleKeysProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.myRow = new SensorTagSimpleKeysTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_KEY_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
    }
    paramContext = (ImageView)this.myRow.findViewById(2131231046);
    if ((this.mBTDevice.getName().equals("CC2650 LaunchPad")) || (this.mBTDevice.getName().equals("CC1350 LaunchPad"))) {
      paramContext.setImageResource(2131492870);
    }
    if (!this.mBTDevice.getName().equals("CC2650 SensorTag")) {
      this.myRow.reedStateImage.setVisibility(4);
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_KEY_SERV.toString()) == 0;
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      paramBluetoothGattCharacteristic = paramBluetoothGattCharacteristic.getValue();
      switch (paramBluetoothGattCharacteristic[0])
      {
      default: 
        this.myRow.leftKeyPressStateImage.setImageResource(2131492899);
        this.myRow.rightKeyPressStateImage.setImageResource(2131492919);
        this.myRow.reedStateImage.setImageResource(2131492917);
      }
    }
    for (;;)
    {
      this.myRow.lastKeys = paramBluetoothGattCharacteristic[0];
      return;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492900);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492919);
      this.myRow.reedStateImage.setImageResource(2131492917);
      continue;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492899);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492920);
      this.myRow.reedStateImage.setImageResource(2131492917);
      continue;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492900);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492920);
      this.myRow.reedStateImage.setImageResource(2131492917);
      continue;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492899);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492919);
      this.myRow.reedStateImage.setImageResource(2131492918);
      continue;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492900);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492919);
      this.myRow.reedStateImage.setImageResource(2131492918);
      continue;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492899);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492920);
      this.myRow.reedStateImage.setImageResource(2131492918);
      continue;
      this.myRow.leftKeyPressStateImage.setImageResource(2131492900);
      this.myRow.rightKeyPressStateImage.setImageResource(2131492920);
      this.myRow.reedStateImage.setImageResource(2131492918);
    }
  }
  
  public void disableService()
  {
    this.isEnabled = false;
  }
  
  public void enableService()
  {
    this.isEnabled = true;
  }
  
  public Map<String, String> getMQTTMap()
  {
    byte[] arrayOfByte = this.dataC.getValue();
    HashMap localHashMap = new HashMap();
    if (arrayOfByte != null)
    {
      localHashMap.put("key_1", String.format("%d", new Object[] { Integer.valueOf(arrayOfByte[0] & 0x1) }));
      localHashMap.put("key_2", String.format("%d", new Object[] { Integer.valueOf(arrayOfByte[0] & 0x2) }));
      localHashMap.put("reed_relay", String.format("%d", new Object[] { Integer.valueOf(arrayOfByte[0] & 0x4) }));
      return localHashMap;
    }
    localHashMap.put("key_1", "0");
    localHashMap.put("key_2", "0");
    localHashMap.put("reed_relay", "0");
    return localHashMap;
  }
  
  public TableRow getTableRow()
  {
    return this.myRow;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagSimpleKeysProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */