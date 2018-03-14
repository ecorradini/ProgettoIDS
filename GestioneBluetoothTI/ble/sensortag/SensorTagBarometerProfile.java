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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SensorTagBarometerProfile
  extends GenericBluetoothProfile
{
  private static final double PA_PER_METER = 12.0D;
  private BluetoothGattCharacteristic calibC;
  private boolean isCalibrated;
  private boolean isHeightCalibrated;
  
  public SensorTagBarometerProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new SensorTagBarometerTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_BAR_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_BAR_CONF.toString())) {
        this.configC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_BAR_PERI.toString())) {
        this.periodC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_BAR_CALI.toString())) {
        this.calibC = paramBluetoothDevice;
      }
    }
    if ((this.mBTDevice.getName().equals("CC2650 SensorTag")) || (this.mBTDevice.getName().equals("CC1350 SensorTag"))) {}
    for (this.isCalibrated = true;; this.isCalibrated = false)
    {
      this.isHeightCalibrated = false;
      this.tRow.sl1.autoScale = true;
      this.tRow.sl1.autoScaleBounceBack = true;
      this.tRow.sl1.setColor(0, 0, 0, 0, 2);
      this.tRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "barometer");
      this.tRow.title.setText("Barometer Data");
      this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
      this.tRow.value.setText("0.0mBar, 0.0m");
      return;
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_BAR_SERV.toString()) == 0;
  }
  
  protected void calibrationButtonTouched()
  {
    this.isHeightCalibrated = false;
  }
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if ((this.calibC != null) && (this.calibC.equals(paramBluetoothGattCharacteristic)))
    {
      paramBluetoothGattCharacteristic = paramBluetoothGattCharacteristic.getValue();
      if (paramBluetoothGattCharacteristic.length == 16) {
        break label31;
      }
    }
    label31:
    int i;
    do
    {
      return;
      ArrayList localArrayList = new ArrayList();
      i = 0;
      int j;
      while (i < 8)
      {
        j = paramBluetoothGattCharacteristic[i];
        localArrayList.add(Integer.valueOf((Integer.valueOf(paramBluetoothGattCharacteristic[(i + 1)] & 0xFF).intValue() << 8) + Integer.valueOf(j & 0xFF).intValue()));
        i += 2;
      }
      i = 8;
      while (i < 16)
      {
        j = paramBluetoothGattCharacteristic[i];
        localArrayList.add(Integer.valueOf((Integer.valueOf(paramBluetoothGattCharacteristic[(i + 1)]).intValue() << 8) + Integer.valueOf(j & 0xFF).intValue()));
        i += 2;
      }
      Log.d("SensorTagBarometerProfile", "Barometer calibrated !!!!!");
      BarometerCalibrationCoefficients.INSTANCE.barometerCalibrationCoefficients = localArrayList;
      this.isCalibrated = true;
      i = this.dev.writeCharacteristicAsync(this.configC, (byte)1);
      if ((i != 0) && (this.configC != null)) {
        Log.d("SensorTagBarometerProfile", "Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + i);
      }
      i = this.dev.setCharacteristicNotificationSync(this.dataC, true);
    } while ((i == 0) || (this.dataC == null));
    Log.d("SensorTagBarometerProfile", "Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + i);
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      paramBluetoothGattCharacteristic = Sensor.BAROMETER.convert(arrayOfByte);
      if (!this.isHeightCalibrated)
      {
        BarometerCalibrationCoefficients.INSTANCE.heightCalibration = paramBluetoothGattCharacteristic.x;
        this.isHeightCalibrated = true;
      }
      double d = Math.round(-((paramBluetoothGattCharacteristic.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / 12.0D) * 10.0D) / 10.0D;
      if (!this.tRow.config) {
        this.tRow.value.setText(String.format("%.1f mBar %.1f meter", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x / 100.0D), Double.valueOf(d) }));
      }
      this.tRow.sl1.addValue((float)paramBluetoothGattCharacteristic.x / 100.0F);
      this.tRow.sl1.addValue((float)d, 1);
    }
  }
  
  public void enableService()
  {
    if (!this.isCalibrated)
    {
      this.dev.writeCharacteristicSync(this.configC, (byte)2);
      this.dev.readCharacteristicSync(this.calibC);
    }
    for (;;)
    {
      this.isEnabled = true;
      return;
      int i = this.dev.writeCharacteristicAsync(this.configC, (byte)1);
      if ((i != 0) && (this.configC != null)) {
        Log.d("SensorTagBarometerProfile", "Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + i);
      }
      i = this.dev.setCharacteristicNotificationSync(this.dataC, true);
      if ((i != 0) && (this.dataC != null)) {
        Log.d("SensorTagBarometerProfile", "Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + i);
      }
    }
  }
  
  public Map<String, String> getMQTTMap()
  {
    Point3D localPoint3D = Sensor.BAROMETER.convert(this.dataC.getValue());
    HashMap localHashMap = new HashMap();
    localHashMap.put("air_pressure", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x / 100.0D) }));
    return localHashMap;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagBarometerProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */