package report.java.rpt.service;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.jrreport.util.JRDataFormat;
import report.java.rpt.util.RptCustomPreview;

public class CustomPreviewService
{
  public Map<String, Object> getJRPrintD(String tableModel, String dataSetsModel, String parmsModel, int currentPage, int pageType, int pageSize, String uid)
  {
    StringBuilder sb = new StringBuilder();
    Map dataMap = new HashMap();
    try
    {
      Document tableDesign = DocumentHelper.parseText(tableModel);

      sb.append("<table class='dynamic' cellspacing='0' cellpadding='0' align='center'>");

      int tempHeight = 0;

      String[] tempArray = { "", "" };
      String[] tabTitle = RptCustomPreview.listTableNode("headtitle", tableDesign.getRootElement(), tempArray, false);
      sb.append("<thead class='rtitle'>" + tabTitle[0] + "</thead>");

      if (!"".equals(tabTitle[1])) {
        tempHeight += Integer.parseInt(tabTitle[1]);
      }

      String[] tempArray1 = { "", "" };
      String[] tabHead = RptCustomPreview.listTableNode("reporthead", tableDesign.getRootElement(), tempArray1, false);
      sb.append("<thead class='rhead'>");
      sb.append(tabHead[0]);
      sb.append("</thead>");

      if (!"".equals(tabHead[1])) {
        tempHeight += Integer.parseInt(tabHead[1]);
      }

      String[] tempArray2 = { "", "" };
      Map DataSetMap = RptCustomPreview.compileReport(dataSetsModel);
      String[] tabData = RptCustomPreview.listTableNode("dataarea", tableDesign.getRootElement(), tempArray2, false);
      String styleData = "";
      if (!"".equals(tabData[0])) {
        styleData = tabData[0].replaceAll("<tr></tr>", "");
      }
      List groupNameList = new ArrayList();

      int dataHeight = 0;
      if (!"".equals(tabData[1])) {
        dataHeight = Integer.parseInt(tabData[1]);
      }
      List<HashMap<String,Object>> list = queryExecuter(getDataSet(styleData), DataSetMap, parmsModel);

      int totalPage = 0;

      if (pageSize == -1) {
        pageSize = list.size();
        totalPage = 1;
        currentPage = 1;
      } else {
        if (pageSize == 0)
        {
          double trueHeight = 0.0D;
          if (pageType == 1)
            trueHeight = 262.0D;
          else {
            trueHeight = 175.0D;
          }

          trueHeight -= (tempHeight + 1) / 96.0D * 25.399999999999999D;
          pageSize = (int)Math.floor(trueHeight / ((dataHeight + 1) / 96.0D * 25.399999999999999D));
        }
        totalPage = getPageCount(list.size(), pageSize);
      }

      dataMap.put("totalPage", Integer.valueOf(totalPage));

      dataMap.put("currentPage", Integer.valueOf(currentPage));

      dataMap.put("pageType", Integer.valueOf(pageType));

      Map summaryMap = new HashMap();

      int currentSumPage = currentPage * pageSize;
      if (list.size() < currentSumPage) {
        currentSumPage = list.size();
      }
      String trueData = "";
      boolean flag = true;

      int rownum = list.size();
      int colnum = groupNameList.size();
      String[][] a = new String[rownum][colnum];
      int rn = 0;
      for (HashMap hashMap : list) {
        int cn = 0;
        for (int gn = 0; gn <= colnum - 1; gn++) {
          a[rn][cn] = hashMap.get(groupNameList.get(gn)).toString();
          cn++;
        }
        rn++;
      }

      String[][] b = new String[rownum][colnum];
      for (int j = 0; j < colnum; j++) {
        for (int i = 0; i < rownum; i++) {
          int k = 1;
          for (int g = 0; g < rownum; g++) {
            if ((g == i) || 
              (!a[g][j].equals(a[i][j]))) continue;
            if (j == 0) {
              k++;
            }
            else if (a[g][(j - 1)].equals(a[i][(j - 1)])) {
              k++;
            }

          }

          b[i][j] = k+"";
        }

      }

      String[][] c = new String[rownum][colnum];
      for (int i = 0; i < rownum; i++) {
        for (int j = 0; j < colnum; j++) {
          if (b[i][j].equals("1")) {
            c[i][j] = "1";
          }
          else if (i == 0) {
            c[i][j] = "1";
          }
          else if (!a[i][j].equals(a[(i - 1)][j])) {
            c[i][j] = "1";
          }
          else if (j == 0) {
            c[i][j] = "0";
          }
          else if (!"0".equals(c[i][(j - 1)]))
            c[i][j] = "1";
          else {
            c[i][j] = "0";
          }

        }

      }

      for (int i = (currentPage - 1) * pageSize; i < currentSumPage; i++) {
        String tempData = styleData;
        HashMap<String,Object> map = (HashMap)list.get(i);

        for (String key : map.keySet()) {
          String value = map.get(key) != null ? map.get(key).toString() : "";
          tempData = tempData.replace("<ROWNUM>", String.valueOf(i + 1));

          if (!groupNameList.contains(key))
            tempData = tempData.replace("<=" + key + ">", value);
          else {
            for (int j = 0; j < groupNameList.size(); j++) {
              if (key.equals(groupNameList.get(j))) {
                if ("0".equals(c[i][j])) {
                  if (i == (currentPage - 1) * pageSize) {
                    if ("0".equals(c[i][j])) {
                      int k = 0;
                      for (int j2 = (currentPage - 1) * pageSize; j2 < c.length; j2++) {
                        if (!"0".equals(c[j2][j])) break;
                        k++;
                      }

                      tempData = tempData.replace("><=" + key + ">", " rowspan=" + k + ">" + value);
                    } else {
                      tempData = tempData.replaceAll("<td(.*?)><=" + key + "></td>", "");
                    }
                  }
                  else tempData = tempData.replaceAll("<td(.*?)><=" + key + "></td>", ""); 
                }
                else {
                  tempData = tempData.replace("><=" + key + ">", " rowspan=" + b[i][j] + ">" + value);
                }
              }
            }
          }
        }
        if (!summaryMap.isEmpty()) {
          flag = false;
        }
        trueData = trueData + tempData;
      }
      sb.append("<tbody>" + trueData + "</tbody>");
      dataMap.put("body", sb.toString());
    }
    catch (DocumentException e) {
      e.printStackTrace();
    }
    return dataMap;
  }

