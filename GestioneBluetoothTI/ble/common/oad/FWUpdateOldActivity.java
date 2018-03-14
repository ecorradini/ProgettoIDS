package com.ti.ble.common.oad;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ti.ble.sensortag.FileActivity;
import com.ti.device_selector.DeviceActivity;
import com.ti.util.Conversion;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FWUpdateOldActivity
  extends AppCompatActivity
{
  private static final int BLOCKS_PER_CONNECTION = 1;
  public static final String EXTRA_MESSAGE = "com.ti.ble.sensortag.MESSAGE";
  public static final int FILE_ACTIVITY_REQ = 0;
  private static final int FILE_BUFFER_SIZE = 262144;
  public static final String FW_CUSTOM_DIRECTORY = Environment.DIRECTORY_DOWNLOADS;
  private static final String FW_FILE_A = "SensorTagImgA.bin";
  private static final String FW_FILE_B = "SensorTagImgB.bin";
  private static final int GATT_WRITE_TIMEOUT = 300;
  private static final int HAL_FLASH_WORD_SIZE = 4;
  private static final int OAD_BLOCK_SIZE = 16;
  private static final int OAD_BUFFER_SIZE = 18;
  private static final short OAD_CONN_INTERVAL = 12;
  private static final int OAD_IMG_HDR_SIZE = 8;
  private static final short OAD_SUPERVISION_TIMEOUT = 50;
  private static final int SEND_INTERVAL = 20;
  private static String TAG = "FWUpdateOldActivity";
  private static final long TIMER_INTERVAL = 1000L;
  private Button mBtnLoadA;
  private Button mBtnLoadB;
  private Button mBtnLoadC;
  private Button mBtnStart;
  private BluetoothGattCharacteristic mCharBlock = null;
  private BluetoothGattCharacteristic mCharConnReq = null;
  private BluetoothGattCharacteristic mCharIdentify = null;
  private List<BluetoothGattCharacteristic> mCharListCc;
  private List<BluetoothGattCharacteristic> mCharListOad;
  private BluetoothGattService mConnControlService = this.mDeviceActivity.getConnControlService();
  private DeviceActivity mDeviceActivity = null;
  private final byte[] mFileBuffer = new byte[262144];
  private TextView mFileImage;
  private ImgHdr mFileImgHdr = new ImgHdr(null);
  private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousIntent.getAction();
    }
  };
  private IntentFilter mIntentFilter;
  private TextView mLog;
  private final byte[] mOadBuffer = new byte[18];
  private BluetoothGattService mOadService = this.mDeviceActivity.getOadService();
  private ProgInfo mProgInfo = new ProgInfo(null);
  private boolean mProgramming = false;
  private ProgressBar mProgressBar;
  private TextView mProgressInfo;
  private boolean mServiceOk = false;
  private TextView mTargImage;
  private ImgHdr mTargImgHdr = new ImgHdr(null);
  FWUpdateOldActivity mThis;
  private Timer mTimer = null;
  private TimerTask mTimerTask = null;
  
  public FWUpdateOldActivity()
  {
    for (;;)
    {
      try
      {
        this.mCharListOad = this.mOadService.getCharacteristics();
        this.mCharListCc = this.mConnControlService.getCharacteristics();
        if ((this.mCharListOad.size() != 2) || (this.mCharListCc.size() < 3)) {
          continue;
        }
        this.mServiceOk = bool;
        if (this.mServiceOk)
        {
          this.mCharIdentify = ((BluetoothGattCharacteristic)this.mCharListOad.get(0));
          this.mCharBlock = ((BluetoothGattCharacteristic)this.mCharListOad.get(1));
          this.mCharBlock.setWriteType(1);
          this.mCharConnReq = ((BluetoothGattCharacteristic)this.mCharListCc.get(1));
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        continue;
      }
      this.mThis = this;
      return;
      bool = false;
    }
  }
  
  private void displayImageInfo(TextView paramTextView, ImgHdr paramImgHdr)
  {
    int i = paramImgHdr.ver;
    int j = paramImgHdr.len;
    paramTextView.setText(Html.fromHtml(String.format("Type: %c Ver.: %d Size: %d", new Object[] { paramImgHdr.imgType, Integer.valueOf(i >> 1), Integer.valueOf(j * 4) })));
  }
  
  private void displayStats()
  {
    int i = this.mProgInfo.iTimeElapsed / 1000;
    if (i > 0)
    {
      int j = this.mProgInfo.iBytes / i;
      String str = String.format("Time: %d / %d sec", new Object[] { Integer.valueOf(i), Integer.valueOf((int)(this.mFileImgHdr.len * 4 / this.mProgInfo.iBytes * i)) });
      str = str + String.format("    Bytes: %d (%d/sec)", new Object[] { Integer.valueOf(this.mProgInfo.iBytes), Integer.valueOf(j) });
      this.mProgressInfo.setText(str);
      return;
    }
  }
  
  private void getTargetImageInfo()
  {
    int i = 0;
    while ((1 != 0) && (i < 5))
    {
      int j = i + 1;
      i = j;
      if (1 == 0) {
        i = j;
      }
    }
  }
  
  private void initIntentFilter()
  {
    this.mIntentFilter = new IntentFilter();
  }
  
  private boolean loadFile(String paramString, boolean paramBoolean)
  {
    boolean bool = true;
    if (paramBoolean) {}
    for (;;)
    {
      try
      {
        localObject = getAssets().open(paramString);
        ((InputStream)localObject).read(this.mFileBuffer, 0, this.mFileBuffer.length);
        ((InputStream)localObject).close();
        this.mFileImgHdr.ver = Conversion.buildUint16(this.mFileBuffer[5], this.mFileBuffer[4]);
        this.mFileImgHdr.len = Conversion.buildUint16(this.mFileBuffer[7], this.mFileBuffer[6]);
        paramString = this.mFileImgHdr;
        if ((this.mFileImgHdr.ver & 0x1) != 1) {
          break label316;
        }
        c = 'B';
        paramString.imgType = Character.valueOf(c);
        System.arraycopy(this.mFileBuffer, 8, this.mFileImgHdr.uid, 0, 4);
        displayImageInfo(this.mFileImage, this.mFileImgHdr);
        if (this.mFileImgHdr.imgType == this.mTargImgHdr.imgType) {
          break label322;
        }
        paramBoolean = bool;
        if (!paramBoolean) {
          break label327;
        }
        i = 2131624268;
        this.mFileImage.setTextAppearance(this, i);
        this.mBtnStart.setEnabled(paramBoolean);
        displayStats();
        this.mLog.setText("Image " + this.mFileImgHdr.imgType + " selected.\n");
        localObject = this.mLog;
        if (!paramBoolean) {
          break label335;
        }
        paramString = "Ready to program device!\n";
        ((TextView)localObject).append(paramString);
        updateGui();
        return false;
      }
      catch (IOException localIOException)
      {
        Object localObject;
        this.mLog.setText("File open failed: " + paramString + "\n");
        return false;
      }
      localObject = new FileInputStream(new File(paramString));
      continue;
      label316:
      char c = 'A';
      continue;
      label322:
      paramBoolean = false;
      continue;
      label327:
      int i = 2131624269;
      continue;
      label335:
      paramString = "Incompatible image, select alternative!\n";
    }
  }
  
  private void programBlock(int paramInt)
  {
    if (!this.mProgramming) {}
    label296:
    for (;;)
    {
      return;
      Object localObject;
      if (this.mProgInfo.iBlocks < this.mProgInfo.nBlocks)
      {
        this.mProgramming = true;
        String str = new String();
        this.mProgInfo.iBlocks = ((short)paramInt);
        this.mOadBuffer[0] = Conversion.loUint16(this.mProgInfo.iBlocks);
        this.mOadBuffer[1] = Conversion.hiUint16(this.mProgInfo.iBlocks);
        System.arraycopy(this.mFileBuffer, this.mProgInfo.iBytes, this.mOadBuffer, 2, 16);
        this.mCharBlock.setValue(this.mOadBuffer);
        Log.d("FWUpdateOldActivity", String.format("TX Block %02x%02x", new Object[] { Byte.valueOf(this.mOadBuffer[1]), Byte.valueOf(this.mOadBuffer[0]) }));
        if (1 != 0)
        {
          localObject = this.mProgInfo;
          ((ProgInfo)localObject).iBlocks = ((short)(((ProgInfo)localObject).iBlocks + 1));
          localObject = this.mProgInfo;
          ((ProgInfo)localObject).iBytes += 16;
          this.mProgressBar.setProgress(this.mProgInfo.iBlocks * 100 / this.mProgInfo.nBlocks);
          localObject = str;
          if (this.mProgInfo.iBlocks == this.mProgInfo.nBlocks)
          {
            runOnUiThread(new Runnable()
            {
              public void run()
              {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(FWUpdateOldActivity.this.mThis);
                localBuilder.setMessage(2131558581);
                localBuilder.setTitle("Programming finished");
                localBuilder.setPositiveButton("OK", null);
                localBuilder.create().show();
              }
            });
            localObject = str;
          }
          if (1 == 0) {
            this.mLog.append((CharSequence)localObject);
          }
        }
      }
      for (;;)
      {
        if (this.mProgramming) {
          break label296;
        }
        runOnUiThread(new Runnable()
        {
          public void run()
          {
            FWUpdateOldActivity.this.displayStats();
            FWUpdateOldActivity.this.stopProgramming();
          }
        });
        return;
        this.mProgramming = false;
        localObject = "GATT writeCharacteristic failed\n";
        break;
        this.mProgramming = false;
      }
    }
  }
  
  private void setConnectionParameters()
  {
    int i = Conversion.loUint16();
    int j = Conversion.hiUint16((short)12);
    int k = Conversion.loUint16((short)12);
    int m = Conversion.hiUint16((short)12);
    int n = Conversion.loUint16((short)50);
    int i1 = Conversion.hiUint16((short)50);
    this.mCharConnReq.setValue(new byte[] { i, j, k, m, 0, 0, n, i1 });
  }
  
  private void startProgramming()
  {
    this.mLog.append("Programming started\n");
    this.mProgramming = true;
    updateGui();
    byte[] arrayOfByte = new byte[12];
    arrayOfByte[0] = Conversion.loUint16(this.mFileImgHdr.ver);
    arrayOfByte[1] = Conversion.hiUint16(this.mFileImgHdr.ver);
    arrayOfByte[2] = Conversion.loUint16(this.mFileImgHdr.len);
    arrayOfByte[3] = Conversion.hiUint16(this.mFileImgHdr.len);
    System.arraycopy(this.mFileImgHdr.uid, 0, arrayOfByte, 4, 4);
    this.mCharIdentify.setValue(arrayOfByte);
    this.mProgInfo.reset();
    new Thread(new OadTask(null)).start();
    this.mTimer = new Timer();
    this.mTimerTask = new ProgTimerTask(null);
    this.mTimer.scheduleAtFixedRate(this.mTimerTask, 0L, 1000L);
  }
  
  private void stopProgramming()
  {
    this.mTimer.cancel();
    this.mTimer.purge();
    this.mTimerTask.cancel();
    this.mTimerTask = null;
    this.mProgramming = false;
    this.mProgressInfo.setText("");
    this.mProgressBar.setProgress(0);
    updateGui();
    if (this.mProgInfo.iBlocks == this.mProgInfo.nBlocks)
    {
      this.mLog.setText("Programming complete!\n");
      return;
    }
    this.mLog.append("Programming cancelled\n");
  }
  
  private void updateGui()
  {
    if (this.mProgramming)
    {
      this.mBtnStart.setText(2131558446);
      this.mBtnLoadA.setEnabled(false);
      this.mBtnLoadB.setEnabled(false);
      this.mBtnLoadC.setEnabled(false);
      return;
    }
    for (;;)
    {
      try
      {
        this.mProgressBar.setProgress(0);
        this.mBtnStart.setText(2131558644);
        if (this.mFileImgHdr.imgType.charValue() == 'A')
        {
          this.mBtnLoadA.setEnabled(false);
          this.mBtnLoadB.setEnabled(true);
          this.mBtnLoadC.setEnabled(true);
          return;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        return;
      }
      if (this.mFileImgHdr.imgType.charValue() == 'B')
      {
        this.mBtnLoadA.setEnabled(true);
        this.mBtnLoadB.setEnabled(false);
      }
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 0) && (paramInt2 == -1)) {
      loadFile(paramIntent.getStringExtra("ti.android.ble.devicemonitor.FILENAME"), false);
    }
  }
  
  public void onBackPressed()
  {
    Log.d(TAG, "onBackPressed");
    if (this.mProgramming)
    {
      Toast.makeText(this, 2131558605, 1).show();
      return;
    }
    super.onBackPressed();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    Log.d(TAG, "onCreate");
    super.onCreate(paramBundle);
    setContentView(2131361827);
    setTitle(2131558670);
    this.mProgressInfo = ((TextView)findViewById(2131231184));
    this.mTargImage = ((TextView)findViewById(2131231186));
    this.mFileImage = ((TextView)findViewById(2131231183));
    this.mLog = ((TextView)findViewById(2131231185));
    this.mLog.setMovementMethod(new ScrollingMovementMethod());
    this.mProgressBar = ((ProgressBar)findViewById(2131230999));
    this.mBtnStart = ((Button)findViewById(2131230820));
    this.mBtnStart.setEnabled(false);
    this.mBtnLoadA = ((Button)findViewById(2131230817));
    this.mBtnLoadB = ((Button)findViewById(2131230818));
    this.mBtnLoadC = ((Button)findViewById(2131230819));
    this.mBtnLoadA.setEnabled(this.mServiceOk);
    this.mBtnLoadB.setEnabled(this.mServiceOk);
    this.mBtnLoadC.setEnabled(this.mServiceOk);
    initIntentFilter();
    getWindow().addFlags(128);
  }
  
  public void onDestroy()
  {
    Log.d(TAG, "onDestroy");
    super.onDestroy();
    if (this.mTimerTask != null) {
      this.mTimerTask.cancel();
    }
    this.mTimer = null;
    getWindow().clearFlags(128);
  }
  
  public void onLoad(View paramView)
  {
    if (paramView.getId() == 2131230817) {
      loadFile("SensorTagImgA.bin", true);
    }
    for (;;)
    {
      updateGui();
      return;
      loadFile("SensorTagImgB.bin", true);
    }
  }
  
  public void onLoadCustom(View paramView)
  {
    paramView = new Intent(this, FileActivity.class);
    paramView.putExtra("com.ti.ble.sensortag.MESSAGE", FW_CUSTOM_DIRECTORY);
    startActivityForResult(paramView, 0);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    Log.d(TAG, "onOptionsItemSelected");
    switch (paramMenuItem.getItemId())
    {
    default: 
      return super.onOptionsItemSelected(paramMenuItem);
    }
    onBackPressed();
    return true;
  }
  
  protected void onPause()
  {
    super.onPause();
    unregisterReceiver(this.mGattUpdateReceiver);
  }
  
  protected void onResume()
  {
    super.onResume();
    if (this.mServiceOk)
    {
      registerReceiver(this.mGattUpdateReceiver, this.mIntentFilter);
      getTargetImageInfo();
      setConnectionParameters();
      return;
    }
    Toast.makeText(this, "OAD service initialisation failed", 1).show();
  }
  
  public void onStart(View paramView)
  {
    if (this.mProgramming)
    {
      stopProgramming();
      return;
    }
    startProgramming();
  }
  
  private class ImgHdr
  {
    Character imgType;
    short len;
    byte[] uid = new byte[4];
    short ver;
    
    private ImgHdr() {}
  }
  
  private class OadTask
    implements Runnable
  {
    private OadTask() {}
    
    public void run()
    {
      while (FWUpdateOldActivity.this.mProgramming)
      {
        try
        {
          Thread.sleep(20L);
          int i = 0;
          while (i < 1)
          {
            j = 1;
            if ((j & FWUpdateOldActivity.this.mProgramming) == 0) {
              break label57;
            }
            i += 1;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          for (;;)
          {
            localInterruptedException.printStackTrace();
            continue;
            int j = 0;
          }
        }
        label57:
        if (FWUpdateOldActivity.this.mProgInfo.iBlocks % 100 == 0) {
          FWUpdateOldActivity.this.runOnUiThread(new Runnable()
          {
            public void run()
            {
              FWUpdateOldActivity.this.displayStats();
            }
          });
        }
      }
    }
  }
  
  private class ProgInfo
  {
    short iBlocks = 0;
    int iBytes = 0;
    int iTimeElapsed = 0;
    short nBlocks = 0;
    
    private ProgInfo() {}
    
    void reset()
    {
      this.iBytes = 0;
      this.iBlocks = 0;
      this.iTimeElapsed = 0;
      this.nBlocks = ((short)(FWUpdateOldActivity.this.mFileImgHdr.len / 4));
    }
  }
  
  private class ProgTimerTask
    extends TimerTask
  {
    private ProgTimerTask() {}
    
    public void run()
    {
      FWUpdateOldActivity.ProgInfo localProgInfo = FWUpdateOldActivity.this.mProgInfo;
      localProgInfo.iTimeElapsed = ((int)(localProgInfo.iTimeElapsed + 1000L));
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateOldActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */