package com.ti.ble.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PowerManager.WakeLock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class IBMIoTCloudProfile
  extends GenericBluetoothProfile
{
  static IBMIoTCloudProfile mThis;
  final String addrShort;
  MqttAndroidClient client;
  BroadcastReceiver cloudConfigUpdateReceiver;
  cloudConfig config;
  public boolean isConnected;
  MemoryPersistence memPer;
  IBMIoTCloudTableRow myRow;
  Timer publishTimer;
  public boolean ready;
  final String startString = "{\n \"d\":{\n";
  final String stopString = "\n}\n}";
  Map<String, String> valueMap = new HashMap();
  private PowerManager.WakeLock wakeLock;
  
  public IBMIoTCloudProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.myRow = new IBMIoTCloudTableRow(paramContext);
    this.myRow.setOnClickListener(null);
    this.config = readCloudConfigFromPrefs();
    this.isConnected = false;
    paramContext = this.mBTDevice.getAddress();
    paramBluetoothDevice = paramContext.split(":");
    paramBluetoothGattService = new int[6];
    int i = 0;
    while (i < 6)
    {
      paramBluetoothGattService[i] = Integer.parseInt(paramBluetoothDevice[i], 16);
      i += 1;
    }
    this.ready = false;
    this.addrShort = String.format("%02x%02x%02x%02x%02x%02x", new Object[] { Integer.valueOf(paramBluetoothGattService[0]), Integer.valueOf(paramBluetoothGattService[1]), Integer.valueOf(paramBluetoothGattService[2]), Integer.valueOf(paramBluetoothGattService[3]), Integer.valueOf(paramBluetoothGattService[4]), Integer.valueOf(paramBluetoothGattService[5]) });
    if (this.config != null)
    {
      Log.d("IBMIoTCloudProfile", "Stored cloud configuration\r\n" + this.config.toString());
      Log.d("IBMIoTCloudProfile", "Device ID : " + this.addrShort);
      this.myRow.title.setText("Cloud View");
    }
    for (;;)
    {
      try
      {
        if ((!this.mBTDevice.getName().equals("CC2650 SensorTag")) && (!this.mBTDevice.getName().equals("CC1350 SensorTag"))) {
          continue;
        }
        this.myRow.setIcon("sensortag2_cloudservice", "", "");
      }
      catch (NullPointerException paramContext)
      {
        this.myRow.setIcon("sensortag2_cloudservice", "", "");
        continue;
        if ((!this.mBTDevice.getName().equals("CC2650 RC")) && (!this.mBTDevice.getName().equals("HID AdvRemote"))) {
          continue;
        }
        this.myRow.setIcon("cc2650_rc_cloudservice", "", "");
        continue;
        if ((!this.mBTDevice.getName().equals("CC2650 LaunchPad")) && (!this.mBTDevice.getName().equals("CC1350 LaunchPad"))) {
          continue;
        }
        this.myRow.icon.setImageResource(2131492867);
        continue;
        if (!this.mBTDevice.getName().equals("Throughput Periph")) {
          continue;
        }
        this.myRow.icon.setImageResource(2131492867);
        continue;
        if ((!this.mBTDevice.getName().equalsIgnoreCase("ProjectZero")) && (!this.mBTDevice.getName().equalsIgnoreCase("Project Zero"))) {
          continue;
        }
        this.myRow.icon.setImageResource(2131492912);
        continue;
        this.myRow.setIcon("sensortag_cloudservice", "", "");
        continue;
        this.myRow.cloudURL.setText("");
        this.myRow.cloudURL.setAlpha(0.1F);
        continue;
      }
      this.myRow.value.setText("Device ID : " + paramContext);
      paramContext = this.myRow;
      paramContext.pushToCloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
        {
          if (paramAnonymousBoolean)
          {
            IBMIoTCloudProfile.this.connect();
            return;
          }
          IBMIoTCloudProfile.this.disconnect();
        }
      });
      paramContext.configureCloud.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          CloudProfileConfigurationDialogFragment.newInstance(IBMIoTCloudProfile.this.addrShort).show(((Activity)IBMIoTCloudProfile.this.context).getFragmentManager(), "CloudConfig");
        }
      });
      if (this.config.service != CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE) {
        continue;
      }
      this.myRow.cloudURL.setText("Open in browser");
      this.myRow.cloudURL.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://quickstart.internetofthings.ibmcloud.com/#/device/" + IBMIoTCloudProfile.this.addrShort + "/sensor/")));
        }
      });
      mThis = this;
      this.cloudConfigUpdateReceiver = new BroadcastReceiver()
      {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
          if (paramAnonymousIntent.getAction().equals("CloudProfileConfigurationDialogFragment.UPDATE"))
          {
            Log.d("IBMIoTCloudProfile", "Cloud configuration was updated !");
            Log.d("IBMIoTCloudProfile", "Old cloud configuration was :" + IBMIoTCloudProfile.this.config.toString());
            IBMIoTCloudProfile.this.config = IBMIoTCloudProfile.this.readCloudConfigFromPrefs();
            Log.d("IBMIoTCloudProfile", "New cloud configuration :" + IBMIoTCloudProfile.this.config.toString());
            if (IBMIoTCloudProfile.this.client == null) {}
          }
          try
          {
            if (IBMIoTCloudProfile.this.client.isConnected())
            {
              IBMIoTCloudProfile.this.disconnect();
              IBMIoTCloudProfile.this.connect();
            }
            return;
          }
          catch (Exception paramAnonymousContext)
          {
            paramAnonymousContext.printStackTrace();
          }
        }
      };
      this.context.registerReceiver(this.cloudConfigUpdateReceiver, makeCloudConfigUpdateFilter());
      return;
      this.config = initPrefsWithIBMQuickStart();
      Log.d("IBMIoTCloudProfile", "Stored cloud configuration was corrupt, starting new based on IBM IoT Quickstart variables" + this.config.toString());
      break;
      if (!this.mBTDevice.getName().equals("SensorTag")) {
        continue;
      }
      this.myRow.setIcon("sensortag_cloudservice", "", "");
    }
  }
  
  public static IBMIoTCloudProfile getInstance()
  {
    return mThis;
  }
  
  private static IntentFilter makeCloudConfigUpdateFilter()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("CloudProfileConfigurationDialogFragment.UPDATE");
    return localIntentFilter;
  }
  
  public void addSensorValueToPendingMessage(String paramString1, String paramString2)
  {
    this.valueMap.put(paramString1, paramString2);
  }
  
  public void addSensorValueToPendingMessage(Map.Entry<String, String> paramEntry)
  {
    this.valueMap.put(paramEntry.getKey(), paramEntry.getValue());
  }
  
  public void configureService() {}
  
  public boolean connect()
  {
    for (;;)
    {
      try
      {
        this.memPer = new MemoryPersistence();
        localObject = this.config.brokerAddress + ":" + this.config.brokerPort;
        Log.d("IBMIoTCloudProfile", "Cloud Broker URL : " + (String)localObject);
        this.client = new MqttAndroidClient(this.context, (String)localObject, this.config.deviceId);
        localObject = null;
        if ((this.config.service == CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE) || (this.config.service == CloudProfileConfigurationDialogFragment.DEF_CLOUD_CUSTOM_CLOUD_SERVICE))
        {
          localObject = new MqttConnectOptions();
          ((MqttConnectOptions)localObject).setCleanSession(this.config.cleanSession);
          if (this.config.username.length() > 0) {
            ((MqttConnectOptions)localObject).setUserName(this.config.username);
          }
          if (this.config.password.length() > 0) {
            ((MqttConnectOptions)localObject).setPassword(this.config.password.toCharArray());
          }
          Log.d("IBMIoTCloudProfile", "Adding Options : Clean Session : " + ((MqttConnectOptions)localObject).isCleanSession() + ", Username : " + this.config.username + ", Password : \"" + this.config.password + "\"");
        }
        if (this.config.service != CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMIOTFOUNDATION_CLOUD_SERVICE) {
          continue;
        }
        this.client.connect((MqttConnectOptions)localObject, null, new IMqttActionListener()
        {
          public void onFailure(IMqttToken paramAnonymousIMqttToken, Throwable paramAnonymousThrowable)
          {
            Log.d("IBMIoTCloudProfile", "Connection to IBM cloud failed !");
            Log.d("IBMIoTCloudProfile", "Error: " + paramAnonymousThrowable.getLocalizedMessage());
            IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492881);
            paramAnonymousIMqttToken = new AlertDialog.Builder(IBMIoTCloudProfile.this.context);
            paramAnonymousIMqttToken.setTitle("Connection to Cloud failed !");
            paramAnonymousIMqttToken.setMessage(Html.fromHtml("<b>Connection to : </b><br>" + IBMIoTCloudProfile.this.client.getServerURI() + "<br><br><b>Device id : </b><br>" + IBMIoTCloudProfile.this.client.getClientId() + "<br><br><b>Failed with error code :</b><br><font color='#FF0000'>" + paramAnonymousThrowable.getLocalizedMessage() + "</font>"));
            paramAnonymousIMqttToken.setPositiveButton("OK", null);
            paramAnonymousIMqttToken.create().show();
            IBMIoTCloudProfile.this.myRow.pushToCloud.setChecked(false);
            IBMIoTCloudProfile.this.disconnect();
          }
          
          public void onSuccess(IMqttToken paramAnonymousIMqttToken)
          {
            Log.d("IBMIoTCloudProfile", "Connected to cloud : " + IBMIoTCloudProfile.this.client.getServerURI() + "," + IBMIoTCloudProfile.this.client.getClientId());
            try
            {
              IBMIoTCloudProfile.this.client.publish(IBMIoTCloudProfile.this.config.publishTopic, IBMIoTCloudProfile.this.jsonEncode("myName", IBMIoTCloudProfile.this.mBTDevice.getName().toString()).getBytes(), 0, false);
              IBMIoTCloudProfile.this.ready = true;
              IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492879);
              return;
            }
            catch (MqttException paramAnonymousIMqttToken)
            {
              for (;;)
              {
                paramAnonymousIMqttToken.printStackTrace();
              }
            }
          }
        });
      }
      catch (MqttException localMqttException)
      {
        Object localObject;
        localMqttException.printStackTrace();
        continue;
        this.client.connect(localMqttException, new IMqttActionListener()
        {
          public void onFailure(IMqttToken paramAnonymousIMqttToken, Throwable paramAnonymousThrowable)
          {
            Log.d("IBMIoTCloudProfile", "Connection to IBM cloud failed !");
            Log.d("IBMIoTCloudProfile", "Error: " + paramAnonymousThrowable.getLocalizedMessage());
            IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492881);
            paramAnonymousIMqttToken = new AlertDialog.Builder(IBMIoTCloudProfile.this.context);
            paramAnonymousIMqttToken.setTitle("Connection to Cloud failed !");
            paramAnonymousIMqttToken.setMessage(Html.fromHtml("<b>Connection to : </b><br>" + IBMIoTCloudProfile.this.client.getServerURI() + "<br><br><b>Device id : </b><br>" + IBMIoTCloudProfile.this.client.getClientId() + "<br><br><b>Failed with error code :</b><br><font color='#FF0000'>" + paramAnonymousThrowable.getLocalizedMessage() + "</font>"));
            paramAnonymousIMqttToken.setPositiveButton("OK", null);
            paramAnonymousIMqttToken.create().show();
            IBMIoTCloudProfile.this.myRow.pushToCloud.setChecked(false);
            IBMIoTCloudProfile.this.disconnect();
          }
          
          public void onSuccess(IMqttToken paramAnonymousIMqttToken)
          {
            Log.d("IBMIoTCloudProfile", "Connected to cloud : " + IBMIoTCloudProfile.this.client.getServerURI() + "," + IBMIoTCloudProfile.this.client.getClientId());
            try
            {
              IBMIoTCloudProfile.this.client.publish(IBMIoTCloudProfile.this.config.publishTopic, IBMIoTCloudProfile.this.jsonEncode("myName", IBMIoTCloudProfile.this.mBTDevice.getName().toString()).getBytes(), 0, false);
              IBMIoTCloudProfile.this.ready = true;
              IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492879);
              IBMIoTCloudProfile.this.isConnected = true;
              return;
            }
            catch (MqttException paramAnonymousIMqttToken)
            {
              for (;;)
              {
                paramAnonymousIMqttToken.printStackTrace();
              }
            }
          }
        });
        continue;
        this.publishTimer = new Timer();
        MQTTTimerTask localMQTTTimerTask = new MQTTTimerTask();
        this.publishTimer.schedule(localMQTTTimerTask, 1000L, 1000L);
        continue;
      }
      if (this.config.service != CloudProfileConfigurationDialogFragment.DEF_CLOUD_DWEET_IO_SERVICE) {
        continue;
      }
      this.publishTimer = new Timer();
      localObject = new dweetIOTimerTask();
      this.publishTimer.schedule((TimerTask)localObject, 1100L, 1100L);
      return true;
      if (this.config.service != CloudProfileConfigurationDialogFragment.DEF_CLOUD_DWEET_IO_SERVICE) {
        continue;
      }
      Log.d("IBMIoTCloudProfile", "Connecting to cloud : " + this.config.brokerAddress + "," + this.config.deviceId);
    }
  }
  
  public void deConfigureService() {}
  
  public void didReadValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {}
  
  public void disableService() {}
  
  public boolean disconnect()
  {
    try
    {
      this.myRow.setCloudConnectionStatusImage(2131492881);
      this.ready = false;
      if (this.publishTimer != null) {
        this.publishTimer.cancel();
      }
      if (this.client != null)
      {
        Log.d("IBMIoTCloudProfile", "Disconnecting from cloud : " + this.client.getServerURI() + "," + this.client.getClientId());
        if (this.client.isConnected()) {
          this.client.disconnect();
        }
        this.client.unregisterResources();
        this.client = null;
        this.memPer = null;
      }
      this.isConnected = false;
      return true;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return false;
  }
  
  public void enableService() {}
  
  public TableRow getTableRow()
  {
    return this.myRow;
  }
  
  public cloudConfig initPrefsWithIBMQuickStart()
  {
    cloudConfig localcloudConfig = new cloudConfig();
    localcloudConfig.service = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE;
    localcloudConfig.username = "";
    localcloudConfig.password = "";
    localcloudConfig.deviceId = ("d:quickstart:st-app:" + this.addrShort);
    localcloudConfig.brokerAddress = "tcp://quickstart.messaging.internetofthings.ibmcloud.com";
    try
    {
      localcloudConfig.brokerPort = Integer.parseInt("1883");
      localcloudConfig.publishTopic = "iot-2/evt/status/fmt/json";
      localcloudConfig.cleanSession = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLEAN_SESSION.booleanValue();
      localcloudConfig.useSSL = CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_USE_SSL.booleanValue();
      return localcloudConfig;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localcloudConfig.brokerPort = 1883;
      }
    }
  }
  
  public String jsonEncode(String paramString)
  {
    String str = new String();
    str = str + "{\n \"d\":{\n";
    paramString = str + paramString;
    return paramString + "\n}\n}";
  }
  
  public String jsonEncode(String paramString1, String paramString2)
  {
    String str = new String();
    str = str + "{\n \"d\":{\n";
    paramString1 = str + "\"" + paramString1 + "\":\"" + paramString2 + "\"";
    return paramString1 + "\n}\n}";
  }
  
  public void onPause()
  {
    super.onPause();
    this.context.unregisterReceiver(this.cloudConfigUpdateReceiver);
  }
  
  public void onResume()
  {
    super.onResume();
    this.context.registerReceiver(this.cloudConfigUpdateReceiver, makeCloudConfigUpdateFilter());
  }
  
  public void publishString(String paramString)
  {
    new MqttMessage();
    try
    {
      this.client.publish(this.config.publishTopic, jsonEncode("Test", "123").getBytes(), 0, false);
      return;
    }
    catch (MqttException paramString)
    {
      paramString.printStackTrace();
    }
  }
  
  public cloudConfig readCloudConfigFromPrefs()
  {
    cloudConfig localcloudConfig = new cloudConfig();
    try
    {
      localcloudConfig.service = Integer.valueOf(Integer.parseInt(CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_service", this.context), 10));
      localcloudConfig.username = CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_username", this.context);
      localcloudConfig.password = CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_password", this.context);
      localcloudConfig.deviceId = CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_device_id", this.context);
      localcloudConfig.brokerAddress = CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_broker_address", this.context);
      localcloudConfig.brokerPort = Integer.parseInt(CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_broker_port", this.context), 10);
      localcloudConfig.publishTopic = CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_publish_topic", this.context);
      localcloudConfig.cleanSession = Boolean.parseBoolean(CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_clean_session", this.context));
      localcloudConfig.useSSL = Boolean.parseBoolean(CloudProfileConfigurationDialogFragment.retrieveCloudPref("cloud_use_ssl", this.context));
      if (localcloudConfig.service == CloudProfileConfigurationDialogFragment.DEF_CLOUD_IBMQUICKSTART_CLOUD_SERVICE)
      {
        this.myRow.cloudURL.setText("Open in browser");
        this.myRow.cloudURL.setAlpha(1.0F);
        this.myRow.cloudURL.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            paramAnonymousView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://quickstart.internetofthings.ibmcloud.com/#/device/" + IBMIoTCloudProfile.this.addrShort + "/sensor/")));
          }
        });
        return localcloudConfig;
      }
      this.myRow.cloudURL.setText("");
      this.myRow.cloudURL.setAlpha(0.1F);
      return localcloudConfig;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public void writeCloudConfigToPrefs(cloudConfig paramcloudConfig)
  {
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_service", paramcloudConfig.service.toString(), this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_username", paramcloudConfig.username, this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_password", paramcloudConfig.password, this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_device_id", paramcloudConfig.deviceId, this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_broker_address", paramcloudConfig.brokerAddress, this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_broker_port", Integer.valueOf(paramcloudConfig.brokerPort).toString(), this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_publish_topic", paramcloudConfig.publishTopic, this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_clean_session", Boolean.valueOf(paramcloudConfig.cleanSession).toString(), this.context);
    CloudProfileConfigurationDialogFragment.setCloudPref("cloud_use_ssl", Boolean.valueOf(paramcloudConfig.useSSL).toString(), this.context);
  }
  
  class MQTTTimerTask
    extends TimerTask
  {
    MQTTTimerTask() {}
    
    public void run()
    {
      try
      {
        if (IBMIoTCloudProfile.this.ready)
        {
          Activity localActivity = (Activity)IBMIoTCloudProfile.this.context;
          localActivity.runOnUiThread(new Runnable()
          {
            public void run()
            {
              IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492880);
            }
          });
          String str1 = "";
          Object localObject1 = new HashMap();
          ((Map)localObject1).putAll(IBMIoTCloudProfile.this.valueMap);
          localObject1 = ((Map)localObject1).entrySet().iterator();
          while (((Iterator)localObject1).hasNext())
          {
            Object localObject2 = (Map.Entry)((Iterator)localObject1).next();
            String str2 = (String)((Map.Entry)localObject2).getKey();
            localObject2 = (String)((Map.Entry)localObject2).getValue();
            str1 = str1 + "\"" + str2 + "\":\"" + (String)localObject2 + "\",\n";
          }
          if (str1.length() > 0)
          {
            str1 = str1.substring(0, str1.length() - 2);
            IBMIoTCloudProfile.this.client.publish(IBMIoTCloudProfile.this.config.publishTopic, IBMIoTCloudProfile.this.jsonEncode(str1).getBytes(), 0, false);
          }
          try
          {
            Thread.sleep(60L);
            localActivity.runOnUiThread(new Runnable()
            {
              public void run()
              {
                IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492879);
              }
            });
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
        Log.d("IBMIoTCloudProfile", "MQTTTimerTask ran, but MQTT not ready");
      }
      catch (MqttException localMqttException)
      {
        localMqttException.printStackTrace();
        return;
      }
    }
  }
  
  class cloudConfig
  {
    public String brokerAddress;
    public int brokerPort;
    public boolean cleanSession;
    public String deviceId;
    public String password;
    public String publishTopic;
    public Integer service;
    public boolean useSSL;
    public String username;
    
    cloudConfig() {}
    
    public String toString()
    {
      new String();
      String str = "Cloud configuration :\r\n" + "Service : " + this.service + "\r\n";
      str = str + "Username : " + this.username + "\r\n";
      str = str + "Password : " + this.password + "\r\n";
      str = str + "Device ID : " + this.deviceId + "\r\n";
      str = str + "Broker Address : " + this.brokerAddress + "\r\n";
      str = str + "Proker Port : " + this.brokerPort + "\r\n";
      str = str + "Publish Topic : " + this.publishTopic + "\r\n";
      str = str + "Clean Session : " + this.cleanSession + "\r\n";
      return str + "Use SSL : " + this.useSSL + "\r\n";
    }
  }
  
  class dweetIOTimerTask
    extends TimerTask
  {
    dweetIOTimerTask() {}
    
    public void run()
    {
      try
      {
        Activity localActivity = (Activity)IBMIoTCloudProfile.this.context;
        localActivity.runOnUiThread(new Runnable()
        {
          public void run()
          {
            IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492880);
          }
        });
        Object localObject1 = "";
        Object localObject3 = new HashMap();
        ((Map)localObject3).putAll(IBMIoTCloudProfile.this.valueMap);
        localObject3 = ((Map)localObject3).entrySet().iterator();
        Object localObject4;
        while (((Iterator)localObject3).hasNext())
        {
          Object localObject5 = (Map.Entry)((Iterator)localObject3).next();
          localObject4 = (String)((Map.Entry)localObject5).getKey();
          localObject5 = (String)((Map.Entry)localObject5).getValue();
          localObject1 = (String)localObject1 + "\"" + (String)localObject4 + "\":\"" + (String)localObject5 + "\",\n";
        }
        if (((String)localObject1).length() > 0)
        {
          localObject3 = "{\n" + ((String)localObject1).substring(0, ((String)localObject1).length() - 2) + "\n}";
          localObject1 = (HttpURLConnection)new URL(IBMIoTCloudProfile.this.config.brokerAddress + "/" + IBMIoTCloudProfile.this.config.deviceId).openConnection();
        }
        try
        {
          ((HttpURLConnection)localObject1).setRequestMethod("POST");
          ((HttpURLConnection)localObject1).setRequestProperty("Content-Type", "application/json; charset=utf-8");
          ((HttpURLConnection)localObject1).setDoOutput(true);
          ((HttpURLConnection)localObject1).setDoInput(true);
          ((HttpURLConnection)localObject1).setChunkedStreamingMode(0);
          localObject4 = new PrintWriter(((HttpURLConnection)localObject1).getOutputStream());
          ((PrintWriter)localObject4).println(((String)localObject3).toString());
          ((PrintWriter)localObject4).flush();
          ((PrintWriter)localObject4).close();
          localObject3 = new BufferedInputStream(((HttpURLConnection)localObject1).getInputStream());
          localObject4 = new byte['Ѐ'];
          ((InputStream)localObject3).read((byte[])localObject4, 0, 1024);
          Log.d("dweetIOIoT", "Response body : " + new String((byte[])localObject4));
          Log.d("dweetIOIoT", "Reply from WEB Server : " + ((HttpURLConnection)localObject1).getResponseCode());
          Log.d("dweetIOIoT", "Reply Text : " + ((HttpURLConnection)localObject1).getResponseMessage());
          ((HttpURLConnection)localObject1).disconnect();
          localActivity.runOnUiThread(new Runnable()
          {
            public void run()
            {
              IBMIoTCloudProfile.this.myRow.setCloudConnectionStatusImage(2131492879);
            }
          });
          return;
        }
        finally
        {
          Log.d("dweetIOIoT", "Reply from WEB Server : " + ((HttpURLConnection)localObject1).getResponseCode());
          Log.d("dweetIOIoT", "Reply Text : " + ((HttpURLConnection)localObject1).getResponseMessage());
          ((HttpURLConnection)localObject1).disconnect();
        }
        return;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\IBMIoTCloudProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */