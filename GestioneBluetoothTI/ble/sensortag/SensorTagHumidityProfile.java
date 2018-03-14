package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
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

public class SensorTagHumidityProfile
  extends GenericBluetoothProfile
{
  public SensorTagHumidityProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new GenericCharacteristicTableRow(paramContext, 1000, true);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_HUM_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_HUM_CONF.toString())) {
        this.configC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_HUM_PERI.toString())) {
        this.periodC = paramBluetoothDevice;
      }
    }
    this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "humidity");
    this.tRow.title.setText("Humidity Data");
    this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
    this.tRow.value.setText("0.0%rH");
    this.tRow.sl1.maxVal(Float.valueOf(100.0F));
    this.tRow.sl1.minVal(Float.valueOf(0.0F));
    this.tRow.sl1.setColor(0, 0, 0, 0, 1);
    this.tRow.sl1.setColor(0, 0, 0, 0, 2);
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    if (paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_HUM_SERV.toString()) == 0)
    {
      Log.d("Test", "Match !");
      return true;
    }
    return false;
  }
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    if (paramBluetoothGattCharacteristic.equals(this.dataC)) {
      if (!SensorTagUtil.isSensorTag2(this.mBTDevice)) {
        break label89;
      }
    }
    label89:
    for (paramBluetoothGattCharacteristic = Sensor.HUMIDITY2.convert(arrayOfByte);; paramBluetoothGattCharacteristic = Sensor.HUMIDITY.convert(arrayOfByte))
    {
      if (!this.tRow.config) {
        this.tRow.value.setText(String.format("%.1f %%rH", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x) }));
      }
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.x);
      return;
    }
  }
  
  public void didWriteValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public Map<String, String> getMQTTMap()
  {
    if (SensorTagUtil.isSensorTag2(this.mBTDevice)) {}
    for (Point3D localPoint3D = Sensor.HUMIDITY2.convert(this.dataC.getValue());; localPoint3D = Sensor.HUMIDITY.convert(this.dataC.getValue()))
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("humidity", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
      return localHashMap;
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagHumidityProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */