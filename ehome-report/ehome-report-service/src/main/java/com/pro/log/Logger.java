package com.pro.log;

import com.pro.util.DateUtil;
import com.pro.util.SequenceUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Logger
{
  public static final String LoggerFlag = "FILE";
  public static final String LoggerFile = "//log";

  public static void log(String log)
  {
    if ("FILE".equals("CONSOLE"))
      System.out.println(log);
    else if ("FILE".equals("FILE"))
      try {
        String filePath = UserLog.getHomeDataPath();
        String logFilePath = "";
        if (!SequenceUtil.getOsType().equals("WINDOWS"))
        {
          if (SequenceUtil.getOsType().equals("LIUNX"))
            filePath.replaceAll("\\", "/");
        }
        File foFile = new File(filePath);
        if (!foFile.exists()) {
          foFile.mkdirs();
        }
        logFilePath = filePath + "//log" + "_" + DateUtil.getDateYear() + ".log";
        File file = new File(logFilePath);
        if (!file.isDirectory()) {
          file.createNewFile();
        }
        FileWriter fw = new FileWriter(logFilePath, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(DateUtil.getDateTime() + " LOG " + new String(log.getBytes("utf-8"), "utf-8"));
        bw.newLine();
        bw.flush();
        bw.close();
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  public static void error(String log)
  {
    if ("FILE".equals("CONSOLE"))
      System.err.println(log);
    else if ("FILE".equals("FILE"))
      try {
        String filePath = UserLog.getHomeDataPath();
        String logFilePath = "";
        if (!SequenceUtil.getOsType().equals("WINDOWS"))
        {
          if (SequenceUtil.getOsType().equals("LIUNX"))
            filePath.replaceAll("\\", "/");
        }
        logFilePath = filePath + "//log" + "_" + DateUtil.getDateYear() + ".log";
        File file = new File(logFilePath);
        if (!file.isDirectory()) {
          file.getParentFile().mkdirs();
          file.createNewFile();
        }
        FileWriter fw = new FileWriter(logFilePath, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(DateUtil.getDateTime() + " ERR " + new String(log.getBytes("utf-8"), "utf-8"));
        bw.newLine();
        bw.flush();
        bw.close();
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.log.Logger
*
 */