  public static List<String> getDataSet(String str)
  {
    List dataList = new ArrayList();
    if (!"".equals(str)) {
      String[] arrTemp = str.split("=");

      for (int i = 0; i < arrTemp.length; i++) {
        if (arrTemp[i].contains(".")) {
          dataList.add(arrTemp[i].substring(0, arrTemp[i].indexOf(".")));
        }
      }
      dataList = new ArrayList(new HashSet(dataList));
    }
    return dataList;
  }

  public static List<HashMap<String, Object>> queryExecuter(List<String> dataSetslist, Map<String, Object> map, String parmsModel)
  {
    List list = new ArrayList();
    for (String key : map.keySet()) {
      if (dataSetslist.contains(key)) {
        Map queryMap = (Map)((Map)map.get(key)).get("query");
        List FieldsList = (List)((Map)map.get(key)).get("fields");
        String dataSourceName = queryMap.get("datasourcename").toString();
        DataSourceBean dataSourceBean = new DataSourceBean();
        dataSourceBean.setDataSourceName(dataSourceName);
        String sql = queryMap.get("commandtext").toString();
        List l = new ArrayList();
        for (Iterator localIterator2 = FieldsList.iterator(); localIterator2.hasNext(); ) { Object obj = localIterator2.next();
          String DataField = ((Map)obj).get("datafield").toString();
          Object FormatValue = ((Map)obj).get("formatvalue");
          Object FormatType = ((Map)obj).get("formattype");
          Object datadic = ((Map)obj).get("datadic");
          JSONObject ob = new JSONObject();
          ob.put("datafield", DataField);
          ob.put("formatvalue", FormatValue);
          ob.put("formattype", FormatType);
          ob.put("datadic", datadic);
          l.add(ob);
        }

        String realPath = BaseAction.realPath + "db-config.xml";
        dataSourceBean = XmlUtil.selectXML(dataSourceBean, realPath);
        Connection conn = DBUtil.getConn(dataSourceBean);
        if ((conn == null) || 
          ("".equals(sql))) continue;
        StringBuilder sb = new StringBuilder();
        int orderIndex = sql.indexOf(" ORDER ");
        if (orderIndex > 0) {
          String orderSql = sql.substring(orderIndex);
          String tempSql = sql.substring(0, orderIndex);
          sb.append(tempSql).append(parmsModel).append(orderSql);
        } else {
          sb.append(sql).append(parmsModel);
        }queryExecuter(conn, sb.toString(), l, list, key);
      }

    }

    return list;
  }

  private static int getPageCount(int recordCount, int pageSize)
  {
    int size = recordCount / pageSize;

    int mod = recordCount % pageSize;
    if (mod != 0)
      size++;
    return recordCount == 0 ? 1 : size;
  }

  public static List<String> getSumCol(String str)
  {
    List sumDataList = new ArrayList();
    if (!"".equals(str)) {
      String[] arrTemp = str.split("</td>");

      for (int i = 0; i < arrTemp.length; i++) {
        if (arrTemp[i].contains("$V")) {
          sumDataList.add(arrTemp[i].substring(arrTemp[i].lastIndexOf("{") + 1, arrTemp[i].lastIndexOf("}")));
        }
      }
    }

    return sumDataList;
  }

  public static void queryExecuter(Connection conn, String sql, List<JSONObject> l, List<HashMap<String, Object>> list, String key)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      System.out.println("执行的sql语句是：" + sql);
      rs = ps.executeQuery();

      while (rs.next()) {
        HashMap map = new HashMap();
        try {
          map = (HashMap)list.get(rs.getRow() - 1);
          for (JSONObject obj : l) {
            String data = "";
            if (!obj.getString("formatvalue").equals("null"))
              data = JRDataFormat.convertData(Integer.parseInt(obj.getString("formattype")), "", rs.getString(obj.getString("datafield")), obj.getString("formatvalue"));
            else {
              data = rs.getString(obj.getString("datafield"));
            }
            map.put(key + "." + obj.getString("datafield"), data);
          }
          list.add(map);
        }
        catch (Exception e)
        {
          String data="";
          for (JSONObject obj : l) {
              data = "";
            if (!obj.getString("formatvalue").equals("null")) {
              data = JRDataFormat.convertData(Integer.parseInt(obj.getString("formattype")), "", rs.getString(obj.getString("datafield")), obj.getString("formatvalue"));
            } else if (!obj.getString("datadic").equals("null")) {
              JSONObject ob = JSONObject.fromObject(obj.getString("datadic"));
              if (ob.get(rs.getString(obj.getString("datafield"))) != null)
                data = ob.getString(rs.getString(obj.getString("datafield")));
              else
                data = rs.getString(obj.getString("datafield"));
            } else {
              data = rs.getString(obj.getString("datafield"));
            }
            map.put(key + "." + obj.getString("datafield"), data);
          }
          list.add(map);
        }
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } finally {
      DBUtil.closeRsPs(rs, ps);
      DBUtil.closeConn(conn);
    }
  }
}

 