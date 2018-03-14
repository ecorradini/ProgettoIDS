package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.GenericCharacteristicTableRow;
import com.ti.util.Point3D;
import com.ti.util.TrippleSparkLineView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SensorTagAmbientTemperatureProfile
  extends GenericBluetoothProfile
{
  public SensorTagAmbientTemperatureProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
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
    this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "temperature");
    this.tRow.title.setText("Ambient Temperature Data");
    this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
    this.tRow.value.setText("0.0'C");
    this.tRow.periodMinVal = 200;
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_IRT_SERV.toString()) == 0;
  }
  
  public void configureService()
  {
    int i = this.dev.writeCharacteristicAsync(this.configC, (byte)1);
    if ((i != 0) && (this.configC != null)) {
      Log.d("SensorTagAmbientTemperatureProfile", "Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    i = this.dev.setCharacteristicNotificationSync(this.dataC, true);
    if ((i != 0) && (this.dataC != null)) {
      Log.d("SensorTagAmbientTemperatureProfile", "Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    this.tRow.periodMinVal = 200;
    this.isConfigured = true;
  }
  
  public void deConfigureService()
  {
    int i = this.dev.writeCharacteristicAsync(this.configC, (byte)0);
    if ((i != 0) && (this.configC != null)) {
      Log.d("SensorTagAmbientTemperatureProfile", "Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    i = this.dev.setCharacteristicNotificationSync(this.dataC, false);
    if ((i != 0) && (this.dataC != null)) {
      Log.d("SensorTagAmbientTemperatureProfile", "Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    this.isConfigured = false;
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      paramBluetoothGattCharacteristic = Sensor.IR_TEMPERATURE.convert(arrayOfByte);
      if (!this.tRow.config)
      {
        if (isEnabledByPrefs("imperial") != true) {
          break label97;
        }
        this.tRow.value.setText(String.format("%.1f°F", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x * 1.8D + 32.0D) }));
      }
    }
    for (;;)
    {
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.x);
      return;
      label97:
      this.tRow.value.setText(String.format("%.1f°C", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x) }));
    }
  }
  
  public Map<String, String> getMQTTMap()
  {
    Point3D localPoint3D = Sensor.IR_TEMPERATURE.convert(this.dataC.getValue());
    HashMap localHashMap = new HashMap();
    localHashMap.put("ambient_temp", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
    return localHashMap;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagAmbientTemperatureProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */