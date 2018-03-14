package com.ti.ble.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.UUID;

public class IBMIoTCloudTableRow
  extends TableRow
{
  ImageView cloudConnectionStatus;
  TextView cloudURL;
  Button configureCloud;
  Context context;
  ImageView icon;
  Paint linePaint;
  Switch pushToCloud;
  TextView pushToCloudCaption;
  TextView title;
  TextView value;
  
  public IBMIoTCloudTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    addView(LayoutInflater.from(paramContext).inflate(2131361838, null, false));
    this.linePaint = new Paint() {};
    setBackgroundColor(0);
    this.icon = ((ImageView)findViewById(2131230871));
    this.title = ((TextView)findViewById(2131230868));
    this.value = ((TextView)findViewById(2131230873));
    this.configureCloud = ((Button)findViewById(2131230867));
    this.cloudConnectionStatus = ((ImageView)findViewById(2131230869));
    this.cloudURL = ((TextView)findViewById(2131230870));
    this.pushToCloud = ((Switch)findViewById(2131230872));
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
  
  public void setCloudConnectionStatusImage(int paramInt)
  {
    this.cloudConnectionStatus.setImageResource(paramInt);
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


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\IBMIoTCloudTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */