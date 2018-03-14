package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Html;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TableRow;
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

public class SensorTagMovementProfile
  extends GenericBluetoothProfile
{
  private final BroadcastReceiver guiReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      String str = paramAnonymousIntent.getStringExtra("com.ti.util.EXTRA_SERVICE_UUID");
      if (SensorTagMovementProfile.this.myRow.uuidLabel.getText().toString().compareTo(str) == 0)
      {
        Log.d("GenericBluetoothProfile", "Matched UUID :" + SensorTagMovementProfile.this.tRow.uuidLabel.getText());
        str = paramAnonymousIntent.getStringExtra("com.ti.ble.common.GenericServiceConfigurationDialog.PERIOD");
        boolean bool = paramAnonymousIntent.getBooleanExtra("com.ti.ble.common.GenericServiceConfigurationDialog.SENSOR_STATE", true);
        if (paramAnonymousContext.compareTo("GenericServiceConfigurationDialogFragment.UPDATE") == 0)
        {
          SensorTagMovementProfile.this.onOffWasUpdated(bool);
          paramAnonymousContext = str.replaceAll("[^0-9.]", "");
          SensorTagMovementProfile.this.periodWasUpdated(Integer.parseInt(paramAnonymousContext));
        }
      }
    }
  };
  public SensorTagMovementTableRow myRow;
  
  public SensorTagMovementProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.myRow = new SensorTagMovementTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_MOV_DATA.toString())) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_MOV_CONF.toString())) {
        this.configC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals(SensorTagGatt.UUID_MOV_PERI.toString())) {
        this.periodC = paramBluetoothDevice;
      }
    }
    this.myRow.setIcon(getIconPrefix(), this.dataC.getUuid().toString(), "motion");
    this.myRow.title.setText("Motion Data");
    this.myRow.uuidLabel.setText(this.dataC.getUuid().toString());
    this.myRow.accValue.setText("X:0.00G, Y:0.00G, Z:0.00G");
    this.myRow.gyroValue.setText("X:0.00'/s, Y:0.00'/s, Z:0.00'/s");
    this.myRow.magValue.setText("X:0.00mT, Y:0.00mT, Z:0.00mT");
    paramContext = this.myRow.sl1;
    paramBluetoothDevice = this.myRow.sl2;
    this.myRow.sl3.autoScale = true;
    paramBluetoothDevice.autoScale = true;
    paramContext.autoScale = true;
    paramContext = this.myRow.sl1;
    paramBluetoothDevice = this.myRow.sl2;
    this.myRow.sl3.autoScaleBounceBack = true;
    paramBluetoothDevice.autoScaleBounceBack = true;
    paramContext.autoScaleBounceBack = true;
    this.myRow.WOS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        paramAnonymousCompoundButton = new byte[2];
        CompoundButton tmp5_4 = paramAnonymousCompoundButton;
        tmp5_4[0] = 127;
        CompoundButton tmp10_5 = tmp5_4;
        tmp10_5[1] = 0;
        tmp10_5;
        if (paramAnonymousBoolean) {
          paramAnonymousCompoundButton[0] = -1;
        }
        int i = SensorTagMovementProfile.this.dev.writeCharacteristicAsync(SensorTagMovementProfile.this.configC, paramAnonymousCompoundButton);
        if ((i != 0) && (SensorTagMovementProfile.this.configC != null)) {
          Log.d("SensorTagMovementProfile", "Sensor config failed: " + SensorTagMovementProfile.this.configC.getUuid().toString() + " Error: " + i);
        }
      }
    });
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo(SensorTagGatt.UUID_MOV_SERV.toString()) == 0;
  }
  
  private static IntentFilter makeFilter()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.ti.util.ACTION_PERIOD_UPDATE");
    localIntentFilter.addAction("com.ti.util.ACTION_ONOFF_UPDATE");
    localIntentFilter.addAction("com.ti.util.ACTION_CALIBRATE");
    localIntentFilter.addAction("GenericServiceConfigurationDialogFragment.UPDATE");
    return localIntentFilter;
  }
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    if (paramBluetoothGattCharacteristic.equals(this.dataC))
    {
      paramBluetoothGattCharacteristic = Sensor.MOVEMENT_ACC.convert(arrayOfByte);
      if (!this.myRow.config) {
        this.myRow.accValue.setText(Html.fromHtml(String.format("<font color=#FF0000>X:%.2fG</font>, <font color=#00967D>Y:%.2fG</font>, <font color=#00000>Z:%.2fG</font>", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x), Double.valueOf(paramBluetoothGattCharacteristic.y), Double.valueOf(paramBluetoothGattCharacteristic.z) })));
      }
      this.myRow.sl1.addValue((float)paramBluetoothGattCharacteristic.x, 0);
      this.myRow.sl1.addValue((float)paramBluetoothGattCharacteristic.y, 1);
      this.myRow.sl1.addValue((float)paramBluetoothGattCharacteristic.z, 2);
      paramBluetoothGattCharacteristic = Sensor.MOVEMENT_GYRO.convert(arrayOfByte);
      this.myRow.gyroValue.setText(Html.fromHtml(String.format("<font color=#FF0000>X:%.2f°/s</font>, <font color=#00967D>Y:%.2f°/s</font>, <font color=#00000>Z:%.2f°/s</font>", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x), Double.valueOf(paramBluetoothGattCharacteristic.y), Double.valueOf(paramBluetoothGattCharacteristic.z) })));
      this.myRow.sl2.addValue((float)paramBluetoothGattCharacteristic.x, 0);
      this.myRow.sl2.addValue((float)paramBluetoothGattCharacteristic.y, 1);
      this.myRow.sl2.addValue((float)paramBluetoothGattCharacteristic.z, 2);
      paramBluetoothGattCharacteristic = Sensor.MOVEMENT_MAG.convert(arrayOfByte);
      this.myRow.magValue.setText(Html.fromHtml(String.format("<font color=#FF0000>X:%.2fuT</font>, <font color=#00967D>Y:%.2fuT</font>, <font color=#00000>Z:%.2fuT</font>", new Object[] { Double.valueOf(paramBluetoothGattCharacteristic.x), Double.valueOf(paramBluetoothGattCharacteristic.y), Double.valueOf(paramBluetoothGattCharacteristic.z) })));
      this.myRow.sl3.addValue((float)paramBluetoothGattCharacteristic.x, 0);
      this.myRow.sl3.addValue((float)paramBluetoothGattCharacteristic.y, 1);
      this.myRow.sl3.addValue((float)paramBluetoothGattCharacteristic.z, 1);
    }
  }
  
  public void didWriteValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void disableService()
  {
    int i = this.dev.writeCharacteristicAsync(this.configC, new byte[] { 0, 0 });
    if ((i != 0) && (this.configC != null)) {
      Log.d("SensorTagMovementProfile", "Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    i = this.dev.setCharacteristicNotificationSync(this.dataC, false);
    if ((i != 0) && (this.dataC != null)) {
      Log.d("SensorTagMovementProfile", "Sensor notification disable failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    this.isEnabled = false;
    this.myRow.servOn = false;
  }
  
  public void enableService()
  {
    byte[] arrayOfByte = new byte[2];
    byte[] tmp5_4 = arrayOfByte;
    tmp5_4[0] = 127;
    byte[] tmp11_5 = tmp5_4;
    tmp11_5[1] = 0;
    tmp11_5;
    if (this.myRow.WOS.isChecked()) {
      arrayOfByte[0] = -1;
    }
    int i = this.dev.writeCharacteristicAsync(this.configC, arrayOfByte);
    if ((i != 0) && (this.configC != null)) {
      Log.d("SensorTagMovementProfile", "Sensor config failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    i = this.dev.setCharacteristicNotificationSync(this.dataC, true);
    if ((i != 0) && (this.dataC != null)) {
      Log.d("SensorTagMovementProfile", "Sensor notification enable failed: " + this.configC.getUuid().toString() + " Error: " + i);
    }
    periodWasUpdated(this.myRow.period);
    this.isEnabled = true;
    this.myRow.servOn = true;
  }
  
  public Map<String, String> getMQTTMap()
  {
    Point3D localPoint3D = Sensor.MOVEMENT_ACC.convert(this.dataC.getValue());
    HashMap localHashMap = new HashMap();
    localHashMap.put("acc_x", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
    localHashMap.put("acc_y", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.y) }));
    localHashMap.put("acc_z", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.z) }));
    localPoint3D = Sensor.MOVEMENT_GYRO.convert(this.dataC.getValue());
    localHashMap.put("gyro_x", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
    localHashMap.put("gyro_y", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.y) }));
    localHashMap.put("gyro_z", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.z) }));
    localPoint3D = Sensor.MOVEMENT_MAG.convert(this.dataC.getValue());
    localHashMap.put("compass_x", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.x) }));
    localHashMap.put("compass_y", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.y) }));
    localHashMap.put("compass_z", String.format("%.2f", new Object[] { Double.valueOf(localPoint3D.z) }));
    return localHashMap;
  }
  
  public TableRow getTableRow()
  {
    return this.myRow;
  }
  
  public void grayOutCell(boolean paramBoolean)
  {
    if (paramBoolean == true)
    {
      this.tRow.setAlpha(0.4F);
      return;
    }
    this.tRow.setAlpha(1.0F);
  }
  
  public void onOffWasUpdated(boolean paramBoolean)
  {
    Log.d("GenericBluetoothProfile", "Config characteristic set to :" + paramBoolean);
    if (paramBoolean)
    {
      configureService();
      enableService();
      this.myRow.servOn = true;
      return;
    }
    deConfigureService();
    disableService();
    this.myRow.servOn = false;
  }
  
  public void onPause()
  {
    if (this.isRegistered == true)
    {
      this.context.unregisterReceiver(this.guiReceiver);
      this.isRegistered = false;
    }
  }
  
  public void onResume()
  {
    if (!this.isRegistered)
    {
      this.context.registerReceiver(this.guiReceiver, makeFilter());
      this.isRegistered = true;
    }
  }
  
  public void periodWasUpdated(int paramInt)
  {
    int i = paramInt;
    if (paramInt > 2450) {
      i = 2450;
    }
    paramInt = i;
    if (i < 100) {
      paramInt = 100;
    }
    byte b = (byte)(paramInt / 10);
    Log.d("GenericBluetoothProfile", "Period characteristic set to :" + paramInt);
    this.myRow.period = paramInt;
    if (this.dev.writeCharacteristicSync(this.periodC, b) == 0) {}
    for (;;)
    {
      paramInt = this.dev.writeCharacteristicAsync(this.periodC, b);
      if ((paramInt != 0) && (this.periodC != null)) {
        printError("Sensor period failed: ", this.periodC, paramInt);
      }
      return;
      Log.d("GenericBluetoothProfile", "Sensor period failed: " + this.periodC.getUuid().toString());
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagMovementProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */