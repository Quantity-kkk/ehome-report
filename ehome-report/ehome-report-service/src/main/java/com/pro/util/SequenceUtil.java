package com.pro.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class SequenceUtil
{
  public static final String OS_TYPE_WIN = "WINDOWS";
  public static final String OS_TYPE_LIUNX = "LIUNX";

  public static String getOsType()
  {
    String osType = "";
    String os = System.getProperty("os.name");
    if (os.toLowerCase().startsWith("win"))
      osType = "WINDOWS";
    else if (os.toLowerCase().startsWith("linux")) {
      osType = "LIUNX";
    }
    return osType;
  }

  public static String getSN()
  {
    String SN = null;
    try {
      if (getOsType().equals("WINDOWS"))
        SN = getCPU4Windows() + getSerialNumber4Windows();
      else if (getOsType().equals("LIUNX")) {
        SN = getSerialNumber4Linux();
      }
      if (SN != null) {
        SN = reverseString(SN);
      }
      return SN;
    } catch (Exception e) {
      e.printStackTrace();
    }return SN;
  }

  public static String reverseString(String str)
  {
    String resultString = "";
    char[] charArray = str.toCharArray();
    for (int i = charArray.length - 1; i >= 0; i--) {
      resultString = resultString + charArray[i];
    }
    return resultString;
  }

  public static String getCPU4Windows()
    throws Exception
  {
    String serial = null;
    Process process = Runtime.getRuntime().exec(
      new String[] { "wmic", "cpu", "get", "ProcessorId" });
    process.getOutputStream().close();
    Scanner sc = new Scanner(process.getInputStream());
    String property = sc.next();
    serial = sc.next();
    return serial;
  }

  public static String getSerialNumber4Windows()
    throws Exception
  {
    String serial = null;
    Process process = Runtime.getRuntime().exec(
      new String[] { "wmic", "bios", "get", "serialnumber" });
    process.getOutputStream().close();
    Scanner sc = new Scanner(process.getInputStream());
    String property = sc.next();
    serial = sc.next();
    return serial;
  }

  private static String getSerialNumber4Linux()
    throws Exception
  {
    String SN = null;
    BufferedReader bufferedReader = null;
    Process process = null;
    String[] command = { "/bin/sh", "-c", "dmidecode |grep -A16 \"System Information$\"" };
    process = Runtime.getRuntime().exec(command);
    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    int index = -1;
    while ((line = bufferedReader.readLine()) != null) {
      index = line.indexOf("Serial Number");
      if (index >= 0) {
        SN = line.substring(index + "Serial Number".length() + 1).trim().replaceAll("\\s*", "");
        break;
      }
    }
    if (bufferedReader != null) {
      bufferedReader.close();
    }
    bufferedReader = null;
    process = null;
    return SN.toUpperCase();
  }

  public static void main(String[] args)
    throws Exception
  {
    System.out.println(getCPU4Windows());
    System.out.println(getSerialNumber4Windows());
    String serial = getSN();
    System.out.println(serial);

    System.out.println(reverseString("ABCDEFGHIGKLMNOPQRSTUVWXYZ"));
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.util.SequenceUtil
*
 */