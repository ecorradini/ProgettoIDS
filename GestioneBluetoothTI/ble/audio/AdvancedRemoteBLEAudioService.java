package com.ti.ble.audio;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class AdvancedRemoteBLEAudioService
  extends Service
{
  public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
  public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
  public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
  public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
  protected static final UUID CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
  public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
  private static final String TAG = "service";
  private BroadcastReceiver AdvancedRemoteBLEAudioServiceMessageReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      int i = paramAnonymousIntent.getIntExtra("message", -1);
      int j = paramAnonymousIntent.getIntExtra("device", -1);
      int k = paramAnonymousIntent.getIntExtra("paired", -1);
      paramAnonymousContext = paramAnonymousIntent.getStringExtra("btaddr");
      if (i >= 0)
      {
        Log.d("info", "Got Message ID : " + i);
        switch (i)
        {
        default: 
          Log.d("error", "Received unknown message id : " + i);
        }
      }
      for (;;)
      {
        return;
        AdvancedRemoteBLEAudioService.this.scanForBLEDevices();
        return;
        AdvancedRemoteBLEAudioService.this.killCommunications();
        return;
        AdvancedRemoteBLEAudioService.this.enableAudio();
        return;
        if (j >= 0)
        {
          Log.d("service", "Got deviceID : " + j);
          paramAnonymousContext = (BluetoothDevice)AdvancedRemoteBLEAudioService.this.mBluetoothDevices.get(j);
          Log.d("service", "Connecting To (" + j + ") - " + paramAnonymousContext.getName() + " [" + paramAnonymousContext.getAddress() + "]");
          AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(paramAnonymousContext);
          return;
        }
        if (k >= 0)
        {
          Log.d("service", "Got PairedId : " + k);
          paramAnonymousContext = (BluetoothDevice)AdvancedRemoteBLEAudioService.this.mPairedDevices.get(k);
          Log.d("service", "Connecting To (" + j + ") - " + paramAnonymousContext.getName() + " [" + paramAnonymousContext.getAddress() + "]");
          AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(paramAnonymousContext);
          return;
        }
        if (paramAnonymousContext != null)
        {
          AdvancedRemoteBLEAudioService.access$102(AdvancedRemoteBLEAudioService.this, paramAnonymousContext);
          AdvancedRemoteBLEAudioService.this.scanForBLEDevices();
          if (AdvancedRemoteBLEAudioService.this.mPairedDevices != null)
          {
            i = 0;
            while (i < AdvancedRemoteBLEAudioService.this.mPairedDevices.size())
            {
              paramAnonymousContext = (BluetoothDevice)AdvancedRemoteBLEAudioService.this.mPairedDevices.get(i);
              if (paramAnonymousContext.getAddress().equals(AdvancedRemoteBLEAudioService.this.mBtAddr))
              {
                Log.d("service", "Device found in paired devices, connecting");
                AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(paramAnonymousContext);
              }
              i += 1;
            }
          }
        }
      }
    }
  };
  public BluetoothGattCharacteristic audioConfigChar;
  public BluetoothGattCharacteristic audioCtrlChar;
  public BluetoothGattService audioService;
  public BluetoothGattCharacteristic audioStreamChar;
  private AdvancedRemoteBLEAudioHandler mAudioHandler;
  private BluetoothAdapter mBluetoothAdapter;
  private ArrayList<BluetoothDevice> mBluetoothDevices;
  private BluetoothGatt mBluetoothGatt;
  private BluetoothManager mBluetoothManager;
  private String mBtAddr;
  private BluetoothGattCharacteristic mConControlCurParam;
  private BluetoothGattService mConControlService;
  private BluetoothGattCharacteristic mConControlSetParam;
  private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
  {
    public void onCharacteristicChanged(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic)
    {
      if (paramAnonymousBluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.audioStreamChar) {
        paramAnonymousBluetoothGatt = paramAnonymousBluetoothGattCharacteristic.getValue();
      }
      do
      {
        do
        {
          try
          {
            AdvancedRemoteBLEAudioService.this.mAudioHandler.rxFrame(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGatt.length);
            return;
          }
          catch (IOException paramAnonymousBluetoothGatt)
          {
            paramAnonymousBluetoothGatt = paramAnonymousBluetoothGatt;
            paramAnonymousBluetoothGatt.printStackTrace();
            return;
          }
          finally {}
          if (paramAnonymousBluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.audioCtrlChar)
          {
            Log.d("service", "Audio control characteristic updated : " + paramAnonymousBluetoothGattCharacteristic.getValue().toString());
            return;
          }
        } while (paramAnonymousBluetoothGattCharacteristic != AdvancedRemoteBLEAudioService.this.mConControlCurParam);
        Log.d("service", "Connection control, received current parameters :" + AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue());
        paramAnonymousBluetoothGatt = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
        paramAnonymousBluetoothGattCharacteristic = new Intent("ARCBLEAudio-From-Service-Events");
        paramAnonymousBluetoothGattCharacteristic.putExtra("conInterval", paramAnonymousBluetoothGatt[0]);
        LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(paramAnonymousBluetoothGattCharacteristic);
        paramAnonymousBluetoothGatt = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
      } while ((paramAnonymousBluetoothGatt[1] << 8 | paramAnonymousBluetoothGatt[0]) <= 8.0D);
      if (!AdvancedRemoteBLEAudioService.this.triedLoweringConnectionInterval)
      {
        AdvancedRemoteBLEAudioService.this.lowerConnectionInterval();
        return;
      }
      paramAnonymousBluetoothGatt = new Intent("ARCBLEAudio-From-Service-Events");
      paramAnonymousBluetoothGatt.putExtra("statusText", "Failed to set connection interval");
      LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(paramAnonymousBluetoothGatt);
    }
    
    public void onCharacteristicRead(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic, int paramAnonymousInt)
    {
      Log.d("service", "r" + paramAnonymousBluetoothGattCharacteristic.getStringValue(0) + " uuid: " + paramAnonymousBluetoothGattCharacteristic.getUuid());
      if ((paramAnonymousInt == 0) && (paramAnonymousBluetoothGattCharacteristic == AdvancedRemoteBLEAudioService.this.mConControlCurParam))
      {
        paramAnonymousBluetoothGatt = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
        Log.d("service", "Connection control, received current parameters :" + paramAnonymousBluetoothGatt[0] + " " + paramAnonymousBluetoothGatt[1] + " " + paramAnonymousBluetoothGatt[2] + " " + paramAnonymousBluetoothGatt[3] + " " + paramAnonymousBluetoothGatt[4] + " " + paramAnonymousBluetoothGatt[5]);
        paramAnonymousBluetoothGattCharacteristic = new Intent("ARCBLEAudio-From-Service-Events");
        paramAnonymousBluetoothGattCharacteristic.putExtra("conInterval", paramAnonymousBluetoothGatt[0]);
        LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(paramAnonymousBluetoothGattCharacteristic);
        paramAnonymousBluetoothGatt = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getValue();
        if ((paramAnonymousBluetoothGatt[1] << 8 | paramAnonymousBluetoothGatt[0]) > 8.0D)
        {
          if (AdvancedRemoteBLEAudioService.this.triedLoweringConnectionInterval) {
            break label231;
          }
          AdvancedRemoteBLEAudioService.this.lowerConnectionInterval();
        }
      }
      return;
      label231:
      paramAnonymousBluetoothGatt = new Intent("ARCBLEAudio-From-Service-Events");
      paramAnonymousBluetoothGatt.putExtra("statusText", "Failed to set connection interval");
      LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(paramAnonymousBluetoothGatt);
    }
    
    public void onCharacteristicWrite(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic, int paramAnonymousInt)
    {
      Log.d("service", "Wrote Characteristic : " + paramAnonymousBluetoothGattCharacteristic.getUuid().toString() + " Status : " + paramAnonymousInt);
    }
    
    public void onConnectionStateChange(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (paramAnonymousInt2 == 2)
      {
        AdvancedRemoteBLEAudioService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_GATT_CONNECTED");
        Log.i("service", "Connected to GATT server. Status : " + paramAnonymousInt1);
        Log.i("service", "Attempting to start service discovery:" + AdvancedRemoteBLEAudioService.this.mBluetoothGatt.discoverServices());
        paramAnonymousBluetoothGatt = new Intent("ARCBLEAudio-From-Service-Events");
        paramAnonymousBluetoothGatt.putExtra("statusText", "Connected");
        LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(paramAnonymousBluetoothGatt);
        if (Build.VERSION.SDK_INT >= 21) {
          Log.d("AdvancedRemoteBLEAudioService", "Requested connection priority HIGH, result : " + AdvancedRemoteBLEAudioService.this.mBluetoothGatt.requestConnectionPriority(1));
        }
      }
      while (paramAnonymousInt2 != 0) {
        return;
      }
      Intent localIntent = new Intent("ARCBLEAudio-From-Service-Events");
      localIntent.putExtra("statusText", "Disconnected");
      LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(localIntent);
      Log.i("service", "Disconnected from GATT server. Status : " + paramAnonymousInt1);
      AdvancedRemoteBLEAudioService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_GATT_DISCONNECTED");
      paramAnonymousBluetoothGatt.connect();
    }
    
    public void onDescriptorRead(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor, int paramAnonymousInt)
    {
      Log.d("service", "Did Read Descriptor");
    }
    
    public void onDescriptorWrite(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor, int paramAnonymousInt)
    {
      Log.d("service", "Did Write Descriptor for characteristic " + paramAnonymousBluetoothGattDescriptor.getCharacteristic().getUuid());
      if (paramAnonymousBluetoothGattDescriptor.getCharacteristic().getUuid().toString().equals("f000b002-0451-4000-b000-000000000000")) {
        if (AdvancedRemoteBLEAudioService.this.mConControlCurParam != null) {
          new Runnable()
          {
            public void run()
            {
              Log.d("service", "Reading Connection parameters from device");
              AdvancedRemoteBLEAudioService.this.mBluetoothGatt.setCharacteristicNotification(AdvancedRemoteBLEAudioService.this.mConControlCurParam, true);
              BluetoothGattDescriptor localBluetoothGattDescriptor = AdvancedRemoteBLEAudioService.this.mConControlCurParam.getDescriptor(AdvancedRemoteBLEAudioService.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
              localBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
              AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeDescriptor(localBluetoothGattDescriptor);
            }
          }.run();
        }
      }
      do
      {
        return;
        if (paramAnonymousBluetoothGattDescriptor.getCharacteristic().getUuid().toString().equals("f000ccc1-0451-4000-b000-000000000000"))
        {
          AdvancedRemoteBLEAudioService.this.mBluetoothGatt.setCharacteristicNotification(AdvancedRemoteBLEAudioService.this.audioCtrlChar, true);
          paramAnonymousBluetoothGatt = AdvancedRemoteBLEAudioService.this.audioCtrlChar.getDescriptor(AdvancedRemoteBLEAudioService.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
          paramAnonymousBluetoothGatt.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
          AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeDescriptor(paramAnonymousBluetoothGatt);
          return;
        }
      } while (!paramAnonymousBluetoothGattDescriptor.getCharacteristic().getUuid().toString().equals("f000b001-0451-4000-b000-000000000000"));
      AdvancedRemoteBLEAudioService.this.mBluetoothGatt.readCharacteristic(AdvancedRemoteBLEAudioService.this.mConControlCurParam);
    }
    
    public void onServicesDiscovered(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt)
    {
      if (paramAnonymousInt == 0)
      {
        AdvancedRemoteBLEAudioService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED");
        AdvancedRemoteBLEAudioService.access$602(AdvancedRemoteBLEAudioService.this, AdvancedRemoteBLEAudioService.this.mBluetoothGatt.getService(UUID.fromString("f000ccc0-0451-4000-b000-000000000000")));
        if (AdvancedRemoteBLEAudioService.this.mConControlService != null)
        {
          Log.d("service", "Found device with Connection control service, using it !");
          AdvancedRemoteBLEAudioService.access$702(AdvancedRemoteBLEAudioService.this, AdvancedRemoteBLEAudioService.this.mConControlService.getCharacteristic(UUID.fromString("f000ccc1-0451-4000-b000-000000000000")));
          AdvancedRemoteBLEAudioService.access$802(AdvancedRemoteBLEAudioService.this, AdvancedRemoteBLEAudioService.this.mConControlService.getCharacteristic(UUID.fromString("f000ccc2-0451-4000-b000-000000000000")));
        }
        Log.d("service", "Audio notification enabled !");
        AdvancedRemoteBLEAudioService.this.enableAudio();
        paramAnonymousBluetoothGatt = new Intent("ARCBLEAudio-From-Service-Events");
        paramAnonymousBluetoothGatt.putExtra("statusText", "Enabling audio");
        LocalBroadcastManager.getInstance(AdvancedRemoteBLEAudioService.this.getApplicationContext()).sendBroadcast(paramAnonymousBluetoothGatt);
        return;
      }
      Log.w("service", "onServicesDiscovered received: " + paramAnonymousInt);
    }
    
    public void printAudioPacket(byte[] paramAnonymousArrayOfByte)
    {
      String str = new String();
      str = str + "Pkt: ";
      int i = 0;
      while (i < paramAnonymousArrayOfByte.length)
      {
        str = str + String.format("%02x", new Object[] { Integer.valueOf(paramAnonymousArrayOfByte[i] & 0xFF) });
        str = str + ",";
        i += 1;
      }
      Log.d("service", str);
    }
  };
  private ArrayList<BluetoothDevice> mPairedDevices;
  private ScanCallback mScanCallback = new ScanCallback()
  {
    public void onScanResult(int paramAnonymousInt, ScanResult paramAnonymousScanResult)
    {
      super.onScanResult(paramAnonymousInt, paramAnonymousScanResult);
      Log.d("service", "onLeScan() -> dev:" + paramAnonymousScanResult.getDevice().getAddress() + " name:" + paramAnonymousScanResult.getDevice().getName());
      if (AdvancedRemoteBLEAudioService.this.mBluetoothDevices == null) {
        AdvancedRemoteBLEAudioService.access$002(AdvancedRemoteBLEAudioService.this, new ArrayList());
      }
      if (AdvancedRemoteBLEAudioService.this.mBluetoothDevices.indexOf(paramAnonymousScanResult.getDevice()) < 0)
      {
        AdvancedRemoteBLEAudioService.this.mBluetoothDevices.add(paramAnonymousScanResult.getDevice());
        AdvancedRemoteBLEAudioService.this.sendDevice(paramAnonymousScanResult.getDevice());
      }
      if (AdvancedRemoteBLEAudioService.this.mBtAddr.equals(paramAnonymousScanResult.getDevice().getAddress()))
      {
        Log.d("AdvancedRemoteBLEAudioService", "Found correct device, connecting ...");
        AdvancedRemoteBLEAudioService.this.initiateRemoteCommunication(paramAnonymousScanResult.getDevice());
      }
    }
  };
  public boolean triedLoweringConnectionInterval;
  
  private void broadcastUpdate(String paramString)
  {
    sendBroadcast(new Intent(paramString));
  }
  
  private void enableAudio()
  {
    this.audioService = this.mBluetoothGatt.getService(UUID.fromString("f000b000-0451-4000-b000-000000000000"));
    this.audioCtrlChar = this.audioService.getCharacteristic(UUID.fromString("f000b001-0451-4000-b000-000000000000"));
    this.audioStreamChar = this.audioService.getCharacteristic(UUID.fromString("f000b002-0451-4000-b000-000000000000"));
    if (this.audioStreamChar != null) {
      new Runnable()
      {
        public void run()
        {
          Log.d("service", "Enabled audioStrmChar : " + AdvancedRemoteBLEAudioService.this.mBluetoothGatt.setCharacteristicNotification(AdvancedRemoteBLEAudioService.this.audioStreamChar, true));
          BluetoothGattDescriptor localBluetoothGattDescriptor = AdvancedRemoteBLEAudioService.this.audioStreamChar.getDescriptor(AdvancedRemoteBLEAudioService.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
          Log.d("service", "Enabled descriptor : " + localBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
          AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeDescriptor(localBluetoothGattDescriptor);
          Log.d("service", localBluetoothGattDescriptor.getUuid().toString());
        }
      }.run();
    }
    readDeviceInformationService();
  }
  
  private void readDeviceInformationService()
  {
    BluetoothGattCharacteristic localBluetoothGattCharacteristic = this.mBluetoothGatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb"));
    this.mBluetoothGatt.readCharacteristic(localBluetoothGattCharacteristic);
  }
  
  public byte[] buildConnectionParameters(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return new byte[] { (byte)(paramInt1 & 0xFF), (byte)(paramInt1 >> 8 & 0xFF), (byte)(paramInt2 & 0xFF), (byte)(paramInt2 >> 8 & 0xFF), (byte)(paramInt3 & 0xFF), (byte)(paramInt3 >> 8 & 0xFF), (byte)(paramInt4 & 0xFF), (byte)(paramInt4 >> 8 & 0xFF) };
  }
  
  public void fillPairedDeviceList()
  {
    Object localObject = this.mBluetoothAdapter.getBondedDevices();
    if (((Set)localObject).size() > 0)
    {
      localObject = ((Set)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        BluetoothDevice localBluetoothDevice = (BluetoothDevice)((Iterator)localObject).next();
        if (localBluetoothDevice.getType() == 2)
        {
          if (this.mPairedDevices == null) {
            this.mPairedDevices = new ArrayList();
          }
          Log.d("service", "fillPairedDeviceList() -> dev:" + localBluetoothDevice.getAddress() + " name:" + localBluetoothDevice.getName());
          if (this.mPairedDevices.indexOf(localBluetoothDevice) < 0)
          {
            this.mPairedDevices.add(localBluetoothDevice);
            sendPaired(localBluetoothDevice);
          }
        }
      }
    }
  }
  
  public void initiateRemoteCommunication(BluetoothDevice paramBluetoothDevice)
  {
    if (this.mBluetoothManager == null) {
      this.mBluetoothManager = ((BluetoothManager)getSystemService("bluetooth"));
    }
    if (this.mBluetoothAdapter == null) {
      this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
    }
    this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(this.mScanCallback);
    this.mBluetoothGatt = paramBluetoothDevice.connectGatt(this, true, this.mGattCallback);
  }
  
  public void killCommunications()
  {
    Log.d("info", "Killing communications");
    if (this.mBluetoothGatt == null) {
      return;
    }
    this.mBluetoothGatt.disconnect();
    this.mBluetoothGatt.close();
    this.mBluetoothGatt = null;
    this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(this.mScanCallback);
    this.mBluetoothAdapter = null;
    this.mBluetoothManager = null;
  }
  
  public void lowerConnectionInterval()
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(2000L);
          Log.d("service", "Current connection interval to high, trying to set lower");
          AdvancedRemoteBLEAudioService.this.mConControlSetParam.setValue(AdvancedRemoteBLEAudioService.this.buildConnectionParameters(8, 8, 1, 50));
          AdvancedRemoteBLEAudioService.this.mBluetoothGatt.writeCharacteristic(AdvancedRemoteBLEAudioService.this.mConControlSetParam);
          AdvancedRemoteBLEAudioService.this.triedLoweringConnectionInterval = true;
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
  
  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }
  
  public void onCreate()
  {
    killCommunications();
    LocalBroadcastManager.getInstance(this).registerReceiver(this.AdvancedRemoteBLEAudioServiceMessageReceiver, new IntentFilter("ARCBLEAudio-To-Service-Events"));
    Log.d("service", "Advanced Remote BLE Audio Service Created !");
    if (this.mBluetoothManager == null) {
      this.mBluetoothManager = ((BluetoothManager)getSystemService("bluetooth"));
    }
    if (this.mBluetoothAdapter == null) {
      this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
    }
    fillPairedDeviceList();
    this.mAudioHandler = new AdvancedRemoteBLEAudioHandler(100, this);
  }
  
  public void onDestroy()
  {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.AdvancedRemoteBLEAudioServiceMessageReceiver);
    super.onDestroy();
  }
  
  public void scanForBLEDevices()
  {
    if (this.mBluetoothManager == null) {
      this.mBluetoothManager = ((BluetoothManager)getSystemService("bluetooth"));
    }
    if (this.mBluetoothAdapter == null) {
      this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
    }
    this.mBluetoothAdapter.getBluetoothLeScanner().startScan(this.mScanCallback);
    sendMsg(3);
    fillPairedDeviceList();
  }
  
  public void sendDevice(BluetoothDevice paramBluetoothDevice)
  {
    Intent localIntent = new Intent("ARCBLEAudio-From-Service-Events");
    localIntent.putExtra("device", paramBluetoothDevice);
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
  }
  
  public void sendMsg(int paramInt)
  {
    Intent localIntent = new Intent("ARCBLEAudio-From-Service-Events");
    localIntent.putExtra("message", paramInt);
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
  }
  
  public void sendPaired(BluetoothDevice paramBluetoothDevice)
  {
    Intent localIntent = new Intent("ARCBLEAudio-From-Service-Events");
    localIntent.putExtra("paired", paramBluetoothDevice);
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\audio\AdvancedRemoteBLEAudioService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */