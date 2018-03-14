package com.ti.ble.sensortag;

import java.util.List;

public enum BarometerCalibrationCoefficients
{
  INSTANCE;
  
  public volatile List<Integer> barometerCalibrationCoefficients;
  public volatile double heightCalibration;
  
  private BarometerCalibrationCoefficients() {}
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\BarometerCalibrationCoefficients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */