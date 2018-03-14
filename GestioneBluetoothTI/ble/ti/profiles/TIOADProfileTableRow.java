package com.ti.ble.ti.profiles;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.ti.util.GenericCharacteristicTableRow;

public class TIOADProfileTableRow
  extends GenericCharacteristicTableRow
{
  public static final String ACTION_VIEW_CLICKED = "TIOADProfileTableRow.ACTION_VIEW_CLICKED";
  
  public TIOADProfileTableRow(Context paramContext)
  {
    super(paramContext, 1000, true);
  }
  
  public void onClick(View paramView)
  {
    paramView = new Intent("TIOADProfileTableRow.ACTION_VIEW_CLICKED");
    this.context.sendBroadcast(paramView);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TIOADProfileTableRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */