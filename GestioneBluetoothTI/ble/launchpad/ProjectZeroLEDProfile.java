package com.ti.ble.launchpad;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProjectZeroLEDProfile
  extends GenericBluetoothProfile
{
  static final String PRZ_LED0_STATE_CHARACTERISIC_UUID = "f0001111-0451-4000-b000-000000000000";
  static final String PRZ_LED1_STATE_CHARACTERISIC_UUID = "f0001112-0451-4000-b000-000000000000";
  static final String PRZ_LED_SERVICE_UUID = "f0001110-0451-4000-b000-000000000000";
  BluetoothGattCharacteristic led0C;
  boolean led0On;
  BluetoothGattCharacteristic led1C;
  boolean led1On;
  ProjectZeroLEDTableRow pTr;
  
  public ProjectZeroLEDProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.pTr = new ProjectZeroLEDTableRow(paramContext);
    this.pTr.greenLed.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        int i = 1;
        paramAnonymousView = ProjectZeroLEDProfile.this;
        boolean bool;
        BluetoothGattCharacteristic localBluetoothGattCharacteristic;
        if (!ProjectZeroLEDProfile.this.led1On)
        {
          bool = true;
          paramAnonymousView.led1On = bool;
          paramAnonymousView = ProjectZeroLEDProfile.this.dev;
          localBluetoothGattCharacteristic = ProjectZeroLEDProfile.this.led1C;
          if (ProjectZeroLEDProfile.this.led1On != true) {
            break label74;
          }
        }
        for (;;)
        {
          paramAnonymousView.writeCharacteristicSync(localBluetoothGattCharacteristic, (byte)i);
          ProjectZeroLEDProfile.this.setPictureForLeds();
          return;
          bool = false;
          break;
          label74:
          i = 0;
        }
      }
    });
    this.pTr.redLed.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        int i = 1;
        paramAnonymousView = ProjectZeroLEDProfile.this;
        boolean bool;
        BluetoothGattCharacteristic localBluetoothGattCharacteristic;
        if (!ProjectZeroLEDProfile.this.led0On)
        {
          bool = true;
          paramAnonymousView.led0On = bool;
          paramAnonymousView = ProjectZeroLEDProfile.this.dev;
          localBluetoothGattCharacteristic = ProjectZeroLEDProfile.this.led0C;
          if (ProjectZeroLEDProfile.this.led0On != true) {
            break label74;
          }
        }
        for (;;)
        {
          paramAnonymousView.writeCharacteristicSync(localBluetoothGattCharacteristic, (byte)i);
          ProjectZeroLEDProfile.this.setPictureForLeds();
          return;
          bool = false;
          break;
          label74:
          i = 0;
        }
      }
    });
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equalsIgnoreCase("f0001111-0451-4000-b000-000000000000")) {
        this.led0C = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equalsIgnoreCase("f0001112-0451-4000-b000-000000000000")) {
        this.led1C = paramBluetoothDevice;
      }
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareToIgnoreCase("f0001110-0451-4000-b000-000000000000") == 0;
  }
  
  public void configureService()
  {
    this.dev.readCharacteristicAsync(this.led0C);
    this.dev.readCharacteristicAsync(this.led1C);
  }
  
  public void deConfigureService() {}
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if (paramBluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.led0C.getUuid().toString())) {
      if (paramBluetoothGattCharacteristic.getValue()[0] != 0)
      {
        this.led0On = true;
        setPictureForLeds();
      }
    }
    while (!paramBluetoothGattCharacteristic.getUuid().toString().equalsIgnoreCase(this.led1C.getUuid().toString())) {
      for (;;)
      {
        return;
        this.led0On = false;
      }
    }
    if (paramBluetoothGattCharacteristic.getValue()[0] != 0) {}
    for (this.led1On = true;; this.led1On = false)
    {
      setPictureForLeds();
      return;
    }
  }
  
  public void disableService() {}
  
  public void enableService() {}
  
  public Map<String, String> getMQTTMap()
  {
    int j = 1;
    HashMap localHashMap = new HashMap();
    if (this.led0On)
    {
      i = 1;
      localHashMap.put("prz_red_led", String.format("%d", new Object[] { Integer.valueOf(i) }));
      if (!this.led1On) {
        break label85;
      }
    }
    label85:
    for (int i = j;; i = 0)
    {
      localHashMap.put("prz_green_led", String.format("%d", new Object[] { Integer.valueOf(i) }));
      return localHashMap;
      i = 0;
      break;
    }
  }
  
  public TableRow getTableRow()
  {
    return this.pTr;
  }
  
  public void setPictureForLeds()
  {
    if (this.led0On) {
      this.pTr.redLed.setImageResource(2131492911);
    }
    while (this.led1On)
    {
      this.pTr.greenLed.setImageResource(2131492908);
      return;
      this.pTr.redLed.setImageResource(2131492910);
    }
    this.pTr.greenLed.setImageResource(2131492907);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\launchpad\ProjectZeroLEDProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */