package com.ti.ble.common.oad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ti.device_selector.TopLevel_;
import com.ti.ti_oad.TIOADEoadClient;
import com.ti.ti_oad.TIOADEoadClientProgressCallback;
import com.ti.ti_oad.TIOADEoadDefinitions;
import com.ti.ti_oad.TIOADEoadDefinitions.oadStatusEnumeration;
import com.ti.util.SparkLineView;
import java.util.List;

public class FWUpdateEOADActivity
  extends AppCompatActivity
{
  static final String TAG = FWUpdateEOADActivity.class.getSimpleName();
  public TextView eoadBlockSize;
  public TextView eoadChipType;
  public TextView eoadCurrentBlock;
  public TextView eoadCurrentSpeed;
  public TextView eoadMTU;
  public ProgressBar eoadProgress;
  public TextView eoadProgressText;
  public SparkLineView eoadSpeedHistory;
  public TextView eoadStatus;
  public TextView eoadTotalBlocks;
  List<FWUpdateTIFirmwareEntry> fwEntries = null;
  public long lastPacketRXTime;
  private BluetoothDevice mBtDevice;
  private BluetoothGatt mBtGatt;
  private TIOADEoadClient mOADClient;
  private FWUpdateEOADActivity mThis = this;
  public String oadImageFileNamePath = null;
  BroadcastReceiver reciever;
  public Button selectFileButton;
  private View.OnClickListener selectFileButtonClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      paramAnonymousView = new FWUpdateBINFileEntriesParser();
      try
      {
        FWUpdateEOADActivity.this.fwEntries = paramAnonymousView.parse(FWUpdateEOADActivity.this.getAssets().open("firmware_list_eoad.xml"));
        FWUpdateEOADSelectorDialogFragment.newInstance(FWUpdateEOADActivity.this.fwEntries, FWUpdateEOADActivity.this.mOADClient.getTIOADEoadDeviceID()).show(FWUpdateEOADActivity.this.mThis.getFragmentManager(), "FWUpdateEOADSelectorDialogFragment");
        return;
      }
      catch (Exception paramAnonymousView)
      {
        for (;;)
        {
          paramAnonymousView.printStackTrace();
        }
      }
    }
  };
  private View.OnClickListener stopOADButtonClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      new AlertDialog.Builder(FWUpdateEOADActivity.this.mThis).setTitle("Abort programming ?").setMessage("Are you sure you want to abort an ongoing programming ?\n").setNegativeButton("NO", null).setIcon(17301543).setPositiveButton("YES", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
        {
          FWUpdateEOADActivity.this.mOADClient.abortProgramming();
          paramAnonymous2DialogInterface = new Intent(FWUpdateEOADActivity.this.mThis, TopLevel_.class);
          paramAnonymous2DialogInterface.setFlags(67108864);
          FWUpdateEOADActivity.this.startActivity(paramAnonymous2DialogInterface);
        }
      }).create().show();
    }
  };
  
  public void moveDeviceToOAD(BluetoothDevice paramBluetoothDevice)
  {
    this.mBtDevice = paramBluetoothDevice;
    this.mOADClient = new TIOADEoadClient(this.mThis);
    this.mOADClient.initializeTIOADEoadProgrammingOnDevice(paramBluetoothDevice, new TIOADEoadClientProgressCallback()
    {
      public void oadProgressUpdate(final float paramAnonymousFloat, final int paramAnonymousInt)
      {
        FWUpdateEOADActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            FWUpdateEOADActivity.this.eoadProgress.setProgress((int)paramAnonymousFloat);
            FWUpdateEOADActivity.this.eoadProgressText.setText(String.format("%.2f%%", new Object[] { Float.valueOf(paramAnonymousFloat) }));
            FWUpdateEOADActivity.this.eoadCurrentBlock.setText(String.format("%d", new Object[] { Integer.valueOf(paramAnonymousInt) }));
            FWUpdateEOADActivity.this.eoadTotalBlocks.setText(String.format("%d", new Object[] { Integer.valueOf(FWUpdateEOADActivity.this.mOADClient.getTIOADEoadTotalBlocks()) }));
            float f = (float)(System.currentTimeMillis() - FWUpdateEOADActivity.this.lastPacketRXTime) / 1000.0F;
            f = FWUpdateEOADActivity.this.mOADClient.getTIOADEoadBlockSize() / f;
            FWUpdateEOADActivity.this.eoadCurrentSpeed.setText(String.format("%.0fb/s", new Object[] { Float.valueOf(f) }));
            FWUpdateEOADActivity.this.eoadSpeedHistory.addValue(f);
            FWUpdateEOADActivity.this.lastPacketRXTime = System.currentTimeMillis();
          }
        });
      }
      
      public void oadStatusUpdate(final TIOADEoadDefinitions.oadStatusEnumeration paramAnonymousoadStatusEnumeration)
      {
        Log.d(FWUpdateEOADActivity.TAG, "TIOADEoad Status Update: " + TIOADEoadDefinitions.oadStatusEnumerationGetDescriptiveString(paramAnonymousoadStatusEnumeration));
        FWUpdateEOADActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            FWUpdateEOADActivity.this.eoadStatus.setText(TIOADEoadDefinitions.oadStatusEnumerationGetDescriptiveString(paramAnonymousoadStatusEnumeration));
            if (paramAnonymousoadStatusEnumeration == TIOADEoadDefinitions.oadStatusEnumeration.tiOADClientReady)
            {
              FWUpdateEOADActivity.this.eoadProgress.setIndeterminate(false);
              FWUpdateEOADActivity.this.selectFileButton.setText(2131558525);
              FWUpdateEOADActivity.this.selectFileButton.setEnabled(true);
              FWUpdateEOADActivity.this.eoadProgressText.setText("0.00%");
              FWUpdateEOADActivity.this.eoadBlockSize.setText(String.format("%d", new Object[] { Integer.valueOf(FWUpdateEOADActivity.this.mOADClient.getTIOADEoadBlockSize()) }));
              FWUpdateEOADActivity.this.eoadMTU.setText(String.format("%d", new Object[] { Integer.valueOf(FWUpdateEOADActivity.this.mOADClient.getMTU()) }));
              FWUpdateEOADActivity.this.eoadChipType.setText(TIOADEoadDefinitions.oadChipTypePrettyPrint(FWUpdateEOADActivity.this.mOADClient.getTIOADEoadDeviceID()));
            }
            if (paramAnonymousoadStatusEnumeration == TIOADEoadDefinitions.oadStatusEnumeration.tiOADClientCompleteFeedbackOK)
            {
              FWUpdateEOADActivity.this.eoadProgress.setIndeterminate(true);
              FWUpdateEOADActivity.this.eoadProgressText.setText("Waiting for disconnect...");
              FWUpdateEOADActivity.this.selectFileButton.setEnabled(false);
              FWUpdateEOADActivity.this.eoadStatus.setEnabled(false);
              FWUpdateEOADActivity.this.eoadBlockSize.setEnabled(false);
              FWUpdateEOADActivity.this.eoadMTU.setEnabled(false);
              FWUpdateEOADActivity.this.eoadCurrentSpeed.setEnabled(false);
              FWUpdateEOADActivity.this.eoadCurrentBlock.setEnabled(false);
              FWUpdateEOADActivity.this.eoadTotalBlocks.setEnabled(false);
              FWUpdateEOADActivity.this.eoadChipType.setEnabled(false);
            }
            if (paramAnonymousoadStatusEnumeration == TIOADEoadDefinitions.oadStatusEnumeration.tiOADClientCompleteDeviceDisconnectedPositive) {
              new AlertDialog.Builder(FWUpdateEOADActivity.this.mThis).setTitle("Success").setMessage("OAD Programming complete !").setIcon(17301659).setPositiveButton("OK", new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface paramAnonymous3DialogInterface, int paramAnonymous3Int)
                {
                  paramAnonymous3DialogInterface = new Intent(FWUpdateEOADActivity.this.mThis, TopLevel_.class);
                  paramAnonymous3DialogInterface.setFlags(67108864);
                  FWUpdateEOADActivity.this.startActivity(paramAnonymous3DialogInterface);
                }
              }).create().show();
            }
          }
        });
      }
    });
  }
  
  protected void onCreate(@Nullable Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2131361825);
    setTitle(2131558521);
    moveDeviceToOAD((BluetoothDevice)getIntent().getParcelableExtra("EXTRA_DEVICE"));
    this.selectFileButton = ((Button)findViewById(2131230780));
    this.selectFileButton.setText(2131558524);
    this.selectFileButton.setOnClickListener(this.selectFileButtonClicked);
    this.eoadProgress = ((ProgressBar)findViewById(2131230778));
    this.eoadProgress.setIndeterminate(true);
    this.eoadStatus = ((TextView)findViewById(2131230782));
    this.eoadCurrentBlock = ((TextView)findViewById(2131230775));
    this.eoadTotalBlocks = ((TextView)findViewById(2131230779));
    this.eoadMTU = ((TextView)findViewById(2131230776));
    this.eoadBlockSize = ((TextView)findViewById(2131230774));
    this.eoadProgressText = ((TextView)findViewById(2131230777));
    this.eoadCurrentSpeed = ((TextView)findViewById(2131230772));
    this.eoadChipType = ((TextView)findViewById(2131230771));
    this.eoadSpeedHistory = ((SparkLineView)findViewById(2131230781));
    this.eoadSpeedHistory.autoScale = true;
    this.eoadSpeedHistory.minVal = 0.0F;
  }
  
  protected void onPause()
  {
    unregisterReceiver(this.reciever);
    super.onPause();
  }
  
  protected void onResume()
  {
    super.onResume();
    this.reciever = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (paramAnonymousIntent.getAction().equals("FWUpdateEOADSelectorDialogFragment.SELECTED"))
        {
          int i = paramAnonymousIntent.getIntExtra("FWUpdateEOADSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX", -1);
          if ((i >= 0) || (i < FWUpdateEOADActivity.this.fwEntries.size()))
          {
            paramAnonymousContext = (FWUpdateTIFirmwareEntry)FWUpdateEOADActivity.this.fwEntries.get(i);
            FWUpdateEOADActivity.this.oadImageFileNamePath = paramAnonymousContext.Filename;
            FWUpdateEOADActivity.this.selectFileButton.setOnClickListener(FWUpdateEOADActivity.this.stopOADButtonClicked);
            FWUpdateEOADActivity.this.selectFileButton.setText("Abort Programming");
            FWUpdateEOADActivity.this.lastPacketRXTime = System.currentTimeMillis();
            if (FWUpdateEOADActivity.this.oadImageFileNamePath != null) {
              FWUpdateEOADActivity.this.mOADClient.start(FWUpdateEOADActivity.this.oadImageFileNamePath);
            }
          }
        }
      }
    };
    registerReceiver(this.reciever, new IntentFilter("FWUpdateEOADSelectorDialogFragment.SELECTED"));
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateEOADActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */