package report.java.jrreport.dao;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import report.java.common.utils.Tools;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.jrreport.util.JRDataFormat;

public class PreviewDao
{
  private static Logger logger = Logger.getLogger(PreviewDao.class);

  public void queryExecuter(DataSourceBean dataSourceBean, String sql, List<JSONObject> l, List<HashMap<String, Object>> list, String key, Object[] params)
  {
    Map objMap = new HashMap();

    for (JSONObject obj : l) {
      Map objm = new HashMap();
      objm = (Map)JSONObject.parseObject(obj.toJSONString(), Map.class);
      if ((objm.get("datadic") != null) && (String.valueOf(objm.get("datadic")).length() > 5) && (String.valueOf(objm.get("datadic")).startsWith("{")) && (String.valueOf(objm.get("datadic")).endsWith("}")))
        try {
          objm.put("datadic", JSONObject.parseObject(String.valueOf(objm.get("datadic")), Map.class));
        } catch (Exception e) {
          objm.put("datadic", "");
          e.printStackTrace();
        }
      else {
        objm.put("datadic", "");
      }
      objMap.put(objm.get("datafield"), objm);
    }
    if (!sql.equals("")) {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    }
    List<Map<String,Object>> listn = DBUtil.getResultToList(dataSourceBean, Boolean.valueOf(true), sql, params);

    int jj = 0;
    for (Map mm : listn) {
      HashMap map = new HashMap();
      jj++;
      if (list.size() > jj - 1) {
        map = (HashMap)list.get(jj - 1);

        Iterator it = mm.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry itEntry = (Map.Entry)it.next();
          String itKey = (String)itEntry.getKey();
          if (objMap.get(itKey) != null) {
            Map ms = (Map)objMap.get(itKey);

            Map datadic = null;
            if ((ms.get("datadic") != null) && (!String.valueOf(ms.get("datadic")).equals(""))) {
              datadic = (Map)ms.get("datadic");
            }
            Object data = "";
            if ((ms.get("formatvalue") != null) && (!"".equals(ms.get("formatvalue"))))
            {
              data = mm.get(itKey);
            } else if (datadic != null)
            {
              String dics = "";
              if (mm.get(itKey) != null) {
                dics = mm.get(itKey).toString();
              }

              if ((dics == null) || ("".equals(dics)) || ((datadic.get(dics) == null) && (dics.toString().indexOf("|") == -1))) {
                if ((dics != null) && (!"".equals(dics.toString()))) {
                  if (datadic.get(dics) != null)
                    data = datadic.get(dics);
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
              data = Tools.getDbValue(String.valueOf(ms.get("datatype")), mm.get(itKey));
            }
            map.put(key + "." + ms.get("datafield"), data);
          }
        }

      }
      else
      {
        Iterator it = mm.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry itEntry = (Map.Entry)it.next();
          String itKey = (String)itEntry.getKey();
          if (objMap.get(itKey) != null) {
            Map ms = (Map)objMap.get(itKey);

            Map datadic = null;
            if ((ms.get("datadic") != null) && (!"".equals(ms.get("datadic")))) {
              datadic = (Map)ms.get("datadic");
            }
            Object data = "";
            if ((ms.get("formatvalue") != null) && (!"".equals(ms.get("formatvalue"))))
            {
              data = mm.get(itKey);
            } else if (datadic != null)
            {
              String dics = "";
              if (mm.get(itKey) != null) {
                dics = mm.get(itKey).toString();
              }

              if ((dics == null) || ("".equals(dics)) || ((datadic.get(dics) == null) && (dics.toString().indexOf("|") == -1))) {
                if ((dics != null) && (!"".equals(dics.toString()))) {
                  if (datadic.get(dics) != null)
                    data = datadic.get(dics);
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
              data = Tools.getDbValue(String.valueOf(ms.get("datatype")), mm.get(itKey));
            }
            map.put(key + "." + ms.get("datafield"), data);
          }
        }
        list.add(map);
      }
    }

    listn = null;
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
              if (data != null) {
                map.put(propertyName, data.toString());
              }
            }
          }
          if (!map.isEmpty())
            list.add(map);
        }
      }
    }
    catch (Exception e)
    {
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

  public void queryExecuter(DataSourceBean dataSourceBean, DruidPooledConnection conn, String sql, List<JSONObject> l, List<HashMap<String, Object>> list, Map<String, Object> queryCondition, String sortName, String sortType)
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
              data = JRDataFormat.convertData(obj.getInteger("formattype").intValue(), "", rs.getString(obj.getString("datafield")), obj.getString("formatvalue"));
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
            if (!obj.getString("formatvalue").equals("null")) {
              data = JRDataFormat.convertData(obj.getInteger("formattype").intValue(), "", rs.getString(obj.getString("datafield")), obj.getString("formatvalue"));
            } else if ((obj.getString("datadic") != null) && (!obj.getString("datadic").equals("")) && (obj.getString("datadic") != "undefined") && (!obj.getString("datadic").equals("null")))
            {
              try
              {
                JSONObject ob = JSON.parseObject(obj.getString("datadic"));

                String dics = rs.getString(obj.getString("datafield"));
                if ((ob.get(dics) == null) && (dics.indexOf("|") == -1)) {
                  data = rs.getString(obj.getString("datafield"));
                } else {
                  String[] dicArray = dics.split("\\|");
                  for (String dic : dicArray) {
                    if ((dic != null) && (!"".equals(dic)))
                      if (ob.get(dic) == null)
                        data = data + dic + ",";
                      else
                        data = data + ob.get(dic) + ",";
                  }
                }
              }
              catch (Exception localException1)
              {
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
