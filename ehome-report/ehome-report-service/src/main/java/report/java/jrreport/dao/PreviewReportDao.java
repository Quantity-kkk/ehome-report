package report.java.jrreport.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.log4j.Logger;
import report.java.common.utils.Tools;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.jrreport.util.JRDataFormat;
import report.java.jrreport.util.JRFunExpImpl;
import report.java.jrreport.util.JRUtilNew;

public class PreviewReportDao
{
  private static Logger logger = Logger.getLogger(PreviewReportDao.class);

  public void queryExecuterNew(DataSourceBean dataSourceBean, Connection conn, String sql, List<JSONObject> l, Map<String, BigDecimal> sum, List<Map<String, Object>> list, String key, String expt, int currentPage, int pageSize, String dbtype, Object[] params)
    throws SQLException
  {
    Map objMap = new CaseInsensitiveMap();
    Map objm = new CaseInsensitiveMap();

    for (JSONObject obj : l) {
      objm = new CaseInsensitiveMap();
      objm = (Map)JSONObject.parseObject(obj.toJSONString(), Map.class);
      if ((objm.get("datadic") != null) && (objm.get("datadic").toString().length() > 5)) {
        if ((objm.get("datadic").toString().startsWith("{")) && (objm.get("datadic").toString().endsWith("}"))) {
          try {
            objm.put("datadic", JSONObject.parseObject(objm.get("datadic").toString(), Map.class));
          } catch (Exception e) {
            objm.put("datadic", "");
            e.printStackTrace();
          }
        } else if (objm.get("datadic").toString().toLowerCase().startsWith("select ")) {
          if (!DBUtil.isJndi().booleanValue()) {
            conn = DBUtil.getConn(dataSourceBean);
          }
          else if ((conn == null) || (conn.isClosed())) {
            conn = DBUtil.getConn(new DataSourceBean());
          }

          String dicsql = objm.get("datadic").toString();
          List<Map<String,Object>> diclist = DBUtil.getResultToList(conn, Boolean.valueOf(true), dicsql, new Object[0]);
          if (diclist.size() > 0) {
            Map ndic = new HashMap();
            for (Map rs : diclist) {
              if ((rs.get("opt_code") != null) && (rs.get("opt_name") != null)) {
                ndic.put(rs.get("opt_code").toString(), rs.get("opt_name"));
              }
            }
            objm.put("datadic", ndic);
          }
        }
      }
      else objm.put("datadic", "");

      objMap.put(objm.get("datafield").toString(), objm);
    }
    if (!sql.equals("")) {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    }
    if ((pageSize > 0) && (currentPage > 0)) {
      if (DBUtil.isJndi().booleanValue()) {
        dbtype = (String)JRUtilNew.baseMap.get("dbtype");
      }
      if ("mysql".equals(dbtype)) {
        sql = sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize;
      } else if ("oracle".equals(dbtype)) {
        sql = "SELECT * FROM ( SELECT AAA.*, ROWNUM RNNN FROM (" + sql + ") AAA WHERE ROWNUM <= " + currentPage * pageSize + ") WHERE RNNN >= " + ((currentPage - 1) * pageSize + 1);
      } else if ("db2".equals(dbtype)) {
        sql = "SELECT * FROM (SELECT BBB.*, ROWNUMBER() OVER() AS RNNN FROM (" + sql + ")AS BBB )AS AAA WHERE AAAA.RNNN BETWEEN " + ((currentPage - 1) * pageSize + 1) + " AND " + currentPage * pageSize;
      } else if ("sqlserver".equals(dbtype)) {
        int sindex = sql.toLowerCase().indexOf("select ");
        if (sindex != -1) {
          if (sql.toLowerCase().indexOf(" union ") == -1)
            sql = "select * from ( select row_number() over (order by temp__Column) temp__RowNumber,* from (" + sql.substring(0, sindex + 7) + "top " + currentPage * pageSize + " temp__Column=0," + sql.substring(sindex + 7) + ") aaa ) bbb where temp__RowNumber>" + 
              (currentPage - 1) * pageSize;
          else
            sql = "select * from ( select row_number() over (order by temp__Column) temp__RowNumber,* from ( select top " + currentPage * pageSize + " temp__Column=0,* from (" + sql + ") aaa ) bbb )ccc where temp__RowNumber>" + (currentPage - 1) * pageSize;
        }
      }
    }
    try
    {
      Boolean isClose = Boolean.valueOf(true);
      if (!DBUtil.isJndi().booleanValue()) {
        conn = DBUtil.getConn(dataSourceBean);
        isClose = Boolean.valueOf(true);
      } else {
        if ((conn == null) || (conn.isClosed())) {
          conn = DBUtil.getConn(new DataSourceBean());
        }
        isClose = Boolean.valueOf(false);
      }
      long dbtime = System.currentTimeMillis();
      List<Map<String,Object>> dlist = DBUtil.getResultToList(conn, isClose, sql, params);
      System.out.println("数据库查询时间：" + (System.currentTimeMillis() - dbtime));
      List expList = new ArrayList();
      int n = 0;
      long dttime = System.currentTimeMillis();
      Map map = null;
      for (Map rs : dlist) {
        n++;
        map = new CaseInsensitiveMap();
        if (list.size() > n - 1) {
          map = (Map)list.get(n - 1);
        }

        Iterator it = rs.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry itEntry = (Map.Entry)it.next();
          String itKey = (String)itEntry.getKey();
          if (objMap.get(itKey) != null) {
            Map ms = (Map)objMap.get(itKey);
            String nkey = key + "." + ms.get("datafield") + expt;

            Object data = rs.get(itKey);
            if ((ms.get("datadic") != null) && (ms.get("datadic").toString().length() > 0)) {
              data = ((Map)ms.get("datadic")).get(data);
            }

            try
            {
              String snkey = nkey.split("\\.")[1];
              Object oo = null;
              if (sum.get(snkey) != null) {
                oo = sum.get(snkey);
              }
              if (sum.get(nkey) != null) {
                oo = sum.get(nkey);
              }
              if (oo != null) {
                BigDecimal bd = new BigDecimal(oo.toString());
                bd = bd.add(new BigDecimal("".equals(data) ? "0" : data.toString()));
                sum.put(snkey, bd);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }

            map.put(nkey, data);
          }
        }

        if (list.size() <= n - 1) {
          list.add(map);
        }
      }

      dlist = null;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  public void queryExecuter(DataSourceBean dataSourceBean, Connection conn, String sql, List<JSONObject> l, List<HashMap<String, Object>> list, String key, String expt, int currentPage, int pageSize, String dbtype, Object[] params)
    throws SQLException
  {
    Map objMap = new HashMap();

    for (JSONObject obj : l) {
      Map objm = new HashMap();
      objm = (Map)JSONObject.parseObject(obj.toJSONString(), Map.class);
      if ((objm.get("datadic") != null) && (objm.get("datadic").toString().length() > 5) && (objm.get("datadic").toString().startsWith("{")) && (objm.get("datadic").toString().endsWith("}")))
        try {
          objm.put("datadic", JSONObject.parseObject(objm.get("datadic").toString(), Map.class));
        } catch (Exception e) {
          objm.put("datadic", "");
          e.printStackTrace();
        }
      else {
        objm.put("datadic", "");
      }
      objMap.put(objm.get("datafield").toString(), objm);
    }

    if (!sql.equals("")) {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    }
    if ((pageSize > 0) && (currentPage > 0)) {
      if (DBUtil.isJndi().booleanValue()) {
        dbtype = (String)JRUtilNew.baseMap.get("dbtype");
      }
      if ("mysql".equals(dbtype))
        sql = sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize;
      else if ("oracle".equals(dbtype))
        sql = "SELECT * FROM ( SELECT AAA.*, ROWNUM RNNN FROM (" + sql + ") AAA WHERE ROWNUM <= " + currentPage * pageSize + ") WHERE RNNN >= " + ((currentPage - 1) * pageSize + 1);
      else if ("db2".equals(dbtype))
        sql = "SELECT * FROM (SELECT BBB.*, ROWNUMBER() OVER() AS RNNN FROM (" + sql + ")AS BBB )AS AAA WHERE AAAA.RNNN BETWEEN " + ((currentPage - 1) * pageSize + 1) + " AND " + currentPage * pageSize;
      else if ("sqlserver".equals(dbtype))
      {
        sql = "select top " + pageSize + " AAA.* from( select row_number() over(order by id asc) as rownumber,* from (" + sql + ") as BBB )as AAA where rownumber>" + currentPage * pageSize;
      }
    }
    try {
      Boolean isClose = Boolean.valueOf(true);
      if (!DBUtil.isJndi().booleanValue()) {
        conn = DBUtil.getConn(dataSourceBean);
        isClose = Boolean.valueOf(true);
      } else {
        if ((conn == null) || (conn.isClosed())) {
          conn = DBUtil.getConn(new DataSourceBean());
        }
        isClose = Boolean.valueOf(false);
      }

      List<Map<String,Object>> dlist = DBUtil.getResultToList(conn, isClose, sql, params);
      int n = 0;
      for (Map rs : dlist)
      {
        n++;
        HashMap map = new HashMap();

        if (list.size() > n - 1) {
          map = (HashMap)list.get(n - 1);

          Iterator it = rs.entrySet().iterator();
          while (it.hasNext()) {
            Map.Entry itEntry = (Map.Entry)it.next();
            String itKey = (String)itEntry.getKey();
            if (objMap.get(itKey) != null) {
              Map ms = (Map)objMap.get(itKey);

              String nkey = key + "." + ms.get("datafield") + expt;
              Map datadic = null;
              if ((ms.get("datadic") != null) && (!String.valueOf(ms.get("datadic")).equals(""))) {
                datadic = (Map)ms.get("datadic");
              }
              Object data = "";
              if ((ms.get("formatvalue") != null) && (!"".equals(ms.get("formatvalue"))))
              {
                data = rs.get(itKey);
              } else if (datadic != null)
              {
                String dics = "";
                if (rs.get(itKey) != null) {
                  dics = rs.get(itKey).toString();
                }

                if ((dics == null) || ("".equals(dics)) || ((datadic.get(dics) == null) && (String.valueOf(ms.get("datadic")).indexOf("|") == -1))) {
                  if ((dics != null) && (!"".equals(dics.toString()))) {
                    if (datadic.get(dics) != null)
                      data = datadic.get(dics).toString();
                    else
                      data = dics;
                  }
                  else
                    data = dics;
                }
                else {
                  String[] dicArray = dics.split("\\|");
                  String ss = "";
                  for (String dic : dicArray) {
                    if ((dic != null) && (dic.length() > 0)) {
                      if (datadic.get(dic) == null)
                        ss = ss + dic + ",";
                      else {
                        ss = ss + datadic.get(dic) + ",";
                      }
                    }
                  }
                  data = ss;
                }
                String sdata = String.valueOf(data);
                if ((data != null) && (sdata.endsWith(",")))
                  data = sdata.substring(0, sdata.length() - 1);
              } else {
                data = Tools.getDbValue(String.valueOf(ms.get("datatype")), rs.get(itKey));
              }
              map.put(nkey, data);
            }

          }

        }
        else
        {
          Iterator it = rs.entrySet().iterator();
          while (it.hasNext()) {
            Map.Entry itEntry = (Map.Entry)it.next();
            String itKey = (String)itEntry.getKey();
            if (objMap.get(itKey) != null) {
              Map ms = (Map)objMap.get(itKey);

              String nkey = key + "." + ms.get("datafield") + expt;
              Map datadic = null;
              if ((ms.get("datadic") != null) && (!"".equals(ms.get("datadic")))) {
                datadic = (Map)ms.get("datadic");
              }
              Object data = "";
              if ((ms.get("formatvalue") != null) && (!"".equals(ms.get("formatvalue"))))
              {
                data = rs.get(itKey);
              } else if (datadic != null)
              {
                String dics = "";
                if (rs.get(itKey) != null) {
                  dics = rs.get(itKey).toString();
                }

                if ((dics == null) || ("".equals(dics)) || ((datadic.get(dics) == null) && (String.valueOf(ms.get("datadic")).indexOf("|") == -1))) {
                  if ((dics != null) && (!"".equals(dics.toString()))) {
                    if (datadic.get(dics) != null)
                      data = datadic.get(dics).toString();
                    else
                      data = dics;
                  }
                  else
                    data = dics;
                }
                else {
                  String[] dicArray = dics.split("\\|");
                  String ss = "";
                  for (String dic : dicArray) {
                    if ((dic != null) && (dic.length() > 0)) {
                      if (datadic.get(dic) == null)
                        ss = ss + dic + ",";
                      else {
                        ss = ss + datadic.get(dic) + ",";
                      }
                    }
                  }
                  data = ss;
                }
                String sdata = String.valueOf(data);
                if ((data != null) && (sdata.endsWith(",")))
                  data = sdata.substring(0, sdata.length() - 1);
              } else {
                data = Tools.getDbValue(String.valueOf(ms.get("datatype")), rs.get(itKey));
              }
              map.put(nkey, data);
            }
          }

          list.add(map);
        }

      }

      dlist = null;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  public int queryExecuterRecord(DataSourceBean dataSourceBean, Connection conn, String sql, Object[] params)
  {
    int record = 0;

    PreparedStatement psr = null;
    ResultSet rsr = null;
    List listUtil = new ArrayList();
    try
    {
      if (!DBUtil.isJndi().booleanValue())
        conn = DBUtil.getConn(dataSourceBean);
      else if ((conn == null) || (conn.isClosed())) {
        conn = DBUtil.getConn(new DataSourceBean());
      }
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
      String realSql = DBUtil.printRealSql(sql, params);
      long startTime = System.currentTimeMillis();
      psr = conn.prepareStatement(realSql);

      rsr = psr.executeQuery();
      Long endTime = Long.valueOf(System.currentTimeMillis());
      logger.info("用时：" + (endTime.longValue() - startTime));
      while (rsr.next()) {
        record += rsr.getInt(1);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } finally {
      DBUtil.closeRsPs(rsr, psr);
      if (!DBUtil.isJndi().booleanValue()) {
        DBUtil.closeConn(conn);
      }
    }

    return record;
  }

  public void queryFromObjectNew(String sql, List<JSONObject> l, Map<String, BigDecimal> sum, List<Map<String, Object>> list, String key, JSONArray jsonArray)
  {
    if (!sql.equals("")) {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    }
    JexlEngine jexl = new JexlEngine();
    JexlContext jc = new MapContext();
    jc.set("ttttttt", new JRFunExpImpl());
    try
    {
      if (!"".equals(sql)) {
        String className = sql;
        if (sql.indexOf("|") != -1) {
          className = sql.split("\\|")[0];
        }
        Class object = Class.forName(className);

        List datalist = jsonArray.toJavaList(object);

        for (Iterator localIterator1 = datalist.iterator(); localIterator1.hasNext(); ) { Object str = localIterator1.next();
          Map map = new CaseInsensitiveMap();
          for (JSONObject obj : l)
          {
            String propertyName = obj.getString("datafield");
            if (!"".equals(propertyName)) {
              Method method = str.getClass().getMethod("get" + getMethodName(propertyName), new Class[0]);

              Object data = method.invoke(str, new Object[0]);
              try
              {
                Object oo = null;
                if (sum.get(propertyName) != null) {
                  oo = sum.get(propertyName);
                }
                if (sum.get(key + "." + propertyName) != null) {
                  oo = sum.get(key + "." + propertyName);
                }
                if (oo != null) {
                  BigDecimal bd = new BigDecimal(oo.toString());
                  bd = bd.add(new BigDecimal(data.toString()));
                  sum.put(propertyName, bd);
                }

              }
              catch (Exception localException1)
              {
              }

              map.put(key + "." + propertyName, data);
            }
          }

          list.add(map); }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  public void queryFromObject(String sql, List<JSONObject> l, List<HashMap<String, Object>> list, String key, JSONArray jsonArray)
  {
    if (!sql.equals("")) {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    }
    try
    {
      if ((!"".equals(sql)) && (sql.indexOf("|") != -1)) {
        String className = sql.split("\\|")[0];

        Class object = Class.forName(className);

        List datalist = jsonArray.toJavaList(object);

        for (Iterator localIterator1 = datalist.iterator(); localIterator1.hasNext(); ) { Object str = localIterator1.next();
          HashMap map = new HashMap();
          for (JSONObject obj : l)
          {
            String propertyName = obj.getString("datafield");
            if (!"".equals(propertyName)) {
              Method method = str.getClass().getMethod("get" + getMethodName(propertyName), new Class[0]);

              Object data = method.invoke(str, new Object[0]);
              map.put(key + "." + propertyName, data);
            }
          }
          list.add(map); }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  private static String getMethodName(String fildeName) throws Exception
  {
    byte[] items = fildeName.getBytes();
    items[0] = (byte)((char)items[0] - 'a' + 65);
    String a = new String(items);
    return new String(items);
  }

  public void queryExecuter(DataSourceBean dataSourceBean, Connection conn, String sql, List<JSONObject> l, List<HashMap<String, Object>> list, Map<String, Object> queryCondition, String sortName, String sortType)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      StringBuffer newSql = new StringBuffer(sql);
      queryCondition.size();

      Set set = queryCondition.entrySet();
      Iterator iterator = set.iterator();
      for (int i = 0; i < set.size(); i++) {
        Map.Entry mapEntry = (Map.Entry)iterator.next();
        if ("".equals(mapEntry.getValue().toString()))
          continue;
        if (mapEntry.getKey().toString().contains("like"))
        {
          newSql.append(" and " + mapEntry.getKey() + " '%" + mapEntry.getValue() + "%'");
        }
        else
        {
          newSql.append(" and " + mapEntry.getKey() + " " + mapEntry.getValue() + " ");
        }
      }

      if ((sortName != null) && (!"".equals(sortName))) {
        newSql.append(" ORDER BY " + sortName);
      }
      if ((sortType != null) && (!"".equals(sortType))) {
        newSql.append(" " + sortType);
      }

      sql = newSql.toString();
      if (!DBUtil.isJndi().booleanValue()) {
        conn = DBUtil.getConn(dataSourceBean);
      }
      ps = conn.prepareStatement(sql);
      System.out.println("执行的sql语句是：" + sql);
      rs = ps.executeQuery();

      while (rs.next()) {
        HashMap map = new HashMap();
        try {
          map = (HashMap)list.get(rs.getRow() - 1);
          for (JSONObject obj : l) {
           String data = "";
            if (!obj.get("formatvalue").equals("null"))
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
          //String data;
          for (JSONObject obj : l) {
            String data = "";
            if (!obj.get("formatvalue").equals("null")) {
              data = JRDataFormat.convertData(Integer.parseInt(obj.getString("formattype")), "", rs.getString(obj.getString("datafield")), obj.getString("formatvalue"));
            } else if (!obj.get("datadic").equals("null")) {
              JSONObject ob = JSON.parseObject(obj.getString("datadic"));

              String dics = rs.getString(obj.getString("datafield"));
              if ((ob.get(dics) == null) && (dics.indexOf("|") == -1)) {
                data = rs.getString(obj.getString("datafield"));
              } else {
                String[] dicArray = dics.split("\\|");
                for (String dic : dicArray) {
                  if ((dic != null) && (!"".equals(dic))) {
                    if (ob.get(dic) == null)
                      data = data + dic + ",";
                    else {
                      data = data + ob.get(dic) + ",";
                    }
                  }
                }
              }
              if (data.endsWith(","))
                data = data.substring(0, data.length() - 1);
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
      if (!DBUtil.isJndi().booleanValue())
        DBUtil.closeConn(conn);
    }
  }
}
