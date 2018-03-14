package com.ti.ble.BluetoothLEController;

import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.common.GattInfo;
import com.ti.util.bleUtility;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EddystoneBeaconDecoder
{
  public static final int MAXSUFFIXVALUE = 13;
  public static final String TAG = "EddystoneBeaconDecoder";
  public static final Map<Integer, String> eddystoneURISchemesPrefix = new HashMap();
  public static final Map<Integer, String> eddystoneURISchemesSuffix;
  public long beaconAdvPDUCount;
  public float beaconBatVoltage;
  public long beaconSecondCount;
  public int beaconTXPower;
  public float beaconTemperature;
  public URL beaconURL;
  public byte beaconURLSchemePrefix;
  public byte beaconVersion;
  
  static
  {
    eddystoneURISchemesPrefix.put(Integer.valueOf(0), "http://www.");
    eddystoneURISchemesPrefix.put(Integer.valueOf(1), "https://www.");
    eddystoneURISchemesPrefix.put(Integer.valueOf(2), "http://");
    eddystoneURISchemesPrefix.put(Integer.valueOf(3), "https://");
    eddystoneURISchemesSuffix = new HashMap();
    eddystoneURISchemesSuffix.put(Integer.valueOf(0), ".com/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(1), ".org/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(2), ".edu/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(3), ".net/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(4), ".info/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(5), ".biz/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(6), ".gov/");
    eddystoneURISchemesSuffix.put(Integer.valueOf(7), ".com");
    eddystoneURISchemesSuffix.put(Integer.valueOf(8), ".org");
    eddystoneURISchemesSuffix.put(Integer.valueOf(9), ".edu");
    eddystoneURISchemesSuffix.put(Integer.valueOf(10), ".net");
    eddystoneURISchemesSuffix.put(Integer.valueOf(11), ".info");
    eddystoneURISchemesSuffix.put(Integer.valueOf(12), ".biz");
    eddystoneURISchemesSuffix.put(Integer.valueOf(13), ".gov");
  }
  
  public static float calcFixedPointFromUINT16(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length != 2) {
      return -1.0F;
    }
    return paramArrayOfByte[0] + paramArrayOfByte[1] / 256.0F;
  }
  
  public boolean updateData(Map<ParcelUuid, byte[]> paramMap)
  {
    if (paramMap.containsKey(new ParcelUuid(GattInfo.EDDYSTONE_SERVICE_UUID)))
    {
      byte[] arrayOfByte = (byte[])paramMap.get(new ParcelUuid(GattInfo.EDDYSTONE_SERVICE_UUID));
      if (arrayOfByte.length > 4) {
        switch (arrayOfByte[0])
        {
        default: 
          Log.d("EddystoneBeaconDecoder", "Unknown Eddystone data found !");
        }
      }
      for (;;)
      {
        return true;
        Log.d("EddystoneBeaconDecoder", "Eddystone URL found !");
        this.beaconTXPower = arrayOfByte[1];
        this.beaconURLSchemePrefix = arrayOfByte[2];
        paramMap = new String();
        paramMap = paramMap + (String)eddystoneURISchemesPrefix.get(new Integer(this.beaconURLSchemePrefix));
        int j = 3;
        if (j < arrayOfByte.length)
        {
          if (arrayOfByte[j] < 13) {}
          int i;
          for (paramMap = paramMap + (String)eddystoneURISchemesSuffix.get(new Integer(arrayOfByte[j]));; paramMap = paramMap + new String(new byte[] { i }))
          {
            j += 1;
            break;
            i = arrayOfByte[j];
          }
        }
        Log.d("EddystoneBeaconDecoder", "TX Power : " + this.beaconTXPower + "dBm");
        Log.d("EddystoneBeaconDecoder", "URL : " + paramMap);
        try
        {
          this.beaconURL = new URL(paramMap);
        }
        catch (MalformedURLException paramMap)
        {
          paramMap.printStackTrace();
        }
        continue;
        Log.d("EddystoneBeaconDecoder", "Eddystone Unencrypted TLM Frame found !");
        this.beaconBatVoltage = (bleUtility.BUILD_UINT16(arrayOfByte[2], arrayOfByte[3]) / 1000.0F);
        Log.d("EddystoneBeaconDecoder", "Beacon Voltage :" + this.beaconBatVoltage);
        this.beaconTemperature = calcFixedPointFromUINT16(new byte[] { arrayOfByte[4], arrayOfByte[5] });
        Log.d("EddystoneBeaconDecoder", "Beacon Temperature : " + this.beaconTemperature);
        this.beaconAdvPDUCount = bleUtility.BUILD_UINT32(arrayOfByte[6], arrayOfByte[7], arrayOfByte[8], arrayOfByte[9]);
        Log.d("EddystoneBeaconDecoder", "Beacon Advertisment count : " + this.beaconAdvPDUCount + String.format(" (0x%08x)", new Object[] { Long.valueOf(this.beaconAdvPDUCount) }));
        this.beaconSecondCount = bleUtility.BUILD_UINT32(arrayOfByte[10], arrayOfByte[11], arrayOfByte[12], arrayOfByte[13]);
        Log.d("EddystoneBeaconDecoder", "Beacon Second Count : " + this.beaconSecondCount + String.format(" (0x%08x)", new Object[] { Long.valueOf(this.beaconSecondCount) }));
      }
    }
    return false;
  }
  
  public void updateTableWithParameters(TableLayout paramTableLayout, Context paramContext)
  {
    paramTableLayout.removeAllViews();
    TableRow localTableRow = (TableRow)((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2131361833, null);
    ((TextView)localTableRow.findViewById(2131230767)).setText("URL :");
    ((TextView)localTableRow.findViewById(2131230765)).setText("");
    TextView localTextView = (TextView)localTableRow.findViewById(2131230766);
    if (this.beaconURL != null) {
      localTextView.setText(this.beaconURL.toString());
    }
    for (;;)
    {
      paramTableLayout.addView(localTableRow);
      paramContext = (TableRow)((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2131361833, null);
      ((TextView)paramContext.findViewById(2131230767)).setText("TX Power Level :");
      ((TextView)paramContext.findViewById(2131230765)).setText("");
      ((TextView)paramContext.findViewById(2131230766)).setText(this.beaconTXPower + "dBm");
      paramTableLayout.addView(paramContext);
      return;
      localTextView.setText("");
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\BluetoothLEController\EddystoneBeaconDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */