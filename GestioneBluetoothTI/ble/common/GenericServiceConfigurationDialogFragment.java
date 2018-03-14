package com.ti.ble.common;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class GenericServiceConfigurationDialogFragment
  extends DialogFragment
  implements SeekBar.OnSeekBarChangeListener
{
  public static final String ACTION_GENERIC_SERVICE_CONFIGURATION_WAS_UPDATED = "GenericServiceConfigurationDialogFragment.UPDATE";
  public static final String VALUE_GENERIC_SERVICE_CONFIGURATION_PERIOD = "com.ti.ble.common.GenericServiceConfigurationDialog.PERIOD";
  public static final String VALUE_GENERIC_SERVICE_CONFIGURATION_SENSOR_STATE = "com.ti.ble.common.GenericServiceConfigurationDialog.SENSOR_STATE";
  private String UUID;
  public SeekBar currentPeriodSeekBar;
  public TextView currentPeriodText;
  public Switch currentSensorState;
  public int lastPeriod;
  public boolean lastServiceOn;
  public int minPeriod;
  public TextView minimumPeriodText;
  private View v;
  
  public static GenericServiceConfigurationDialogFragment newInstance(String paramString, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    GenericServiceConfigurationDialogFragment localGenericServiceConfigurationDialogFragment = new GenericServiceConfigurationDialogFragment();
    new Bundle();
    localGenericServiceConfigurationDialogFragment.UUID = paramString;
    localGenericServiceConfigurationDialogFragment.lastPeriod = paramInt1;
    localGenericServiceConfigurationDialogFragment.lastServiceOn = paramBoolean;
    localGenericServiceConfigurationDialogFragment.minPeriod = paramInt2;
    return localGenericServiceConfigurationDialogFragment;
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity()).setTitle("Cloud configuration").setPositiveButton("Save", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface = (TextView)GenericServiceConfigurationDialogFragment.this.v.findViewById(2131230953);
        Switch localSwitch = (Switch)GenericServiceConfigurationDialogFragment.this.v.findViewById(2131230956);
        Intent localIntent = new Intent("GenericServiceConfigurationDialogFragment.UPDATE");
        localIntent.putExtra("com.ti.util.EXTRA_SERVICE_UUID", GenericServiceConfigurationDialogFragment.this.UUID);
        localIntent.putExtra("com.ti.ble.common.GenericServiceConfigurationDialog.PERIOD", paramAnonymousDialogInterface.getText().toString());
        localIntent.putExtra("com.ti.ble.common.GenericServiceConfigurationDialog.SENSOR_STATE", localSwitch.isChecked());
        GenericServiceConfigurationDialogFragment.this.getActivity().sendBroadcast(localIntent);
      }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Toast.makeText(GenericServiceConfigurationDialogFragment.this.getActivity(), "No values changed", 1);
      }
    });
    this.v = getActivity().getLayoutInflater().inflate(2131361859, null);
    paramBundle.setTitle("Service configuration");
    paramBundle.setView(this.v);
    this.currentPeriodSeekBar = ((SeekBar)this.v.findViewById(2131230955));
    this.currentPeriodSeekBar.setMax(2500);
    this.currentPeriodText = ((TextView)this.v.findViewById(2131230953));
    this.currentSensorState = ((Switch)this.v.findViewById(2131230956));
    this.currentSensorState.setChecked(this.lastServiceOn);
    this.currentPeriodSeekBar.setProgress(this.lastPeriod);
    this.currentPeriodText.setText(this.lastPeriod + "ms");
    this.currentPeriodSeekBar.setOnSeekBarChangeListener(this);
    this.minimumPeriodText = ((TextView)this.v.findViewById(2131230954));
    this.minimumPeriodText.setText(this.minPeriod + "ms");
    return paramBundle.create();
  }
  
  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    this.currentPeriodText = ((TextView)this.v.findViewById(2131230953));
    int i = paramInt;
    if (paramInt < this.minPeriod) {
      i = this.minPeriod;
    }
    paramInt = i;
    if (i % 10 != 0) {
      paramInt = i - i % 10;
    }
    this.currentPeriodText.setText(paramInt + "ms");
  }
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStopTrackingTouch(SeekBar paramSeekBar) {}
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\GenericServiceConfigurationDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */