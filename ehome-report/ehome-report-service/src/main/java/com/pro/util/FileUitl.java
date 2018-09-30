package com.pro.util;

import java.io.IOException;

public class FileUitl
{
  public static void hiddenFoder(String filePath)
  {
    try
    {
      String sets = "attrib +H \"" + filePath + "\"";
      Runtime.getRuntime().exec(sets);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.util.FileUitl
*
 */