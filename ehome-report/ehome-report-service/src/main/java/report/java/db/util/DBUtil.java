package report.java.db.util;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;
import report.java.common.utils.PropertyUtil;
import report.java.db.bean.DataSourceBean;
import report.java.jrreport.util.JRUtilNew;

public class DBUtil
{
  private static Logger logger = Logger.getLogger(DBUtil.class);

  public static Map<String, DruidDataSource> dataSourceMap = new HashMap();
  private static Map<String, DataSource> dsMap = new HashMap();
  public static Map<String, Map<String, String>> DicMap = new CaseInsensitiveMap();

  public static Boolean isJndi() {
    Boolean isjndi = Boolean.valueOf(false);
    if ((JRUtilNew.baseMap.get("isjndi") != null) && 
      (!((String)JRUtilNew.baseMap.get("isjndi")).equals("0"))) {
      isjndi = Boolean.valueOf(true);
    }
    return isjndi;
  }

  public static Connection getConn(DataSourceBean dataSourceBean)
  {
    Connection conn = null;
    try {
      if (!isJndi().booleanValue())
      {
        String dsName = dataSourceBean.getDataSourceName();
        if ((dataSourceMap.get(dsName) == null) || (((DruidDataSource)dataSourceMap.get(dsName)).isClosed()))
        {
          String driver = dataSourceBean.getDriver();
          String dataBaseUrl = dataSourceBean.getDataBaseUrl();
          String userName = dataSourceBean.getUserName();
          String password = dataSourceBean.getPassword();
          try {
            Class.forName(driver);
          }
          catch (ClassNotFoundException e) {
            e.printStackTrace();
          }

          try
          {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(dataBaseUrl);

            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            dataSource.setInitialSize(Integer.parseInt(((String)JRUtilNew.baseMap.get("initialSize")).trim()));
            dataSource.setMaxActive(Integer.parseInt(((String)JRUtilNew.baseMap.get("maxActive")).trim()));
            dataSource.setMinIdle(0);
            if (JRUtilNew.baseMap.get("maxWait") != null) {
              dataSource.setMaxWait(Integer.parseInt(((String)JRUtilNew.baseMap.get("maxWait")).trim()));
            }
            if (dataSourceBean.getType().equals("sqlserver"))
              dataSource.setValidationQuery("SELECT 1");
            else {
              dataSource.setValidationQuery("SELECT 'x' from dual");
            }
            dataSource.setPoolPreparedStatements(Boolean.parseBoolean((String)JRUtilNew.baseMap.get("poolPreparedStatements")));
            dataSource.setMaxOpenPreparedStatements(Integer.parseInt((String)JRUtilNew.baseMap.get("maxOpenPreparedStatements")));

            if (!"mydbconfig".equals(dsName)) {
              dataSource.setDefaultReadOnly(Boolean.valueOf(true));
            }
            dataSource.setRemoveAbandoned(true);
            dataSource.setRemoveAbandonedTimeout(1800);
            dataSource.setFilters("stat,log4j");
            conn = dataSource.getConnection();
            dataSourceMap.put(dsName, dataSource);
          }
          catch (SQLException e) {
            e.printStackTrace();
          }
        } else {
          conn = ((DruidDataSource)dataSourceMap.get(dsName)).getConnection();
        }
      }
      else if ((conn == null) || (conn.isClosed()))
      {
        try
        {
          conn = getConnectionUseJNDI();
        } catch (NamingException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  public static Connection getConnectionUseJNDI() throws NamingException, SQLException {
    Connection conn = null;

    DataSource dataSource = (DataSource)dsMap.get("jndi");
    try {
      if (dataSource == null) {
        if (((String)JRUtilNew.baseMap.get("isjndi")).equals("2")) {
          Properties properties = PropertyUtil.getProperties("druid.properties");
          if (properties.getProperty("decrypt").equals("true"))
            try {
              properties.put("password", ConfigTools.decrypt(properties.getProperty("publicKey"), properties.getProperty("password")));
            } catch (Exception e) {
              e.printStackTrace();
            }
          try
          {
            dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
            dsMap.put("jndi", dataSource);
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          String jndiname = (String)JRUtilNew.baseMap.get("jndiname");
          Context ctx = new InitialContext();
          dataSource = (DataSource)ctx.lookup(jndiname);
          dsMap.put("jndi", dataSource);
        }

      }

      conn = dataSource.getConnection();
    } catch (NamingException e) {
      e.printStackTrace();
    }

    return conn;
  }

  public static void closeRsPs(ResultSet rs, PreparedStatement pstmt)
  {
    try
    {
      if (rs != null) {
        rs.close();
      }

      if (pstmt != null) {
        pstmt.close();
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void closeConn(Connection conn)
  {
    try {
      if ((conn != null) && (!conn.isClosed())) {
        conn.close();
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static List<Map<String, Object>> getResultToList(DataSourceBean dataSourceBean, Boolean closeConn, String sql, Object[] params)
  {
    PreparedStatement ps = null;
    Connection conn = getConn(dataSourceBean);
    ResultSet rs = null;
    List rows = new ArrayList();
    if ((sql == null) || ("".equals(sql)))
      return new ArrayList();
    try
    {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
      String realSql = printRealSql(sql, params);
      long startTime = System.currentTimeMillis();
      ps = conn.prepareStatement(realSql);

      rs = ps.executeQuery();
      Long endTime = Long.valueOf(System.currentTimeMillis());
      logger.info("用时：" + (endTime.longValue() - startTime));
      if (rs != null) {
        ResultSetMetaData rsm = rs.getMetaData();
        int count = rsm.getColumnCount();
        Map record = null;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        if (count > 0)
          while (rs.next()) {
            record = new CaseInsensitiveMap();
            for (int j = 0; j < count; j++) {
              Object obj = rs.getObject(j + 1);
              if (((obj instanceof Double)) || ((obj instanceof Float))) {
                obj = nf.format(obj);
              }
              String columnName = rsm.getColumnName(j + 1);
              record.put(columnName, obj == null ? "" : obj);
            }
            rows.add(record);
          }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    } finally {
      if (closeConn.booleanValue())
        release(new Object[] { 
          rs, ps, conn });
      else {
        release(new Object[] { 
          rs, ps });
      }
    }

    return rows;
  }

  public static List<Map<String, Object>> getResultToList(Connection conn, Boolean closeConn, String sql, Object[] params)
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    List rows = new ArrayList();
    if ((sql == null) || ("".equals(sql)))
      return new ArrayList();
    try
    {
      sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
      String realSql = printRealSql(sql, params);
      long startTime = System.currentTimeMillis();
      ps = conn.prepareStatement(realSql);

      rs = ps.executeQuery();
      Long endTime = Long.valueOf(System.currentTimeMillis());
      logger.info("用时：" + (endTime.longValue() - startTime));
      if (rs != null) {
        ResultSetMetaData rsm = rs.getMetaData();
        int count = rsm.getColumnCount();
        Map record = null;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        if (count > 0)
          while (rs.next()) {
            record = new CaseInsensitiveMap();
            for (int j = 0; j < count; j++) {
              Object obj = rs.getObject(j + 1);
              if (((obj instanceof Double)) || ((obj instanceof Float))) {
                obj = nf.format(obj);
              }
              String columnName = rsm.getColumnName(j + 1);
              record.put(columnName, obj == null ? "" : obj);
            }
            rows.add(record);
          }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    } finally {
      if (closeConn.booleanValue())
        release(new Object[] { 
          rs, ps, conn });
      else {
        release(new Object[] { 
          rs, ps });
      }
    }

    return rows;
  }

  public static void release(Object[] obj)
  {
    if (obj != null) {
      Object[] arrayOfObject = obj; int j = obj.length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
        try {
          if ((o instanceof ResultSet))
            ((ResultSet)o).close();
          else if ((o instanceof Statement))
            ((Statement)o).close();
          else if ((o instanceof Connection)) {
            if (!((Connection)o).isClosed())
              ((Connection)o).close();
          }
          else if ((o instanceof PreparedStatement))
            ((PreparedStatement)o).close();
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  public static String printRealSql(String sql, Object[] params)
  {
    if ((params == null) || (params.length == 0)) {
      logger.info("The SQL is------------>\n" + sql);
      return sql;
    }

    if (!match(sql, params)) {
      logger.info("SQL 语句中的占位符与参数个数不匹配。SQL：" + sql);
      return null;
    }

    int cols = params.length;
    Object[] values = new Object[cols];
    System.arraycopy(params, 0, values, 0, cols);
    String[] arrSql = sql.split("\\?");
    for (int i = 0; i < cols; i++) {
      Object value = values[i];
      if ((arrSql.length > i + 1) && ((arrSql[i].endsWith("'%")) || (arrSql[(i + 1)].startsWith("%'")))) {
        values[i] = value;
      }
      else if ((value instanceof Date)) {
        values[i] = ("'" + value + "'");
      } else if ((value instanceof String)) {
        String viStr = values[i].toString().toLowerCase();
        if ((viStr.length() > 0) && (viStr.startsWith("【")) && (viStr.endsWith("】")))
          values[i] = value.toString().substring(1, value.toString().length() - 1);
        else
          values[i] = ("'" + value + "'");
      }
      else if ((value instanceof Boolean)) {
        values[i] = Integer.valueOf(((Boolean)value).booleanValue() ? 1 : 0);
      }

    }

    String statement = String.format(sql.replace("%", "％").replaceAll("\\?", "%s"), values).replace("％", "%");
    logger.info("The SQL is------------>\n" + statement);
    return statement;
  }

  private static boolean match(String sql, Object[] params)
  {
    if ((params == null) || (params.length == 0)) {
      return true;
    }
    Matcher m = Pattern.compile("(\\?)").matcher(sql);
    int count = 0;
    while (m.find()) {
      count++;
    }

    return count == params.length;
  }

  public static void main(String[] args) {
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDriver("com.mysql.jdbc.Driver");
    dataSourceBean.setDataBaseUrl("jdbc:mysql://localhost:3306/test");
    dataSourceBean.setUserName("root");
    dataSourceBean.setPassword("454113");

    getConn(dataSourceBean);
  }
}

/* 
 * Qualified Name:     report.java.db.util.DBUtil
*
 */