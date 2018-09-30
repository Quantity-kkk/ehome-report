package report.java.design.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import report.java.db.bean.DataSourceBean;
import report.java.db.bean.TableColumnBean;
import report.java.db.util.DBUtil;
import report.java.db.util.DataSourceUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.design.bean.ReportFileBean;
import report.java.design.bean.XMLbean;
import report.java.design.util.DesignXmlUtil;
import report.java.jrreport.util.JRUtilNew;

public class DesignService
{
  private static String realPath = BaseAction.realPath + "db-config.xml";

  public static boolean saveXML(String uuid, String type, String desc, String dataSetsModel, String tableXMLData, String expressionData, String parmsModel, String parmsExtModel, String fieldModel, String filePath, String reportVersion, String reportMemo, String mainUuid) {
    return DesignXmlUtil.saveXML(uuid, type, desc, dataSetsModel, tableXMLData, expressionData, parmsModel, parmsExtModel, fieldModel, filePath, reportVersion, reportMemo, mainUuid);
  }

  public static List<ReportFileBean> findAllReportFile(String filePath, ReportFileBean conditions)
  {
    List<ReportFileBean> list = new ArrayList();
    try {
      File file = new File(filePath);
      File[] files = file.listFiles();
      Comparator comparator = new Comparator<File>() {
        public int compare(File f1, File f2) {
          Long a = new Long(f1.lastModified());
          Long b = new Long(f2.lastModified());
          if (a.longValue() == b.longValue()) {
            return 1;
          }
          return a.longValue() < b.longValue() ? 1 : -1;
        }
      };
      System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
      try {
        Arrays.sort(files, comparator);
      }
      catch (Exception localException1) {
      }
      for (File f : files) {
        if (!f.isFile())
          continue;
        SAXReader reader = new SAXReader();

        Document document = reader.read(f);
        Element root = document.getRootElement();
        String uuid = root.attributeValue("uuid");
        Double version = Double.valueOf(root.attributeValue("version") != null ? Double.parseDouble(root.attributeValue("version")) : 1.0D);
        String reportStyle = root.attributeValue("reportStyle");
        String reportDesc = root.attributeValue("reportDescription");
        String mainUuid = root.attributeValue("mainUuid");
        String reprotVersion = root.attributeValue("reportVersion");
        String reprotMemo = root.attributeValue("reportMemo");

        String reportVersion = root.attributeValue("reportVersion");
        if ((reportVersion == null) || ("".equals(reportVersion))) {
          Map dataMap = DesignXmlUtil.openXML(f.getAbsolutePath());
          XMLbean xmlBean = (XMLbean)dataMap.get("xml");
          xmlBean.setReportVersion("1.0");
          xmlBean.setReportMemo(xmlBean.getTitle());
          xmlBean.setMainUuid(xmlBean.getUuid());

          mainUuid = xmlBean.getUuid();
          reprotVersion = xmlBean.getReportVersion();
          reprotMemo = xmlBean.getReportMemo();

          DesignXmlUtil.saveXML(xmlBean.getUuid(), xmlBean.getType(), xmlBean.getTitle(), xmlBean.getBody(), xmlBean.getDataSets(), xmlBean.getExpression(), xmlBean.getParmslist(), xmlBean.getParmsExtlist(), xmlBean.getFillreports(), f.getAbsolutePath(), xmlBean.getReportVersion(), 
            xmlBean.getReportMemo(), xmlBean.getMainUuid());
        }

        String iscustom = root.attributeValue("iscustom");
        if ("1".equals(iscustom))
        {
          continue;
        }
        ReportFileBean reportFileBean = new ReportFileBean();

        reportFileBean.setReportStyle(reportStyle.equals("N") ? "普通模板" : reportStyle.equals("D") ? "动态模板" : "图表");
        reportFileBean.setUuid(mainUuid);
        reportFileBean.setVersion(version);
        reportFileBean.setName(reportDesc);

        if ((conditions == null) || (conditions.getName() == null) || (conditions.getName().equals("")) || (reportDesc.contains(conditions.getName())) || (uuid.contains(conditions.getName()))) {
          ReportFileBean reportBean = null;
          for (ReportFileBean temp : list) {
            if ((mainUuid == null) || (temp.getUuid() == null) || 
              (!temp.getUuid().equals(mainUuid))) continue;
            reportBean = temp;
          }

          if (reportBean == null)
            list.add(reportFileBean);
          else {
            reportFileBean = reportBean;
          }
          ReportFileBean bean = new ReportFileBean();
          Calendar cal = Calendar.getInstance();
          cal.setTimeInMillis(f.lastModified());
          bean.setReportStyle(reportStyle.equals("N") ? "普通模板" : reportStyle.equals("D") ? "动态模板" : "图表");
          bean.setUuid(uuid);
          bean.setVersion(version);
          bean.setName(reportDesc);
          bean.setLastEditDate(cal.getTime().toLocaleString());
          bean.setReportVersion(reprotVersion);
          bean.setReportMemo(reprotMemo);
          bean.setMainUuid(mainUuid);

          reportFileBean.getReportFiles().add(bean);
        }
      }

      for (ReportFileBean bean : list)
        Collections.sort(bean.getReportFiles(), new Comparator<ReportFileBean>()
        {
          public int compare(ReportFileBean bean1, ReportFileBean bean2) {
            return -1 * bean1.getReportVersion().compareTo(bean2.getReportVersion());
          } } );
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public static void setReportFileVersion(String filePath)
  {
    try
    {
      File file = new File(filePath);
      File[] files = file.listFiles();

      for (File f : files) {
        Map dataMap = DesignXmlUtil.openXML(f.getAbsolutePath());
        XMLbean xmlBean = (XMLbean)dataMap.get("xml");
        if ((xmlBean.getReportVersion() == null) || ("".equals(xmlBean.getReportVersion()))) {
          xmlBean.setReportVersion("1.0");
          xmlBean.setReportMemo(xmlBean.getTitle());
          xmlBean.setMainUuid(xmlBean.getUuid());

          DesignXmlUtil.saveXML(xmlBean.getUuid(), xmlBean.getType(), xmlBean.getTitle(), xmlBean.getBody(), xmlBean.getDataSets(), xmlBean.getExpression(), xmlBean.getParmslist(), xmlBean.getParmsExtlist(), xmlBean.getFillreports(), f.getAbsolutePath(), xmlBean.getReportVersion(), 
            xmlBean.getReportMemo(), xmlBean.getMainUuid());
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static List<ReportFileBean> findAllReportFile(String filePath)
  {
    return findAllReportFile(filePath, null);
  }

  public static void deleteReport(String filePath) {
    File file = new File(filePath);
    file.delete();
  }

  public static String versionReport(String orgUuid, String maxVersion, String reportMemo) {
    String version = "";
    try {
      String uuid = UUID.randomUUID().toString().replace("-", "");

      Map dataMap = DesignXmlUtil.openXML(JRUtilNew.getBaseFilePath() + System.getProperty("file.separator") + orgUuid + ".xml");
      XMLbean xmlBean = (XMLbean)dataMap.get("xml");
      version = String.valueOf((int)Double.parseDouble(maxVersion) + 1) + ".0";
      xmlBean.setReportVersion(version);
      xmlBean.setReportMemo(reportMemo);

      DesignXmlUtil.saveXML(uuid, xmlBean.getType(), xmlBean.getTitle(), xmlBean.getBody(), xmlBean.getDataSets(), xmlBean.getExpression(), xmlBean.getParmslist(), xmlBean.getParmsExtlist(), xmlBean.getFillreports(), JRUtilNew.getBaseFilePath() + System.getProperty("file.separator") + uuid + 
        ".xml", xmlBean.getReportVersion(), xmlBean.getReportMemo(), xmlBean.getMainUuid());
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return version;
  }

  public static String openDesignXML(String filePath, String uuid) {
    try {
      File file = new File(filePath + System.getProperty("file.separator") + uuid + ".xml");

      if (file.exists())
        return uuid + ".xml";
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String parFields(String dataSetsModel, String dataSetName) {
    try {
      Document dataSetsDom = DocumentHelper.parseText(dataSetsModel);
      Element root = dataSetsDom.getRootElement();
      List<Node> dataSetList = root.selectNodes("dataset");
      for (Node node : dataSetList) {
    	  Element element = (Element)node;
        if (element.attribute("name").getStringValue().equals(dataSetName)) {
          Element queryElement = (Element)element.selectNodes("query").get(0);
          String name = queryElement.element("datasourcename").getStringValue();
          String sqlStr = queryElement.element("commandtext").getStringValue();
          sqlStr = sqlStr.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
          DataSourceBean dataSourceBean = new DataSourceBean();
          dataSourceBean.setDataSourceName(name);
          DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, realPath);
          DataSourceUtil sourceUtil = new DataSourceUtil();
          if (sqlStr.endsWith(";")) {
            sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
          }

          List<TableColumnBean> list = new ArrayList();
          if ("javabean".equals(name)) {
            if (sqlStr.indexOf("|") != -1) {
              String className = sqlStr.split("\\|")[0];

              list = sourceUtil.getProperties(className);
            }
          }
          else list = sourceUtil.executeSQL(dsb, sqlStr);

          Map fieldMap = new LinkedHashMap();
          List dicNodeList = element.selectNodes("fields/field/datadic");
          String fieldName;
          for (int i = 0; i < dicNodeList.size(); i++) {
            Element parentElement = ((Element)dicNodeList.get(i)).getParent();
            String fieldValue = ((Element)dicNodeList.get(i)).getText();
            fieldName = parentElement.attributeValue("name");
            fieldMap.put(fieldName, fieldValue);
          }

          if (list == null)
            return "";
          Element fieldsElement = (Element)element.selectNodes("fields").get(0);
          element.remove(fieldsElement);
          Element fields = element.addElement("fields");

          for (TableColumnBean bean : list) {
            Element field = fields.addElement("field").addAttribute("name", bean.getColumnName());
            field.addElement("datafield").setText(bean.getColumnName());
            if (bean.getColumnName().equals("ROWNUM"))
              field.addElement("datatype").setText("NVARCHAR");
            else {
              field.addElement("datatype").setText(bean.getColumnType());
            }
            String datadic = (String)fieldMap.get(bean.getColumnName());

            if ((!"".equals(datadic)) && (datadic != null))
              field.addElement("datadic").setText((String)fieldMap.get(bean.getColumnName()));
          }
        }
      }
      return root.asXML();
    } catch (DocumentException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return "";
  }

  public static String selectDic(String SQLText, String dataSourceName)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(dataSourceName);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, realPath);
    Connection conn = DBUtil.getConn(dsb);
    try {
      ps = conn.prepareStatement(SQLText);
      rs = ps.executeQuery();
      ResultSetMetaData rsm = rs.getMetaData();
      JSONArray arr = new JSONArray();
      while (rs.next()) {
        JSONObject obj = new JSONObject();
        for (int i = 1; i <= rsm.getColumnCount(); i++) {
          obj.put(rsm.getColumnName(i), rs.getString(i));
        }
        arr.add(obj);
      }
      String str = arr.toString();
      return str;
    }
    catch (SQLException e)
    {
      return null;
    } finally {
      DBUtil.release(new Object[] { 
        ps, rs, conn });
    }//throw SQLException;
  }

  public static List selectTable(String dataSourceName) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String SQLText = "";
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(dataSourceName);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, realPath);
    Connection conn = DBUtil.getConn(dsb);
    try {
      if (dsb.getType().equals("oracle")) {
        SQLText = "select table_name from USER_TABLES order by table_name";
      } else if (dsb.getType().equals("mysql")) {
        String dataname = dsb.getDataBaseUrl().split("/")[(dsb.getDataBaseUrl().split("/").length - 1)];
        SQLText = "select table_name from information_schema.tables where table_schema='" + dataname + "' order by table_name";
      }
      ps = conn.prepareStatement(SQLText);
      rs = ps.executeQuery();
      List tablenamelist = new ArrayList();
      while (rs.next()) {
        tablenamelist.add(rs.getString(1));
      }

      List localList1 = tablenamelist;
      return localList1;
    }
    catch (SQLException e)
    {
      return null;
    } finally {
      DBUtil.release(new Object[] { 
        ps, rs, conn });
    }//throw localObject;
  }

  public static List getFieldNames(String dataSourceName, String tableName)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(dataSourceName);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, realPath);
    Connection conn = DBUtil.getConn(dsb);
    try {
      String tempSql = "";
      String type = dataSourceBean.getType();
      if ("oracle".equals(type)) {
        tempSql = "select column_name from user_tab_columns where table_name='" + tableName + "' order by column_name";
      } else if ("db2".equals(type)) {
        tempSql = "select name from syscolumns where tbname='" + tableName + "' order by name";
      } else if ("mysql".equals(type)) {
        String url = dataSourceBean.getDataBaseUrl();
        if (!"".equals(url))
          url = url.substring(url.lastIndexOf("/") + 1, url.length());
        tempSql = "select column_name from information_schema.COLUMNS where table_name ='" + tableName + "' and table_schema='" + url + "' order by column_name";
      } else {
        tempSql = "select name from syscolumns where id=Object_Id('" + tableName + "') order by name";
      }

      ps = conn.prepareStatement(tempSql);
      rs = ps.executeQuery();
      JSONArray arr = new JSONArray();
      List fieldList = new ArrayList();
      while (rs.next()) {
        fieldList.add(rs.getString(1));
      }
      List localList1 = fieldList;
      return localList1;
    }
    catch (SQLException e)
    {
      return null;
    } finally {
      DBUtil.release(new Object[] { 
        ps, rs, conn });
    }//throw localObject;
  }
}

/*
 * Qualified Name:     report.java.design.service.DesignService
*
 */