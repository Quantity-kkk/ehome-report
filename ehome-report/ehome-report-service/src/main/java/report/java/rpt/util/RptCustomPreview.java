package report.java.rpt.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import report.java.jrreport.report.jr.util.ReportElementUtil;
import report.java.rpt.action.CustomPreviewAction;

public class RptCustomPreview
{
  public static String[] listTableNode(String tableType, Element node, String[] backStr, boolean flag)
  {
    if (tableType.equals(node.attributeValue("category")))
    {
      int tmp17_16 = 0;
      String[] tmp17_15 = backStr; tmp17_15[tmp17_16] = (tmp17_15[tmp17_16] + "<tr>");
      for (Iterator childNode = node.elementIterator(); childNode.hasNext(); ) {
        String styleStr = "";
        String tdStr = "";
        String expText = "";
        String extParm = "";
        Element eChild = (Element)childNode.next();
        if (!"".equals(eChild.getText())) {
          List<Attribute> list = eChild.attributes();
          for (Attribute attribute : list)
          {
            if (isStyle(attribute.getName())) {
              if ((!flag) && ("height".equals(attribute.getName()))) {
                backStr[1] = attribute.getValue().split("px")[0];
                flag = true;
              }
              styleStr = styleStr + getTDStyle(attribute.getName(), attribute.getValue());
              try {
                extParm = extParm + getBDParm(attribute.getName(), 
                  attribute.getValue());
              }
              catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
            } else {
              String tempValue = attribute.getValue();
              if (("null".equals(tempValue)) || (tempValue == null) || ("".equals(tempValue)))
                tempValue = "''";
              tdStr = tdStr + attribute.getName() + "=" + tempValue + " ";
            }
            if (("expText".equals(attribute.getName())) && (attribute.getValue().length() > 0)) {
              expText = attribute.getValue();
            }
          }

          String eTex = "";
          String neTex = eChild.getText();

          boolean v = false;
          if (neTex.indexOf("＋") != -1) {
            v = true;
            eTex = neTex.split("＋")[0];
          } else {
            eTex = neTex;
          }
          if ((!expText.equals("")) && (expText != null)) {
            expText = ReportElementUtil.getRplcExp(expText);

            while (expText.indexOf("$F{") != -1) {
              expText = expText.replace(expText.substring(expText.indexOf("$F{"), expText.indexOf("}") + 1), "\"<" + 
                eTex.split("\\.")[0] + "." + expText.substring(expText.indexOf("$F{") + 3, expText.indexOf("}")) + 
                ">\"");
            }

            try
            {
              eTex = "{{" + expText + "}}";
            } catch (Exception localException) {
            }
          }
          else {
            eTex = "<" + eTex + ">";
          }

          if (v) {
            eTex = eTex + neTex.split("＋")[1];
          }

          if ("dataarea".equals(tableType))
          {
            int tmp662_661 = 0;
            String[] tmp662_660 = backStr; tmp662_660[tmp662_661] = (tmp662_660[tmp662_661] + "<td " + tdStr + "style=\"" + styleStr.replace("\"", "") + "\">" + eTex + "</td>");
          } else {
            String eStr = eChild.getText();
            if ((tableType.equals("reporthead")) && 
              (eStr.indexOf(",") != -1))
              try {
                eStr = "<img src=\"../../imageLine?str=" + 
                  URLEncoder.encode(eStr, 
                  "utf-8") + extParm + "\">";
              }
              catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
            int tmp799_798 = 0;
            String[] tmp799_797 = backStr; tmp799_797[tmp799_798] = 
              (tmp799_797[tmp799_798] + "<td " + tdStr + "style=\"" + 
              styleStr.replace("\"", "") + "\">" + eStr + 
              "</td>");
          }
        }
      }
      int tmp870_869 = 0;
      String[] tmp870_868 = backStr; tmp870_868[tmp870_869] = (tmp870_868[tmp870_869] + "</tr>");
    }

    Iterator iterator = node.elementIterator();
    while (iterator.hasNext()) {
      Element e = (Element)iterator.next();
      backStr = listTableNode(tableType, e, backStr, flag);
    }
    return backStr;
  }

  private static boolean isStyle(String styleName)
  {
    String styleArr = "rowspan,colspan,relatedid";

    return !styleArr.contains(styleName);
  }

  private static String getTDStyle(String attrName, String attrValue)
  {
    String[] tempArr = { "border", "font-color", "background-color", "height", "font-family", "font-size", "font-weight", "font-style", "text-decoration", "vertical-align", "white-space" };
    String returnStr = "";

    if (Arrays.asList(tempArr).contains(attrName)) {
      if ("border".equals(attrName)) {
        if (!"".equals(attrValue)) {
          String[] valueStr = attrValue.split("\",");
          for (int i = 0; i < valueStr.length; i++) {
            String[] detailStr = valueStr[i].split(":");
            if ("rightColor".equals(detailStr[0].substring(1)))
              returnStr = returnStr + "border-right:1px solid " + detailStr[1].substring(1) + ";";
            else if ("bottomColor".equals(detailStr[0]))
              returnStr = returnStr + "border-bottom:1px solid " + detailStr[1].substring(1, detailStr[1].length() - 2) + ";";
          }
        }
      } else if ("font-color".equals(attrName))
        returnStr = "color:" + attrValue + ";";
      else if (("background-color".equals(attrName)) && ("transparent".equals(attrValue)))
        returnStr = "";
      else
        returnStr = attrName + ":" + attrValue + ";";
    }
    return returnStr;
  }

  private static String getBDParm(String attrName, String attrValue)
    throws UnsupportedEncodingException
  {
    String[] tempArr = { "font-color", "background-color", "colwidth", "height", "font-family", "font-size", "font-weight" };
    String returnStr = "";
    if (Arrays.asList(tempArr).contains(attrName)) {
      if ("colwidth".equals(attrName))
        returnStr = "&width=" + URLEncoder.encode(attrValue, "utf-8");
      else if (("background-color".equals(attrName)) && ("transparent".equals(attrValue)))
        returnStr = "";
      else
        returnStr = "&" + attrName + "=" + URLEncoder.encode(attrValue, "utf-8");
    }
    return returnStr;
  }

  public static Map<String, Object> compileReport(String dataSetsModel) {
    Map DataSetMap = new HashMap();
    Map map = new HashMap();
    if (dataSetsModel.equals("")) return map; try
    {
      Document dataSets = DocumentHelper.parseText(dataSetsModel);
      listNodes(map, DataSetMap, dataSets.getRootElement());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    return map;
  }

  public static void listNodes(Map<String, Object> maps, Map<String, Object> DataSetMap, Element node) {
    if (node.getName().equals("dataset")) {
      DataSetMap = new HashMap();
      DataSetMap.put("dataset", node.attributeValue("name"));
    } else if (node.getName().equals("query")) {
      DataSetMap.put("query", new HashMap());
    } else if (node.getName().equals("datasourcename")) {
      ((Map)DataSetMap.get("query")).put("datasourcename", node.getText());
    } else if (node.getName().equals("commandtext")) {
      ((Map)DataSetMap.get("query")).put("commandtext", node.getText());
    } else if (node.getName().equals("parameters")) {
      ((Map)DataSetMap.get("query")).put("parameters", new LinkedHashMap());
    } else if (node.getName().equals("parameter")) {
      LinkedHashMap qmap = (LinkedHashMap)((Map)DataSetMap.get("query")).get("parameters");
      Map map = new HashMap();
      map.put("name", node.attributeValue("name"));
      map.put("dbType", node.attributeValue("dbType"));
      qmap.put(qmap.size(), map);
    } else if (node.getName().equals("fields")) {
      DataSetMap.put("fields", new ArrayList());
    } else if (node.getName().equals("datafield")) {
      Map map = new HashMap();

      CustomPreviewAction.dateFields.add(node.getText());
      map.put("datafield", node.getText());
      map.put("formatvalue", node.attributeValue("formatvalue"));
      map.put("formattype", node.attributeValue("formattype"));
      ((List)DataSetMap.get("fields")).add(map);
    } else if (node.getName().equals("datatype")) {
      List list = (List)DataSetMap.get("fields");
      ((Map)list.get(list.size() - 1)).put("datatype", node.getText());
    } else if (node.getName().equals("datadic")) {
      List list = (List)DataSetMap.get("fields");
      ((Map)list.get(list.size() - 1)).put("datadic", node.getText());
    }

    if (DataSetMap.get("dataset") != null) {
      maps.put((String)DataSetMap.get("dataset"), DataSetMap);
    }
    Iterator iterator = node.elementIterator();
    while (iterator.hasNext()) {
      Element e = (Element)iterator.next();
      listNodes(maps, DataSetMap, e);
    }
  }
}

/*
 * Qualified Name:     report.java.rpt.util.RptCustomPreview
*
 */