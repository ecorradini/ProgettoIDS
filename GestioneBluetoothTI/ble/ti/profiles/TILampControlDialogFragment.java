package com.ti.ble.ti.profiles;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TILampControlDialogFragment
  extends DialogFragment
{
  public static final String ACTION_LAMP_HSI_COLOR_CHANGED = "org.example.ti.ble.ti.profiles.ACTION_LAMP_HSI_COLOR_CHANGED";
  public static final String EXTRA_LAMP_HSI_COLOR_H = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_H";
  public static final String EXTRA_LAMP_HSI_COLOR_I = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_I";
  public static final String EXTRA_LAMP_HSI_COLOR_S = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_S";
  double H;
  double I;
  double S;
  float downx = 0.0F;
  float downy = 0.0F;
  SeekBar intensityBar;
  float upx = 0.0F;
  float upy = 0.0F;
  View v;
  
  public static TILampControlDialogFragment newInstance()
  {
    TILampControlDialogFragment localTILampControlDialogFragment = new TILampControlDialogFragment();
    new Bundle();
    return localTILampControlDialogFragment;
  }
  
  void broadCastLightValue()
  {
    Intent localIntent = new Intent("org.example.ti.ble.ti.profiles.ACTION_LAMP_HSI_COLOR_CHANGED");
    localIntent.putExtra("org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_H", this.H);
    localIntent.putExtra("org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_I", this.I);
    localIntent.putExtra("org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_S", this.S);
    getActivity().sendBroadcast(localIntent);
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity()).setTitle("Set color").setPositiveButton("Close", null);
    this.v = getActivity().getLayoutInflater().inflate(2131361860, null);
    ImageView localImageView = (ImageView)this.v.findViewById(2131230887);
    this.intensityBar = ((SeekBar)this.v.findViewById(2131230886));
    this.intensityBar.setMax(1000);
    if ((localImageView != null) && (this.intensityBar != null))
    {
      this.intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
      {
        public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
          TILampControlDialogFragment.this.I = (paramAnonymousInt / 1000.0D);
          TILampControlDialogFragment.this.broadCastLightValue();
        }
        
        public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
        
        public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      });
      localImageView.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          switch (paramAnonymousMotionEvent.getAction())
          {
          }
          for (;;)
          {
            double d1 = TILampControlDialogFragment.this.upx - paramAnonymousView.getWidth() / 2;
            double d2 = TILampControlDialogFragment.this.upy - paramAnonymousView.getHeight() / 2;
            TILampControlDialogFragment.this.S = (Math.sqrt(Math.pow(d1, 2.0D) + Math.pow(d2, 2.0D)) / (paramAnonymousView.getWidth() / 2));
            TILampControlDialogFragment.this.H = (Math.atan2(d2, d1) * 180.0D / 3.141592653589793D - 180.0D);
            TILampControlDialogFragment.this.I = (TILampControlDialogFragment.this.intensityBar.getProgress() / 1000.0D);
            paramAnonymousView = TILampControlDialogFragment.this;
            paramAnonymousView.H += 180.0D;
            if (TILampControlDialogFragment.this.H > 360.0D)
            {
              paramAnonymousView = TILampControlDialogFragment.this;
              paramAnonymousView.H -= 360.0D;
            }
            if (TILampControlDialogFragment.this.H < 0.0D)
            {
              paramAnonymousView = TILampControlDialogFragment.this;
              paramAnonymousView.H += 360.0D;
            }
            if (TILampControlDialogFragment.this.S > 1.0D) {
              TILampControlDialogFragment.this.S = 1.0D;
            }
            if (TILampControlDialogFragment.this.S < 0.0D) {
              TILampControlDialogFragment.this.S = 0.0D;
            }
            Log.d("TILampControlDialogFragment", "S: " + TILampControlDialogFragment.this.S + " H:" + TILampControlDialogFragment.this.H + " I:" + TILampControlDialogFragment.this.I);
            TILampControlDialogFragment.this.broadCastLightValue();
            return true;
            TILampControlDialogFragment.this.downx = paramAnonymousMotionEvent.getX();
            TILampControlDialogFragment.this.downy = paramAnonymousMotionEvent.getY();
            continue;
            TILampControlDialogFragment.this.upx = paramAnonymousMotionEvent.getX();
            TILampControlDialogFragment.this.upy = paramAnonymousMotionEvent.getY();
            TILampControlDialogFragment.this.downx = TILampControlDialogFragment.this.upx;
            TILampControlDialogFragment.this.downy = TILampControlDialogFragment.this.upy;
            continue;
            TILampControlDialogFragment.this.upx = paramAnonymousMotionEvent.getX();
            TILampControlDialogFragment.this.upy = paramAnonymousMotionEvent.getY();
          }
        }
      });
    }
    paramBundle.setView(this.v);
    return paramBundle.create();
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\ti\profiles\TILampControlDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */