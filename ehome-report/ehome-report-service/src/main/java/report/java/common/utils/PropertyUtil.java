package report.java.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import report.java.design.action.BaseAction;

public class PropertyUtil
{
  private static String rpath = BaseAction.realPath;

  public static Properties getProperty(String resourceName)
  {
    InputStream in = null;
    Properties props = new Properties();
    try {
      in = new FileInputStream(new File(rpath + resourceName));
      if (in != null)
        props.load(in);
    }
    catch (IOException e) {
      System.out.println("加载属性文件出错！:" + e);
    } finally {
      IOUtil.closeQuietly(in);
    }
    return props;
  }

  public static Map<String, String> getPropertyMap(String resourceName)
  {
    InputStream in = null;
    Map map = new HashMap();
    try {
      in = new FileInputStream(new File(rpath + resourceName));
      if (in == null) {
        throw new RuntimeException("没有找到资源文件 [" + resourceName + "]");
      }
      Properties prop = new Properties();
      prop.load(in);
      Set set = prop.entrySet();
      Iterator it = set.iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry)it.next();
        String key = entry.getKey().toString();
        String value = entry.getValue().toString();
        String fuKey = getWildcard(value);
        if ((fuKey != null) && (prop.get(fuKey) != null)) {
          String fuValue = prop.get(fuKey).toString();
          value = value.replaceAll("\\$\\{" + fuKey + "\\}", fuValue);
        }
        map.put(key, value);
      }
    }
    catch (IOException e) {
      System.out.println("加载属性文件出错:" + e);
    } finally {
      IOUtil.closeQuietly(in);
    }
    return map;
  }

  public static Properties getProperties(String resourceName)
  {
    InputStream in = null;
    Properties prop = new Properties();
    try {
      in = new FileInputStream(new File(rpath + resourceName));
      if (in == null) {
        throw new RuntimeException("没有找到资源文件 [" + resourceName + "]");
      }
      prop.load(in);
    } catch (IOException e) {
      System.out.println("加载属性文件出错:" + e);
    } finally {
      IOUtil.closeQuietly(in);
    }
    return prop;
  }

  private static String getWildcard(String str) {
    if ((str != null) && (str.indexOf("${") != -1)) {
      int start = str.indexOf("${");
      int end = str.indexOf("}");
      if ((start != -1) && (end != -1)) {
        return str.substring(start + 2, end);
      }
    }
    return null;
  }

  public static Map<String, String> getPropertyMap(Properties prop)
  {
    Map map = new HashMap();
    Set set = prop.entrySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry)it.next();
      map.put(entry.getKey().toString(), entry.getValue().toString());
    }
    return map;
  }
}

/*
 * Qualified Name:     report.java.common.utils.PropertyUtil
*
 */