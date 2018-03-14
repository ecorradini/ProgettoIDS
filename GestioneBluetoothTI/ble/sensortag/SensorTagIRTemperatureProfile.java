package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.TextView;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.GenericCharacteristicTableRow;
import com.ti.util.Point3D;
import com.ti.util.TrippleSparkLineView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SensorTagIRTemperatureProfile
  extends GenericBluetoothProfile
{
  public SensorTagIRTemperatureProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new GenericCharacteristicTableRow(paramContext, 1000, true);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_IRT_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_IRT_CONF.toString())) {
        this.configC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_IRT_PERI.toString())) {
        this.periodC = paramBluetoothDevice;
      }
    }
    this.tRow.sl1.autoScale = true;
    this.tRow.sl1.autoScaleBounceBack = true;
    this.tRow.sl1.setColor(0, 0, 0, 0, 1);
    this.tRow.sl1.setColor(0, 0, 0, 0, 2);
    this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString());
    this.tRow.title.setText("IR Temperature Data");
    this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
    this.tRow.value.setText("0.0'C");
    this.tRow.periodMinVal = 200;
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_IRT_SERV.toString()) == 0;
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      if ((!this.mBTDevice.getName().equals("CC2650 SensorTag")) && (!this.mBTDevice.getName().equals("CC1350 SensorTag"))) {
        break label149;
      }
      paramBluetoothGattCharacteristic = Sensor.IR_TEMPERATURE.convert(arrayOfByte);
      if (isEnabledByPrefs("imperial") != true) {
        break label117;
      }
      this.tRow.value.setText(String.format("%.1f°F", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.z * 1.8D + 32.0D) }));
    }
    for (;;)
    {
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.z);
      return;
      label117:
      this.tRow.value.setText(String.format("%.1f°C", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.z) }));
    }
    label149:
    paramBluetoothGattCharacteristic = Sensor.IR_TEMPERATURE.convert(arrayOfByte);
    if (!this.tRow.config)
    {
      if (isEnabledByPrefs("imperial") != true) {
        break label230;
      }
      this.tRow.value.setText(String.format("%.1f°F", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.y * 1.8D + 32.0D) }));
    }
    for (;;)
    {
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.y);
      return;
      label230:
      this.tRow.value.setText(String.format("%.1f°C", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.y) }));
    }
  }
  
  public Map<String, String> getMQTTMap()
  {
    Point3D localPoint3D = Sensor.IR_TEMPERATURE.convert(this.dataC.getValue());
    HashMap localHashMap = new HashMap();
    if ((this.mBTDevice.getName().equals("CC2650 SensorTag")) || (this.mBTDevice.getName().equals("CC1350 SensorTag")))
    {
      localHashMap.put("object_temp", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.z) }));
      return localHashMap;
    }
    localHashMap.put("object_temp", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.y) }));
    return localHashMap;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagIRTemperatureProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */