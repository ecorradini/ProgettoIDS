package com.ti.ble.common.oad;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FWUpdateBINFileEntriesParser
{
  private static final String ns = null;
  
  private boolean readBoolean(XmlPullParser paramXmlPullParser, String paramString)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, ns, paramString);
    boolean bool = false;
    if (paramXmlPullParser.next() == 4)
    {
      bool = Boolean.getBoolean(paramXmlPullParser.getText());
      paramXmlPullParser.nextTag();
    }
    paramXmlPullParser.require(3, ns, paramString);
    return bool;
  }
  
  private float readFloat(XmlPullParser paramXmlPullParser, String paramString)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, ns, paramString);
    float f = 0.0F;
    if (paramXmlPullParser.next() == 4)
    {
      f = Float.parseFloat(paramXmlPullParser.getText());
      paramXmlPullParser.nextTag();
    }
    paramXmlPullParser.require(3, ns, paramString);
    return f;
  }
  
  private List readFw(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    ArrayList localArrayList = new ArrayList();
    paramXmlPullParser.require(2, ns, "FirmwareEntries");
    while (paramXmlPullParser.next() != 3) {
      if (paramXmlPullParser.getEventType() == 2) {
        if (paramXmlPullParser.getName().equals("FirmwareEntry")) {
          localArrayList.add(readEntry(paramXmlPullParser));
        } else {
          skip(paramXmlPullParser);
        }
      }
    }
    return localArrayList;
  }
  
  private int readInt(XmlPullParser paramXmlPullParser, String paramString)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, ns, paramString);
    int i = 0;
    if (paramXmlPullParser.next() == 4)
    {
      i = Integer.parseInt(paramXmlPullParser.getText());
      paramXmlPullParser.nextTag();
    }
    paramXmlPullParser.require(3, ns, paramString);
    return i;
  }
  
  private String readTag(XmlPullParser paramXmlPullParser, String paramString)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, ns, paramString);
    String str = "";
    if (paramXmlPullParser.next() == 4)
    {
      str = paramXmlPullParser.getText();
      paramXmlPullParser.nextTag();
    }
    paramXmlPullParser.require(3, ns, paramString);
    return str;
  }
  
  private void skip(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    if (paramXmlPullParser.getEventType() != 2) {
      throw new IllegalStateException();
    }
    int i = 1;
    while (i != 0) {
      switch (paramXmlPullParser.next())
      {
      default: 
        break;
      case 2: 
        i += 1;
        break;
      case 3: 
        i -= 1;
      }
    }
  }
  
  public List parse(InputStream paramInputStream)
    throws XmlPullParserException, IOException
  {
    try
    {
      Object localObject1 = Xml.newPullParser();
      ((XmlPullParser)localObject1).setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
      ((XmlPullParser)localObject1).setInput(paramInputStream, "UTF-8");
      ((XmlPullParser)localObject1).nextTag();
      localObject1 = readFw((XmlPullParser)localObject1);
      return (List)localObject1;
    }
    finally
    {
      paramInputStream.close();
    }
  }
  
  public FWUpdateTIFirmwareEntry readEntry(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, ns, "FirmwareEntry");
    Boolean localBoolean2 = Boolean.valueOf(false);
    String str6 = "";
    String str5 = "";
    int i = 0;
    String str4 = "";
    float f2 = 0.0F;
    Boolean localBoolean1 = Boolean.valueOf(false);
    float f1 = 0.0F;
    String str7 = "";
    String str3 = "";
    String str2 = "";
    String str1 = "";
    while (paramXmlPullParser.next() != 3) {
      if (paramXmlPullParser.getEventType() == 2)
      {
        String str8 = paramXmlPullParser.getName();
        if (str8.equals("Custom")) {
          localBoolean2 = Boolean.valueOf(readBoolean(paramXmlPullParser, "Custom"));
        } else if (str8.equals("WirelessStandard")) {
          str6 = readTag(paramXmlPullParser, "WirelessStandard");
        } else if (str8.equals("Type")) {
          str5 = readTag(paramXmlPullParser, "Type");
        } else if (str8.equals("OADAlgo")) {
          i = readInt(paramXmlPullParser, "OADAlgo");
        } else if (str8.equals("BoardType")) {
          str4 = readTag(paramXmlPullParser, "BoardType");
        } else if (str8.equals("RequiredVersionRev")) {
          f2 = readFloat(paramXmlPullParser, "RequiredVersionRev");
        } else if (str8.equals("SafeMode")) {
          localBoolean1 = Boolean.valueOf(readBoolean(paramXmlPullParser, "SafeMode"));
        } else if (str8.equals("Version")) {
          f1 = readFloat(paramXmlPullParser, "Version");
        } else if (str8.equals("Filename")) {
          str7 = readTag(paramXmlPullParser, "Filename");
        } else if (str8.equals("DevPack")) {
          str3 = readTag(paramXmlPullParser, "DevPack");
        } else if (str8.equals("Description")) {
          str2 = readTag(paramXmlPullParser, "Description");
        } else if (str8.equals("MCU")) {
          str1 = readTag(paramXmlPullParser, "MCU");
        }
      }
    }
    return new FWUpdateTIFirmwareEntry(str7, localBoolean2.booleanValue(), str6, str5, i, str4, f2, localBoolean1.booleanValue(), f1, str3, str2, str1);
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\oad\FWUpdateBINFileEntriesParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */