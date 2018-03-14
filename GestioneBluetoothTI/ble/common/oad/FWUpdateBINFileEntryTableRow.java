package com.ti.ble.common.oad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class FWUpdateBINFileEntryTableRow
  extends TableRow
{
  Context context;
  ImageView download;
  FWUpdateTIFirmwareEntry ent;
  private final Paint linePaint;
  public int position;
  TextView subTitleView;
  TextView tV;
  
  public FWUpdateBINFileEntryTableRow(Context paramContext, FWUpdateTIFirmwareEntry paramFWUpdateTIFirmwareEntry)
  {
    super(paramContext);
    this.context = paramContext;
    addView(LayoutInflater.from(paramContext).inflate(2131361855, null, false));
    this.linePaint = new Paint() {};
    this.tV = ((TextView)findViewById(2131230935));
    if (this.tV != null) {
      this.tV.setText(String.format("%s %1.2f %s(%s)", new Object[] { paramFWUpdateTIFirmwareEntry.BoardType, Float.valueOf(paramFWUpdateTIFirmwareEntry.Version), paramFWUpdateTIFirmwareEntry.DevPack + " ", paramFWUpdateTIFirmwareEntry.WirelessStandard }));
    }
    this.subTitleView = ((TextView)findViewById(2131230937));
    if (this.subTitleView != null)
    {
      if (paramFWUpdateTIFirmwareEntry.compatible) {
        this.subTitleView.setText(String.format("%s", new Object[] { "Compatible" }));
      }
    }
    else {
      return;
    }
    this.subTitleView.setText("Not compatible");
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawLine(0.0F, paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), paramCanvas.getWidth(), paramCanvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
  }
  
  public void setGrayedOut(Boolean paramBoolean)
  {
    if (paramBoolean.booleanValue() == true)
    {
      this.tV.setTextColor(-3355444);
      this.subTitleView.setTextColor(-3355444);
      return;
    }
    this.tV.setTextColor(-16777216);
    this.subTitleView.setTextColor(-16777216);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateBINFileEntryTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */