package com.ti.ble.sensortag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import com.ti.util.TrippleSparkLineView;
import java.util.Timer;
import java.util.TimerTask;

public class SensorTagSimpleKeysTableRow
  extends TableRow
{
  protected TrippleSparkLineView buttonSL;
  public byte lastKeys;
  protected ImageView leftKeyPressStateImage;
  public Paint linePaint;
  protected ImageView reedStateImage;
  protected ImageView rightKeyPressStateImage;
  protected updateSparkLinesTimerTask sparkLineUpdateTask;
  protected Timer sparkLineUpdateTimer;
  
  public SensorTagSimpleKeysTableRow(Context paramContext)
  {
    super(paramContext);
    addView(LayoutInflater.from(paramContext).inflate(2131361878, null, false));
    this.linePaint = new Paint() {};
    setBackgroundColor(0);
    this.leftKeyPressStateImage = ((ImageView)findViewById(2131231047));
    this.rightKeyPressStateImage = ((ImageView)findViewById(2131231049));
    this.reedStateImage = ((ImageView)findViewById(2131231048));
    this.buttonSL = ((TrippleSparkLineView)findViewById(2131231050));
    this.sparkLineUpdateTimer = new Timer();
    this.sparkLineUpdateTask = new updateSparkLinesTimerTask(this);
    this.sparkLineUpdateTimer.scheduleAtFixedRate(this.sparkLineUpdateTask, 1000L, 100L);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
  
  class updateSparkLinesTimerTask
    extends TimerTask
  {
    SensorTagSimpleKeysTableRow param;
    
    public updateSparkLinesTimerTask(SensorTagSimpleKeysTableRow paramSensorTagSimpleKeysTableRow)
    {
      this.param = paramSensorTagSimpleKeysTableRow;
    }
    
    public void run()
    {
      this.param.post(new Runnable()
      {
        public void run()
        {
          if ((SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.lastKeys & 0x1) == 1)
          {
            SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.buttonSL.addValue(1.0F, 0);
            if ((SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.lastKeys & 0x2) != 2) {
              break label112;
            }
            SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.buttonSL.addValue(1.0F, 1);
          }
          for (;;)
          {
            if ((SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.lastKeys & 0x4) != 4) {
              break label130;
            }
            SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.buttonSL.addValue(1.0F, 2);
            return;
            SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.buttonSL.addValue(0.0F, 0);
            break;
            label112:
            SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.buttonSL.addValue(0.0F, 1);
          }
          label130:
          SensorTagSimpleKeysTableRow.updateSparkLinesTimerTask.this.param.buttonSL.addValue(0.0F, 2);
        }
      });
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagSimpleKeysTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */