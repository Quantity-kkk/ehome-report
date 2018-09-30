package report.java.jrreport.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectUtil
{
  public static Map<String, List<HashMap<String, Object>>> dataSetMap;
  public static HashMap<String, Object> currentMap;
  public static String styleString;

  public static Map<String, List<HashMap<String, Object>>> getDataSetMap()
  {
    return dataSetMap;
  }

  public static void setDataSetMap(Map<String, List<HashMap<String, Object>>> dataSetMap) {
    dataSetMap = dataSetMap;
  }

  public static String getStyleString()
  {
    return styleString;
  }
  public static void setStyleString(String styleString) {
    styleString = styleString;
  }
  public static HashMap<String, Object> getCurrentMap() {
    return currentMap;
  }
  public static void setCurrentMap(HashMap<String, Object> currentMap) {
    currentMap = currentMap;
  }
  public static void addMap(String key, List<HashMap<String, Object>> list) {
    if (dataSetMap == null) {
      dataSetMap = new HashMap();
    }
    dataSetMap.put(key, list);
  }
  public static void clearMap() {
    if (currentMap != null)
      currentMap.clear();
  }

  public static void putAll(HashMap<String, Object> map) {
    if (currentMap == null) {
      currentMap = new HashMap();
    }
    currentMap.putAll(map);
  }
  public static String getValue(String key) {
    String valueString = currentMap.get(key) != null ? currentMap.get(key).toString() : null;
    return valueString;
  }

  public static String replaceStr(String expression, String vStr, Map<String, Object> mm, String tempData) {
    if (expression.indexOf("select") == -1) {
      tempData = tempData.replace("{{" + vStr + "}}", DyMethodUtil.invokeMethod(expression, mm).toString());
    }
    else if (DyMethodUtil.invokeMethod(expression, mm).toString() == "")
      tempData = "";
    else {
      tempData = tempData.replace("{{" + vStr + "}}", DyMethodUtil.invokeMethod(expression, mm).toString());
    }

    return tempData;
  }
}

/* 
 * Qualified Name:     report.java.jrreport.util.SelectUtil
*
 */