package com.ti.ble.sensortag;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.common.GattInfo;
import com.ti.ble.common.GenericServiceConfigurationDialogFragment;
import com.ti.util.TrippleSparkLineView;
import java.util.UUID;

public class SensorTagMovementTableRow
  extends TableRow
  implements View.OnClickListener, Animation.AnimationListener
{
  public Switch WOS;
  public final TextView accValue;
  public boolean config;
  public Context context;
  public final TextView gyroValue;
  public final ImageView icon;
  public Paint linePaint;
  public final TextView magValue;
  public int period;
  public boolean servOn;
  public final TrippleSparkLineView sl1;
  public final TrippleSparkLineView sl2;
  public final TrippleSparkLineView sl3;
  public TableRow tRow;
  public final TextView title;
  public TextView uuidLabel;
  
  public SensorTagMovementTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    addView(LayoutInflater.from(paramContext).inflate(2131361877, null, false));
    this.icon = ((ImageView)findViewById(2131231056));
    this.title = ((TextView)findViewById(2131231054));
    this.accValue = ((TextView)findViewById(2131231060));
    this.gyroValue = ((TextView)findViewById(2131231061));
    this.magValue = ((TextView)findViewById(2131231062));
    this.sl1 = ((TrippleSparkLineView)findViewById(2131231057));
    this.sl2 = ((TrippleSparkLineView)findViewById(2131231058));
    this.sl3 = ((TrippleSparkLineView)findViewById(2131231059));
    this.WOS = ((Switch)findViewById(2131231063));
    this.WOS.setVisibility(4);
    this.uuidLabel = new TextView(this.context);
    setBackgroundColor(0);
    this.linePaint = new Paint() {};
    getChildAt(0).setOnClickListener(this);
    this.period = 1000;
  }
  
  public void onAnimationEnd(Animation paramAnimation) {}
  
  public void onAnimationRepeat(Animation paramAnimation) {}
  
  public void onAnimationStart(Animation paramAnimation) {}
  
  public void onClick(View paramView)
  {
    Log.d("onClick", "Row ID " + this.title.getText());
    GenericServiceConfigurationDialogFragment.newInstance(this.uuidLabel.getText().toString(), this.period, this.servOn, 100).show(((SensorTagApplicationClass)this.context.getApplicationContext()).currentActivity.getFragmentManager(), "ServiceConfig");
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
  
  public void setIcon(String paramString1, String paramString2)
  {
    Log.d("GenericCharacteristicTableRow", "Getting MipMap for :" + paramString1 + GattInfo.uuidToIcon(UUID.fromString(paramString2)));
    int i = getResources().getIdentifier(paramString1 + GattInfo.uuidToIcon(UUID.fromString(paramString2)), "mipmap", this.context.getPackageName());
    if (i != -1)
    {
      this.icon.setImageResource(i);
      return;
    }
    Log.d("GenericCharacteristicTableRow", "Icon for : " + paramString1 + GattInfo.uuidToIcon(UUID.fromString(paramString2)) + " Not found !");
  }
  
  public void setIcon(String paramString1, String paramString2, String paramString3)
  {
    Log.d("GenericCharacteristicTableRow", "Getting MipMap for :" + paramString1 + paramString3);
    int i = getResources().getIdentifier(paramString1 + paramString3, "mipmap", this.context.getPackageName());
    if (i != -1)
    {
      this.icon.setImageResource(i);
      return;
    }
    Log.d("GenericCharacteristicTableRow", "Icon for : " + paramString1 + paramString3 + " Not found !");
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagMovementTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */