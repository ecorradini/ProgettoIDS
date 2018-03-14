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
import android.widget.ImageView;
import android.widget.TextView;
import com.ti.ti_oad.TIOADEoadDefinitions;
import com.ti.ti_oad.TIOADEoadHeader;
import com.ti.ti_oad.TIOADEoadImageReader;
import java.util.List;

public class FWUpdateEOADFWInfoDialogFragment
  extends DialogFragment
{
  static final String TAG = FWUpdateEOADFWInfoDialogFragment.class.getSimpleName();
  byte[] chipType;
  public List<FWUpdateTIFirmwareEntry> firmwares;
  public int fwPosition;
  
  public static FWUpdateEOADFWInfoDialogFragment newInstance(List<FWUpdateTIFirmwareEntry> paramList, byte[] paramArrayOfByte, int paramInt)
  {
    FWUpdateEOADFWInfoDialogFragment localFWUpdateEOADFWInfoDialogFragment = new FWUpdateEOADFWInfoDialogFragment();
    localFWUpdateEOADFWInfoDialogFragment.firmwares = paramList;
    localFWUpdateEOADFWInfoDialogFragment.chipType = paramArrayOfByte;
    localFWUpdateEOADFWInfoDialogFragment.fwPosition = paramInt;
    new Bundle();
    return localFWUpdateEOADFWInfoDialogFragment;
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity()).setTitle("").setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        FWUpdateEOADFWInfoDialogFragment.this.dismiss();
      }
    }).setPositiveButton("Download", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface = new Intent("FWUpdateEOADSelectorDialogFragment.SELECTED");
        paramAnonymousDialogInterface.putExtra("FWUpdateEOADSelectorDialogFragment.EXTRA_SELECTED_FW_INDEX", FWUpdateEOADFWInfoDialogFragment.this.fwPosition);
        FWUpdateEOADFWInfoDialogFragment.this.getActivity().sendBroadcast(paramAnonymousDialogInterface);
        FWUpdateEOADFWInfoDialogFragment.this.dismiss();
      }
    });
    View localView = LayoutInflater.from(getActivity()).inflate(2131361854, null);
    Log.d(TAG, "Loading file : " + ((FWUpdateTIFirmwareEntry)this.firmwares.get(this.fwPosition)).Filename);
    Object localObject1 = new TIOADEoadImageReader(((FWUpdateTIFirmwareEntry)this.firmwares.get(this.fwPosition)).Filename, getContext());
    Object localObject2 = (ImageView)localView.findViewById(2131230928);
    if ((((TIOADEoadImageReader)localObject1).imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_BLE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_BLE) {
      ((ImageView)localObject2).setImageResource(2131492886);
    }
    for (;;)
    {
      localObject2 = (FWUpdateTIFirmwareEntry)this.firmwares.get(this.fwPosition);
      ((TextView)localView.findViewById(2131230929)).setText(String.format("%s %s(%s)", new Object[] { ((FWUpdateTIFirmwareEntry)localObject2).BoardType, ((FWUpdateTIFirmwareEntry)localObject2).DevPack + " ", ((FWUpdateTIFirmwareEntry)localObject2).WirelessStandard }));
      ((TextView)localView.findViewById(2131230930)).setText(String.format("Version %1.2f", new Object[] { Float.valueOf(((FWUpdateTIFirmwareEntry)localObject2).Version) }));
      ((TextView)localView.findViewById(2131230934)).setText(TIOADEoadHeader.WirelessStdToString(((TIOADEoadImageReader)localObject1).imageHeader.TIOADEoadImageWirelessTechnology));
      ((TextView)localView.findViewById(2131230932)).setText(((FWUpdateTIFirmwareEntry)localObject2).MCU);
      ((TextView)localView.findViewById(2131230926)).setText(((FWUpdateTIFirmwareEntry)localObject2).BoardType);
      ((TextView)localView.findViewById(2131230931)).setText(((FWUpdateTIFirmwareEntry)localObject2).Type);
      ((TextView)localView.findViewById(2131230933)).setText(String.format("%.1fKb", new Object[] { Float.valueOf((float)((TIOADEoadImageReader)localObject1).imageHeader.TIOADEoadImageLength / 1024.0F) }));
      localObject1 = (TextView)localView.findViewById(2131230927);
      ((TextView)localObject1).setText(Html.fromHtml(new String(Base64.decode(((FWUpdateTIFirmwareEntry)localObject2).Description, 0))));
      ((TextView)localObject1).setMovementMethod(LinkMovementMethod.getInstance());
      paramBundle.setView(localView);
      return paramBundle.create();
      if ((((TIOADEoadImageReader)localObject1).imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_THREAD) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_THREAD) {
        ((ImageView)localObject2).setImageResource(2131492888);
      } else if ((((TIOADEoadImageReader)localObject1).imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_ZIGBEE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_ZIGBEE) {
        ((ImageView)localObject2).setImageResource(2131492890);
      } else if ((((TIOADEoadImageReader)localObject1).imageHeader.TIOADEoadImageWirelessTechnology & TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE) != TIOADEoadDefinitions.TI_OAD_EOAD_WIRELESS_STD_802_15_4_SUB_ONE) {
        ((ImageView)localObject2).setImageResource(2131492889);
      }
    }
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateEOADFWInfoDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */