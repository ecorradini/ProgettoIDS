package com.ti.ble.ti.profiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.TableRow;

public class ThroughputTestServiceTableRow
  extends TableRow
{
  Context context;
  public Paint linePaint;
  
  ThroughputTestServiceTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    addView(LayoutInflater.from(paramContext).inflate(2131361883, null, false));
    setBackgroundColor(0);
    this.linePaint = new Paint() {};
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\ThroughputTestServiceTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */