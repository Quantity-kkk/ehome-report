package com.pro.log;

import com.pro.util.FileUitl;
import com.pro.util.SequenceUtil;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;

public class UserLog
{
  public static String getHomeDataPath()
  {
    Properties sp = System.getProperties();
    checkLocalDataDir();
    return sp.getProperty("user.home") + "\\AppData\\Roaming\\plat";
  }
  public static String getHomeDataDevpPath() {
    Properties sp = System.getProperties();
    checkLocalDataDir();
    return sp.getProperty("user.home") + "\\AppData\\Roaming\\devp";
  }
  public static String getHomeDataRepPath() {
    Properties sp = System.getProperties();
    checkLocalDataDir();
    return sp.getProperty("user.home") + "\\AppData\\Roaming\\Rep";
  }

  public static boolean checkLocalDataDir()
  {
    Properties sp = System.getProperties();
    String filePath = sp.getProperty("user.home") + "\\AppData";
    if (SequenceUtil.getOsType().equals("WINDOWS")) {
      File file = new File(filePath);
      if (!file.exists()) {
        file.mkdir();
        FileUitl.hiddenFoder(file.getAbsolutePath());
      }
      filePath = filePath + "\\Roaming";
      file = new File(filePath);
      if (!file.exists())
        file.mkdir();
    }
    else if (SequenceUtil.getOsType().equals("LIUNX")) {
      try {
        filePath = filePath.replaceAll("\\\\", "/");
        File file = new File(filePath);
        if (!file.exists()) {
          file.mkdir();
        }

        filePath = filePath + "/Roaming";
        file = new File(filePath);
        if (!file.exists())
          file.mkdir();
      }
      catch (Exception e) {
        System.out.println("报错了");
        e.printStackTrace();
      }
    }
    return false;
  }

  public static void main(String[] args) {
    checkLocalDataDir();
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.log.UserLog
*
 */