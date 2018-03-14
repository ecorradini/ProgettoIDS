package com.ti.ble.ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.ToggleButton;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TIConnectionControlService
  extends GenericBluetoothProfile
{
  public static final String connectionControlService_UUID = "f000ccc0-0451-4000-b000-000000000000";
  public static final String currentConnectionParams_UUID = "f000ccc1-0451-4000-b000-000000000000";
  public static final String disconnectRequest_UUID = "f000ccc3-0451-4000-b000-000000000000";
  public static final String requestConnectionParams_UUID = "f000ccc2-0451-4000-b000-000000000000";
  public TIConnectionControlServiceTableRow cRow;
  private BluetoothGattCharacteristic curConParamsChar;
  private BluetoothGattCharacteristic disconnectReqChar;
  private BluetoothGattCharacteristic reqConParamsChar;
  
  public TIConnectionControlService(final Context paramContext, final BluetoothDevice paramBluetoothDevice, final BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.cRow = new TIConnectionControlServiceTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals("f000ccc1-0451-4000-b000-000000000000".toString())) {
        this.curConParamsChar = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals("f000ccc2-0451-4000-b000-000000000000".toString())) {
        this.reqConParamsChar = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals("f000ccc3-0451-4000-b000-000000000000".toString())) {
        this.disconnectReqChar = paramBluetoothDevice;
      }
    }
    if ((this.mBTDevice.getName().equals("CC2650 LaunchPad")) || (this.mBTDevice.getName().equals("CC1350 LaunchPad"))) {
      ((ImageView)this.cRow.findViewById(2131230841)).setImageResource(2131492866);
    }
    for (;;)
    {
      ((SeekBar)this.cRow.findViewById(2131230844)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
      {
        public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
          TIConnectionControlService.this.cRow.setConIntText(paramAnonymousInt + 6);
        }
        
        public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
        {
          TIConnectionControlService.this.cRow.invalidateConnectionParameters();
        }
        
        public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
        {
          int i2 = (int)((paramAnonymousSeekBar.getProgress() + 6) / 1.25F);
          int i3 = i2 + 10;
          int i4 = i2 * 4;
          int i = (byte)(i2 & 0xFF);
          int j = (byte)((i2 & 0xFF00) >> 8);
          int k = (byte)(i3 & 0xFF);
          int m = (byte)((i3 & 0xFF00) >> 8);
          int n = (byte)(i4 & 0xFF);
          int i1 = (byte)((i4 & 0xFF00) >> 8);
          TIConnectionControlService.this.dev.writeCharacteristicAsync(TIConnectionControlService.this.reqConParamsChar, new byte[] { i, j, k, m, 0, 0, n, i1 });
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                Thread.sleep(3000L);
                TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                return;
              }
              catch (Exception localException)
              {
                for (;;)
                {
                  localException.printStackTrace();
                }
              }
            }
          }).start();
        }
      });
      paramContext = (ToggleButton)this.cRow.findViewById(2131230840);
      paramBluetoothDevice = (ToggleButton)this.cRow.findViewById(2131230833);
      paramBluetoothGattService = (ToggleButton)this.cRow.findViewById(2131230842);
      paramContext.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Log.d("TIConnectionControlService", "High Priority touched !");
          TIConnectionControlService.this.dev.setCurrentConnectionPriority(1);
          paramContext.setChecked(true);
          paramBluetoothDevice.setChecked(false);
          paramBluetoothGattService.setChecked(false);
          TIConnectionControlService.this.cRow.invalidateConnectionParameters();
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                Thread.sleep(3000L);
                TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                return;
              }
              catch (Exception localException)
              {
                for (;;)
                {
                  localException.printStackTrace();
                }
              }
            }
          }).start();
        }
      });
      paramBluetoothDevice.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Log.d("TIConnectionControlService", "Balanced Priority touched !");
          TIConnectionControlService.this.dev.setCurrentConnectionPriority(0);
          paramContext.setChecked(false);
          paramBluetoothDevice.setChecked(true);
          paramBluetoothGattService.setChecked(false);
          TIConnectionControlService.this.cRow.invalidateConnectionParameters();
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                Thread.sleep(3000L);
                TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                return;
              }
              catch (Exception localException)
              {
                for (;;)
                {
                  localException.printStackTrace();
                }
              }
            }
          }).start();
        }
      });
      paramBluetoothGattService.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Log.d("TIConnectionControlService", "Low Power Priority touched !");
          TIConnectionControlService.this.dev.setCurrentConnectionPriority(2);
          paramContext.setChecked(false);
          paramBluetoothDevice.setChecked(false);
          paramBluetoothGattService.setChecked(true);
          TIConnectionControlService.this.cRow.invalidateConnectionParameters();
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                Thread.sleep(3000L);
                TIConnectionControlService.this.dev.readCharacteristicAsync(TIConnectionControlService.this.curConParamsChar);
                return;
              }
              catch (Exception localException)
              {
                for (;;)
                {
                  localException.printStackTrace();
                }
              }
            }
          }).start();
        }
      });
      return;
      if (this.mBTDevice.getName().equals("Throughput Periph")) {
        ((ImageView)this.cRow.findViewById(2131230841)).setImageResource(2131492866);
      }
    }
  }
  
  public static float[] getValuesFromBytes(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > 5)
    {
      int i = paramArrayOfByte[1];
      int j = paramArrayOfByte[0];
      int k = paramArrayOfByte[3];
      int m = paramArrayOfByte[2];
      int n = paramArrayOfByte[5];
      int i1 = paramArrayOfByte[4];
      return new float[] { ((i & 0xFF) << 8 | j & 0xFF) * 1.25F, ((k & 0xFF) << 8 | m & 0xFF) * 1.25F, ((n & 0xFF) << 8 | i1 & 0xFF) * 1.25F };
    }
    return new float[3];
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("f000ccc0-0451-4000-b000-000000000000") == 0;
  }
  
  public void configureService()
  {
    this.dev.readCharacteristicAsync(this.curConParamsChar);
  }
  
  public void deConfigureService() {}
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    if (paramBluetoothGattCharacteristic.equals(this.curConParamsChar))
    {
      paramBluetoothGattCharacteristic = paramBluetoothGattCharacteristic.getValue();
      if (paramBluetoothGattCharacteristic.length > 5)
      {
        int i = (paramBluetoothGattCharacteristic[1] & 0xFF) << 8 | paramBluetoothGattCharacteristic[0] & 0xFF;
        int j = (paramBluetoothGattCharacteristic[3] & 0xFF) << 8 | paramBluetoothGattCharacteristic[2] & 0xFF;
        int k = (paramBluetoothGattCharacteristic[5] & 0xFF) << 8 | paramBluetoothGattCharacteristic[4] & 0xFF;
        Log.d("TIConnectionControlService", "Got new connection parameters : " + i + ", " + j + ", " + k);
        this.cRow.setConnectionParameters(i, j, k, this.dev.getCurrentConnectionPriority());
      }
    }
  }
  
  public void disableService() {}
  
  public void enableService() {}
  
  public TableRow getTableRow()
  {
    return this.cRow;
  }
  
  public void onPause()
  {
    super.onPause();
  }
  
  public void onResume()
  {
    super.onResume();
  }
  
  public void periodWasUpdated(int paramInt) {}
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TIConnectionControlService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */