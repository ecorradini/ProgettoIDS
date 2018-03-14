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

public class SensorTagLuxometerProfile
  extends GenericBluetoothProfile
{
  public SensorTagLuxometerProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new GenericCharacteristicTableRow(paramContext, 1000, true);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_OPT_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_OPT_CONF.toString())) {
        this.configC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_OPT_PERI.toString())) {
        this.periodC = paramBluetoothDevice;
      }
    }
    this.tRow.sl1.autoScale = true;
    this.tRow.sl1.autoScaleBounceBack = true;
    this.tRow.sl1.setColor(0, 0, 0, 0, 1);
    this.tRow.sl1.setColor(0, 0, 0, 0, 2);
    this.tRow.sl1.setColor(255, 0, 150, 125);
    this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "lightsensor");
    this.tRow.title.setText("Light Sensor Data");
    this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
    this.tRow.value.setText("0.0 Lux");
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_OPT_SERV.toString()) == 0;
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      paramBluetoothGattCharacteristic = Sensor.LUXOMETER.convert(arrayOfByte);
      if (!this.tRow.config) {
        this.tRow.value.setText(String.format("%.1f Lux", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x) }));
      }
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.x);
    }
  }
  
  public Map<String, String> getMQTTMap()
  {
    Point3D localPoint3D = Sensor.LUXOMETER.convert(this.dataC.getValue());
    HashMap localHashMap = new HashMap();
    localHashMap.put("light", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
    return localHashMap;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagLuxometerProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */