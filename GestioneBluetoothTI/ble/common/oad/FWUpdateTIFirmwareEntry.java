package com.ti.ble.common.oad;

public class FWUpdateTIFirmwareEntry
{
  public final String BoardType;
  public final boolean Custom;
  public final String Description;
  public final String DevPack;
  public final String Filename;
  public final String MCU;
  public final int OADAlgo;
  public final float RequiredVersionRev;
  public final boolean SafeMode;
  public final String Type;
  public final float Version;
  public final String WirelessStandard;
  public boolean compatible;
  
  public FWUpdateTIFirmwareEntry(String paramString1, boolean paramBoolean1, String paramString2, String paramString3, int paramInt, String paramString4, float paramFloat1, boolean paramBoolean2, float paramFloat2, String paramString5, String paramString6, String paramString7)
  {
    this.Filename = paramString1;
    this.Custom = paramBoolean1;
    this.WirelessStandard = paramString2;
    this.Type = paramString3;
    this.OADAlgo = paramInt;
    this.BoardType = paramString4;
    this.RequiredVersionRev = paramFloat1;
    this.SafeMode = paramBoolean2;
    this.Version = paramFloat2;
    this.compatible = true;
    this.DevPack = paramString5;
    this.Description = paramString6;
    this.MCU = paramString7;
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateTIFirmwareEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */