package com.ti.ble.ti.profiles;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.ti.ble.BluetoothLEController.BluetoothLEDevice;
import com.ti.ble.audio.AudioActivity;
import com.ti.ble.common.GenericBluetoothProfile;
import com.ti.util.GenericCharacteristicTableRow;
import com.ti.util.TrippleSparkLineView;
import java.util.UUID;

public class TIAudioProfile
  extends GenericBluetoothProfile
  implements View.OnClickListener
{
  public final Context context;
  
  public TIAudioProfile(Context paramContext, BluetoothDevice paramBluetoothDevice, BluetoothGattService paramBluetoothGattService)
  {
    super(paramContext, paramBluetoothDevice, paramBluetoothGattService);
    this.tRow = new GenericCharacteristicTableRow(paramContext, 1000, true);
    this.tRow.sl1.setVisibility(4);
    if (this.mBTDevice.getName().equals("SensorTag")) {
      this.tRow.icon.setImageResource(2131492928);
    }
    for (;;)
    {
      this.tRow.title.setText("Audio Service");
      this.tRow.value.setText("");
      this.tRow.getChildAt(0).setOnClickListener(this);
      this.context = paramContext;
      return;
      this.tRow.icon.setImageResource(2131492873);
    }
  }
  
  public static boolean isCorrectService(BluetoothGattService paramBluetoothGattService)
  {
    return paramBluetoothGattService.getUuid().toString().compareTo("f000b000-0451-4000-b000-000000000000") == 0;
  }
  
  public void configureService() {}
  
  public void deConfigureService() {}
  
  public void disableService() {}
  
  public void enableService() {}
  
  public void onClick(View paramView)
  {
    Log.d("TIAudioProfile", "Cell was clicked !");
    this.dev.disconnectDevice();
    paramView = new Intent(this.context, AudioActivity.class);
    paramView.putExtra("AudioActivity.BTADDRESS_EXTRA", this.mBTDevice.getAddress());
    this.context.startActivity(paramView);
  }
  
  public void onPause()
  {
    super.onPause();
  }
  
  public void onResume()
  {
    super.onResume();
  }
  
  public void periodWasUpdated(int paramInt) {}
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TIAudioProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */