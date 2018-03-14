package com.ti.ble.common.oad;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import com.ti.ti_oad.TIOADEoadDefinitions;
import java.util.List;

public class FWUpdateEOADSelectorDialogFragment
  extends DialogFragment
{
  public static final String ACTION_FW_WAS_SELECTED = "FWUpdateEOADSelectorDialogFragment.SELECTED";
  public static final String EXTRA_SELECTED_FW_INDEX = "FWUpdateEOADSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX";
  static final String TAG = FWUpdateEOADSelectorDialogFragment.class.getSimpleName();
  byte[] chipID;
  List<FWUpdateTIFirmwareEntry> firmwares;
  BroadcastReceiver recv;
  TableLayout table;
  
  public static FWUpdateEOADSelectorDialogFragment newInstance(List<FWUpdateTIFirmwareEntry> paramList, byte[] paramArrayOfByte)
  {
    FWUpdateEOADSelectorDialogFragment localFWUpdateEOADSelectorDialogFragment = new FWUpdateEOADSelectorDialogFragment();
    localFWUpdateEOADSelectorDialogFragment.firmwares = paramList;
    localFWUpdateEOADSelectorDialogFragment.chipID = paramArrayOfByte;
    new Bundle();
    return localFWUpdateEOADSelectorDialogFragment;
  }
  
  public void dismiss()
  {
    if ((this.recv != null) && (getActivity() != null)) {
      getActivity().unregisterReceiver(this.recv);
    }
    super.dismiss();
  }
  
  public void onAttach(Context paramContext)
  {
    super.onAttach(paramContext);
    this.recv = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (paramAnonymousIntent.getAction().equals("FWUpdateEOADSelectorDialogFragment.SELECTED")) {
          FWUpdateEOADSelectorDialogFragment.this.dismiss();
        }
      }
    };
    getActivity().registerReceiver(this.recv, new IntentFilter("FWUpdateEOADSelectorDialogFragment.SELECTED"));
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity()).setTitle("Select FW").setNegativeButton("Cancel", null);
    View localView = LayoutInflater.from(getActivity()).inflate(2131361856, null);
    this.table = ((TableLayout)localView.findViewById(2131230944));
    this.table.removeAllViews();
    Object localObject1 = new Rect();
    getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame((Rect)localObject1);
    this.table.setMinimumWidth(((Rect)localObject1).width());
    if (this.firmwares != null)
    {
      int i = 0;
      if (i < this.firmwares.size())
      {
        localObject1 = (FWUpdateTIFirmwareEntry)this.firmwares.get(i);
        if (!((FWUpdateTIFirmwareEntry)localObject1).MCU.equalsIgnoreCase(TIOADEoadDefinitions.oadChipTypePrettyPrint(this.chipID))) {
          ((FWUpdateTIFirmwareEntry)localObject1).compatible = false;
        }
        final FWUpdateBINFileEntryTableRow localFWUpdateBINFileEntryTableRow = new FWUpdateBINFileEntryTableRow(getActivity(), (FWUpdateTIFirmwareEntry)localObject1);
        Object localObject2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { -1, -3355444 });
        ((GradientDrawable)localObject2).setGradientType(0);
        StateListDrawable localStateListDrawable = new StateListDrawable();
        localStateListDrawable.addState(new int[] { 16842919, -16842913 }, (Drawable)localObject2);
        localFWUpdateBINFileEntryTableRow.setBackground(localStateListDrawable);
        localFWUpdateBINFileEntryTableRow.position = i;
        localFWUpdateBINFileEntryTableRow.getChildAt(0).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            FWUpdateEOADFWInfoDialogFragment.newInstance(FWUpdateEOADSelectorDialogFragment.this.firmwares, FWUpdateEOADSelectorDialogFragment.this.chipID, localFWUpdateBINFileEntryTableRow.position).show(FWUpdateEOADSelectorDialogFragment.this.getActivity().getFragmentManager(), "FWUpdateEOADFWInfoDialogFragment");
          }
        });
        localObject2 = (ImageView)localFWUpdateBINFileEntryTableRow.getChildAt(0).findViewById(2131230936);
        ((ImageView)localObject2).setClickable(true);
        ((ImageView)localObject2).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            paramAnonymousView = new Intent("FWUpdateEOADSelectorDialogFragment.SELECTED");
            paramAnonymousView.putExtra("FWUpdateEOADSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX", localFWUpdateBINFileEntryTableRow.position);
            FWUpdateEOADSelectorDialogFragment.this.getActivity().sendBroadcast(paramAnonymousView);
          }
        });
        if (((FWUpdateTIFirmwareEntry)localObject1).compatible) {
          localFWUpdateBINFileEntryTableRow.setGrayedOut(Boolean.valueOf(false));
        }
        for (;;)
        {
          this.table.addView(localFWUpdateBINFileEntryTableRow);
          this.table.requestLayout();
          i += 1;
          break;
          localFWUpdateBINFileEntryTableRow.setGrayedOut(Boolean.valueOf(true));
        }
      }
    }
    paramBundle.setView(localView);
    return paramBundle.create();
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateEOADSelectorDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */