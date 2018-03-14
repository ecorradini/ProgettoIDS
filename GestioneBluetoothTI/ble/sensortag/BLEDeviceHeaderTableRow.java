package com.ti.ble.sensortag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.TableRow;

public class BLEDeviceHeaderTableRow
  extends TableRow
{
  public Paint linePaint;
  
  public BLEDeviceHeaderTableRow(Context paramContext)
  {
    super(paramContext);
    addView(LayoutInflater.from(paramContext).inflate(2131361835, null, false));
    this.linePaint = new Paint() {};
    setBackgroundColor(0);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\BLEDeviceHeaderTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */