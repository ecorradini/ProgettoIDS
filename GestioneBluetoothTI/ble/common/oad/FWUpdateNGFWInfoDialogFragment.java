package com.ti.ble.common.oad;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.util.List;

public class FWUpdateNGFWInfoDialogFragment
  extends DialogFragment
{
  public float cFW;
  public String cName;
  public List<FWUpdateTIFirmwareEntry> firmwares;
  public int fwPosition;
  
  public static FWUpdateNGFWInfoDialogFragment newInstance(List<FWUpdateTIFirmwareEntry> paramList, float paramFloat, String paramString, int paramInt)
  {
    FWUpdateNGFWInfoDialogFragment localFWUpdateNGFWInfoDialogFragment = new FWUpdateNGFWInfoDialogFragment();
    localFWUpdateNGFWInfoDialogFragment.firmwares = paramList;
    localFWUpdateNGFWInfoDialogFragment.cFW = paramFloat;
    localFWUpdateNGFWInfoDialogFragment.cName = paramString;
    localFWUpdateNGFWInfoDialogFragment.fwPosition = paramInt;
    new Bundle();
    Log.d("FWUpdateNGSelectorDialogFragment", "Current firmware version : " + paramFloat);
    return localFWUpdateNGFWInfoDialogFragment;
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity()).setTitle("").setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        FWUpdateNGFWInfoDialogFragment.this.dismiss();
      }
    }).setPositiveButton("Download", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface = new Intent("FWUpdateNGSelectorDialogFragment.SELECTED");
        paramAnonymousDialogInterface.putExtra("FWUpdateNGSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX", FWUpdateNGFWInfoDialogFragment.this.fwPosition);
        FWUpdateNGFWInfoDialogFragment.this.getActivity().sendBroadcast(paramAnonymousDialogInterface);
        FWUpdateNGFWInfoDialogFragment.this.dismiss();
      }
    });
    View localView = LayoutInflater.from(getActivity()).inflate(2131361854, null);
    FWUpdateTIFirmwareEntry localFWUpdateTIFirmwareEntry = (FWUpdateTIFirmwareEntry)this.firmwares.get(this.fwPosition);
    ((TextView)localView.findViewById(2131230929)).setText(String.format("%s %s(%s)", new Object[] { localFWUpdateTIFirmwareEntry.BoardType, localFWUpdateTIFirmwareEntry.DevPack + " ", localFWUpdateTIFirmwareEntry.WirelessStandard }));
    ((TextView)localView.findViewById(2131230930)).setText(String.format("Version %1.2f", new Object[] { Float.valueOf(localFWUpdateTIFirmwareEntry.Version) }));
    TextView localTextView = (TextView)localView.findViewById(2131230934);
    if (localFWUpdateTIFirmwareEntry.WirelessStandard.equals("BLE")) {
      localTextView.setText("Bluetooth Smart");
    }
    for (;;)
    {
      ((TextView)localView.findViewById(2131230932)).setText(localFWUpdateTIFirmwareEntry.MCU);
      ((TextView)localView.findViewById(2131230926)).setText(localFWUpdateTIFirmwareEntry.BoardType);
      ((TextView)localView.findViewById(2131230931)).setText(localFWUpdateTIFirmwareEntry.Type);
      localTextView = (TextView)localView.findViewById(2131230933);
      localTextView = (TextView)localView.findViewById(2131230927);
      localTextView.setText(Html.fromHtml(new String(Base64.decode(localFWUpdateTIFirmwareEntry.Description, 0))));
      localTextView.setMovementMethod(LinkMovementMethod.getInstance());
      paramBundle.setView(localView);
      return paramBundle.create();
      if (localFWUpdateTIFirmwareEntry.WirelessStandard.equals("RF4CE")) {
        localTextView.setText("RF4CE");
      } else if (localFWUpdateTIFirmwareEntry.WirelessStandard.equals("Sub-One")) {
        localTextView.setText("Sub-1GHz");
      } else if (localFWUpdateTIFirmwareEntry.WirelessStandard.equals("ZigBee")) {
        localTextView.setText("ZigBee");
      } else {
        localTextView.setText("Unknown");
      }
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateNGFWInfoDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */