package com.ti.ble.sensortag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileActivity
  extends Activity
{
  public static final String EXTRA_FILENAME = "ti.android.ble.devicemonitor.FILENAME";
  private static final String TAG = "FileActivity";
  private Button mConfirm;
  private File mDir;
  private String mDirectoryName;
  private FileAdapter mFileAdapter;
  private AdapterView.OnItemClickListener mFileClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      FileActivity.this.mFileAdapter.setSelectedPosition(paramAnonymousInt);
    }
  };
  private List<String> mFileList;
  private ListView mLwFileList;
  private String mSelectedFile;
  private TextView mTwDirName;
  
  public FileActivity()
  {
    Log.i("FileActivity", "construct");
  }
  
  public void onConfirm(View paramView)
  {
    paramView = new Intent();
    if (this.mFileList.size() > 0)
    {
      paramView.putExtra("ti.android.ble.devicemonitor.FILENAME", this.mDir.getAbsolutePath() + File.separator + this.mSelectedFile);
      setResult(-1, paramView);
    }
    for (;;)
    {
      finish();
      return;
      setResult(0, paramView);
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2131361824);
    this.mDirectoryName = getIntent().getStringExtra("com.ti.ble.sensortag.MESSAGE");
    this.mDir = Environment.getExternalStoragePublicDirectory(this.mDirectoryName);
    Log.i("FileActivity", this.mDirectoryName);
    this.mTwDirName = ((TextView)findViewById(2131231182));
    this.mConfirm = ((Button)findViewById(2131230816));
    this.mLwFileList = ((ListView)findViewById(2131230984));
    this.mLwFileList.setOnItemClickListener(this.mFileClickListener);
    this.mFileList = new ArrayList();
    this.mFileAdapter = new FileAdapter(this, this.mFileList);
    this.mLwFileList.setAdapter(this.mFileAdapter);
    if (this.mDir.exists())
    {
      this.mTwDirName.setText(this.mDir.getAbsolutePath());
      paramBundle = new FilenameFilter()
      {
        public boolean accept(File paramAnonymousFile, String paramAnonymousString)
        {
          return paramAnonymousString.toLowerCase().endsWith(".bin");
        }
      };
      Log.i("FileActivity", this.mDir.getPath());
      paramBundle = this.mDir.listFiles(paramBundle);
      int j = paramBundle.length;
      int i = 0;
      while (i < j)
      {
        Object localObject = paramBundle[i];
        if (!((File)localObject).isDirectory()) {
          this.mFileList.add(((File)localObject).getName());
        }
        i += 1;
      }
      if (this.mFileList.size() == 0) {
        Toast.makeText(this, "No OAD images available", 1).show();
      }
    }
    while (this.mFileList.size() > 0)
    {
      this.mFileAdapter.setSelectedPosition(0);
      return;
      Toast.makeText(this, this.mDirectoryName + " does not exist", 1).show();
    }
    this.mConfirm.setText("Cancel");
  }
  
  public void onDestroy()
  {
    this.mFileList = null;
    this.mFileAdapter = null;
    super.onDestroy();
  }
  
  class FileAdapter
    extends BaseAdapter
  {
    Context mContext;
    List<String> mFiles;
    LayoutInflater mInflater;
    int mSelectedPos;
    
    public FileAdapter(List<String> paramList)
    {
      this.mInflater = LayoutInflater.from(paramList);
      this.mContext = paramList;
      List localList;
      this.mFiles = localList;
      this.mSelectedPos = 0;
    }
    
    public int getCount()
    {
      return this.mFiles.size();
    }
    
    public Object getItem(int paramInt)
    {
      return this.mFiles.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public int getSelectedPosition()
    {
      return this.mSelectedPos;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView != null) {}
      TextView localTextView;
      for (paramView = (ViewGroup)paramView;; paramView = (ViewGroup)this.mInflater.inflate(2131361853, null))
      {
        paramViewGroup = (String)this.mFiles.get(paramInt);
        localTextView = (TextView)paramView.findViewById(2131230990);
        localTextView.setText(paramViewGroup);
        if (paramInt != this.mSelectedPos) {
          break;
        }
        localTextView.setTextAppearance(this.mContext, 2131624277);
        return paramView;
      }
      localTextView.setTextAppearance(this.mContext, 2131624275);
      return paramView;
    }
    
    public void setSelectedPosition(int paramInt)
    {
      FileActivity.access$102(FileActivity.this, (String)FileActivity.this.mFileList.get(paramInt));
      this.mSelectedPos = paramInt;
      notifyDataSetChanged();
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\FileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */