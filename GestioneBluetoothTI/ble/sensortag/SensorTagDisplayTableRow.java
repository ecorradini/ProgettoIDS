package com.ti.ble.sensortag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class SensorTagDisplayTableRow
  extends TableRow
{
  Context context;
  ImageView displayAccessory;
  CheckBox displayClock;
  ImageView displayIcon;
  CheckBox displayInvert;
  TextView displaySubTitle;
  EditText displayText;
  TextView displayTitle;
  public Paint linePaint;
  
  SensorTagDisplayTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    paramContext = LayoutInflater.from(paramContext).inflate(2131361852, null, false);
    addView(paramContext);
    this.displayTitle = ((TextView)paramContext.findViewById(2131230909));
    this.displaySubTitle = ((TextView)paramContext.findViewById(2131230917));
    this.displayIcon = ((ImageView)paramContext.findViewById(2131230913));
    this.displayAccessory = ((ImageView)paramContext.findViewById(2131230911));
    this.displayText = ((EditText)paramContext.findViewById(2131230912));
    this.displayInvert = ((CheckBox)paramContext.findViewById(2131230914));
    this.displayClock = ((CheckBox)paramContext.findViewById(2131230910));
    this.displayTitle.setText("Display control");
    this.displaySubTitle.setText("");
    this.displayIcon.setImageResource(2131492933);
    this.displayAccessory.setVisibility(4);
    this.displayText.setMaxLines(1);
    this.displayText.setInputType(1);
    this.displayText.setImeOptions(6);
    this.displayText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16) });
    setBackgroundColor(0);
    this.linePaint = new Paint() {};
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagDisplayTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */