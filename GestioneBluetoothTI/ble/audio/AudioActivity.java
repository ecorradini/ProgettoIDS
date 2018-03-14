package com.ti.ble.audio;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AudioActivity
  extends AppCompatActivity
{
  public static final String BT_ADDRESS_EXTRA = "AudioActivity.BTADDRESS_EXTRA";
  private BroadcastReceiver AdvancedRemoteBLEAudioMessageReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = (BluetoothDevice)paramAnonymousIntent.getParcelableExtra("device");
      BluetoothDevice localBluetoothDevice = (BluetoothDevice)paramAnonymousIntent.getParcelableExtra("paired");
      final String str1 = paramAnonymousIntent.getStringExtra("statusText");
      String str2 = paramAnonymousIntent.getStringExtra("fileInfoText");
      String str3 = paramAnonymousIntent.getStringExtra("searchTerm");
      int i = paramAnonymousIntent.getByteExtra("conInterval", (byte)-1);
      int j = paramAnonymousIntent.getIntExtra("peakVU", 0);
      if ((str3 == null) || (str2 != null)) {
        Log.d("AudioActivity", str2);
      }
      if (str1 != null)
      {
        Log.d("AudioActivity", "Got status :" + str1);
        AudioActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            AudioActivity.this.conStatus.setText(str1);
          }
        });
        if (str1.equals("Enabling audio")) {
          Log.d("activity", "Enabling notification");
        }
      }
      for (;;)
      {
        if (j != 0) {
          AudioActivity.this.calcAndDisplayVUFromMaxSampleValue(j);
        }
        if (i != -1)
        {
          Log.d("AudioActivity", "Received Connection interval " + i * 1.25D + " ms");
          AudioActivity.this.conInterval.setText(i * 1.25D + " ms");
        }
        return;
        if (str1.equals("Disconnected"))
        {
          Log.d("activity", "Changing notification");
        }
        else if (str1.equals("Connected"))
        {
          Log.d("activity", "Changing notification");
        }
        else if (str1.equals("Failed to set connection interval"))
        {
          paramAnonymousContext = new AlertDialog.Builder(AudioActivity.this.mThis);
          paramAnonymousContext.setPositiveButton("OK", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {}
          });
          paramAnonymousContext.setTitle("Unable to set correct connection interval");
          paramAnonymousContext.setMessage("Tried to set correct connection interval (10ms), but got (" + AudioActivity.this.conInterval.getText() + ") Sound transfer will be degraded...");
          paramAnonymousContext.create().show();
          continue;
          if (paramAnonymousContext != null) {
            Log.d("activity", "Found device :" + paramAnonymousContext.getAddress() + " name: " + paramAnonymousContext.getName());
          } else if (localBluetoothDevice != null) {
            Log.d("activity", "Found paired :" + localBluetoothDevice.getAddress() + " name: " + localBluetoothDevice.getName());
          }
        }
      }
    }
  };
  AdvancedRemoteBLEAudioService aService;
  Intent audioServiceIntent;
  TextView conInterval;
  TextView conStatus;
  ImageView icon;
  ArrayList<ImageView> ledArray;
  String mBtAddr;
  BroadcastReceiver mReceiver;
  AudioActivity mThis = this;
  
  public void calcAndDisplayVUFromMaxSampleValue(int paramInt)
  {
    float f = 20.0F * (float)Math.log10(paramInt / 32768.0F);
    if (f >= 0.0F) {
      paramInt = 7;
    }
    for (;;)
    {
      int i = 0;
      while (i < paramInt)
      {
        ((ImageView)this.ledArray.get(i)).setImageResource(2131492898);
        i += 1;
      }
      if (f >= -6.0F) {
        paramInt = 6;
      } else if (f >= -12.0F) {
        paramInt = 5;
      } else if (f >= -18.0F) {
        paramInt = 4;
      } else if (f >= -24.0F) {
        paramInt = 3;
      } else if (f >= -30.0F) {
        paramInt = 2;
      } else if (f >= -36.0F) {
        paramInt = 1;
      } else {
        paramInt = 0;
      }
    }
    while (paramInt < 7)
    {
      ((ImageView)this.ledArray.get(paramInt)).setImageResource(2131492897);
      paramInt += 1;
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    this.mBtAddr = getIntent().getStringExtra("AudioActivity.BTADDRESS_EXTRA");
    super.onCreate(paramBundle);
    setContentView(2131361817);
    this.conStatus = ((TextView)findViewById(2131230721));
    if (this.conStatus != null) {
      this.conStatus.setText("Starting Service ...");
    }
    this.ledArray = new ArrayList();
    this.ledArray.add((ImageView)findViewById(2131230723));
    this.ledArray.add((ImageView)findViewById(2131230724));
    this.ledArray.add((ImageView)findViewById(2131230725));
    this.ledArray.add((ImageView)findViewById(2131230726));
    this.ledArray.add((ImageView)findViewById(2131230727));
    this.ledArray.add((ImageView)findViewById(2131230728));
    this.ledArray.add((ImageView)findViewById(2131230729));
    this.conInterval = ((TextView)findViewById(2131230720));
    this.icon = ((ImageView)findViewById(2131230722));
    this.icon.setImageResource(2131492873);
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    Intent localIntent = new Intent("ARCBLEAudio-To-Service-Events");
    localIntent.putExtra("message", 3);
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    stopService(this.audioServiceIntent);
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
  }
  
  public void onPause()
  {
    super.onPause();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
  }
  
  public void onResume()
  {
    super.onResume();
    this.mReceiver = this.AdvancedRemoteBLEAudioMessageReceiver;
    LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, new IntentFilter("ARCBLEAudio-From-Service-Events"));
    this.audioServiceIntent = new Intent(this, AdvancedRemoteBLEAudioService.class);
    startService(this.audioServiceIntent);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        Log.d("AudioActivity", "Connecting to " + AudioActivity.this.mBtAddr);
        Intent localIntent = new Intent("ARCBLEAudio-To-Service-Events");
        localIntent.putExtra("btaddr", AudioActivity.this.mBtAddr);
        LocalBroadcastManager.getInstance(AudioActivity.this.mThis).sendBroadcast(localIntent);
      }
    }, 1000L);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\audio\AudioActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */