package com.ti.ble.BluetoothLEController;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;
import com.ti.ble.btsig.DeviceInformationServiceProfile;
import com.ti.ble.common.GattInfo;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.ble.launchpad.ProjectZeroLEDProfile;
import com.ti.ble.launchpad.ProjectZeroSwitchProfile;
import com.ti.ble.sensortag.SensorTagAccelerometerProfile;
import com.ti.ble.sensortag.SensorTagAmbientTemperatureProfile;
import com.ti.ble.sensortag.SensorTagBarometerProfile;
import com.ti.ble.sensortag.SensorTagDisplayProfile;
import com.ti.ble.sensortag.SensorTagHumidityProfile;
import com.ti.ble.sensortag.SensorTagIRTemperatureProfile;
import com.ti.ble.sensortag.SensorTagLuxometerProfile;
import com.ti.ble.sensortag.SensorTagMovementProfile;
import com.ti.ble.sensortag.SensorTagSimpleKeysProfile;
import com.ti.ble.ti.profiles.TIAudioProfile;
import com.ti.ble.ti.profiles.TIConnectionControlService;
import com.ti.ble.ti.profiles.TILampControlProfile;
import com.ti.ble.ti.profiles.TIOADProfile;
import com.ti.ble.ti.profiles.ThroughputTestService;
import com.ti.util.PreferenceWR;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BluetoothLEDevice
{
  public static final int CONNECTION_TIMEOUT = 5000;
  public static final int DISCOVERY_TIMEOUT = 10000;
  public static final int MAX_RETRIES = 4;
  static final String TAG = BluetoothLEDevice.class.getSimpleName();
  BluetoothGattCallback BluetoothLEDeviceCB = new BluetoothGattCallback()
  {
    public void onCharacteristicChanged(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic)
    {
      super.onCharacteristicChanged(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGattCharacteristic);
      if (BluetoothLEDevice.this.myCB != null) {
        BluetoothLEDevice.this.myCB.didUpdateCharacteristicData(BluetoothLEDevice.this.mThis, paramAnonymousBluetoothGattCharacteristic);
      }
    }
    
    public void onCharacteristicRead(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic, int paramAnonymousInt)
    {
      super.onCharacteristicRead(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGattCharacteristic, paramAnonymousInt);
      if ((BluetoothLEDevice.this.currentTransaction != null) && (BluetoothLEDevice.this.currentTransaction.transactionFinished != true))
      {
        BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
        BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
        BluetoothLEDevice.this.currentTransaction = null;
      }
      Log.d(BluetoothLEDevice.TAG, "onCharacteristicRead: Read " + paramAnonymousBluetoothGattCharacteristic.getUuid().toString());
      if (BluetoothLEDevice.this.myCB != null) {
        BluetoothLEDevice.this.myCB.didReadCharacteristicData(BluetoothLEDevice.this.mThis, paramAnonymousBluetoothGattCharacteristic);
      }
    }
    
    public void onCharacteristicWrite(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic, int paramAnonymousInt)
    {
      super.onCharacteristicWrite(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGattCharacteristic, paramAnonymousInt);
      if ((BluetoothLEDevice.this.currentTransaction != null) && (BluetoothLEDevice.this.currentTransaction.transactionFinished != true))
      {
        BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
        BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
        BluetoothLEDevice.this.currentTransaction = null;
      }
      Log.d(BluetoothLEDevice.TAG, "onCharacteristicWrite: Wrote to " + paramAnonymousBluetoothGattCharacteristic.getUuid().toString());
      if (BluetoothLEDevice.this.myCB != null) {
        BluetoothLEDevice.this.myCB.didWriteCharacteristicData(BluetoothLEDevice.this.mThis, paramAnonymousBluetoothGattCharacteristic);
      }
    }
    
    public void onConnectionStateChange(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      super.onConnectionStateChange(paramAnonymousBluetoothGatt, paramAnonymousInt1, paramAnonymousInt2);
      if (paramAnonymousInt2 == 2)
      {
        Log.d(BluetoothLEDevice.TAG, "Device " + paramAnonymousBluetoothGatt.getDevice().getAddress().toString() + " CONNECTED");
        BluetoothLEDevice.this.isConnected = true;
        if (!BluetoothLEDevice.this.refreshDeviceCache()) {}
      }
      try
      {
        Thread.sleep(1000L);
        paramAnonymousBluetoothGatt.discoverServices();
        if (BluetoothLEDevice.this.mThis.g == null)
        {
          BluetoothLEDevice.this.mThis.g = paramAnonymousBluetoothGatt;
          Log.d(BluetoothLEDevice.TAG, "Did not have BluetoothGatt Property set correctly !");
        }
        do
        {
          return;
          if (paramAnonymousInt2 == 3)
          {
            Log.d(BluetoothLEDevice.TAG, "Device " + paramAnonymousBluetoothGatt.getDevice().getAddress().toString() + " DISCONNECTING");
            BluetoothLEDevice.this.isConnected = false;
            BluetoothLEDevice.this.isDiscovered = false;
            return;
          }
        } while (paramAnonymousInt2 != 0);
        BluetoothLEDevice.this.isConnected = false;
        BluetoothLEDevice.this.isDiscovered = false;
        Log.d(BluetoothLEDevice.TAG, "Device " + paramAnonymousBluetoothGatt.getDevice().getAddress().toString() + " DISCONNECTED");
        if (BluetoothLEDevice.this.shouldReconnect)
        {
          BluetoothLEDevice.this.connectDevice();
          BluetoothLEDevice.this.isConnected = false;
          return;
        }
        paramAnonymousBluetoothGatt.close();
        BluetoothLEDevice.this.mThis.myCB.deviceDidDisconnect(BluetoothLEDevice.this.mThis);
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        for (;;) {}
      }
    }
    
    public void onDescriptorRead(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor, int paramAnonymousInt)
    {
      super.onDescriptorRead(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGattDescriptor, paramAnonymousInt);
      if ((BluetoothLEDevice.this.currentTransaction != null) && (BluetoothLEDevice.this.currentTransaction.transactionFinished != true))
      {
        BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
        BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
        BluetoothLEDevice.this.currentTransaction = null;
      }
      Log.d(BluetoothLEDevice.TAG, "onDescriptorRead: Read " + paramAnonymousBluetoothGattDescriptor.getUuid().toString());
      if (BluetoothLEDevice.this.myCB != null) {
        BluetoothLEDevice.this.myCB.didReadDescriptor(BluetoothLEDevice.this.mThis, paramAnonymousBluetoothGattDescriptor);
      }
    }
    
    public void onDescriptorWrite(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor, int paramAnonymousInt)
    {
      super.onDescriptorWrite(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGattDescriptor, paramAnonymousInt);
      if ((BluetoothLEDevice.this.currentTransaction != null) && (BluetoothLEDevice.this.currentTransaction.transactionFinished != true))
      {
        BluetoothLEDevice.this.currentTransaction.transactionFinished = true;
        BluetoothLEDevice.this.deviceTransactions.remove(BluetoothLEDevice.this.currentTransaction);
        BluetoothLEDevice.this.currentTransaction = null;
      }
      Log.d(BluetoothLEDevice.TAG, "onDescriptorWrite: Wrote to " + paramAnonymousBluetoothGattDescriptor.getCharacteristic().getUuid().toString());
      if (BluetoothLEDevice.this.myCB != null) {
        BluetoothLEDevice.this.myCB.didUpdateCharacteristicNotification(BluetoothLEDevice.this.mThis, paramAnonymousBluetoothGattDescriptor.getCharacteristic());
      }
    }
    
    public void onMtuChanged(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      Log.d(BluetoothLEDevice.TAG, "onMtuChanged: Got new MTU setting : MTU = " + paramAnonymousInt1 + "status = " + paramAnonymousInt2);
      super.onMtuChanged(paramAnonymousBluetoothGatt, paramAnonymousInt1, paramAnonymousInt2);
      BluetoothLEDevice.this.myCB.mtuValueChanged(paramAnonymousInt1);
    }
    
    public void onReadRemoteRssi(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      super.onReadRemoteRssi(paramAnonymousBluetoothGatt, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onReliableWriteCompleted(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt)
    {
      super.onReliableWriteCompleted(paramAnonymousBluetoothGatt, paramAnonymousInt);
    }
    
    public void onServicesDiscovered(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt)
    {
      super.onServicesDiscovered(paramAnonymousBluetoothGatt, paramAnonymousInt);
      Log.d(BluetoothLEDevice.TAG, "Device " + paramAnonymousBluetoothGatt.getDevice().getAddress().toString() + " SERVICES DISCOVEREDStatus" + paramAnonymousInt);
      if (paramAnonymousInt != 0) {
        Log.d(BluetoothLEDevice.TAG, "Device " + paramAnonymousBluetoothGatt.getDevice().getAddress().toString() + "Service Discovery FAILED !");
      }
      do
      {
        return;
        BluetoothLEDevice.this.services = paramAnonymousBluetoothGatt.getServices();
        paramAnonymousBluetoothGatt = BluetoothLEDevice.this.services.iterator();
        while (paramAnonymousBluetoothGatt.hasNext())
        {
          Iterator localIterator = ((BluetoothGattService)paramAnonymousBluetoothGatt.next()).getCharacteristics().iterator();
          while (localIterator.hasNext())
          {
            BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)localIterator.next();
            BluetoothLEDevice.this.chars.add(localBluetoothGattCharacteristic);
          }
        }
        BluetoothLEDevice.this.PrintAllServicesAndCharacteristics();
        BluetoothLEDevice.this.TransactionHandlerThread = new Thread(BluetoothLEDevice.this.deviceTransactionHandler);
        BluetoothLEDevice.this.TransactionHandlerThread.start();
        BluetoothLEDevice.this.isDiscovered = true;
      } while (BluetoothLEDevice.this.myCB == null);
      BluetoothLEDevice.this.myCB.deviceReady(BluetoothLEDevice.this.mThis);
    }
  };
  Thread TransactionHandlerThread;
  public EddystoneBeaconDecoder beaconDecoder;
  Context c;
  public List<BluetoothGattCharacteristic> chars;
  int currentConnectionPriority = 0;
  BluetoothLETransaction currentTransaction;
  public BluetoothDevice d;
  BluetoothLEDeviceDebugVariables dVars;
  public Runnable deviceTransactionHandler = new Runnable()
  {
    public void run()
    {
      Log.d(BluetoothLEDevice.TAG, "deviceTransactionHandler started for device : " + BluetoothLEDevice.this.mThis.d.getAddress().toString());
      while (!BluetoothLEDevice.this.stopTransactionHandler)
      {
        if (BluetoothLEDevice.this.currentTransaction != null)
        {
          long l1 = BluetoothLEDevice.this.currentTransaction.transactionStartDate.getTime();
          long l2 = new Date().getTime();
          if (Math.abs(l1 - l2) > 5000L)
          {
            Log.d(BluetoothLEDevice.TAG, "Transaction has used more than " + Math.abs(l1 - l2) / 1000L + " seconds to complete !");
            BluetoothLEDevice.this.currentTransaction = null;
          }
        }
        else if (BluetoothLEDevice.this.deviceTransactions.size() > 0)
        {
          BluetoothLEDevice.this.currentTransaction = ((BluetoothLETransaction)BluetoothLEDevice.this.deviceTransactions.get(0));
          BluetoothLEDevice.this.currentTransaction.transactionStartDate = new Date();
          if (!BluetoothLEDevice.this.commitTransactionToBT(BluetoothLEDevice.this.currentTransaction))
          {
            BluetoothLEDevice.this.currentTransaction = null;
            continue;
          }
        }
        try
        {
          Thread.sleep(10L, 0);
        }
        catch (InterruptedException localInterruptedException)
        {
          Log.d(BluetoothLEDevice.TAG, "deviceTransactionHandler: interrupted while running");
        }
      }
    }
  };
  ArrayList<BluetoothLETransaction> deviceTransactions;
  public BluetoothGatt g;
  public boolean isConnected;
  public boolean isDiscovered;
  public BluetoothManager m;
  BluetoothLEDevice mThis;
  public BluetoothLEDeviceCB myCB;
  public boolean needsBroadcastScreen;
  public ScanResult sR;
  public List<BluetoothGattService> services;
  public boolean shouldReconnect;
  boolean stopTransactionHandler = false;
  
  public BluetoothLEDevice(BluetoothDevice paramBluetoothDevice, Context paramContext)
  {
    this.d = paramBluetoothDevice;
    this.c = paramContext;
    this.dVars = new BluetoothLEDeviceDebugVariables(null);
    this.beaconDecoder = new EddystoneBeaconDecoder();
    this.chars = new ArrayList();
    this.deviceTransactions = new ArrayList();
    this.currentTransaction = null;
    this.mThis = this;
  }
  
  public void PrintAllServicesAndCharacteristics()
  {
    int i = 0;
    Iterator localIterator = this.services.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (BluetoothGattService)localIterator.next();
      Log.d(TAG, "Service[" + i + "] : " + ((BluetoothGattService)localObject).getUuid().toString());
      int j = 0;
      localObject = ((BluetoothGattService)localObject).getCharacteristics().iterator();
      while (((Iterator)localObject).hasNext())
      {
        BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)((Iterator)localObject).next();
        Log.d(TAG, "    Characteristic[" + j + "] : " + localBluetoothGattCharacteristic.getUuid().toString());
        j += 1;
      }
      i += 1;
    }
  }
  
  public boolean commitTransactionToBT(BluetoothLETransaction paramBluetoothLETransaction)
  {
    boolean bool = false;
    if ((paramBluetoothLETransaction.characteristic == null) && (paramBluetoothLETransaction.descriptor == null)) {
      return false;
    }
    switch (paramBluetoothLETransaction.transactionType)
    {
    }
    for (;;)
    {
      return bool;
      this.g.setCharacteristicNotification(paramBluetoothLETransaction.characteristic, true);
      BluetoothGattDescriptor localBluetoothGattDescriptor = paramBluetoothLETransaction.characteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
      if (localBluetoothGattDescriptor == null)
      {
        Log.d(TAG, "Set Notification failed for :" + paramBluetoothLETransaction.characteristic.getUuid().toString());
        bool = true;
      }
      else
      {
        localBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        this.g.writeDescriptor(localBluetoothGattDescriptor);
        bool = true;
        continue;
        this.g.setCharacteristicNotification(paramBluetoothLETransaction.characteristic, true);
        localBluetoothGattDescriptor = paramBluetoothLETransaction.characteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
        if (localBluetoothGattDescriptor == null)
        {
          Log.d(TAG, "Set Indication failed for :" + paramBluetoothLETransaction.characteristic.getUuid().toString());
          bool = true;
        }
        else
        {
          localBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
          this.g.writeDescriptor(localBluetoothGattDescriptor);
          bool = true;
          continue;
          this.g.setCharacteristicNotification(paramBluetoothLETransaction.characteristic, false);
          paramBluetoothLETransaction = paramBluetoothLETransaction.characteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
          paramBluetoothLETransaction.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
          this.g.writeDescriptor(paramBluetoothLETransaction);
          bool = true;
          continue;
          this.g.setCharacteristicNotification(paramBluetoothLETransaction.characteristic, false);
          paramBluetoothLETransaction = paramBluetoothLETransaction.characteristic.getDescriptor(GattInfo.CLIENT_CHARACTERISTIC_CONFIG);
          paramBluetoothLETransaction.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
          this.g.writeDescriptor(paramBluetoothLETransaction);
          bool = true;
          continue;
          bool = this.g.readCharacteristic(paramBluetoothLETransaction.characteristic);
          continue;
          paramBluetoothLETransaction.characteristic.setValue(paramBluetoothLETransaction.dat);
          bool = this.g.writeCharacteristic(paramBluetoothLETransaction.characteristic);
          continue;
          bool = this.g.readDescriptor(paramBluetoothLETransaction.descriptor);
        }
      }
    }
  }
  
  public void connectDevice()
  {
    Object localObject = this.dVars;
    ((BluetoothLEDeviceDebugVariables)localObject).connectionCalls += 1;
    localObject = Thread.currentThread().getStackTrace();
    if (localObject.length > 3) {
      Log.d(TAG, "Connect called from : " + localObject[3].getClassName() + " " + localObject[3].getMethodName());
    }
    new Thread(new Runnable()
    {
      public void run()
      {
        int i = 0;
        int j;
        if ((i < 5) && (!BluetoothLEDevice.this.isConnected))
        {
          j = 5000;
          BluetoothLEDevice.this.g = BluetoothLEDevice.this.d.connectGatt(BluetoothLEDevice.this.c, false, BluetoothLEDevice.this.BluetoothLEDeviceCB);
          label53:
          if (BluetoothLEDevice.this.isConnected) {}
        }
        label225:
        label406:
        for (;;)
        {
          int k;
          try
          {
            Thread.sleep(1L, 0);
            k = j - 1;
            if ((BluetoothLEDevice.this.myCB != null) && (k % 250 == 0)) {
              BluetoothLEDevice.this.myCB.waitingForConnect(BluetoothLEDevice.this.mThis, k, i);
            }
            j = k;
            if (k >= 0) {
              break label53;
            }
            j = k;
            if (BluetoothLEDevice.this.g == null) {
              break label53;
            }
            Log.d(BluetoothLEDevice.TAG, "Timeout while connecting");
            BluetoothLEDevice.this.g.disconnect();
            if (i == 4)
            {
              if (BluetoothLEDevice.this.myCB != null) {
                BluetoothLEDevice.this.myCB.deviceConnectTimedOut(BluetoothLEDevice.this.mThis);
              }
              return;
            }
          }
          catch (Exception localException1)
          {
            Log.d(BluetoothLEDevice.TAG, "Interrupted while waiting for connect");
            continue;
          }
          i += 1;
          break;
          i = 0;
          for (;;)
          {
            if ((i >= 5) || (BluetoothLEDevice.this.isDiscovered)) {
              break label406;
            }
            j = 10000;
            if (!BluetoothLEDevice.this.isDiscovered) {
              try
              {
                Thread.sleep(1L, 0);
                k = j - 1;
                if ((BluetoothLEDevice.this.myCB != null) && (k % 250 == 0)) {
                  BluetoothLEDevice.this.myCB.waitingForDiscovery(BluetoothLEDevice.this.mThis, k, i);
                }
                j = k;
                if (k >= 0) {
                  break label225;
                }
                j = k;
                if (BluetoothLEDevice.this.g == null) {
                  break label225;
                }
                Log.d(BluetoothLEDevice.TAG, "Timeout while discovering services");
                BluetoothLEDevice.this.g.disconnect();
                if (i == 4)
                {
                  if (BluetoothLEDevice.this.myCB == null) {
                    break;
                  }
                  BluetoothLEDevice.this.myCB.deviceDiscoveryTimedOut(BluetoothLEDevice.this.mThis);
                  return;
                }
              }
              catch (Exception localException2)
              {
                for (;;)
                {
                  Log.d(BluetoothLEDevice.TAG, "Interrupted while waiting for service discovery");
                }
                BluetoothLEDevice.this.g = BluetoothLEDevice.this.d.connectGatt(BluetoothLEDevice.this.c, false, BluetoothLEDevice.this.BluetoothLEDeviceCB);
              }
            }
            i += 1;
          }
        }
      }
    }).start();
  }
  
  public void disconnectDevice()
  {
    Object localObject = this.dVars;
    ((BluetoothLEDeviceDebugVariables)localObject).disconnectionCalls += 1;
    this.stopTransactionHandler = true;
    localObject = Thread.currentThread().getStackTrace();
    if (localObject.length > 3) {
      Log.d(TAG, "Disconnect called from : " + localObject[3].getClassName() + " " + localObject[3].getMethodName());
    }
    if (this.g != null)
    {
      this.g.disconnect();
      return;
    }
    Log.d(TAG, "FAILURE !!!! Device did not have a BluetoothGatt when isConnected = true !");
    try
    {
      Log.d(TAG, "Current state is: " + this.m.getConnectionState(this.d, 7));
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      Log.d(TAG, "Not able to read state, device was already null !");
    }
  }
  
  public GenericBluetoothProfile getBluetoothProfileForUUID(BluetoothGattService paramBluetoothGattService)
  {
    if (SensorTagAmbientTemperatureProfile.isCorrectService(paramBluetoothGattService))
    {
      Log.d(TAG, "Found SensorTag Ambient Temperature Service !");
      return new SensorTagAmbientTemperatureProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagIRTemperatureProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagIRTemperatureProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagAccelerometerProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagAccelerometerProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagBarometerProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagBarometerProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagDisplayProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagDisplayProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagHumidityProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagHumidityProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagLuxometerProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagLuxometerProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagMovementProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagMovementProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (SensorTagSimpleKeysProfile.isCorrectService(paramBluetoothGattService)) {
      return new SensorTagSimpleKeysProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (DeviceInformationServiceProfile.isCorrectService(paramBluetoothGattService)) {
      return new DeviceInformationServiceProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (TIAudioProfile.isCorrectService(paramBluetoothGattService)) {
      return new TIAudioProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (TIConnectionControlService.isCorrectService(paramBluetoothGattService)) {
      return new TIConnectionControlService(this.c, this.d, paramBluetoothGattService);
    }
    if (TILampControlProfile.isCorrectService(paramBluetoothGattService)) {
      return new TILampControlProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (TIOADProfile.isCorrectService(paramBluetoothGattService)) {
      return new TIOADProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (ThroughputTestService.isCorrectService(paramBluetoothGattService)) {
      return new ThroughputTestService(this.c, this.d, paramBluetoothGattService);
    }
    if (ProjectZeroLEDProfile.isCorrectService(paramBluetoothGattService)) {
      return new ProjectZeroLEDProfile(this.c, this.d, paramBluetoothGattService);
    }
    if (ProjectZeroSwitchProfile.isCorrectService(paramBluetoothGattService)) {
      return new ProjectZeroSwitchProfile(this.c, this.d, paramBluetoothGattService);
    }
    return null;
  }
  
  public int getCurrentConnectionPriority()
  {
    return this.currentConnectionPriority;
  }
  
  public boolean is1350Launchpad()
  {
    return (this.d.getName() != null) && (this.d.getName().equals("CC1350 LaunchPad"));
  }
  
  public boolean is2650Launchpad()
  {
    return (this.d.getName() != null) && ((this.d.getName().equals("Launchpad")) || (this.d.getName().equals("CC2650 LaunchPad")));
  }
  
  public boolean isCC2650Programmer()
  {
    return (this.d.getName() != null) && (this.d.getName().equalsIgnoreCase("CC2650 Programmer"));
  }
  
  public boolean isCC2650RC()
  {
    return (this.d.getName() != null) && ((this.d.getName().equals("CC2650 RC")) || (this.d.getName().equals("HID AdvRemote")));
  }
  
  public boolean isSensorTag2()
  {
    return (this.d.getName() != null) && ((this.d.getName().equals("SensorTag2")) || (this.d.getName().equals("CC2650 SensorTag")) || (this.d.getName().equals("CC1350 SensorTag")));
  }
  
  public int readCharacteristicAsync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.READ_SYNC, null);
    this.deviceTransactions.add(paramBluetoothGattCharacteristic);
    while (!paramBluetoothGattCharacteristic.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    return 0;
  }
  
  public int readCharacteristicSync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.READ_SYNC, null);
    this.deviceTransactions.add(paramBluetoothGattCharacteristic);
    while (!paramBluetoothGattCharacteristic.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    return 0;
  }
  
  public int readDescriptorSync(BluetoothGattDescriptor paramBluetoothGattDescriptor)
  {
    paramBluetoothGattDescriptor = new BluetoothLETransaction(this, paramBluetoothGattDescriptor, BluetoothLETransaction.BluetoothLETransactionType.READ_DESC_SYNC, null);
    this.deviceTransactions.add(paramBluetoothGattDescriptor);
    while (!paramBluetoothGattDescriptor.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    return 0;
  }
  
  public boolean refreshDeviceCache()
  {
    PreferenceWR localPreferenceWR = new PreferenceWR(this.d.getAddress(), this.c);
    if (localPreferenceWR.getBooleanPreference("refresh") == true)
    {
      Log.d(TAG, "Device needs refresh !");
      for (;;)
      {
        try
        {
          Method localMethod = this.g.getClass().getMethod("refresh", new Class[0]);
          if (localMethod != null)
          {
            if (!((Boolean)localMethod.invoke(this.g, new Object[0])).booleanValue()) {
              continue;
            }
            Log.d(TAG, "Refreshed device before scanning !");
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          continue;
        }
        localPreferenceWR.setBooleanPreference("refresh", false);
        return true;
        Log.d(TAG, "Unable to refresh device");
      }
    }
    Log.d(TAG, "No device refresh neccessary");
    return false;
  }
  
  public boolean requestMTUChange(int paramInt)
  {
    return this.g.requestMtu(paramInt);
  }
  
  public int setCharacteristicIndicationSync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.ENABLE_INDICATION_SYNC, null);; paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.DISABLE_INDICATION_SYNC, null))
    {
      this.deviceTransactions.add(paramBluetoothGattCharacteristic);
      while (!paramBluetoothGattCharacteristic.transactionFinished) {
        try
        {
          Thread.sleep(20L, 0);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
    return 0;
  }
  
  public int setCharacteristicNotificationAsync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (BluetoothLETransaction.BluetoothLETransactionType localBluetoothLETransactionType = BluetoothLETransaction.BluetoothLETransactionType.ENABLE_NOTIFICATION_ASYNC;; localBluetoothLETransactionType = BluetoothLETransaction.BluetoothLETransactionType.DISABLE_NOTIFICATION_ASYNC)
    {
      paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, localBluetoothLETransactionType, null);
      this.deviceTransactions.add(paramBluetoothGattCharacteristic);
      return 0;
    }
  }
  
  public int setCharacteristicNotificationSync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean)
  {
    if (paramBoolean) {}
    BluetoothLETransaction.BluetoothLETransactionType localBluetoothLETransactionType2;
    for (BluetoothLETransaction.BluetoothLETransactionType localBluetoothLETransactionType1 = BluetoothLETransaction.BluetoothLETransactionType.ENABLE_NOTIFICATION_SYNC;; localBluetoothLETransactionType2 = BluetoothLETransaction.BluetoothLETransactionType.DISABLE_NOTIFICATION_SYNC)
    {
      paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, localBluetoothLETransactionType1, null);
      this.deviceTransactions.add(paramBluetoothGattCharacteristic);
      while (!paramBluetoothGattCharacteristic.transactionFinished) {
        try
        {
          Thread.sleep(20L, 0);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
    return 0;
  }
  
  public boolean setCurrentConnectionPriority(int paramInt)
  {
    if (this.g.requestConnectionPriority(paramInt))
    {
      this.currentConnectionPriority = paramInt;
      return true;
    }
    return false;
  }
  
  public int writeCharacteristicAsync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, byte paramByte)
  {
    paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.WRITE_SYNC, new byte[] { paramByte });
    this.deviceTransactions.add(paramBluetoothGattCharacteristic);
    while (!paramBluetoothGattCharacteristic.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    return 0;
  }
  
  public int writeCharacteristicAsync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, byte[] paramArrayOfByte)
  {
    paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.WRITE_SYNC, paramArrayOfByte);
    this.deviceTransactions.add(paramBluetoothGattCharacteristic);
    while (!paramBluetoothGattCharacteristic.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException paramArrayOfByte) {}
    }
    return 0;
  }
  
  public int writeCharacteristicSync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, byte paramByte)
  {
    paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.WRITE_SYNC, new byte[] { paramByte });
    this.deviceTransactions.add(paramBluetoothGattCharacteristic);
    while (!paramBluetoothGattCharacteristic.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    return 0;
  }
  
  public int writeCharacteristicSync(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, byte[] paramArrayOfByte)
  {
    paramBluetoothGattCharacteristic = new BluetoothLETransaction(this, paramBluetoothGattCharacteristic, BluetoothLETransaction.BluetoothLETransactionType.WRITE_SYNC, paramArrayOfByte);
    this.deviceTransactions.add(paramBluetoothGattCharacteristic);
    while (!paramBluetoothGattCharacteristic.transactionFinished) {
      try
      {
        Thread.sleep(20L, 0);
      }
      catch (InterruptedException paramArrayOfByte) {}
    }
    return 0;
  }
  
  public static abstract interface BluetoothLEDeviceCB
  {
    public abstract void deviceConnectTimedOut(BluetoothLEDevice paramBluetoothLEDevice);
    
    public abstract void deviceDidDisconnect(BluetoothLEDevice paramBluetoothLEDevice);
    
    public abstract void deviceDiscoveryTimedOut(BluetoothLEDevice paramBluetoothLEDevice);
    
    public abstract void deviceFailed(BluetoothLEDevice paramBluetoothLEDevice);
    
    public abstract void deviceReady(BluetoothLEDevice paramBluetoothLEDevice);
    
    public abstract void didReadCharacteristicData(BluetoothLEDevice paramBluetoothLEDevice, BluetoothGattCharacteristic paramBluetoothGattCharacteristic);
    
    public abstract void didReadDescriptor(BluetoothLEDevice paramBluetoothLEDevice, BluetoothGattDescriptor paramBluetoothGattDescriptor);
    
    public abstract void didUpdateCharacteristicData(BluetoothLEDevice paramBluetoothLEDevice, BluetoothGattCharacteristic paramBluetoothGattCharacteristic);
    
    public abstract void didUpdateCharacteristicIndication(BluetoothLEDevice paramBluetoothLEDevice);
    
    public abstract void didUpdateCharacteristicNotification(BluetoothLEDevice paramBluetoothLEDevice, BluetoothGattCharacteristic paramBluetoothGattCharacteristic);
    
    public abstract void didWriteCharacteristicData(BluetoothLEDevice paramBluetoothLEDevice, BluetoothGattCharacteristic paramBluetoothGattCharacteristic);
    
    public abstract void mtuValueChanged(int paramInt);
    
    public abstract void waitingForConnect(BluetoothLEDevice paramBluetoothLEDevice, int paramInt1, int paramInt2);
    
    public abstract void waitingForDiscovery(BluetoothLEDevice paramBluetoothLEDevice, int paramInt1, int paramInt2);
  }
  
  private class BluetoothLEDeviceDebugVariables
  {
    public int connectionCalls;
    public int disconnectionCalls;
    public int reads;
    public int writes;
    
    private BluetoothLEDeviceDebugVariables() {}
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\BluetoothLEController\BluetoothLEDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */