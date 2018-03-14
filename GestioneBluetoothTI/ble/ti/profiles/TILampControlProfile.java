package com.ti.ble.ti.profiles;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.GenericCharacteristicTableRow;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TILampControlProfile
  extends GenericBluetoothProfile
{
  private static final String lightBlue_UUID = "0000ffb3-0000-1000-8000-00805f9b34fb";
  private static final String lightCompound_UUID = "0000ffb5-0000-1000-8000-00805f9b34fb";
  private static final String lightGreen_UUID = "0000ffb2-0000-1000-8000-00805f9b34fb";
  private static final String lightRed_UUID = "0000ffb1-0000-1000-8000-00805f9b34fb";
  private static final String lightService_UUID = "0000ffb0-0000-1000-8000-00805f9b34fb";
  private static final String lightWhite_UUID = "0000ffb4-0000-1000-8000-00805f9b34fb";
  int B = 10;
  int G = 10;
  int R = 10;
  int W = 10;
  TILampControlProfileTableRow cRow;
  BluetoothGattCharacteristic compoundCharacteristic;
  BroadcastReceiver lightControlReceiver;
  Timer updateLampTimer;
  
  public TILampControlProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.cRow = new TILampControlProfileTableRow(paramContext);
    this.cRow.setColorButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TILampControlDialogFragment.newInstance().show(((Activity)TILampControlProfile.this.context).getFragmentManager(), "LampSetting");
      }
    });
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals("0000ffb5-0000-1000-8000-00805f9b34fb".toString())) {
        this.compoundCharacteristic = paramBluetoothDevice;
      }
    }
    this.tRow.setIcon(getIconPrefix(), paramBluetoothGattService.getUuid().toString());
    this.lightControlReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (paramAnonymousIntent.getAction().equals("org.example.ti.ble.ti.profiles.ACTION_LAMP_HSI_COLOR_CHANGED"))
        {
          double d1 = paramAnonymousIntent.getDoubleExtra("org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_H", 0.0D);
          double d2 = paramAnonymousIntent.getDoubleExtra("org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_S", 0.0D);
          double d3 = paramAnonymousIntent.getDoubleExtra("org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_I", 0.0D);
          TILampControlProfile.this.setLightHSV(d1, d2, d3);
        }
      }
    };
    this.context.registerReceiver(this.lightControlReceiver, makeTILampBroadcastFilter());
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("0000ffb0-0000-1000-8000-00805f9b34fb") == 0;
  }
  
  private static IntentFilter makeTILampBroadcastFilter()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("org.example.ti.ble.ti.profiles.ACTION_LAMP_HSI_COLOR_CHANGED");
    return localIntentFilter;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void disableService()
  {
    this.updateLampTimer.cancel();
    this.updateLampTimer = null;
  }
  
  public void enableService()
  {
    this.updateLampTimer = new Timer();
    this.updateLampTimer.schedule(new updateCompoundTask(null), 1000L, 100L);
  }
  
  public TableRow getTableRow()
  {
    return this.cRow;
  }
  
  public void onPause()
  {
    super.onPause();
    this.context.unregisterReceiver(this.lightControlReceiver);
  }
  
  public void onResume()
  {
    super.onResume();
    this.context.registerReceiver(this.lightControlReceiver, makeTILampBroadcastFilter());
  }
  
  public void periodWasUpdated(int paramInt) {}
  
  public void setLightHSV(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    paramDouble1 = 3.14159D * (paramDouble1 % 360.0D) / 180.0D;
    if (paramDouble2 > 0.0D) {
      if (paramDouble2 < 1.0D)
      {
        if (paramDouble3 <= 0.0D) {
          break label150;
        }
        if (paramDouble3 >= 1.0D) {
          break label144;
        }
      }
    }
    for (;;)
    {
      if (paramDouble1 >= 2.09439D) {
        break label156;
      }
      d = Math.cos(paramDouble1);
      paramDouble1 = Math.cos(1.047196667D - paramDouble1);
      this.R = ((int)(255.0D * paramDouble2 * paramDouble3 / 3.0D * (1.0D + d / paramDouble1)));
      this.G = ((int)(255.0D * paramDouble2 * paramDouble3 / 3.0D * (1.0D + (1.0D - d / paramDouble1))));
      this.B = 0;
      this.W = ((int)(255.0D * (1.0D - paramDouble2) * paramDouble3));
      return;
      paramDouble2 = 1.0D;
      break;
      paramDouble2 = 0.0D;
      break;
      label144:
      paramDouble3 = 1.0D;
      continue;
      label150:
      paramDouble3 = 0.0D;
    }
    label156:
    if (paramDouble1 < 4.188787D)
    {
      d = paramDouble1 - 2.09439D;
      paramDouble1 = Math.cos(d);
      d = Math.cos(1.047196667D - d);
      this.G = ((int)(255.0D * paramDouble2 * paramDouble3 / 3.0D * (1.0D + paramDouble1 / d)));
      this.B = ((int)(255.0D * paramDouble2 * paramDouble3 / 3.0D * (1.0D + (1.0D - paramDouble1 / d))));
      this.R = 0;
      this.W = ((int)(255.0D * (1.0D - paramDouble2) * paramDouble3));
      return;
    }
    double d = paramDouble1 - 4.188787D;
    paramDouble1 = Math.cos(d);
    d = Math.cos(1.047196667D - d);
    this.B = ((int)(255.0D * paramDouble2 * paramDouble3 / 3.0D * (1.0D + paramDouble1 / d)));
    this.R = ((int)(255.0D * paramDouble2 * paramDouble3 / 3.0D * (1.0D + (1.0D - paramDouble1 / d))));
    this.G = 0;
    this.W = ((int)(255.0D * (1.0D - paramDouble2) * paramDouble3));
  }
  
  private class updateCompoundTask
    extends TimerTask
  {
    byte oldB;
    byte oldG;
    byte oldR;
    byte oldW;
    
    private updateCompoundTask() {}
    
    public void run()
    {
      byte[] arrayOfByte;
      if (TILampControlProfile.this.compoundCharacteristic != null)
      {
        arrayOfByte = new byte[4];
        arrayOfByte[0] = ((byte)TILampControlProfile.this.R);
        arrayOfByte[1] = ((byte)TILampControlProfile.this.G);
        arrayOfByte[2] = ((byte)TILampControlProfile.this.B);
        arrayOfByte[3] = ((byte)TILampControlProfile.this.W);
        if ((arrayOfByte[0] != this.oldR) || (arrayOfByte[1] != this.oldG) || (arrayOfByte[2] != this.oldB) || (arrayOfByte[3] != this.oldW)) {}
      }
      else
      {
        return;
      }
      if (TILampControlProfile.this.dev.writeCharacteristicAsync(TILampControlProfile.this.compoundCharacteristic, arrayOfByte) != 0) {
        Log.d("TILampControlProfile", "Error writing compound color characteristic !");
      }
      this.oldR = arrayOfByte[0];
      this.oldG = arrayOfByte[1];
      this.oldB = arrayOfByte[2];
      this.oldW = arrayOfByte[3];
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TILampControlProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */