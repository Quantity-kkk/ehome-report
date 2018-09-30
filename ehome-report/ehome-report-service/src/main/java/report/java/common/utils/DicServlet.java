package report.java.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;

public class DicServlet extends HttpServlet
{
  private static Logger logger = Logger.getLogger(DicServlet.class);

  public void init() throws ServletException
  {
    Properties prop = new Properties();
    String realPath = getServletConfig().getServletContext().getRealPath("/") + "/WEB-INF/";
    try {
      prop.load(new FileInputStream(realPath + "base.properties"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if ((prop.get("dicdb") != null) && (prop.get("dicdb").equals("1"))) {
      logger.info("数据字典加载中");
      DataSourceBean dataSourceBean = new DataSourceBean();
      Connection conn = null;
      if ((prop.get("dicdatasource") != null) && (!prop.get("dicdb").equals(""))) {
        if (prop.get("dicdatasource").toString().equals("")) {
          String jndiname = prop.get("jndiname").toString();
          Context ctx = null;
          try {
            ctx = new InitialContext();
          } catch (NamingException e) {
            e.printStackTrace();
          }
          try {
            DataSource dataSource = (DataSource)ctx.lookup(jndiname);
            try {
              conn = dataSource.getConnection();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          } catch (NamingException e) {
            e.printStackTrace();
          }
        } else {
          dataSourceBean.setDataSourceName(prop.get("dicdatasource").toString());
          String dsPath = realPath + "db-config.xml";
          dataSourceBean = XmlUtil.selectXML(dataSourceBean, dsPath);
          String driver = dataSourceBean.getDriver();
          String dataBaseUrl = dataSourceBean.getDataBaseUrl();
          String userName = dataSourceBean.getUserName();
          String password = dataSourceBean.getPassword();
          try {
            Class.forName(driver);
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
          try {
            conn = DriverManager.getConnection(dataBaseUrl, userName, password);
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        }
      }
      String sql = prop.get("dicsql").toString();
      if ((sql != null) && (!sql.equals(""))) {
        if (sql.indexOf(";") != -1) {
          String[] asql = sql.split(";");
          for (int i = 0; i < asql.length; i++)
            if (i + 1 == asql.length)
              InitDic(conn, asql[i], Boolean.valueOf(true));
            else
              InitDic(conn, asql[i], Boolean.valueOf(false));
        }
        else
        {
          InitDic(conn, sql, Boolean.valueOf(true));
        }
      }

      logger.info("数据字典加载完毕");
    }
  }

  private void InitDic(Connection conn, String sql, Boolean isClose) {
    List list = DBUtil.getResultToList(conn, isClose, sql, new Object[0]);
    for (int i = 0; i < list.size(); i++) {
      Map m = new HashMap();
      Set<String> ss = ((Map)list.get(i)).keySet();
      for (String set : ss) {
        m.put(set.toUpperCase(), ((Map)list.get(i)).get(set));
      }
      Map map = new HashMap();
      if (!"".equals(m.get("OPT_CODE").toString())) {
        map.put(m.get("OPT_CODE").toString(), m.get("OPT_NAME").toString());
        if ((DBUtil.DicMap.get(m.get("KEY_NAME")) == null) && (!"".equals(m.get("KEY_NAME").toString()))) {
          DBUtil.DicMap.put(m.get("KEY_NAME").toString(), map);
        } else {
          map = (Map)DBUtil.DicMap.get(m.get("KEY_NAME").toString());
          map.put(m.get("OPT_CODE").toString(), m.get("OPT_NAME").toString());
          DBUtil.DicMap.put(m.get("KEY_NAME").toString(), map);
        }
      }
    }
  }
}

/* 
 * Qualified Name:     report.java.common.utils.DicServlet
*
 */