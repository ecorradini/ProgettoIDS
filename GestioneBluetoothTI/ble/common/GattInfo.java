package com.ti.ble.common;

import android.content.res.XmlResourceParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParserException;

public class GattInfo
{
  public static final UUID CC_SERVICE_UUID;
  public static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
  public static final UUID EDDYSTONE_SERVICE_UUID;
  public static final UUID OAD_SERVICE_UUID = UUID.fromString("f000ffc0-0451-4000-b000-000000000000");
  public static final UUID THROUGHPUT_TEST_UUID;
  private static Map<String, String> mDescrMap = new HashMap();
  private static Map<String, String> mIconMap = new HashMap();
  private static Map<String, String> mNameMap;
  private static Map<String, String> mTitleMap = new HashMap();
  private static final String uuidBtSigBase = "0000****-0000-1000-8000-00805f9b34fb";
  private static final String uuidTiBase = "f000****-0451-4000-b000-000000000000";
  
  static
  {
    CC_SERVICE_UUID = UUID.fromString("f000ccc0-0451-4000-b000-000000000000");
    THROUGHPUT_TEST_UUID = UUID.fromString("f0001234-0451-4000-b000-000000000000");
    EDDYSTONE_SERVICE_UUID = UUID.fromString("0000feaa-0000-1000-8000-00805f9b34fb");
    mNameMap = new HashMap();
  }
  
  public GattInfo(XmlResourceParser paramXmlResourceParser)
  {
    try
    {
      readUuidData(paramXmlResourceParser);
      return;
    }
    catch (XmlPullParserException paramXmlResourceParser)
    {
      paramXmlResourceParser.printStackTrace();
      return;
    }
    catch (IOException paramXmlResourceParser)
    {
      paramXmlResourceParser.printStackTrace();
    }
  }
  
  public static String getDescription(UUID paramUUID)
  {
    paramUUID = toShortUuidStr(paramUUID);
    return (String)mDescrMap.get(paramUUID.toUpperCase(Locale.ENGLISH));
  }
  
  public static String getTitle(UUID paramUUID)
  {
    return (String)mTitleMap.get(paramUUID.toString());
  }
  
  public static boolean isBtSigUuid(UUID paramUUID)
  {
    return paramUUID.toString().replace(toShortUuidStr(paramUUID), "****").equals("0000****-0000-1000-8000-00805f9b34fb");
  }
  
  public static boolean isTiUuid(UUID paramUUID)
  {
    return paramUUID.toString().replace(toShortUuidStr(paramUUID), "****").equals("f000****-0451-4000-b000-000000000000");
  }
  
  private void readUuidData(XmlResourceParser paramXmlResourceParser)
    throws XmlPullParserException, IOException
  {
    paramXmlResourceParser.next();
    Object localObject3 = null;
    Object localObject4 = null;
    Object localObject8 = null;
    Object localObject6 = null;
    int i = paramXmlResourceParser.getEventType();
    if (i != 1)
    {
      Object localObject1;
      Object localObject7;
      Object localObject5;
      Object localObject2;
      if (i == 0)
      {
        localObject1 = localObject4;
        localObject7 = localObject3;
        localObject5 = localObject6;
        localObject2 = localObject8;
      }
      for (;;)
      {
        i = paramXmlResourceParser.next();
        localObject8 = localObject2;
        localObject6 = localObject5;
        localObject3 = localObject7;
        localObject4 = localObject1;
        break;
        if (i == 2)
        {
          localObject7 = paramXmlResourceParser.getName();
          localObject1 = paramXmlResourceParser.getAttributeValue(null, "uuid");
          localObject2 = paramXmlResourceParser.getAttributeValue(null, "descr");
          localObject5 = paramXmlResourceParser.getAttributeValue(null, "icon");
        }
        else
        {
          localObject2 = localObject8;
          localObject5 = localObject6;
          localObject7 = localObject3;
          localObject1 = localObject4;
          if (i != 3)
          {
            localObject2 = localObject8;
            localObject5 = localObject6;
            localObject7 = localObject3;
            localObject1 = localObject4;
            if (i == 4)
            {
              localObject2 = localObject8;
              localObject5 = localObject6;
              localObject7 = localObject3;
              localObject1 = localObject4;
              if (((String)localObject3).equalsIgnoreCase("item"))
              {
                String str = paramXmlResourceParser.getText();
                localObject2 = localObject8;
                localObject5 = localObject6;
                localObject7 = localObject3;
                localObject1 = localObject4;
                if (!((String)localObject4).isEmpty())
                {
                  localObject1 = ((String)localObject4).replace("0x", "");
                  mNameMap.put(localObject1, paramXmlResourceParser.getText());
                  mDescrMap.put(localObject1, localObject8);
                  mIconMap.put(localObject1, localObject6);
                  mTitleMap.put(localObject1, str);
                  localObject2 = localObject8;
                  localObject5 = localObject6;
                  localObject7 = localObject3;
                }
              }
            }
          }
        }
      }
    }
  }
  
  private static String toShortUuidStr(UUID paramUUID)
  {
    return paramUUID.toString().substring(4, 8);
  }
  
  private static String uuidToIcon(String paramString)
  {
    return (String)mIconMap.get(paramString);
  }
  
  public static String uuidToIcon(UUID paramUUID)
  {
    return uuidToIcon(toShortUuidStr(paramUUID).toUpperCase(Locale.ENGLISH));
  }
  
  private static String uuidToName(String paramString)
  {
    return (String)mNameMap.get(paramString);
  }
  
  public static String uuidToName(UUID paramUUID)
  {
    return uuidToName(toShortUuidStr(paramUUID).toUpperCase(Locale.ENGLISH));
  }
  
  public static String uuidToString(UUID paramUUID)
  {
    if (isBtSigUuid(paramUUID)) {}
    for (paramUUID = toShortUuidStr(paramUUID);; paramUUID = paramUUID.toString()) {
      return paramUUID.toUpperCase(Locale.ENGLISH);
    }
  }
  
  public String descriptionOfUUID(String paramString)
  {
    return (String)mDescrMap.get(paramString);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\GattInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */