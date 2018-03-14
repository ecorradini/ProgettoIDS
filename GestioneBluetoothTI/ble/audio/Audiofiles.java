package com.ti.ble.audio;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Audiofiles
  extends Fragment
{
  private static final String ARG_SECTION_NUMBER = "section_number";
  private BroadcastReceiver AdvancedRemoteBLEAudioMessageReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getStringExtra("newFile") != null)
      {
        Log.d("Audiofiles", "New File available !");
        Audiofiles.this.reloadListView();
      }
    }
  };
  Button clearFiles;
  ListView fileList;
  ArrayList<String> list;
  
  public static Audiofiles newInstance(int paramInt)
  {
    Audiofiles localAudiofiles = new Audiofiles();
    Bundle localBundle = new Bundle();
    localBundle.putInt("section_number", paramInt);
    localAudiofiles.setArguments(localBundle);
    return localAudiofiles;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    int i = 0;
    paramLayoutInflater = paramLayoutInflater.inflate(2131361834, paramViewGroup, false);
    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.AdvancedRemoteBLEAudioMessageReceiver, new IntentFilter("ARCBLEAudio-From-Service-Events"));
    if (!Environment.getExternalStorageState().equals("mounted")) {}
    for (;;)
    {
      this.fileList = ((ListView)paramLayoutInflater.findViewById(2131230938));
      this.fileList.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          try
          {
            Audiofiles.this.playRecording((String)Audiofiles.this.list.get(paramAnonymousInt));
            return;
          }
          catch (IOException paramAnonymousAdapterView) {}
        }
      });
      reloadListView();
      this.clearFiles = ((Button)paramLayoutInflater.findViewById(2131231075));
      this.clearFiles.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Log.d("info", "Clear files button clicked !");
          paramAnonymousView = new File(Environment.getExternalStorageDirectory().getPath() + "/BLEAudioFiles");
          if (!paramAnonymousView.exists()) {
            paramAnonymousView.mkdirs();
          }
          paramAnonymousView = paramAnonymousView.listFiles();
          int j = paramAnonymousView.length;
          int i = 0;
          while (i < j)
          {
            Object localObject = paramAnonymousView[i];
            Log.d("Audiofiles", "File : " + ((File)localObject).getAbsolutePath());
            ((File)localObject).delete();
            i += 1;
          }
          Audiofiles.this.reloadListView();
        }
      });
      return paramLayoutInflater;
      paramViewGroup = new File(Environment.getExternalStorageDirectory().getPath() + "/BLEAudioFiles");
      if (!paramViewGroup.exists()) {
        paramViewGroup.mkdirs();
      }
      paramViewGroup = paramViewGroup.listFiles();
      int j = paramViewGroup.length;
      while (i < j)
      {
        paramBundle = paramViewGroup[i];
        Log.d("Audiofiles", "File : " + paramBundle.getAbsolutePath());
        i += 1;
      }
    }
  }
  
  public void playRecording(String paramString)
    throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
  {
    paramString = Uri.parse("file:///" + Environment.getExternalStorageDirectory().getPath() + "/BLEAudioFiles/" + paramString);
    MediaPlayer localMediaPlayer = new MediaPlayer();
    if (localMediaPlayer != null)
    {
      localMediaPlayer.setAudioStreamType(3);
      localMediaPlayer.setDataSource(getActivity(), paramString);
      localMediaPlayer.prepare();
      localMediaPlayer.start();
    }
  }
  
  public void reloadListView()
  {
    Object localObject1 = new File(Environment.getExternalStorageDirectory().getPath() + "/BLEAudioFiles");
    if (!((File)localObject1).exists()) {
      ((File)localObject1).mkdirs();
    }
    this.list = new ArrayList();
    localObject1 = ((File)localObject1).listFiles();
    int j = localObject1.length;
    int i = 0;
    while (i < j)
    {
      Object localObject2 = localObject1[i];
      Log.d("Audiofiles", "File : " + ((File)localObject2).getAbsolutePath());
      this.list.add(((File)localObject2).getName());
      i += 1;
    }
    localObject1 = new fileListArrayAdapter(getActivity(), 17367043, this.list);
    this.fileList.setAdapter((ListAdapter)localObject1);
  }
  
  private class fileListArrayAdapter
    extends ArrayAdapter<String>
  {
    HashMap<String, Integer> mIdMap = new HashMap();
    
    public fileListArrayAdapter(int paramInt, List<String> paramList)
    {
      super(paramList, localList);
      paramList = 0;
      while (paramList < localList.size())
      {
        this.mIdMap.put(localList.get(paramList), Integer.valueOf(paramList));
        paramList += 1;
      }
    }
    
    public long getItemId(int paramInt)
    {
      String str = (String)getItem(paramInt);
      return ((Integer)this.mIdMap.get(str)).intValue();
    }
    
    public boolean hasStableIds()
    {
      return true;
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\audio\Audiofiles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */