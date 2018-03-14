package com.ti.ble.sensortag;

import java.util.UUID;

public class SensorTagGatt
{
  public static final UUID UUID_ACC_CONF;
  public static final UUID UUID_ACC_DATA;
  public static final UUID UUID_ACC_PERI;
  public static final UUID UUID_ACC_SERV;
  public static final UUID UUID_AUDIO_AUDIO = UUID.fromString("f000b002-0451-4000-b000-000000000000");
  public static final UUID UUID_AUDIO_SERV;
  public static final UUID UUID_AUDIO_STARTSTOP;
  public static final UUID UUID_BAR_CALI;
  public static final UUID UUID_BAR_CONF;
  public static final UUID UUID_BAR_DATA;
  public static final UUID UUID_BAR_PERI;
  public static final UUID UUID_BAR_SERV;
  public static final UUID UUID_DEVINFO_FWREV;
  public static final UUID UUID_DEVINFO_SERV = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
  public static final UUID UUID_GYR_CONF;
  public static final UUID UUID_GYR_DATA;
  public static final UUID UUID_GYR_PERI;
  public static final UUID UUID_GYR_SERV;
  public static final UUID UUID_HUM_CONF;
  public static final UUID UUID_HUM_DATA;
  public static final UUID UUID_HUM_PERI;
  public static final UUID UUID_HUM_SERV;
  public static final UUID UUID_IRT_CONF;
  public static final UUID UUID_IRT_DATA;
  public static final UUID UUID_IRT_PERI;
  public static final UUID UUID_IRT_SERV;
  public static final UUID UUID_KEY_DATA;
  public static final UUID UUID_KEY_SERV;
  public static final UUID UUID_MAG_CONF;
  public static final UUID UUID_MAG_DATA;
  public static final UUID UUID_MAG_PERI;
  public static final UUID UUID_MAG_SERV;
  public static final UUID UUID_MOV_CONF;
  public static final UUID UUID_MOV_DATA;
  public static final UUID UUID_MOV_PERI;
  public static final UUID UUID_MOV_SERV;
  public static final UUID UUID_OPT_CONF;
  public static final UUID UUID_OPT_DATA;
  public static final UUID UUID_OPT_PERI;
  public static final UUID UUID_OPT_SERV;
  public static final UUID UUID_TST_DATA;
  public static final UUID UUID_TST_SERV;
  
  static
  {
    UUID_DEVINFO_FWREV = UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb");
    UUID_IRT_SERV = UUID.fromString("f000aa00-0451-4000-b000-000000000000");
    UUID_IRT_DATA = UUID.fromString("f000aa01-0451-4000-b000-000000000000");
    UUID_IRT_CONF = UUID.fromString("f000aa02-0451-4000-b000-000000000000");
    UUID_IRT_PERI = UUID.fromString("f000aa03-0451-4000-b000-000000000000");
    UUID_ACC_SERV = UUID.fromString("f000aa10-0451-4000-b000-000000000000");
    UUID_ACC_DATA = UUID.fromString("f000aa11-0451-4000-b000-000000000000");
    UUID_ACC_CONF = UUID.fromString("f000aa12-0451-4000-b000-000000000000");
    UUID_ACC_PERI = UUID.fromString("f000aa13-0451-4000-b000-000000000000");
    UUID_HUM_SERV = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    UUID_HUM_DATA = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
    UUID_HUM_CONF = UUID.fromString("f000aa22-0451-4000-b000-000000000000");
    UUID_HUM_PERI = UUID.fromString("f000aa23-0451-4000-b000-000000000000");
    UUID_MAG_SERV = UUID.fromString("f000aa30-0451-4000-b000-000000000000");
    UUID_MAG_DATA = UUID.fromString("f000aa31-0451-4000-b000-000000000000");
    UUID_MAG_CONF = UUID.fromString("f000aa32-0451-4000-b000-000000000000");
    UUID_MAG_PERI = UUID.fromString("f000aa33-0451-4000-b000-000000000000");
    UUID_OPT_SERV = UUID.fromString("f000aa70-0451-4000-b000-000000000000");
    UUID_OPT_DATA = UUID.fromString("f000aa71-0451-4000-b000-000000000000");
    UUID_OPT_CONF = UUID.fromString("f000aa72-0451-4000-b000-000000000000");
    UUID_OPT_PERI = UUID.fromString("f000aa73-0451-4000-b000-000000000000");
    UUID_BAR_SERV = UUID.fromString("f000aa40-0451-4000-b000-000000000000");
    UUID_BAR_DATA = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
    UUID_BAR_CONF = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
    UUID_BAR_CALI = UUID.fromString("f000aa43-0451-4000-b000-000000000000");
    UUID_BAR_PERI = UUID.fromString("f000aa44-0451-4000-b000-000000000000");
    UUID_GYR_SERV = UUID.fromString("f000aa50-0451-4000-b000-000000000000");
    UUID_GYR_DATA = UUID.fromString("f000aa51-0451-4000-b000-000000000000");
    UUID_GYR_CONF = UUID.fromString("f000aa52-0451-4000-b000-000000000000");
    UUID_GYR_PERI = UUID.fromString("f000aa53-0451-4000-b000-000000000000");
    UUID_MOV_SERV = UUID.fromString("f000aa80-0451-4000-b000-000000000000");
    UUID_MOV_DATA = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
    UUID_MOV_CONF = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
    UUID_MOV_PERI = UUID.fromString("f000aa83-0451-4000-b000-000000000000");
    UUID_TST_SERV = UUID.fromString("f000aa64-0451-4000-b000-000000000000");
    UUID_TST_DATA = UUID.fromString("f000aa65-0451-4000-b000-000000000000");
    UUID_KEY_SERV = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    UUID_KEY_DATA = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    UUID_AUDIO_SERV = UUID.fromString("f000b000-0451-4000-b000-000000000000");
    UUID_AUDIO_STARTSTOP = UUID.fromString("f000b001-0451-4000-b000-000000000000");
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\sensortag\SensorTagGatt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */