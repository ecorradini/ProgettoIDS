package com.ti.ble.btsig;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.BluetoothLEController.BluetoothLEManager;
import com.ti.ble.common.GenericBluetoothProfile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DeviceInformationServiceProfile
  extends GenericBluetoothProfile
{
  public static final String ACTION_FW_REV_UPDATED = "com.ti.ble.btsig.ACTION_FW_REV_UPDATED";
  public static final String EXTRA_FW_REV_STRING = "com.ti.ble.btsig.EXTRA_FW_REV_STRING";
  public static final String dISFirmwareREV_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
  public static final String dISHardwareREV_UUID = "00002a27-0000-1000-8000-00805f9b34fb";
  public static final String dISManifacturerNAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb";
  public static final String dISModelNR_UUID = "00002a24-0000-1000-8000-00805f9b34fb";
  public static final String dISSerialNR_UUID = "00002a25-0000-1000-8000-00805f9b34fb";
  public static final String dISService_UUID = "0000180a-0000-1000-8000-00805f9b34fb";
  public static final String dISSoftwareREV_UUID = "00002a28-0000-1000-8000-00805f9b34fb";
  public static final String dISSystemID_UUID = "00002a23-0000-1000-8000-00805f9b34fb";
  BluetoothGattCharacteristic ManifacturerNAMEc;
  BluetoothLEDevice dev;
  BluetoothGattCharacteristic firmwareREVc;
  BluetoothGattCharacteristic hardwareREVc;
  BluetoothLEManager man;
  BluetoothGattCharacteristic modelNRc;
  BluetoothGattCharacteristic serialNRc;
  BluetoothGattCharacteristic softwareREVc;
  BluetoothGattCharacteristic systemIDc;
  DeviceInformationServiceTableRow tRow;
  
  public DeviceInformationServiceProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new DeviceInformationServiceTableRow(paramContext);
    this.man = BluetoothLEManager.getInstance(paramContext);
    try
    {
      this.dev = this.man.deviceForBluetoothDev(paramBluetoothDevice);
      paramContext = this.mBTService.getCharacteristics().iterator();
      while (paramContext.hasNext())
      {
        paramBluetoothDevice = (BluetoothGattCharacteristic)paramContext.next();
        if (paramBluetoothDevice.getUuid().toString().equals("00002a23-0000-1000-8000-00805f9b34fb")) {
          this.systemIDc = paramBluetoothDevice;
        }
        if (paramBluetoothDevice.getUuid().toString().equals("00002a24-0000-1000-8000-00805f9b34fb")) {
          this.modelNRc = paramBluetoothDevice;
        }
        if (paramBluetoothDevice.getUuid().toString().equals("00002a25-0000-1000-8000-00805f9b34fb")) {
          this.serialNRc = paramBluetoothDevice;
        }
        if (paramBluetoothDevice.getUuid().toString().equals("00002a26-0000-1000-8000-00805f9b34fb")) {
          this.firmwareREVc = paramBluetoothDevice;
        }
        if (paramBluetoothDevice.getUuid().toString().equals("00002a27-0000-1000-8000-00805f9b34fb")) {
          this.hardwareREVc = paramBluetoothDevice;
        }
        if (paramBluetoothDevice.getUuid().toString().equals("00002a28-0000-1000-8000-00805f9b34fb")) {
          this.softwareREVc = paramBluetoothDevice;
        }
        if (paramBluetoothDevice.getUuid().toString().equals("00002a29-0000-1000-8000-00805f9b34fb")) {
          this.ManifacturerNAMEc = paramBluetoothDevice;
        }
      }
    }
    catch (Exception paramContext)
    {
      for (;;)
      {
        paramContext.printStackTrace();
      }
      this.tRow.setIcon(getIconPrefix(), paramBluetoothGattService.getUuid().toString(), "deviceinfo");
      this.tRow.title.setText("Device Information Service");
    }
  }
  
  private String getValueSafe(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
    paramBluetoothGattCharacteristic = arrayOfByte;
    if (arrayOfByte == null) {
      paramBluetoothGattCharacteristic = "N/A".getBytes(Charset.forName("UTF-8"));
    }
    try
    {
      paramBluetoothGattCharacteristic = new String(paramBluetoothGattCharacteristic, "UTF-8");
      return paramBluetoothGattCharacteristic;
    }
    catch (UnsupportedEncodingException paramBluetoothGattCharacteristic)
    {
      paramBluetoothGattCharacteristic.printStackTrace();
    }
    return "";
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("0000180a-0000-1000-8000-00805f9b34fb") == 0;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    int i = 0;
    Object localObject;
    if ((this.systemIDc != null) && (paramBluetoothGattCharacteristic != null) && (paramBluetoothGattCharacteristic.equals(this.systemIDc)))
    {
      String str1 = "System ID: ";
      localObject = str1;
      try
      {
        byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
        localObject = str1;
        int j = arrayOfByte.length;
        for (;;)
        {
          localObject = str1;
          if (i >= j) {
            break;
          }
          byte b = arrayOfByte[i];
          localObject = str1;
          str1 = str1 + String.format("%02x:", new Object[] { Byte.valueOf(b) });
          i += 1;
        }
        if (this.modelNRc == null) {
          break label179;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        this.tRow.SystemIDLabel.setText((CharSequence)localObject);
      }
    }
    if (paramBluetoothGattCharacteristic.equals(this.modelNRc)) {
      this.tRow.ModelNRLabel.setText("Model NR: " + getValueSafe(paramBluetoothGattCharacteristic));
    }
    label179:
    if ((this.serialNRc != null) && (paramBluetoothGattCharacteristic.equals(this.serialNRc))) {
      this.tRow.SerialNRLabel.setText("Serial NR: " + getValueSafe(paramBluetoothGattCharacteristic));
    }
    if ((this.firmwareREVc != null) && (paramBluetoothGattCharacteristic.equals(this.firmwareREVc)))
    {
      String str2 = getValueSafe(paramBluetoothGattCharacteristic);
      this.tRow.FirmwareREVLabel.setText("Firmware Revision: " + str2);
      localObject = new Intent("com.ti.ble.btsig.ACTION_FW_REV_UPDATED");
      ((Intent)localObject).putExtra("com.ti.ble.btsig.EXTRA_FW_REV_STRING", str2);
      this.context.sendBroadcast((Intent)localObject);
    }
    if ((this.hardwareREVc != null) && (paramBluetoothGattCharacteristic.equals(this.hardwareREVc))) {
      this.tRow.HardwareREVLabel.setText("Hardware Revision: " + getValueSafe(paramBluetoothGattCharacteristic));
    }
    if ((this.softwareREVc != null) && (paramBluetoothGattCharacteristic.equals(this.softwareREVc))) {
      this.tRow.SoftwareREVLabel.setText("Software Revision: " + getValueSafe(paramBluetoothGattCharacteristic));
    }
    if ((this.ManifacturerNAMEc != null) && (paramBluetoothGattCharacteristic.equals(this.ManifacturerNAMEc))) {
      this.tRow.ManifacturerNAMELabel.setText("Manufacturer Name: " + getValueSafe(paramBluetoothGattCharacteristic));
    }
  }
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void didWriteValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void disableService() {}
  
  public void enableService()
  {
    this.dev.readCharacteristicSync(this.systemIDc);
    this.dev.readCharacteristicSync(this.modelNRc);
    this.dev.readCharacteristicSync(this.serialNRc);
    this.dev.readCharacteristicSync(this.firmwareREVc);
    this.dev.readCharacteristicSync(this.hardwareREVc);
    this.dev.readCharacteristicSync(this.softwareREVc);
    this.dev.readCharacteristicSync(this.ManifacturerNAMEc);
  }
  
  public String getIconPrefix()
  {
    if (this.mBTDevice.getName() == null) {
      return "sensortag2_";
    }
    if ((this.mBTDevice.getName().equals("CC2650 SensorTag")) || (this.mBTDevice.getName().equals("CC1350 SensorTag"))) {
      return "sensortag2_";
    }
    if (this.mBTDevice.getName().equals("SensorTag")) {
      return "sensortag_";
    }
    if ((this.mBTDevice.getName().equals("CC2650 RC")) || (this.mBTDevice.getName().equals("HID AdvRemote"))) {
      return "cc2650_rc_";
    }
    if ((this.mBTDevice.getName().equals("CC2650 LaunchPad")) || (this.mBTDevice.getName().equals("CC1350 LaunchPad"))) {
      return "cc2650_launchpad_";
    }
    if (this.mBTDevice.getName().equals("Throughput Periph")) {
      return "cc2650_launchpad_";
    }
    if ((this.mBTDevice.getName().equalsIgnoreCase("ProjectZero")) || (this.mBTDevice.getName().equalsIgnoreCase("Project Zero"))) {
      return "prz_";
    }
    return "";
  }
  
  public TableRow getTableRow()
  {
    return this.tRow;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\btsig\DeviceInformationServiceProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */