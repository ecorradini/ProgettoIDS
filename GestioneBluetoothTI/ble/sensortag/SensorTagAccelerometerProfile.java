package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.TextView;
import com.ti.ble.common.GattInfo;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.GenericCharacteristicTableRow;
import com.ti.util.Point3D;
import com.ti.util.TrippleSparkLineView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SensorTagAccelerometerProfile
  extends GenericBluetoothProfile
{
  public SensorTagAccelerometerProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new GenericCharacteristicTableRow(paramContext, 1000, true);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_ACC_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_ACC_CONF.toString())) {
        this.configC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_ACC_PERI.toString())) {
        this.periodC = paramBluetoothDevice;
      }
    }
    this.tRow.sl1.autoScale = true;
    this.tRow.sl1.autoScaleBounceBack = true;
    this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString());
    this.tRow.title.setText(GattInfo.uuidToName(UUID.fromString(this.dataC.getUuid().toString())));
    this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
    this.tRow.value.setText("X:0.00G, Y:0.00G, Z:0.00G");
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_ACC_SERV.toString()) == 0;
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      paramBluetoothGattCharacteristic = Sensor.ACCELEROMETER.convert(this.dataC.getValue());
      if (!this.tRow.config) {
        this.tRow.value.setText(String.format("X:%.2fG, Y:%.2fG, Z:%.2fG", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x), Double.valueOf(paramBluetoothGattCharacteristic.y), Double.valueOf(paramBluetoothGattCharacteristic.z) }));
      }
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.x);
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.y, 1);
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.z, 2);
    }
  }
  
  public Map<String, String> getMQTTMap()
  {
    Point3D localPoint3D = Sensor.ACCELEROMETER.convert(this.dataC.getValue());
    HashMap localHashMap = new HashMap();
    localHashMap.put("acc_x", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
    localHashMap.put("acc_y", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.y) }));
    localHashMap.put("acc_z", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.z) }));
    return localHashMap;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagAccelerometerProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */