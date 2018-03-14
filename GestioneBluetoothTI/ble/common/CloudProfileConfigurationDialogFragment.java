package com.ti.ble.common;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CloudProfileConfigurationDialogFragment
  extends DialogFragment
  implements AdapterView.OnItemSelectedListener
{
  public static final String ACTION_CLOUD_CONFIG_WAS_UPDATED = "CloudProfileConfigurationDialogFragment.UPDATE";
  public static final Integer DEF_CLOUD_CUSTOM_CLOUD_SERVICE = Integer.valueOf(3);
  public static final Integer DEF_CLOUD_DWEET_IO_SERVICE;
  public static final Integer DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE;
  public static final String DEF_CLOUD_IBMQUICKSTART_BROKER_ADDR = "tcp://quickstart.messaging.internetofthings.ibmcloud.com";
  public static final String DEF_CLOUD_IBMQUICKSTART_BROKER_PORT = "1883";
  public static final Boolean DEF_CLOUD_IBMQUICKSTART_CLEAN_SESSION = Boolean.valueOf(true);
  public static final Integer DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE;
  public static final String DEF_CLOUD_IBMQUICKSTART_DEVICEID_PREFIX = "d:quickstart:st-app:";
  public static final String DEF_CLOUD_IBMQUICKSTART_PASSWORD = "";
  public static final String DEF_CLOUD_IBMQUICKSTART_PUBLISH_TOPIC = "iot-2/evt/status/fmt/json";
  public static final String DEF_CLOUD_IBMQUICKSTART_USERNAME = "";
  public static final Boolean DEF_CLOUD_IBMQUICKSTART_USE_SSL = Boolean.valueOf(false);
  public static final String PREF_CLOUD_BROKER_ADDR = "cloud_broker_address";
  public static final String PREF_CLOUD_BROKER_PORT = "cloud_broker_port";
  public static final String PREF_CLOUD_CLEAN_SESSION = "cloud_clean_session";
  public static final String PREF_CLOUD_DEVICE_ID = "cloud_device_id";
  public static final String PREF_CLOUD_PASSWORD = "cloud_password";
  public static final String PREF_CLOUD_PUBLISH_TOPIC = "cloud_publish_topic";
  public static final String PREF_CLOUD_SERVICE = "cloud_service";
  public static final String PREF_CLOUD_USERNAME = "cloud_username";
  public static final String PREF_CLOUD_USE_SSL = "cloud_use_ssl";
  private String deviceId = "";
  private TextView dwDeviceIdLAbel;
  private TextView dwKeyLabel;
  private TextView dwLockLabel;
  SharedPreferences prefs = null;
  private View v;
  
  static
  {
    DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE = Integer.valueOf(0);
    DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE = Integer.valueOf(1);
    DEF_CLOUD_DWEET_IO_SERVICE = Integer.valueOf(2);
  }
  
  public static CloudProfileConfigurationDialogFragment newInstance(String paramString)
  {
    CloudProfileConfigurationDialogFragment localCloudProfileConfigurationDialogFragment = new CloudProfileConfigurationDialogFragment();
    localCloudProfileConfigurationDialogFragment.deviceId = paramString;
    new Bundle();
    return localCloudProfileConfigurationDialogFragment;
  }
  
  public static String retrieveCloudPref(String paramString, Context paramContext)
  {
    new StringBuilder().append("pref_cloud_config_").append(paramString).toString();
    return "_";
  }
  
  public static boolean setCloudPref(String paramString1, String paramString2, Context paramContext)
  {
    new StringBuilder().append("pref_cloud_config_").append(paramString1).toString();
    return true;
  }
  
  public void enDisBrokerAddressPort(boolean paramBoolean, String paramString1, String paramString2)
  {
    TextView localTextView1 = (TextView)this.v.findViewById(2131230862);
    EditText localEditText1 = (EditText)this.v.findViewById(2131230861);
    TextView localTextView2 = (TextView)this.v.findViewById(2131230864);
    EditText localEditText2 = (EditText)this.v.findViewById(2131230863);
    localEditText1.setEnabled(paramBoolean);
    localEditText2.setEnabled(paramBoolean);
    localEditText1.setText(paramString1);
    localEditText2.setText(paramString2);
    if (paramBoolean)
    {
      localTextView1.setAlpha(1.0F);
      localEditText1.setAlpha(1.0F);
      localTextView2.setAlpha(1.0F);
      localEditText2.setAlpha(1.0F);
      return;
    }
    localTextView1.setAlpha(0.4F);
    localTextView2.setAlpha(0.4F);
    localEditText1.setAlpha(0.4F);
    localEditText2.setAlpha(0.4F);
  }
  
  public void enDisCleanSession(boolean paramBoolean1, boolean paramBoolean2)
  {
    CheckBox localCheckBox = (CheckBox)this.v.findViewById(2131230865);
    localCheckBox.setEnabled(paramBoolean1);
    localCheckBox.setChecked(paramBoolean2);
  }
  
  public void enDisPassword(boolean paramBoolean, String paramString)
  {
    TextView localTextView = (TextView)this.v.findViewById(2131230878);
    EditText localEditText = (EditText)this.v.findViewById(2131230877);
    localEditText.setEnabled(paramBoolean);
    localEditText.setText(paramString);
    if (paramBoolean)
    {
      localTextView.setAlpha(1.0F);
      localEditText.setAlpha(1.0F);
      return;
    }
    localTextView.setAlpha(0.4F);
    localEditText.setAlpha(0.4F);
  }
  
  public void enDisPassword(boolean paramBoolean, String paramString1, String paramString2)
  {
    TextView localTextView = (TextView)this.v.findViewById(2131230878);
    EditText localEditText = (EditText)this.v.findViewById(2131230877);
    localEditText.setEnabled(paramBoolean);
    localEditText.setText(paramString1);
    localEditText.setHint(paramString2);
    if (paramBoolean)
    {
      localTextView.setAlpha(1.0F);
      localEditText.setAlpha(1.0F);
      return;
    }
    localTextView.setAlpha(0.4F);
    localEditText.setAlpha(0.4F);
  }
  
  public void enDisTopic(boolean paramBoolean, String paramString)
  {
    TextView localTextView = (TextView)this.v.findViewById(2131230880);
    EditText localEditText = (EditText)this.v.findViewById(2131230879);
    localEditText.setEnabled(paramBoolean);
    localEditText.setText(paramString);
    if (paramBoolean)
    {
      localTextView.setAlpha(1.0F);
      localEditText.setAlpha(1.0F);
      return;
    }
    localTextView.setAlpha(0.4F);
    localEditText.setAlpha(0.4F);
  }
  
  public void enDisUseSSL(boolean paramBoolean1, boolean paramBoolean2)
  {
    CheckBox localCheckBox = (CheckBox)this.v.findViewById(2131230882);
    localCheckBox.setEnabled(paramBoolean1);
    localCheckBox.setChecked(paramBoolean2);
  }
  
  public void enDisUsername(boolean paramBoolean, String paramString)
  {
    TextView localTextView = (TextView)this.v.findViewById(2131230884);
    EditText localEditText = (EditText)this.v.findViewById(2131230883);
    localEditText.setEnabled(paramBoolean);
    localEditText.setText(paramString);
    if (paramBoolean)
    {
      localTextView.setAlpha(1.0F);
      localEditText.setAlpha(1.0F);
      return;
    }
    localTextView.setAlpha(0.4F);
    localEditText.setAlpha(0.4F);
  }
  
  public void enDisUsername(boolean paramBoolean, String paramString1, String paramString2)
  {
    TextView localTextView = (TextView)this.v.findViewById(2131230884);
    EditText localEditText = (EditText)this.v.findViewById(2131230883);
    localEditText.setEnabled(paramBoolean);
    localEditText.setText(paramString1);
    localEditText.setHint(paramString2);
    if (paramBoolean)
    {
      localTextView.setAlpha(1.0F);
      localEditText.setAlpha(1.0F);
      return;
    }
    localTextView.setAlpha(0.4F);
    localEditText.setAlpha(0.4F);
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity()).setTitle("Cloud configuration").setPositiveButton("Save", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface = Integer.valueOf(((Spinner)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230881)).getSelectedItemPosition());
        CloudProfileConfigurationDialogFragment.setCloudPref("cloud_service", paramAnonymousDialogInterface.toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
        CloudProfileConfigurationDialogFragment.setCloudPref("cloud_username", ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230883)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
        CloudProfileConfigurationDialogFragment.setCloudPref("cloud_password", ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230877)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
        if (paramAnonymousDialogInterface == CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE)
        {
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_device_id", "d:quickstart:st-app:" + ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230875)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_broker_address", ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230861)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_broker_port", ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230863)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_publish_topic", ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230879)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
          if (!((CheckBox)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230865)).isChecked()) {
            break label488;
          }
          paramAnonymousDialogInterface = "true";
          label292:
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_clean_session", paramAnonymousDialogInterface, CloudProfileConfigurationDialogFragment.this.getActivity());
          if (!((CheckBox)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230882)).isChecked()) {
            break label494;
          }
        }
        label488:
        label494:
        for (paramAnonymousDialogInterface = "true";; paramAnonymousDialogInterface = "false")
        {
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_use_ssl", paramAnonymousDialogInterface, CloudProfileConfigurationDialogFragment.this.getActivity());
          CloudProfileConfigurationDialogFragment.this.prefs = PreferenceManager.getDefaultSharedPreferences(CloudProfileConfigurationDialogFragment.this.getActivity());
          paramAnonymousDialogInterface = CloudProfileConfigurationDialogFragment.this.prefs.getAll().entrySet().iterator();
          while (paramAnonymousDialogInterface.hasNext())
          {
            Map.Entry localEntry = (Map.Entry)paramAnonymousDialogInterface.next();
            Log.d("CloudProfileConfigurationDialogFragment", (String)localEntry.getKey() + ":" + localEntry.getValue().toString());
          }
          CloudProfileConfigurationDialogFragment.setCloudPref("cloud_device_id", ((EditText)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230875)).getText().toString(), CloudProfileConfigurationDialogFragment.this.getActivity());
          break;
          paramAnonymousDialogInterface = "false";
          break label292;
        }
        paramAnonymousDialogInterface = new Intent("CloudProfileConfigurationDialogFragment.UPDATE");
        CloudProfileConfigurationDialogFragment.this.getActivity().sendBroadcast(paramAnonymousDialogInterface);
      }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Toast.makeText(CloudProfileConfigurationDialogFragment.this.getActivity(), "No values changed", 1);
      }
    });
    this.v = getActivity().getLayoutInflater().inflate(2131361837, null);
    paramBundle.setTitle("Cloud Setup");
    Spinner localSpinner = (Spinner)this.v.findViewById(2131230881);
    ArrayAdapter localArrayAdapter = ArrayAdapter.createFromResource(getActivity(), 2130837504, 17367048);
    localArrayAdapter.setDropDownViewResource(17367049);
    localSpinner.setAdapter(localArrayAdapter);
    localArrayAdapter.notifyDataSetChanged();
    localSpinner.setOnItemSelectedListener(this);
    try
    {
      localSpinner.setSelection(Integer.valueOf(Integer.parseInt(retrieveCloudPref("cloud_service", getActivity()), 10)).intValue());
      paramBundle.setView(this.v);
      return paramBundle.create();
    }
    catch (Exception localException)
    {
      for (;;) {}
    }
  }
  
  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Log.d("CloudProfileConfigurationDialogFragment", "onItemSelected :" + paramInt);
    switch (paramInt)
    {
    default: 
      this.dwDeviceIdLAbel = ((TextView)this.v.findViewById(2131230876));
      this.dwDeviceIdLAbel.setText("Device ID :");
      this.dwKeyLabel = ((TextView)this.v.findViewById(2131230884));
      this.dwKeyLabel.setText("Username :");
      this.dwLockLabel = ((TextView)this.v.findViewById(2131230878));
      this.dwLockLabel.setText("Password :");
      enDisUsername(true, retrieveCloudPref("cloud_username", getActivity()), "");
      enDisPassword(true, retrieveCloudPref("cloud_password", getActivity()), "");
      setDeviceId(retrieveCloudPref("cloud_device_id", getActivity()));
      enDisBrokerAddressPort(true, retrieveCloudPref("cloud_broker_address", getActivity()), retrieveCloudPref("cloud_broker_port", getActivity()));
      enDisTopic(true, retrieveCloudPref("cloud_publish_topic", getActivity()));
    }
    try
    {
      bool = Boolean.parseBoolean(retrieveCloudPref("cloud_clean_session", getActivity()));
      enDisCleanSession(true, bool);
    }
    catch (Exception paramAdapterView)
    {
      try
      {
        bool = Boolean.parseBoolean(retrieveCloudPref("cloud_use_ssl", getActivity()));
        enDisUseSSL(true, bool);
        return;
        this.dwDeviceIdLAbel = ((TextView)this.v.findViewById(2131230876));
        this.dwDeviceIdLAbel.setText("Device ID :");
        this.dwKeyLabel = ((TextView)this.v.findViewById(2131230884));
        this.dwKeyLabel.setText("Username :");
        this.dwLockLabel = ((TextView)this.v.findViewById(2131230878));
        this.dwLockLabel.setText("Password :");
        enDisUsername(false, "", "");
        enDisPassword(false, "", "");
        setDeviceId(this.deviceId);
        enDisBrokerAddressPort(false, "tcp://quickstart.messaging.internetofthings.ibmcloud.com", "1883");
        enDisTopic(false, "iot-2/evt/status/fmt/json");
        enDisCleanSession(false, DEF_CLOUD_IBMQUICKSTART_CLEAN_SESSION.booleanValue());
        enDisUseSSL(false, DEF_CLOUD_IBMQUICKSTART_USE_SSL.booleanValue());
        return;
        this.dwDeviceIdLAbel = ((TextView)this.v.findViewById(2131230876));
        this.dwDeviceIdLAbel.setText("Device ID :");
        this.dwKeyLabel = ((TextView)this.v.findViewById(2131230884));
        this.dwKeyLabel.setText("Username :");
        this.dwLockLabel = ((TextView)this.v.findViewById(2131230878));
        this.dwLockLabel.setText("Password :");
        enDisUsername(true, retrieveCloudPref("cloud_username", getActivity()), "");
        enDisPassword(true, retrieveCloudPref("cloud_password", getActivity()), "");
        setDeviceId(retrieveCloudPref("cloud_device_id", getActivity()));
        enDisBrokerAddressPort(true, retrieveCloudPref("cloud_broker_address", getActivity()), retrieveCloudPref("cloud_broker_port", getActivity()));
        enDisTopic(true, retrieveCloudPref("cloud_publish_topic", getActivity()));
        try
        {
          bool = Boolean.parseBoolean(retrieveCloudPref("cloud_clean_session", getActivity()));
          enDisCleanSession(true, bool);
        }
        catch (Exception paramAdapterView)
        {
          try
          {
            bool = Boolean.parseBoolean(retrieveCloudPref("cloud_use_ssl", getActivity()));
            enDisUseSSL(true, bool);
            return;
            paramAdapterView = paramAdapterView;
            paramAdapterView.printStackTrace();
            bool = false;
          }
          catch (Exception paramAdapterView)
          {
            for (;;)
            {
              paramAdapterView.printStackTrace();
              bool = false;
            }
          }
        }
        getActivity().runOnUiThread(new Runnable()
        {
          public void run()
          {
            CloudProfileConfigurationDialogFragment.access$102(CloudProfileConfigurationDialogFragment.this, (TextView)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230876));
            CloudProfileConfigurationDialogFragment.this.dwDeviceIdLAbel.setText("Thing :");
            CloudProfileConfigurationDialogFragment.access$202(CloudProfileConfigurationDialogFragment.this, (TextView)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230884));
            CloudProfileConfigurationDialogFragment.this.dwKeyLabel.setText("Key");
            CloudProfileConfigurationDialogFragment.access$302(CloudProfileConfigurationDialogFragment.this, (TextView)CloudProfileConfigurationDialogFragment.this.v.findViewById(2131230878));
            CloudProfileConfigurationDialogFragment.this.dwLockLabel.setText("Lock");
            CloudProfileConfigurationDialogFragment.this.enDisUsername(true, CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_username", CloudProfileConfigurationDialogFragment.this.getActivity()), "Enter Key here, or none for non-locked mode");
            CloudProfileConfigurationDialogFragment.this.enDisPassword(true, CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_password", CloudProfileConfigurationDialogFragment.this.getActivity()), "Enter Lock here, or none for non-locked mode");
            CloudProfileConfigurationDialogFragment.this.setDeviceId(CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_device_id", CloudProfileConfigurationDialogFragment.this.getActivity()));
            CloudProfileConfigurationDialogFragment.this.enDisUseSSL(false, false);
            CloudProfileConfigurationDialogFragment.this.enDisCleanSession(false, false);
            CloudProfileConfigurationDialogFragment.this.enDisBrokerAddressPort(false, "https://dweet.io:443/dweet/for", "0");
            CloudProfileConfigurationDialogFragment.this.enDisTopic(false, "");
          }
        });
        return;
        enDisUsername(true, retrieveCloudPref("cloud_username", getActivity()), "");
        enDisPassword(true, retrieveCloudPref("cloud_password", getActivity()), "");
        setDeviceId(retrieveCloudPref("cloud_device_id", getActivity()));
        enDisBrokerAddressPort(true, retrieveCloudPref("cloud_broker_address", getActivity()), retrieveCloudPref("cloud_broker_port", getActivity()));
        enDisTopic(true, retrieveCloudPref("cloud_publish_topic", getActivity()));
        try
        {
          bool = Boolean.parseBoolean(retrieveCloudPref("cloud_clean_session", getActivity()));
          enDisCleanSession(true, bool);
        }
        catch (Exception paramAdapterView)
        {
          try
          {
            bool = Boolean.parseBoolean(retrieveCloudPref("cloud_use_ssl", getActivity()));
            enDisUseSSL(true, bool);
            return;
            paramAdapterView = paramAdapterView;
            paramAdapterView.printStackTrace();
            bool = false;
          }
          catch (Exception paramAdapterView)
          {
            for (;;)
            {
              paramAdapterView.printStackTrace();
              bool = false;
            }
          }
        }
        paramAdapterView = paramAdapterView;
        paramAdapterView.printStackTrace();
        bool = false;
      }
      catch (Exception paramAdapterView)
      {
        for (;;)
        {
          paramAdapterView.printStackTrace();
          boolean bool = false;
        }
      }
    }
  }
  
  public void onNothingSelected(AdapterView<?> paramAdapterView)
  {
    Log.d("CloudProfileConfigurationDialogFragment", "onNothingSelected" + paramAdapterView);
  }
  
  public void setDeviceId(String paramString)
  {
    ((EditText)this.v.findViewById(2131230875)).setText(paramString);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\CloudProfileConfigurationDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */