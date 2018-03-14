package com.ti.ble.sensortag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import com.ti.ble.common.GenericServiceConfigurationDialogFragment;
import com.ti.util.GenericCharacteristicTableRow;

public class SensorTagBarometerTableRow
  extends GenericCharacteristicTableRow
{
  public SensorTagBarometerTableRow(Context paramContext)
  {
    super(paramContext, 1000, true);
    getChildAt(0).setOnClickListener(this);
  }
  
  public void calibrationButtonTouched()
  {
    Intent localIntent = new Intent("com.ti.util.ACTION_CALIBRATE");
    localIntent.putExtra("com.ti.util.EXTRA_SERVICE_UUID", this.uuidLabel.getText());
    this.context.sendBroadcast(localIntent);
  }
  
  public void grayedOut(boolean paramBoolean)
  {
    super.grayedOut(paramBoolean);
    if (paramBoolean) {}
  }
  
  public void onAnimationEnd(Animation paramAnimation)
  {
    if (this.config == true) {}
  }
  
  public void onAnimationRepeat(Animation paramAnimation) {}
  
  public void onAnimationStart(Animation paramAnimation) {}
  
  public void onClick(View paramView)
  {
    Log.d("onClick", "Row ID " + this.title.getText());
    GenericServiceConfigurationDialogFragment.newInstance(this.uuidLabel.getText().toString(), this.period, this.servOn, this.periodMinVal).show(((SensorTagApplicationClass)this.context.getApplicationContext()).currentActivity.getFragmentManager(), "ServiceConfig");
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagBarometerTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */