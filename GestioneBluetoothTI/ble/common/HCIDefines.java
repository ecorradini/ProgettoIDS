package com.ti.ble.common;

import java.util.HashMap;
import java.util.Map;

public class HCIDefines
{
  public static final Map<Integer, String> hciErrorCodeStrings = new HashMap();
  
  static
  {
    hciErrorCodeStrings.put(Integer.valueOf(1), "Unknown HCI Command");
    hciErrorCodeStrings.put(Integer.valueOf(2), "Unknown Connection Identifier");
    hciErrorCodeStrings.put(Integer.valueOf(3), "Hardware Failure");
    hciErrorCodeStrings.put(Integer.valueOf(4), "Page Timeout");
    hciErrorCodeStrings.put(Integer.valueOf(5), "Authentication Failure");
    hciErrorCodeStrings.put(Integer.valueOf(6), "Pin or Key Missing");
    hciErrorCodeStrings.put(Integer.valueOf(7), "Memory Capacity Exceeded");
    hciErrorCodeStrings.put(Integer.valueOf(8), "Connnection Timeout");
    hciErrorCodeStrings.put(Integer.valueOf(9), "Connection Limit Exceeded");
    hciErrorCodeStrings.put(Integer.valueOf(10), "Synchronous Connection Limit To A Device Exceeded");
    hciErrorCodeStrings.put(Integer.valueOf(11), "ACL Connection Already Exists");
    hciErrorCodeStrings.put(Integer.valueOf(12), "Command Disallowed");
    hciErrorCodeStrings.put(Integer.valueOf(13), "Connection Rejected Due To Limited Resources");
    hciErrorCodeStrings.put(Integer.valueOf(14), "Connection Rejected Due To Security Reasons");
    hciErrorCodeStrings.put(Integer.valueOf(15), "Connection Rejected Due To Unacceptable BD_ADDR");
    hciErrorCodeStrings.put(Integer.valueOf(16), "Connection Accept Timeout Exceeded");
    hciErrorCodeStrings.put(Integer.valueOf(17), "Unsupported Feature Or Parameter Value");
    hciErrorCodeStrings.put(Integer.valueOf(18), "Invalid HCI Command Parameters");
    hciErrorCodeStrings.put(Integer.valueOf(19), "Remote User Terminated Connection");
    hciErrorCodeStrings.put(Integer.valueOf(20), "Remote Device Terminated Connection Due To Low Resources");
    hciErrorCodeStrings.put(Integer.valueOf(21), "Remote Device Terminated Connection Due To Power Off");
    hciErrorCodeStrings.put(Integer.valueOf(22), "Connection Terminated By Local Host");
    hciErrorCodeStrings.put(Integer.valueOf(23), "Repeated Attempts");
    hciErrorCodeStrings.put(Integer.valueOf(24), "Pairing Not Allowed");
    hciErrorCodeStrings.put(Integer.valueOf(25), "Unknown LMP PDU");
    hciErrorCodeStrings.put(Integer.valueOf(26), "Unsupported Remote Feature / Unsupported LMP Feature");
    hciErrorCodeStrings.put(Integer.valueOf(27), "SCO Offset Rejected");
    hciErrorCodeStrings.put(Integer.valueOf(28), "SCO Interval Rejected");
    hciErrorCodeStrings.put(Integer.valueOf(29), "SCO Air Mode Rejected");
    hciErrorCodeStrings.put(Integer.valueOf(30), "Invalid LMP Parameters / Invalid LL Parameters");
    hciErrorCodeStrings.put(Integer.valueOf(31), "Unspecified Error");
    hciErrorCodeStrings.put(Integer.valueOf(32), "Unsupported LMP Parameter Value / Unsupported LL Parameter Value");
    hciErrorCodeStrings.put(Integer.valueOf(33), "Role Change Not Allowed");
    hciErrorCodeStrings.put(Integer.valueOf(34), "LMP Response Timeout / LL Response Timeout");
    hciErrorCodeStrings.put(Integer.valueOf(35), "LMP Error Transaction Collision");
    hciErrorCodeStrings.put(Integer.valueOf(36), "LMP PDU Not Allowed");
    hciErrorCodeStrings.put(Integer.valueOf(37), "Encryption Mode Not Acceptable");
    hciErrorCodeStrings.put(Integer.valueOf(38), "Link Key Cannot Be Changed");
    hciErrorCodeStrings.put(Integer.valueOf(39), "Requested QoS Not Supported");
    hciErrorCodeStrings.put(Integer.valueOf(40), "Instant Passed");
    hciErrorCodeStrings.put(Integer.valueOf(41), "Pairing With Unit Key Not Supported");
    hciErrorCodeStrings.put(Integer.valueOf(42), "Differemt Tramsaction Collision");
    hciErrorCodeStrings.put(Integer.valueOf(44), "QoS Unacceptable Parameter");
    hciErrorCodeStrings.put(Integer.valueOf(45), "QoS Rejected");
    hciErrorCodeStrings.put(Integer.valueOf(46), "Channel Assessment Not Supported");
    hciErrorCodeStrings.put(Integer.valueOf(47), "Insufficient Security");
    hciErrorCodeStrings.put(Integer.valueOf(48), "Parameter Out of Mandatory Range");
    hciErrorCodeStrings.put(Integer.valueOf(50), "Role Switch Pending");
    hciErrorCodeStrings.put(Integer.valueOf(52), "Reserved Slot Violation");
    hciErrorCodeStrings.put(Integer.valueOf(53), "Role Switch Failed");
    hciErrorCodeStrings.put(Integer.valueOf(54), "Extended Inquiry Response Too Large");
    hciErrorCodeStrings.put(Integer.valueOf(55), "Simple Pairing Not Supported By Host");
    hciErrorCodeStrings.put(Integer.valueOf(56), "Host Busy-Pairing");
    hciErrorCodeStrings.put(Integer.valueOf(57), "Connection Rejected Due To No Suitable Channel Found");
    hciErrorCodeStrings.put(Integer.valueOf(58), "Controller Busy");
    hciErrorCodeStrings.put(Integer.valueOf(59), "Unacceptable Connection Parameters");
    hciErrorCodeStrings.put(Integer.valueOf(60), "Directed Advertising Timeout");
    hciErrorCodeStrings.put(Integer.valueOf(61), "Connection Terminated Due To MIC Failure");
    hciErrorCodeStrings.put(Integer.valueOf(62), "Connection Failed To Be Established");
    hciErrorCodeStrings.put(Integer.valueOf(63), "MAC Connection Failed");
    hciErrorCodeStrings.put(Integer.valueOf(64), "Coarse Clock Adjustment Rejected But Will Try to Adjust Using Clock Dragging");
  }
}


/* Location:              D:\classes-dex2jar.jar!\com\ti\ble\common\HCIDefines.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */