package com.ti.ble.sensortag;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableRow;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class SensorTagDisplayProfile
  extends GenericBluetoothProfile
{
  public static final String TI_SENSORTAG_TWO_DISPLAY_CONTROL_UUID = "f000ad02-0451-4000-b000-000000000000";
  public static final String TI_SENSORTAG_TWO_DISPLAY_DATA_UUID = "f000ad01-0451-4000-b000-000000000000";
  public static final String TI_SENSORTAG_TWO_DISPLAY_SERVICE_UUID = "f000ad00-0451-4000-b000-000000000000";
  SensorTagDisplayTableRow cRow;
  Timer displayClock;
  
  public SensorTagDisplayProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.cRow = new SensorTagDisplayTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals("f000ad01-0451-4000-b000-000000000000")) {
        this.dataC = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals("f000ad02-0451-4000-b000-000000000000")) {
        this.configC = paramBluetoothDevice;
      }
    }
    this.cRow.displayClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          if (SensorTagDisplayProfile.this.displayClock != null) {
            SensorTagDisplayProfile.this.displayClock.cancel();
          }
          SensorTagDisplayProfile.this.displayClock = new Timer();
          SensorTagDisplayProfile.this.displayClock.schedule(new SensorTagDisplayProfile.clockTask(SensorTagDisplayProfile.this, null), 1000L, 1000L);
        }
        while (SensorTagDisplayProfile.this.displayClock == null) {
          return;
        }
        SensorTagDisplayProfile.this.displayClock.cancel();
      }
    });
    this.cRow.displayInvert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if ((SensorTagDisplayProfile.this.configC != null) && (SensorTagDisplayProfile.this.dev.writeCharacteristicAsync(SensorTagDisplayProfile.this.configC, (byte)5) != 0)) {
          Log.d("SensorTagDisplayProfile", "Error writing config characteristic !");
        }
      }
    });
    this.cRow.displayText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable) {}
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        Log.d("SensorTagDisplayProfile", "New Display Text:" + paramAnonymousCharSequence);
        byte[] arrayOfByte = new byte[paramAnonymousCharSequence.length()];
        paramAnonymousInt1 = 0;
        while (paramAnonymousInt1 < paramAnonymousCharSequence.length())
        {
          arrayOfByte[paramAnonymousInt1] = ((byte)paramAnonymousCharSequence.charAt(paramAnonymousInt1));
          paramAnonymousInt1 += 1;
        }
        if ((SensorTagDisplayProfile.this.dataC != null) && (SensorTagDisplayProfile.this.dev.writeCharacteristicAsync(SensorTagDisplayProfile.this.dataC, arrayOfByte) != 0)) {
          Log.d("SensorTagDisplayProfile", "Error writing data characteristic !");
        }
      }
    });
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("f000ad00-0451-4000-b000-000000000000".toString()) == 0;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void disableService()
  {
    if (this.displayClock != null) {
      this.displayClock.cancel();
    }
  }
  
  public void enableService()
  {
    if (this.cRow.displayClock.isChecked())
    {
      if (this.displayClock != null) {
        this.displayClock.cancel();
      }
      this.displayClock = new Timer();
      this.displayClock.schedule(new clockTask(null), 1000L, 1000L);
    }
  }
  
  public TableRow getTableRow()
  {
    return this.cRow;
  }
  
  private class clockTask
    extends TimerTask
  {
    private clockTask() {}
    
    public void run()
    {
      final Object localObject = new Date();
      localObject = String.format("%02d:%02d:%02d        ", new Object[] { Integer.valueOf(((Date)localObject).getHours()), Integer.valueOf(((Date)localObject).getMinutes()), Integer.valueOf(((Date)localObject).getSeconds()) });
      byte[] arrayOfByte = new byte[((String)localObject).length()];
      int i = 0;
      while (i < ((String)localObject).length())
      {
        arrayOfByte[i] = ((byte)((String)localObject).charAt(i));
        i += 1;
      }
      if (SensorTagDisplayProfile.this.dataC != null) {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            SensorTagDisplayProfile.this.cRow.displayText.setText(localObject);
          }
        });
      }
      try
      {
        Thread.sleep(1000L);
        return;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagDisplayProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */