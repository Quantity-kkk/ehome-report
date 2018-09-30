package report.java.rpt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import report.java.common.utils.Aes;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;

public class DbConfig
{
  public static Connection getDbConfig()
  {
    Properties prop = new Properties();
    String path = null;
    FileInputStream fis = null;
    try {
      path = RptGlobal.MYDBCONFIG_PATH;
      fis = new FileInputStream(new File(path));
      prop.load(fis);
      String driver = prop.getProperty("driverClassName");
      String dataBaseUrl = prop.getProperty("url");
      String userName = prop.getProperty("username");
      String password = prop.getProperty("password");
      String dbtype = prop.getProperty("dbtype");

      DataSourceBean dataSourceBean = new DataSourceBean();
      dataSourceBean.setDataSourceName("mydbconfig");
      dataSourceBean.setDataBaseUrl(dataBaseUrl);
      dataSourceBean.setDriver(driver);
      dataSourceBean.setUserName(userName);
      dataSourceBean.setPassword(password);
      dataSourceBean.setType(dbtype);
      return DBUtil.getConn(dataSourceBean);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int sqlTest(String sql, Connection conn) throws SQLException {
    int result = 0;

    if ((sql.length() > 0) && (sql.toLowerCase().indexOf("select ") == -1)) {
      sql = Aes.aesDecrypt(sql);
    }
    int sqlIndex = sql.indexOf(" WHERE ");
    if (sqlIndex > 0) {
      sql = sql.substring(0, sqlIndex);
    }

    PreparedStatement ps = null;
    ResultSet rs = null;
    System.out.println("SQL==" + sql);
    try {
      ps = conn.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      result = 1;
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
    }
    return result;
  }
}

/*
 * Qualified Name:     report.java.rpt.util.DbConfig
*
 */