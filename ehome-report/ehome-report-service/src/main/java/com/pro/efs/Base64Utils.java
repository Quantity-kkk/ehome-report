package com.pro.efs;

import com.pro.encryption.entrance.report.AuthRepConstant;
import com.pro.util.SequenceUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Utils
{
  private static final int CACHE_SIZE = 1024;

  public static byte[] decode(String base64)
    throws Exception
  {
    return new BASE64Decoder().decodeBuffer(base64);
  }

  public static String encode(byte[] bytes)
    throws Exception
  {
    return new BASE64Encoder().encode(bytes);
  }

  public static String encodeFile(String filePath)
    throws Exception
  {
    byte[] bytes = fileToByte(filePath);
    return encode(bytes);
  }

  public static void decodeToFile(String filePath, String base64)
    throws Exception
  {
    byte[] bytes = decode(base64);
    byteArrayToFile(bytes, filePath);
  }

  public static String fileToDecode(String filePath)
    throws Exception
  {
    byte[] bytes = fileToByte(filePath);
    return encode(bytes);
  }

  public static byte[] fileToByte(String filePath)
    throws Exception
  {
    byte[] data = new byte[0];
    if (SequenceUtil.getOsType().equals("LIUNX")) {
      filePath = filePath.replaceAll("\\\\", "/");
    }
    File file = new File(filePath);
    if (file.exists()) {
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
      byte[] cache = new byte[1024];
      int nRead = 0;
      while ((nRead = in.read(cache)) != -1) {
        out.write(cache, 0, nRead);
        out.flush();
      }
      out.close();
      in.close();
      data = out.toByteArray();
    }
    return data;
  }

  public static void byteArrayToFile(byte[] bytes, String filePath)
    throws Exception
  {
    InputStream in = new ByteArrayInputStream(bytes);
    File destFile = null;
    if ((!SequenceUtil.getOsType().equals("WINDOWS")) && 
      (SequenceUtil.getOsType().equals("LIUNX"))) {
      filePath = filePath.replaceAll("\\\\", "/");
    }
    destFile = new File(filePath);
    if (!destFile.getParentFile().exists()) {
      destFile.getParentFile().mkdirs();
    }
    destFile.createNewFile();
    OutputStream out = new FileOutputStream(destFile);
    byte[] cache = new byte[1024];
    int nRead = 0;
    while ((nRead = in.read(cache)) != -1) {
      out.write(cache, 0, nRead);
      out.flush();
    }
    out.close();
    in.close();
  }
  public static void main(String[] args) {
    String filePath = AuthRepConstant.LOCK_REP_PATH;
    System.out.println(filePath);
    System.out.println(filePath.replaceAll("\\\\", "/"));
    File destFile = new File(filePath);
    if (!destFile.getParentFile().exists())
      destFile.getParentFile().mkdirs();
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.efs.Base64Utils
*
 */