package com.ti.ble.ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.TrippleSparkLineView;
import com.ti.util.bleUtility;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ThroughputTestService
  extends GenericBluetoothProfile
{
  public static final String TAG = ThroughputTestService.class.getSimpleName();
  public static final String throughputServiceToggleThroughputTest_UUID = "f0001237-0451-4000-b000-000000000000";
  public static final String throughputServiceUpdatePDU_UUID = "f0001235-0451-4000-b000-000000000000";
  public static final String throughputServiceUpdatePHY_UUID = "f0001236-0451-4000-b000-000000000000";
  public static final String throughputService_UUID = "f0001234-0451-4000-b000-000000000000";
  private int bytesReceivedInInterval;
  private long bytesReceivedTotal;
  private ToggleButton codingButton;
  private TextView currentPHYText;
  private TextView currentPacketsPerSecondText;
  private TextView currentSpeedText;
  private Switch enableThroughputTest;
  private long lastSequenceNumber;
  private ToggleButton oneMbsButton;
  private int packetsReceivedInInterval;
  private SeekBar pduLengthSeekBar;
  private TrippleSparkLineView speedGraph;
  public ThroughputTestServiceTableRow tRow;
  private BluetoothGattCharacteristic toggleThroughputTestChar;
  private ToggleButton twoMbsButton;
  private BluetoothGattCharacteristic updatePDUChar;
  private BluetoothGattCharacteristic updatePHYChar;
  
  public ThroughputTestService(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new ThroughputTestServiceTableRow(paramContext);
    paramContext = this.mBTService.getCharacteristics().iterator();
    while (paramContext.hasNext())
    {
      paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
      if (paramBluetoothDevice.getUuid().toString().equals("f0001235-0451-4000-b000-000000000000".toString())) {
        this.updatePDUChar = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals("f0001236-0451-4000-b000-000000000000".toString())) {
        this.updatePHYChar = paramBluetoothDevice;
      }
      if (paramBluetoothDevice.getUuid().toString().equals("f0001237-0451-4000-b000-000000000000".toString())) {
        this.toggleThroughputTestChar = paramBluetoothDevice;
      }
    }
    this.oneMbsButton = ((ToggleButton)this.tRow.findViewById(2131231164));
    this.oneMbsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          ThroughputTestService.this.twoMbsButton.setChecked(false);
          ThroughputTestService.this.codingButton.setChecked(false);
          ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePHYChar, (byte)0);
          ThroughputTestService.this.dev.readCharacteristicSync(ThroughputTestService.this.updatePHYChar);
          Log.d(ThroughputTestService.TAG, "Set PHY to " + 0);
        }
      }
    });
    this.twoMbsButton = ((ToggleButton)this.tRow.findViewById(2131231169));
    this.twoMbsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          ThroughputTestService.this.oneMbsButton.setChecked(false);
          ThroughputTestService.this.codingButton.setChecked(false);
          ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePHYChar, (byte)1);
          ThroughputTestService.this.dev.readCharacteristicSync(ThroughputTestService.this.updatePHYChar);
          Log.d(ThroughputTestService.TAG, "Set PHY to " + 1);
        }
      }
    });
    this.codingButton = ((ToggleButton)this.tRow.findViewById(2131231158));
    this.codingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          ThroughputTestService.this.oneMbsButton.setChecked(false);
          ThroughputTestService.this.twoMbsButton.setChecked(false);
          ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePHYChar, (byte)2);
          ThroughputTestService.this.dev.readCharacteristicSync(ThroughputTestService.this.updatePHYChar);
          Log.d(ThroughputTestService.TAG, "Set PHY to " + 2);
        }
      }
    });
    this.enableThroughputTest = ((Switch)this.tRow.findViewById(2131231163));
    this.enableThroughputTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.toggleThroughputTestChar, new byte[] { 2 });
        String str = ThroughputTestService.TAG;
        StringBuilder localStringBuilder = new StringBuilder().append("Throughput Test :");
        if (paramAnonymousBoolean) {}
        for (paramAnonymousCompoundButton = "Enabled";; paramAnonymousCompoundButton = "Disabled")
        {
          Log.d(str, paramAnonymousCompoundButton);
          new Thread(new Runnable()
          {
            public void run()
            {
              for (;;)
              {
                if (ThroughputTestService.this.enableThroughputTest.isChecked()) {
                  try
                  {
                    Thread.sleep(1000L, 0);
                    final int i = ThroughputTestService.this.bytesReceivedInInterval;
                    final int j = ThroughputTestService.this.packetsReceivedInInterval;
                    ThroughputTestService.access$1302(ThroughputTestService.this, 0);
                    ThroughputTestService.access$1402(ThroughputTestService.this, 0);
                    ThroughputTestService.this.speedGraph.post(new Runnable()
                    {
                      public void run()
                      {
                        ThroughputTestService.this.speedGraph.addValue(i);
                      }
                    });
                    ThroughputTestService.this.currentSpeedText.post(new Runnable()
                    {
                      public void run()
                      {
                        ThroughputTestService.this.currentSpeedText.setText(String.format("%d bps", new Object[] { Integer.valueOf(i) }));
                      }
                    });
                    ThroughputTestService.this.currentPacketsPerSecondText.post(new Runnable()
                    {
                      public void run()
                      {
                        ThroughputTestService.this.currentPacketsPerSecondText.setText(String.format("%d p/s", new Object[] { Integer.valueOf(j) }));
                      }
                    });
                  }
                  catch (InterruptedException localInterruptedException)
                  {
                    for (;;)
                    {
                      localInterruptedException.printStackTrace();
                    }
                  }
                }
              }
            }
          }).start();
          return;
        }
      }
    });
    this.pduLengthSeekBar = ((SeekBar)this.tRow.findViewById(2131231166));
    this.pduLengthSeekBar.setProgress(0);
    this.pduLengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        ThroughputTestService.this.dev.writeCharacteristicSync(ThroughputTestService.this.updatePDUChar, (byte)(paramAnonymousInt + 20));
        Log.d(ThroughputTestService.TAG, "Set PDU size to " + (paramAnonymousInt + 20) + "bytes");
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    });
    this.currentPHYText = ((TextView)this.tRow.findViewById(2131231160));
    this.currentPHYText.setText("---");
    this.currentSpeedText = ((TextView)this.tRow.findViewById(2131231161));
    this.currentPHYText.setText("0 2131558658");
    this.currentPacketsPerSecondText = ((TextView)this.tRow.findViewById(2131231165));
    this.currentPacketsPerSecondText.setText("0 2131558672");
    this.speedGraph = ((TrippleSparkLineView)this.tRow.findViewById(2131231168));
    this.speedGraph.setColor(0, 0, 0, 0, 1);
    this.speedGraph.setColor(0, 0, 0, 0, 2);
    this.speedGraph.setColor(255, 255, 0, 0);
    this.speedGraph.autoScale = true;
    this.speedGraph.autoScaleBounceBack = true;
    this.bytesReceivedInInterval = 0;
    this.bytesReceivedTotal = 0L;
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("f0001234-0451-4000-b000-000000000000") == 0;
  }
  
  public void configureService()
  {
    this.dev.setCharacteristicNotificationSync(this.toggleThroughputTestChar, true);
    this.dev.setCurrentConnectionPriority(1);
  }
  
  public void deConfigureService()
  {
    this.dev.setCharacteristicNotificationSync(this.toggleThroughputTestChar, false);
  }
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    super.didReadValueForCharacteristic(paramBluetoothGattCharacteristic);
    if (paramBluetoothGattCharacteristic.getUuid().toString().equals(this.updatePDUChar.getUuid().toString()))
    {
      paramBluetoothGattCharacteristic = paramBluetoothGattCharacteristic.getValue();
      this.pduLengthSeekBar.setProgress(paramBluetoothGattCharacteristic[0] - 20);
    }
    while (!paramBluetoothGattCharacteristic.getUuid().toString().equals(this.updatePHYChar.getUuid().toString())) {
      return;
    }
    switch (paramBluetoothGattCharacteristic.getValue()[0])
    {
    default: 
      return;
    case 0: 
      this.oneMbsButton.setChecked(true);
      this.twoMbsButton.setChecked(false);
      this.codingButton.setChecked(false);
      this.currentPHYText.setText("1 Mbps");
      return;
    case 1: 
      this.oneMbsButton.setChecked(false);
      this.twoMbsButton.setChecked(true);
      this.codingButton.setChecked(false);
      this.currentPHYText.setText("2 Mbps");
      return;
    }
    this.oneMbsButton.setChecked(false);
    this.twoMbsButton.setChecked(false);
    this.codingButton.setChecked(true);
    this.currentPHYText.setText("Coded");
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    long l;
    if (paramBluetoothGattCharacteristic.getUuid().toString().equals(this.toggleThroughputTestChar.getUuid().toString()))
    {
      byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
      if (paramBluetoothGattCharacteristic.getValue().length > 4)
      {
        l = bleUtility.BUILD_UINT32(arrayOfByte[0], arrayOfByte[1], arrayOfByte[2], arrayOfByte[3]);
        if (l <= this.lastSequenceNumber) {
          break label154;
        }
        Log.d(TAG, "RX with length " + paramBluetoothGattCharacteristic.getValue().length + " SEQ:" + String.format("0x%08x", new Object[] { Long.valueOf(l) }));
        this.bytesReceivedInInterval += paramBluetoothGattCharacteristic.getValue().length;
        this.packetsReceivedInInterval += 1;
        this.lastSequenceNumber = l;
      }
    }
    return;
    label154:
    Log.d(TAG, "Duplicate Seq found ! " + l);
  }
  
  public void disableService() {}
  
  public void enableService()
  {
    this.dev.readCharacteristicSync(this.updatePDUChar);
    this.dev.readCharacteristicSync(this.updatePHYChar);
    this.dev.readCharacteristicSync(this.toggleThroughputTestChar);
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(500L, 0);
          ThroughputTestService.this.dev.requestMTUChange(245);
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
          for (;;)
          {
            localInterruptedException.printStackTrace();
          }
        }
      }
    }).start();
  }
  
  public TableRow getTableRow()
  {
    return this.tRow;
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


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\ThroughputTestService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */