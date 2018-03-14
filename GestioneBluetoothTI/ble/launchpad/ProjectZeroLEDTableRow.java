package com.ti.ble.launchpad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;

public class ProjectZeroLEDTableRow
  extends TableRow
{
  Context con;
  public ImageView greenLed;
  Paint linePaint;
  public ImageView redLed;
  
  ProjectZeroLEDTableRow(Context paramContext)
  {
    super(paramContext);
    paramContext = LayoutInflater.from(paramContext).inflate(2131361872, null, false);
    this.redLed = ((ImageView)paramContext.findViewById(2131231011));
    this.greenLed = ((ImageView)paramContext.findViewById(2131231010));
    addView(paramContext);
    setBackgroundColor(0);
    this.linePaint = new Paint() {};
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\launchpad\ProjectZeroLEDTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */