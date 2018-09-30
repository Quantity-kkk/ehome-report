package com.pro.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnProperty
{
  private static final String PROP_FILE = "path.properties";
  private static InputStream inputStream = null;

  public static String getPropValue(String name)
  {
    Properties prop = new Properties();
    String value = null;
    try {
      inputStream = EnProperty.class.getClassLoader().getResourceAsStream("path.properties");
      prop.load(inputStream);
      value = prop.getProperty(name).trim();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return value;
  }

  public static InputStream getInputStream() {
    return inputStream;
  }
  public static void setInputStream(InputStream inputStream) {
    inputStream = inputStream;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.util.EnProperty
*
 */