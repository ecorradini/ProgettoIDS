package com.ti.ble.ti.profiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

public class TILampControlProfileTableRow
  extends TableRow
{
  Context context;
  public Paint linePaint;
  public Button setColorButton;
  
  TILampControlProfileTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    paramContext = LayoutInflater.from(paramContext).inflate(2131361861, null, false);
    addView(paramContext);
    this.setColorButton = ((Button)paramContext.findViewById(2131230981));
    setBackgroundColor(0);
    this.linePaint = new Paint() {};
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TILampControlProfileTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */