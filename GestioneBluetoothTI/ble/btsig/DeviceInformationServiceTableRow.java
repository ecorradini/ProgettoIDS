package com.ti.ble.btsig;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.ti.ble.common.GattInfo;
import java.util.UUID;

public class DeviceInformationServiceTableRow
  extends TableRow
{
  public final TextView FirmwareREVLabel;
  public final TextView HardwareREVLabel;
  public final TextView ManifacturerNAMELabel;
  public final TextView ModelNRLabel;
  public final TextView SerialNRLabel;
  public final TextView SoftwareREVLabel;
  public final TextView SystemIDLabel;
  Context context;
  public final ImageView icon;
  public final Paint linePaint;
  public final TextView title;
  
  public DeviceInformationServiceTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    addView(LayoutInflater.from(paramContext).inflate(2131361836, null, false));
    this.linePaint = new Paint() {};
    setBackgroundColor(0);
    this.icon = ((ImageView)findViewById(2131230823));
    this.title = ((TextView)findViewById(2131230830));
    this.SystemIDLabel = ((TextView)findViewById(2131230829));
    this.ModelNRLabel = ((TextView)findViewById(2131230825));
    this.SerialNRLabel = ((TextView)findViewById(2131230827));
    this.FirmwareREVLabel = ((TextView)findViewById(2131230821));
    this.HardwareREVLabel = ((TextView)findViewById(2131230822));
    this.SoftwareREVLabel = ((TextView)findViewById(2131230828));
    this.ManifacturerNAMELabel = ((TextView)findViewById(2131230824));
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
  
  public void setIcon(String paramString1, String paramString2)
  {
    Log.d("DeviceInformationServiceTableRow", "Getting MipMap for :" + paramString1 + GattInfo.uuidToIcon(UUID.fromString(paramString2)));
    int i = getResources().getIdentifier(paramString1 + GattInfo.uuidToIcon(UUID.fromString(paramString2)), "mipmap", this.context.getPackageName());
    if (i != -1)
    {
      this.icon.setImageResource(i);
      return;
    }
    Log.d("DeviceInformationServiceTableRow", "Icon for : " + paramString1 + GattInfo.uuidToIcon(UUID.fromString(paramString2)) + " Not found !");
  }
  
  public void setIcon(String paramString1, String paramString2, String paramString3)
  {
    Log.d("DeviceInformationServiceTableRow", "Getting MipMap for :" + paramString1 + paramString3);
    int i = getResources().getIdentifier(paramString1 + paramString3, "mipmap", this.context.getPackageName());
    if (i == 0) {
      this.icon.setImageResource(2131492868);
    }
    if (i != -1)
    {
      this.icon.setImageResource(i);
      return;
    }
    Log.d("DeviceInformationServiceTableRow", "Icon for : " + paramString1 + paramString3 + " Not found !");
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\btsig\DeviceInformationServiceTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */