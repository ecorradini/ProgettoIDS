package com.ti.ble.ti.profiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TIConnectionControlServiceTableRow
  extends TableRow
{
  Context context;
  public Paint linePaint;
  
  TIConnectionControlServiceTableRow(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    addView(LayoutInflater.from(paramContext).inflate(2131361839, null, false));
    setBackgroundColor(0);
    this.linePaint = new Paint() {};
  }
  
  public void invalidateConnectionParameters()
  {
    TextView localTextView1 = (TextView)findViewById(2131230836);
    TextView localTextView2 = (TextView)findViewById(2131230837);
    TextView localTextView3 = (TextView)findViewById(2131230838);
    TextView localTextView4 = (TextView)findViewById(2131230835);
    localTextView1.setTextColor(-65536);
    localTextView2.setTextColor(-65536);
    localTextView3.setTextColor(-65536);
    localTextView4.setTextColor(-65536);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
  
  public void setConIntText(int paramInt)
  {
    ((TextView)findViewById(2131230836)).setText(paramInt * 1.25F + " ms");
  }
  
  public void setConnectionParameters(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject = (TextView)findViewById(2131230836);
    TextView localTextView2 = (TextView)findViewById(2131230837);
    TextView localTextView3 = (TextView)findViewById(2131230838);
    TextView localTextView1 = (TextView)findViewById(2131230835);
    ToggleButton localToggleButton1 = (ToggleButton)findViewById(2131230840);
    ToggleButton localToggleButton2 = (ToggleButton)findViewById(2131230833);
    ToggleButton localToggleButton3 = (ToggleButton)findViewById(2131230842);
    SeekBar localSeekBar = (SeekBar)findViewById(2131230844);
    ((TextView)localObject).setTextColor(-16777216);
    localTextView2.setTextColor(-16777216);
    localTextView3.setTextColor(-16777216);
    localTextView1.setTextColor(-16777216);
    localSeekBar.setProgress((int)(paramInt1 * 1.25F - 8.0F));
    ((TextView)localObject).setText(paramInt1 * 1.25F + " ms");
    localTextView2.setText(paramInt2 + " ");
    localTextView3.setText(paramInt3 * 1.25F + " ms");
    if (paramInt4 == 0) {
      localObject = "Balanced";
    }
    for (;;)
    {
      localTextView1.setText((CharSequence)localObject);
      if (paramInt4 != 1) {
        break;
      }
      localToggleButton1.setChecked(true);
      localToggleButton2.setChecked(false);
      localToggleButton3.setChecked(false);
      return;
      if (paramInt4 == 1) {
        localObject = "High";
      } else if (paramInt4 == 2) {
        localObject = "Low Power";
      } else {
        localObject = "Unknown";
      }
    }
    if (paramInt4 == 2)
    {
      localToggleButton1.setChecked(false);
      localToggleButton2.setChecked(false);
      localToggleButton3.setChecked(true);
      return;
    }
    localToggleButton1.setChecked(false);
    localToggleButton2.setChecked(true);
    localToggleButton3.setChecked(false);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TIConnectionControlServiceTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */