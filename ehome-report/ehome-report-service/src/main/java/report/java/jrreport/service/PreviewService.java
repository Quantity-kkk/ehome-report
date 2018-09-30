package report.java.jrreport.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import report.java.common.utils.Tools;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.echarts.DataZoom;
import report.java.echarts.Polar;
import report.java.echarts.Toolbox;
import report.java.echarts.axis.Axis;
import report.java.echarts.axis.AxisTick;
import report.java.echarts.axis.CategoryAxis;
import report.java.echarts.axis.SplitLine;
import report.java.echarts.axis.ValueAxis;
import report.java.echarts.code.AxisType;
import report.java.echarts.code.Orient;
import report.java.echarts.code.Tool;
import report.java.echarts.code.Trigger;
import report.java.echarts.code.X;
import report.java.echarts.code.Y;
import report.java.echarts.json.GsonOption;
import report.java.echarts.series.Bar;
import report.java.echarts.series.Gauge;
import report.java.echarts.series.K;
import report.java.echarts.series.Line;
import report.java.echarts.series.Pie;
import report.java.echarts.series.Radar;
import report.java.jrreport.action.PreviewAction;
import report.java.jrreport.dao.PreviewDao;
import report.java.jrreport.util.JRFunExpImpl;
import report.java.jrreport.util.JRUtilNew;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PreviewService
{
  private static Logger logger = Logger.getLogger(PreviewService.class);

  public static int topCount = 0;

  public Map<String, Object> compileReport(String dataSetsModel) {
    Map DataSetMap = new HashMap();
    Map map = new HashMap();
    if (dataSetsModel.equals(""))
      return map;
    try {
      Document dataSets = DocumentHelper.parseText(dataSetsModel);
      listNodes(map, DataSetMap, dataSets.getRootElement());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    return map;
  }

  public List<HashMap<String, Object>> queryExecuter(List<String> dataSetslist, Map<String, Object> map, String parmsModel, JSONArray jsonArray, Map dicMap)
  {
    PreviewDao pd = new PreviewDao();
    List groupslist = new ArrayList();
    List list = new ArrayList();

    List nodes = null;
    HttpServletRequest request = ServletActionContext.getRequest();
    if (!parmsModel.equals("")) {
      try {
        Document document = DocumentHelper.parseText(parmsModel);
        Element rootElm = document.getRootElement();
        nodes = rootElm.elements("parm");
      }
      catch (DocumentException e) {
        e.printStackTrace();
      }
    }
    List lj = new ArrayList();
    String DataField;
    for (String key : map.keySet()) {
      List FieldsList = (List)((Map)map.get(key)).get("fields");
      for (Iterator localIterator2 = FieldsList.iterator(); localIterator2.hasNext(); ) { Object obj = localIterator2.next();
        DataField = "";
        if (((Map)obj).get("datafield") != null) {
          DataField = ((Map)obj).get("datafield").toString();
        }
        Object FormatValue = "";
        if (((Map)obj).get("formatvalue") != null) {
          FormatValue = ((Map)obj).get("formatvalue");
        }
        Object FormatType = "";
        if (((Map)obj).get("formattype") != null) {
          FormatType = ((Map)obj).get("formattype");
        }
        Object datatype = "";
        if (((Map)obj).get("datatype") != null) {
          datatype = ((Map)obj).get("datatype");
        }
        Object datadic = "";
        if ((JRUtilNew.baseMap.get("dicdb") != null) && (((String)JRUtilNew.baseMap.get("dicdb")).equals("1")))
        {
          if (dicMap.get(key + "." + DataField) != null) {
            datadic = JSON.toJSON(DBUtil.DicMap.get(dicMap.get(key + "." + DataField)));
          }
        }
        else if (((Map)obj).get("datadic") != null) {
          datadic = ((Map)obj).get("datadic");
        }

        JSONObject ob = new JSONObject();
        ob.put("datafield", DataField);
        ob.put("formatvalue", FormatValue.toString());
        ob.put("formattype", FormatType.toString());
        ob.put("datatype", datatype.toString());
        ob.put("datadic", datadic.toString());
        lj.add(ob);
      }
    }
    for (String key : map.keySet()) {
      if (dataSetslist.contains(key)) {
        List l = new ArrayList();
        List<Map<String,Object>> FieldsList = (List)((Map)map.get(key)).get("fields");
        for ( Map obj : FieldsList  ) {
        	DataField="";
          if (((Map)obj).get("datafield") != null) {
            DataField = ((Map)obj).get("datafield").toString();
          }
          Object FormatValue = "";
          if (((Map)obj).get("formatvalue") != null) {
            FormatValue = ((Map)obj).get("formatvalue");
          }
          Object FormatType = "";
          if (((Map)obj).get("formattype") != null) {
            FormatType = ((Map)obj).get("formattype");
          }
          Object datatype = "";
          if (((Map)obj).get("datatype") != null) {
            datatype = ((Map)obj).get("datatype");
          }
          Object datadic = "";
          if (((Map)obj).get("datadic") != null) {
            datadic = ((Map)obj).get("datadic").toString();
          }
          if ((JRUtilNew.baseMap.get("dicdb") != null) && (((String)JRUtilNew.baseMap.get("dicdb")).equals("1")))
          {
            if (dicMap.get(key + "." + DataField) != null) {
              datadic = JSON.toJSON(DBUtil.DicMap.get(dicMap.get(key + "." + DataField)));
            }
          }
          else if (((Map)obj).get("datadic") != null) {
            datadic = ((Map)obj).get("datadic").toString();
          }

          JSONObject ob = new JSONObject();
          ob.put("datafield", DataField);
          ob.put("formatvalue", FormatValue.toString());
          ob.put("formattype", FormatType.toString());
          ob.put("datatype", datatype.toString());
          ob.put("datadic", datadic.toString());
          l.add(ob);
          groupslist.add(DataField);
        }
        Map queryMap = (Map)((Map)map.get(key)).get("query");
        String dataSourceName = queryMap.get("datasourcename").toString();
        DataSourceBean dataSourceBean = new DataSourceBean();
        if (!DBUtil.isJndi().booleanValue()) {
          dataSourceBean.setDataSourceName(dataSourceName);
        }
        String sql = queryMap.get("commandtext").toString();
        if ("javabean".equals(dataSourceName))
        {
          if (list.size() == 0)
          {
            for (int i = 0; i < jsonArray.size(); i++)
            {
              HashMap mm = (HashMap)Tools.parseJSON2MapList(jsonArray.getString(i), lj);
              list.add(mm);
            }
          }
          else {
            List listnew = new ArrayList();

            for (int i = 0; i < jsonArray.size(); i++)
            {
              HashMap mm;
              if (i < list.size())
              {
                 mm = (HashMap)Tools.parseJSON2MapList(jsonArray.getString(i), lj);
                mm.putAll((Map)list.get(i));
              }
              else
              {
                mm = (HashMap)Tools.parseJSON2MapList(jsonArray.getString(i), lj);
              }
              listnew.add(mm);
            }
            if (list.size() > listnew.size()) {
              for (int i = listnew.size(); i < list.size(); i++) {
                listnew.add((HashMap)list.get(i));
              }
            }

            list = listnew;
          }
        } else {
          if (!DBUtil.isJndi().booleanValue()) {
            String realPath = BaseAction.realPath + "db-config.xml";
            dataSourceBean = XmlUtil.selectXML(dataSourceBean, realPath);
          }

          sql = sql.replaceAll("  ", " ");
          LinkedHashMap<String,Object> parameters = (LinkedHashMap)queryMap.get("parameters");
          ArrayList objParamsList = new ArrayList();
          int i = 0;
          int iii = 0;
          String[] sqlarr = sql.split("\\?");
          String nslq = sql;
          String sin = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTNVWXYZ0123456789-_";
          for (String k : parameters.keySet()) {
            Map parameter = (Map)parameters.get(k);
            Object value = request.getParameter(parameter.get("name").toString());

            String dbtype = "";
            if (nodes.size() > 0) {
              for (Iterator it = nodes.iterator(); it.hasNext(); ) {
                Element elm = (Element)it.next();
                if (parameter.get("name").equals(elm.attributeValue("name"))) {
                  dbtype = elm.attributeValue("dbtype");
                }
              }
            }
            Boolean isw = Boolean.valueOf(false);
            if ((dbtype != null) && (dbtype.length() > 0) && ((dbtype.toLowerCase().equals("double")) || (dbtype.toLowerCase().equals("int"))) && 
              (sqlarr[iii].length() > 4) && (sin.indexOf(sqlarr[iii].substring(sqlarr[iii].length() - 1)) != -1)) {
              isw = Boolean.valueOf(true);
            }

            if (isw.booleanValue()) {
              nslq = nslq.replace(sqlarr[iii] + "?", sqlarr[iii] + String.valueOf(value));
            } else {
              if ((dbtype != null) && (dbtype.length() > 0) && ((dbtype.toLowerCase().equals("double")) || (dbtype.toLowerCase().equals("int"))))
                objParamsList.add("【" + value + "】");
              else {
                objParamsList.add(value);
              }
              i++;
            }
            iii++;
          }

          Object[] objParams = objParamsList.toArray();
          Long startTime1 = Long.valueOf(System.currentTimeMillis());
          pd.queryExecuter(dataSourceBean, nslq, l, list, key, objParams);
          Long startTime2 = Long.valueOf(System.currentTimeMillis());
          logger.info("SQL-DAO用时：" + (startTime2.longValue() - startTime1.longValue()));
        }
      }
    }

    return list;
  }

  public Map<String, List<HashMap<String, Object>>> queryExecuterN(Connection conn, List<String> dataSetslist, Map<String, Object> map) {
    PreviewDao pd = new PreviewDao();
    List groupslist = new ArrayList();
    HttpServletRequest request = ServletActionContext.getRequest();
    Map dataMap = new HashMap();
    for (String key : map.keySet()) {
      if (dataSetslist.contains(key)) {
        List list = new ArrayList();
        Map queryMap = (Map)((Map)map.get(key)).get("query");
        List<Map<String,Object>> FieldsList = (List)((Map)map.get(key)).get("fields");
        DataSourceBean dataSourceBean = new DataSourceBean();
        if (!DBUtil.isJndi().booleanValue()) {
          String dataSourceName = queryMap.get("datasourcename").toString();
          dataSourceBean.setDataSourceName(dataSourceName);
          String realPath = BaseAction.realPath + "db-config.xml";
          dataSourceBean = XmlUtil.selectXML(dataSourceBean, realPath);
        }

        String sql = queryMap.get("commandtext").toString();
        sql = sql.replaceAll("  ", " ");
        LinkedHashMap<String,Object> parameters = (LinkedHashMap)queryMap.get("parameters");
        for (String k : parameters.keySet()) {
          Map parameter = (Map)parameters.get(k);
          String value = request.getParameter(parameter.get("name").toString());

          value = "'" + value + "'";
          sql = sql.replaceFirst("\\?", value).replace(" '%'", " '%").replace("'%' ", "%' ");
        }
        List l = new ArrayList();
        for (Object obj : parameters.entrySet()  ) { 
        	//Object obj = parameter.next();
          String DataField = "";
          if (((Map)obj).get("datafield") != null) {
            DataField = ((Map)obj).get("datafield").toString();
          }
          Object FormatValue = "";
          if (((Map)obj).get("formatvalue") != null) {
            FormatValue = ((Map)obj).get("formatvalue");
          }
          Object FormatType = "";
          if (((Map)obj).get("formattype") != null) {
            FormatType = ((Map)obj).get("formattype");
          }
          Object datatype = "";
          if (((Map)obj).get("datatype") != null) {
            datatype = ((Map)obj).get("datatype");
          }
          Object datadic = "";
          if (((Map)obj).get("datadic") != null) {
            datadic = ((Map)obj).get("datadic").toString();
          }
          JSONObject ob = new JSONObject();
          ob.put("datafield", DataField);
          ob.put("formatvalue", FormatValue.toString());
          ob.put("formattype", FormatType.toString());
          ob.put("datatype", datatype.toString());
          ob.put("datadic", datadic.toString());
          l.add(ob);
          groupslist.add(DataField);
        }
        pd.queryExecuter(dataSourceBean, sql, l, list, key, new Object[0]);
        dataMap.put(key, list);
      }
    }

    return dataMap;
  }

  public void listNodes(Map<String, Object> maps, Map<String, Object> DataSetMap, Element node) {
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
      map.put("dbType", node.attributeValue("dbtype"));
      qmap.put(qmap.size(), map);
    } else if (node.getName().equals("fields")) {
      DataSetMap.put("fields", new ArrayList());
    } else if (node.getName().equals("datafield")) {
      Map map = new HashMap();
      PreviewAction.dateFields.add(node.getText());
      map.put("datafield", node.getText());
      map.put("formatvalue", node.attributeValue("formatvalue"));
      map.put("formattype", node.attributeValue("formattype"));
      ((List)DataSetMap.get("fields")).add(map);
    } else if (node.getName().toLowerCase().equals("datatype")) {
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

  public Map<String, Object> compileParms(String parms) {
    if ((parms == null) || (parms.equals("")))
      return null;
    Map map = new HashMap();
    try {
      Document parmsDoc = DocumentHelper.parseText(parms);

      Element parmRoot = parmsDoc.getRootElement();
      Iterator iterator = parmRoot.elementIterator();
      while (iterator.hasNext()) {
        Element node = (Element)iterator.next();

        map.put(node.attributeValue("name"), node);
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return map;
  }

  public Map<String, Object> compileTableReport(String tableModel)
  {
    try {
      Document tableDesign = DocumentHelper.parseText(tableModel);
      Map map = new HashMap();
      listTableNodes(map, tableDesign.getRootElement(), null);

      return convertTableMap(map);
    } catch (DocumentException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Map<String, Object> compileTableReportN(String tableModel) {
    try {
      topCount = 0;
      Document tableDesign = DocumentHelper.parseText(tableModel);
      Map map = new HashMap();
      listTableNodesN(map, tableDesign.getRootElement(), null);
      return convertTableMapN(map);
    } catch (DocumentException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    return null;
  }

  public Map<String, Object> convertTableMap(Map<String, Object> map) {
    List list = (List)map.get("dataSets");
    List dl = new ArrayList();
    List gl = new ArrayList();
    Iterator localIterator;
    if ((list != null) && (list.size() != 0)) {
      for (localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        String str = ((Map)obj).get("header").toString();
        if (str.equals("")) continue;
        try {
          dl.add(str.substring(1, str.indexOf(".")));
          Map m = (Map)obj;
          if (str.indexOf("{#=") == -1) {
            if (str.indexOf("＋") != -1)
              m.put("header", "$F{" + str.substring(str.indexOf(".") + 1).replace("＋", "}+\"") + "\"");
            else
              m.put("header", str.substring(str.indexOf(".") + 1));
          }
          else {
            m.put("header", "\"" + str.substring(0, str.indexOf("{#=")) + "\"" + "+$F{" + str.substring(str.indexOf(".") + 1).replace("#}", "}+\"") + "\"");
          }
          gl.add(m);
        }
        catch (Exception e) {
          Map m = (Map)obj;
          m.put("header", str);
          gl.add(m);
        } }
    }
    else
    {
      list = (List)map.get("header");
      for (localIterator = list.iterator(); localIterator.hasNext(); ) { 
    	  Object obj = localIterator.next();
        for ( Iterator e = ((List)obj).iterator(); e.hasNext(); ) { Object ob = e.next();
          String str = ((Map)ob).get("header").toString();

          Map m = (Map)ob;

          gl.add(m);
        }
      }
    }

    dl = new ArrayList(new HashSet(dl));
    map.put("dataSets", dl);
    map.put("groups", gl);

    return map;
  }

  public Map<String, Object> convertTableMapN(Map<String, Object> map) {
    List list = (List)map.get("dataSets");
    List dl = new ArrayList();
    List gl = new ArrayList();
    if ((list != null) && (list.size() != 0)) {
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        String str = ((Map)obj).get("header").toString();
        dl.add(str.substring(1, str.indexOf(".")));
        if (str.startsWith("%")) {
          Map m = new HashMap();
          m.put("header", str.substring(str.indexOf(".") + 1));
          m.put("style", ((Map)obj).get("style"));
          gl.add(m);
        }

      }

    }

    dl = new ArrayList(new HashSet(dl));
    map.put("dataSets", dl);
    map.put("groups", gl);
    return map;
  }

  public void getdicList(Element node, Map dicMap)
  {
    for (Iterator childNode = node.elementIterator(); childNode.hasNext(); ) {
      Element eChild = (Element)childNode.next();
      String txt = eChild.getText();
      if (("".equals(txt)) || 
        (eChild.attribute("dic") == null) || ("".equals(eChild.attribute("dic").getValue()))) continue;
      if (txt.substring(0, 1).equals("=")) {
        txt = txt.substring(1);
      }
      if (eChild.attribute("dic").getValue().indexOf("=") != -1)
        dicMap.put(txt, eChild.attribute("dic").getValue().split("\\=")[1]);
      else {
        dicMap.put(txt, eChild.attribute("dic").getValue());
      }

    }

    Iterator iterator = node.elementIterator();
    while (iterator.hasNext()) {
      Element e = (Element)iterator.next();
      getdicList(e, dicMap);
    }
  }

  public void listTableNodes(Map<String, Object> map, Element node, String flag) {
    if (node.getName().equals("row")) {
      if ("headtitle".equals(node.attributeValue("category"))) {
        flag = "title";
      } else if ("reporthead".equals(node.attributeValue("category"))) {
        if (map.get("header") == null) {
          map.put("header", new ArrayList());
        }
        List list = (List)map.get("header");
        list.add(new ArrayList());
        flag = "header";
      } else if ("dataarea".equals(node.attributeValue("category"))) {
        if (map.get("dataSets") == null) {
          map.put("dataSets", new ArrayList());
        }
        flag = "dataSets";
      } else if ("reportfoot".equals(node.attributeValue("category"))) {
        if (map.get("reportfoot") == null) {
          map.put("reportfoot", new ArrayList());
        }
        List list = (List)map.get("reportfoot");
        list.add(new ArrayList());
        flag = "reportfoot";
      }
    }
    else if (node.getName().equals("col")) {
      List<Attribute> list = node.attributes();
      try {
        if (flag != null) {
          if ((flag.equals("title")) && (node.getTextTrim() != null) && (!node.getTextTrim().equals(""))) {
            Map styMap = new HashMap();
            Map styleMap = new HashMap();
            for (Attribute attribute : list) {
              if ((!attribute.getName().equals("link")) && (!attribute.getName().equals("frameid")) && (!attribute.getValue().equals(""))) {
                styleMap.put(attribute.getName(), attribute.getValue());
              }
            }
            styMap.put("style", styleMap);
            styMap.put("title", node.getText());
            map.put("title", styMap);
          } else if ((flag.equals("header")) && (!node.getTextTrim().equals(""))) {
            Map styMap = new HashMap();
            Map styleMap = new HashMap();
            for (Attribute attribute : list) {
              if ((!attribute.getName().equals("link")) && (!attribute.getName().equals("frameid"))) {
                styleMap.put(attribute.getName(), attribute.getValue());
              }
            }
            styMap.put("style", styleMap);
            styMap.put("header", node.getText());
            List l = (List)map.get("header");
            ((List)l.get(l.size() - 1)).add(styMap);
          }
          else if (flag.equals("dataSets")) {
            Map styMap = new HashMap();
            Map styleMap = new HashMap();
            for (Attribute attribute : list) {
              if ((!attribute.getName().equals("link")) && (!attribute.getName().equals("frameid"))) {
                if ((attribute.getName().equals("expText")) && (attribute.getValue().length() > 0) && (attribute.getValue().startsWith("if")) && (attribute.getValue().endsWith("}")) && (attribute.getValue().indexOf("}else{") != -1)) {
                  String hexp = attribute.getValue().substring(3, attribute.getValue().length() - 1).replace("){", "?").replace("}else{", ":");
                  styleMap.put(attribute.getName(), hexp);
                } else {
                  styleMap.put(attribute.getName(), attribute.getValue());
                }
              }
            }
            styMap.put("style", styleMap);
            styMap.put("header", node.getText());
            ((List)map.get("dataSets")).add(styMap);
          }
          else if (flag.equals("reportfoot")) {
            Map styMap = new HashMap();
            Map styleMap = new HashMap();
            for (Attribute attribute : list) {
              if ((!attribute.getName().equals("link")) && (!attribute.getName().equals("frameid"))) {
                styleMap.put(attribute.getName(), attribute.getValue());
              }
            }
            styMap.put("style", styleMap);
            styMap.put("reportfoot", node.getText());
            List l = (List)map.get("reportfoot");
            ((List)l.get(l.size() - 1)).add(styMap);
          }

        }

      }
      catch (Exception e)
      {
        System.out.println("Get a Exception：" + e.getMessage());
      }
    }

    Iterator iterator = node.elementIterator();
    while (iterator.hasNext()) {
      Element e = (Element)iterator.next();
      listTableNodes(map, e, flag);
    }
  }

  public void listTableNodesN(Map<String, Object> map, Element node, String flag) {
    if (node.getName().equals("row")) {
      if ("headtitle".equals(node.attributeValue("category"))) {
        flag = "title";
      } else if ("reporthead".equals(node.attributeValue("category"))) {
        if (topCount > 0) {
          if (map.get("footer") == null) {
            map.put("footer", new ArrayList());
          }
          List list = (List)map.get("footer");
          list.add(new ArrayList());
          flag = "footer";
        } else {
          if (map.get("header") == null) {
            map.put("header", new ArrayList());
          }
          List list = (List)map.get("header");
          list.add(new ArrayList());
          flag = "header";
        }
      } else if ("dataarea".equals(node.attributeValue("category"))) {
        if (map.get("dataSets") == null) {
          map.put("dataSets", new ArrayList());
        }
        List list = (List)map.get("header");
        list.add(new ArrayList());
        flag = "dataSets";
      }
    }
    else if (node.getName().equals("col")) {
      List<Attribute> list = node.attributes();
      if ((flag.equals("title")) && (!node.getTextTrim().equals(""))) {
        Map styMap = new HashMap();
        Map styleMap = new HashMap();
        for (Attribute attribute : list) {
          styleMap.put(attribute.getName(), attribute.getValue());
        }
        styMap.put("style", styleMap);
        styMap.put("title", node.getText());
        map.put("title", styMap);
      } else if ((flag.equals("header")) && (!node.getTextTrim().equals(""))) {
        Map styMap = new HashMap();
        Map styleMap = new HashMap();
        for (Attribute attribute : list) {
          styleMap.put(attribute.getName(), attribute.getValue());
        }
        styMap.put("style", styleMap);
        styMap.put("header", node.getText());
        List l = (List)map.get("header");
        System.out.println("header styMap:" + styMap);
        ((List)l.get(l.size() - 1)).add(styMap);
        if (node.getTextTrim().startsWith("=")) {
          if (map.get("dataSets") == null) {
            map.put("dataSets", new ArrayList());
          }
          ((List)map.get("dataSets")).add(styMap);
        }
      } else if ((flag.equals("footer")) && (!node.getTextTrim().equals(""))) {
        Map styMap = new HashMap();
        Map styleMap = new HashMap();
        for (Attribute attribute : list) {
          styleMap.put(attribute.getName(), attribute.getValue());
        }
        styMap.put("style", styleMap);
        styMap.put("header", node.getText());
        List l = (List)map.get("footer");
        ((List)l.get(l.size() - 1)).add(styMap);
        if (node.getTextTrim().startsWith("="))
          ((List)map.get("dataSets")).add(styMap);
      }
      else if ((flag.equals("dataSets")) && (node.getTextTrim().startsWith("="))) {
        Map styMap = new HashMap();
        Map styleMap = new HashMap();
        for (Attribute attribute : list) {
          styleMap.put(attribute.getName(), attribute.getValue());
          if (attribute.getName().equals("offsetTop")) {
            topCount = Integer.parseInt(attribute.getValue());
          }

        }

        styMap.put("style", styleMap);
        styMap.put("header", "%" + node.getText().substring(1));
        List l = (List)map.get("header");
        ((List)l.get(l.size() - 1)).add(styMap);
        ((List)map.get("dataSets")).add(styMap);
      }
    }
    Iterator iterator = node.elementIterator();
    while (iterator.hasNext()) {
      Element e = (Element)iterator.next();
      listTableNodesN(map, e, flag);
    }
  }

  public String[][] listToArr(String type, List<Object> list) {
    if (list == null)
      return null;
    String[][] arr = new String[list.size()][];
    for (int i = 0; i < arr.length; i++) {
      if (type.equals("group")) {
        if (i < arr.length - 1) {
          arr[i] = new String[0];
        } else {
          String[] temp = new String[arr.length];
          for (int j = 0; j < temp.length; j++) {
            temp[j] = ((Map)list.get(j)).get("header").toString();
          }
          if ((temp.length > 0) && (temp != null))
            arr[i] = temp;
        }
      }
      else if (type.equals("alias")) {
        List l = (List)list.get(i);
        String[] temp = new String[l.size()];
        for (int j = 0; j < l.size(); j++) {
          temp[j] = ((Map)l.get(j)).get("header").toString();
          if ((temp[j].indexOf(".") != -1) && (temp[j].indexOf("{#") == -1) && (temp[j].indexOf("#}") == -1)) {
            temp[j] = temp[j].substring(temp[j].indexOf(".") + 1);
          }
        }
        arr[i] = temp;
      } else if (type.equals("summary")) {
        List l = (List)list.get(i);
        String[] temp = new String[l.size()];
        for (int j = 0; j < l.size(); j++) {
          temp[j] = ((Map)l.get(j)).get("reportfoot").toString();
          if ((temp[j].indexOf("=") == 0) && (temp[j].indexOf(".") != -1)) {
            temp[j] = temp[j].substring(1);
          }

        }

        arr[i] = temp;
      }
    }
    return arr;
  }

  public String[][] listToArrN(String type, List<Object> list, Map<String, List<HashMap<String, Object>>> dataMap) {
    String[][] arr = new String[list.size()][];
    String[][] garr = new String[1][];
    for (int i = 0; i < arr.length; i++) {
      if (type.equals("group")) {
        String[] temp = new String[arr.length];
        for (int j = 0; j < temp.length; j++) {
          String str = ((Map)list.get(j)).get("header").toString();
          temp[j] = str;
        }
        garr[0] = temp;
        return garr;
      }if ((type.equals("alias")) || (type.equals("footer"))) {
        List l = (List)list.get(i);
        String[] temp = new String[l.size()];
        for (int j = 0; j < l.size(); j++) {
          String str = ((Map)l.get(j)).get("header").toString();
          if (str.startsWith("="))
          {
            String key = str.substring(1, str.indexOf("."));
            String value = str.substring(str.indexOf(".") + 1);
            str = ((HashMap)((List)dataMap.get(key)).get(0)).get(value).toString();
          } else if (str.startsWith("%")) {
            str = "";
          }
          temp[j] = str;
        }
        arr[i] = temp;
      }
    }
    return arr;
  }

  public String previewCharts(String chartsModel, String dataSetsModel) {
    Map DataSetMap = compileReport(dataSetsModel);
    return compileCharts(chartsModel, DataSetMap);
  }

  public String compileCharts(String chartsModel, Map<String, Object> DataSetMap) {
    try {
      String jsonStr = "[";
      Document charts = DocumentHelper.parseText(chartsModel);
      Element body = charts.getRootElement();
      List chartsList = body.selectNodes("charts");
      List<Node> chartList = ((Element)chartsList.get(0)).selectNodes("chart");
      for (Node ec1 : chartList) {
    	  Element ec = (Element)ec1; 
        String type = ec.attribute("category").getStringValue();
        String uuid = ec.attribute("uuid").getStringValue();
        String width = ec.attribute("width").getStringValue();
        String height = ec.attribute("height").getStringValue();
        String legend = "";

        GsonOption option = createOption(type);
        Element title = (Element)ec.selectNodes("titles").get(0);
        String zbt = title.elementTextTrim("text");
        String fbt = title.elementTextTrim("subtext");
        option.title().setText(zbt);
        option.title().setSubtext(fbt);

        List seriesList = ec.selectNodes("series");
        List<Node> serieList = ((Element)seriesList.get(0)).selectNodes("serie");
        for (Node node : serieList) {
        	Element serie = (Element)node;
          String name = serie.elementTextTrim("name");

          String data = serie.elementTextTrim("data");
          String dataSetName = data.split("\\.")[0].substring(1);

          JSONArray jsonarr = getDataByCol(dataSetName, DataSetMap, type);
          getSeries(option, name, jsonarr, type);
        }
        Map m = new HashMap();
        String ss = "{width:" + width + ",height:" + height + ",option:" + option.toString() + "}";
        jsonStr = jsonStr + ss + ",";
      }
      jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
      jsonStr = jsonStr + "]";
      return jsonStr;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public GsonOption createOption(String type) {
    GsonOption option = new GsonOption();
    ((Toolbox)option.toolbox().show(Boolean.valueOf(true))).feature(new Object[] { Tool.saveAsImage });
    if ((type.equals("line")) || (type.equals("bar"))) {
      option.tooltip().setTrigger(Trigger.axis);
      option.calculable(Boolean.valueOf(true));
    } else if (type.equals("k")) {
      ((DataZoom)option.dataZoom().show(Boolean.valueOf(true))).realtime(Boolean.valueOf(true)).start(Integer.valueOf(50)).end(Integer.valueOf(100));
      option.tooltip().setTrigger(Trigger.axis);
    } else if (type.equals("pie")) {
      option.title().x(X.center);
      option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
      option.legend().orient(Orient.vertical);
      option.legend().x(X.left);
      option.calculable(Boolean.valueOf(true));
    } else if (type.equals("radar")) {
      option.tooltip().setTrigger(Trigger.axis);
      option.legend().orient(Orient.vertical);
      option.legend().x(X.right);
      option.legend().y(Y.bottom);
      option.polar().add(new Polar());
      option.calculable(Boolean.valueOf(true));
    } else if (type.equals("gauge")) {
      option.tooltip().setFormatter("{a} <br/>{b} : {c}%");
    }
    return option;
  }

  public void getSeries(GsonOption option, String name, JSONArray jsonarr, String type) {
    if (type.equals("line"))
    {
      for (int i = 0; i < jsonarr.size(); i++) {
        JSONObject jo = (JSONObject)jsonarr.get(i);
        List l = option.legend().data();
        l.add(jo.getString("name"));
        option.legend().setData(l);

        Line line = new Line();
        line.name(jo.getString("name"));
        List valueList = new ArrayList();
        String value = jo.getString("value");
        String[] arrValue = value.split(",");
        for (int j = 0; j < arrValue.length; j++) {
          valueList.add(Integer.valueOf(Integer.parseInt(arrValue[j])));
        }
        line.setData(valueList);
        option.series().add(line);

        if (i == 0) {
          List listTemp = new ArrayList();
          String columnName = jo.getString("columnName");
          String[] arr = columnName.split(",");
          for (int j = 0; j < arr.length; j++) {
            listTemp.add(arr[j]);
          }
          CategoryAxis categoryAxis = new CategoryAxis();
          categoryAxis.setType(AxisType.category);
          categoryAxis.setBoundaryGap(Boolean.valueOf(false));
          categoryAxis.setData(listTemp);
          option.xAxis(new Axis[] { categoryAxis });
        }

      }

      ValueAxis valueAxis = new ValueAxis();
      valueAxis.setType(AxisType.value);
      option.yAxis(new Axis[] { valueAxis });
    } else if (type.equals("bar"))
    {
      Bar bar = new Bar();
      bar.name(name);
      List list = new ArrayList();
      List listX = new ArrayList();
      for (int i = 0; i < jsonarr.size(); i++) {
        JSONObject jo = (JSONObject)jsonarr.get(i);
        list.add(jo.get("value"));
        listX.add(jo.getString("name"));
      }

      bar.setData(list);
      List l = option.legend().data();
      l.add(name);
      option.legend().setData(l);
      option.series().add(bar);

      CategoryAxis categoryAxis = new CategoryAxis();
      categoryAxis.setType(AxisType.category);
      categoryAxis.setBoundaryGap(Boolean.valueOf(true));
      AxisTick axisTick = new AxisTick();
      axisTick.setOnGap(Boolean.valueOf(false));
      categoryAxis.setAxisTick(axisTick);
      SplitLine splitLine = new SplitLine();
      splitLine.setShow(Boolean.valueOf(false));
      categoryAxis.setSplitLine(splitLine);
      categoryAxis.setData(listX);
      option.xAxis(new Axis[] { categoryAxis });

      ValueAxis valueAxis = new ValueAxis();
      valueAxis.setType(AxisType.value);
      valueAxis.setScale(Boolean.valueOf(true));
      valueAxis.setData(list);
      option.yAxis(new Axis[] { valueAxis });
    } else if (type.equals("k")) {
      K k = new K();
      k.name(name);
      List list = new ArrayList();
      List listX = new ArrayList();
      for (int i = 0; i < jsonarr.size(); i++) {
        JSONObject jo = (JSONObject)jsonarr.get(i);

        String temp = jo.get("value").toString();

        String[] arr = temp.split(",");

        Double[] douArr = new Double[arr.length];
        for (int j = 0; j < arr.length; j++) {
          douArr[j] = Double.valueOf(Double.parseDouble(arr[j]));
        }
        list.add(douArr);
        listX.add(jo.getString("name"));

        if (i == 0) {
          String tipContent = "var res = params[0].seriesName + ' ' + params[0].name;";
          List listTemp = new ArrayList();
          String columnName = jo.getString("columnName");
          String[] arrtip = columnName.split(",");
          for (int j = 0; j < arrtip.length; j++) {
            tipContent = tipContent + " res += '<br/>" + arrtip[j] + ": ' + params[0].value[" + j + "];";
          }
          tipContent = "function (params) {" + tipContent + "return res;}";
          k.tooltip().trigger(Trigger.axis).formatter(tipContent);
        }
      }
      k.setData(list);

      List l = option.legend().data();
      l.add(name);
      option.legend().setData(l);
      option.series().add(k);

      CategoryAxis categoryAxis = new CategoryAxis();
      categoryAxis.setType(AxisType.category);
      categoryAxis.setBoundaryGap(Boolean.valueOf(true));
      AxisTick axisTick = new AxisTick();
      axisTick.setOnGap(Boolean.valueOf(false));
      categoryAxis.setAxisTick(axisTick);
      SplitLine splitLine = new SplitLine();
      splitLine.setShow(Boolean.valueOf(false));
      categoryAxis.setSplitLine(splitLine);
      categoryAxis.setData(listX);
      option.xAxis(new Axis[] { categoryAxis });

      ValueAxis valueAxis = new ValueAxis();
      valueAxis.setType(AxisType.value);
      valueAxis.setScale(Boolean.valueOf(true));

      option.yAxis(new Axis[] { valueAxis });
    } else if (type.equals("pie"))
    {
      Pie pie = new Pie();
      pie.name(name);

      List list = new ArrayList();
      for (int i = 0; i < jsonarr.size(); i++) {
        JSONObject jo = (JSONObject)jsonarr.get(i);
        Map map = new HashMap();
        map.put("value", jo.getString("value"));
        map.put("name", jo.getString("name"));
        List l = option.legend().data();
        l.add(jo.getString("name"));
        option.legend().setData(l);
        list.add(map);
      }
      pie.setData(list);
      pie.radius("55%");
      String[] s = new String[2];
      s[0] = "50%";
      s[1] = "60%";
      pie.center(s);
      option.series().add(pie);
    } else if (type.equals("radar")) {
      Radar radar = new Radar();
      radar.name(name);
      int max = 1000;
      int min = 500;
      Random random = new Random();

      List list = new ArrayList();
      for (int i = 0; i < 3; i++) {
        Map map = new HashMap();
        List arr = new ArrayList();
        ((Polar)option.polar().get(0)).indicator().clear();
        for (int j = 0; j < 5; j++) {
          int s = random.nextInt(max) % (max - min + 1) + min;
          arr.add(Integer.valueOf(s));
          Map m = new HashMap();
          m.put("text", Integer.valueOf(s));
          ((Polar)option.polar().get(0)).indicator().add(m);
        }
        map.put("value", arr);
        map.put("name", "数据" + i);
        List l = option.legend().data();
        l.add("数据" + i);
        option.legend().setData(l);
        list.add(map);
      }
      radar.setData(list);
      option.series().add(radar);
    } else if (type.equals("gauge"))
    {
      Gauge gauge = new Gauge();
      List list = new ArrayList();
      for (int i = 0; i < jsonarr.size(); i++) {
        JSONObject jo = (JSONObject)jsonarr.get(i);
        Map map = new HashMap();
        map.put("value", jo.getString("value"));
        map.put("name", jo.getString("name"));
        list.add(map);
        gauge.setData(list);
        gauge.detail().formatter(jo.getString("value") + "%");
        option.series().add(gauge);
      }
    }
  }

  public JSONArray getDataByCol(String dataSetName, Map<String, Object> DataSetMap, String tabType) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    JSONArray jsonarr = null;
    String str = "[";
    String sql = ((Map)((Map)DataSetMap.get(dataSetName)).get("query")).get("commandtext").toString();
    DataSourceBean dataSourceBean = new DataSourceBean();
    String dataSourceName = ((Map)((Map)DataSetMap.get(dataSetName)).get("query")).get("datasourcename").toString();
    dataSourceBean.setDataSourceName(dataSourceName);
    String realPath = BaseAction.realPath + "db-config.xml";
    dataSourceBean = XmlUtil.selectXML(dataSourceBean, realPath);
    Connection conn = DBUtil.getConn(dataSourceBean);
    if (conn != null) {
      if (!sql.equals(""))
        sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
      try
      {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        boolean flag = true;
        while (rs.next())
        {
          if (("bar".equals(tabType)) || ("pie".equals(tabType)) || ("gauge".equals(tabType))) {
            str = str + "{name:'" + rs.getString(1) + "',value:'" + rs.getString(2) + "'},";
          } else if (("line".equals(tabType)) || ("k".equals(tabType))) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String tempValue = "";
            String columnName = "";

            for (int i = 2; i <= columnCount; i++) {
              if (i != columnCount)
                tempValue = tempValue + rs.getString(i) + ",";
              else {
                tempValue = tempValue + rs.getString(i);
              }
            }
            if (flag)
            {
              for (int i = 2; i <= columnCount; i++) {
                if (i != columnCount)
                  columnName = columnName + rsmd.getColumnName(i) + ",";
                else
                  columnName = columnName + rsmd.getColumnName(i);
              }
              str = str + "{name:'" + rs.getString(1) + "',value:'" + tempValue + "',columnName:'" + columnName + "'},";
              flag = false;
            } else {
              str = str + "{name:'" + rs.getString(1) + "',value:'" + tempValue + "'},";
            }
          }
        }
        str = str + "]";
        jsonarr = JSONArray.parseArray(str.replace("},]", "}]"));
      }
      catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
      } finally {
        DBUtil.closeRsPs(rs, ps);
        DBUtil.closeConn(conn);
      }
    }
    return jsonarr;
  }

  private static String cExp(String str)
  {
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
    scriptEngine.put("JRFunExp", new JRFunExpImpl());
    return str;
  }
}

/* 
 * Qualified Name:     report.java.jrreport.service.PreviewService
*
 */