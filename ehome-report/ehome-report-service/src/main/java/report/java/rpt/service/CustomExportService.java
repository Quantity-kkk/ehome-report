package report.java.rpt.service;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.jrreport.util.JRDataFormat;
import report.java.rpt.bean.ColExtendBean;

public class CustomExportService
{
  public String getSerarchStr(List<ColExtendBean> conditionList)
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    String tempWhere = "";
    for (ColExtendBean bean : conditionList) {
      String colName = bean.getCol_name();
      String[] searchValue = request.getParameterValues(colName);
      String tempValue = "";
      String colType = bean.getCol_type();
      if (searchValue != null) {
        if ("1".equals(colType)) {
          tempValue = searchValue[0];
          if (!"".equals(tempValue)) {
            tempValue = "'" + searchValue[0] + "'";

            String colOperate = bean.getWhere_operate1();
            if (!"".equals(colOperate)) {
              if ("like".equalsIgnoreCase(colOperate))
                tempWhere = tempWhere + " AND " + colName + " " + colOperate + " '%" + tempValue.replaceAll("'", "") + "%' ";
              else
                tempWhere = tempWhere + " AND " + colName + " " + colOperate + " " + tempValue + " ";
            } else {
              colOperate = "=";
              tempWhere = tempWhere + " AND " + colName + " " + colOperate + " " + tempValue + " ";
            }

          }

        }
        else if (("2".equals(colType)) || ("3".equals(colType)))
        {
          String tempStr = "";
          String colOperate1 = bean.getWhere_operate1();
          if (!"".equals(colOperate1)) {
            if (colOperate1.contains(">")) {
              tempStr = searchValue[0];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate1 + " " + tempStr + " ";
              }
            } else {
              tempStr = searchValue[1];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate1 + " " + tempStr + " ";
              }
            }
          }
          String colOperate2 = bean.getWhere_operate2();
          if (!"".equals(colOperate2))
            if (colOperate2.contains(">")) {
              tempStr = searchValue[0];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate2 + " " + tempStr + " ";
              }
            } else {
              tempStr = searchValue[1];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate2 + " " + tempStr + " ";
              }
            }
        }
        else if ("4".equals(colType)) {
          if (searchValue != null) {
            for (int i = 0; i < searchValue.length; i++) {
              if (i != searchValue.length - 1)
                tempValue = tempValue + "'" + searchValue[i] + "'" + ",";
              else {
                tempValue = tempValue + "'" + searchValue[i] + "'";
              }
            }
            String colOperate = bean.getWhere_operate1();
            tempWhere = tempWhere + " AND " + colName + " " + colOperate + " (" + tempValue + ") ";
          }
        } else {
          if (!"5".equals(colType))
            continue;
          tempValue = searchValue[0];
          if (!"".equals(tempValue)) {
            tempValue = "'" + searchValue[0] + "'";
            String colOperate = bean.getWhere_operate1();
            tempWhere = tempWhere + " AND " + colName + " " + colOperate + " " + tempValue + " ";
          }
        }
      }
    }
    return tempWhere;
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
        }
        queryExecuter(conn, sb.toString(), l, list, key);
      }

    }

    return list;
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
           String  data = "";
            if (!obj.getString("formatvalue").equals("null"))
              data = JRDataFormat.convertData(Integer.parseInt(obj.getString("formattype")), "", rs.getString(obj.getString("datafield")), obj.getString("formatvalue"));
            else {
              data = rs.getString(obj.getString("datafield"));
            }
            map.put(obj.getString("datafield"), data);
          }
          list.add(map);
        }
        catch (Exception e)
        {
          String data="";
          for (JSONObject obj : l) {
           // String data = "";
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
            map.put(obj.getString("datafield"), data);
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

/*
 * Qualified Name:     report.java.rpt.service.CustomExportService
*
 */