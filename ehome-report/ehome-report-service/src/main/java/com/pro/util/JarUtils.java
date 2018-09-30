package com.pro.util;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarUtils
{
  private static String getJarPath()
  {
    String jarPath = "";
    URL url = JarUtils.class.getProtectionDomain().getCodeSource().getLocation();
    try {
      jarPath = URLDecoder.decode(url.getPath(), "utf-8");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return jarPath;
  }
  public static String getJarImplementationVersion() {
    String version = "";
    try {
      JarFile jarfile = new JarFile(getJarPath());

      Manifest manifest = jarfile.getManifest();
      Attributes att = manifest.getMainAttributes();
      version = att.getValue("Implementation-Version");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return version;
  }

  public static String getJarSpecificationVersion() {
    String version = "";
    try {
      JarFile jarfile = new JarFile(getJarPath());

      Manifest manifest = jarfile.getManifest();
      Attributes att = manifest.getMainAttributes();
      version = att.getValue("Specification-Version");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return version;
  }

  public static String getJarManifestVersion() {
    String version = "";
    try {
      JarFile jarfile = new JarFile(getJarPath());

      Manifest manifest = jarfile.getManifest();
      Attributes att = manifest.getMainAttributes();
      version = att.getValue("Manifest-Version");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return version;
  }

  public static void main(String[] args) throws IOException {
    System.out.println(getJarPath());
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.util.JarUtils
*
 */