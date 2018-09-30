package report.java.db.util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Util
{
  public static void main(String[] args)
  {
    long startTime = System.currentTimeMillis();
    new Util().queryExecuter("小组1");
    long endTime = System.currentTimeMillis();
    long times = endTime - startTime;
    System.out.println(times);
  }

  public void queryExecuter(String val) {
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    Connection conn = getConn();
    try {
      ps = conn.prepareStatement("SELECT table_name,column_name FROM user_col_comments");
      rs = ps.executeQuery();
      while (rs.next()) {
        String tableName = rs.getString("table_name");
        String colName = rs.getString("column_name");
        String sql = "SELECT " + colName + " FROM " + tableName + " WHERE '" + val + "' IN " + colName;
        try {
          ps2 = conn.prepareStatement(sql);
          rs2 = ps2.executeQuery();
          if (rs2.next())
            System.out.println("数据：" + val + " 存在于表：" + tableName + " 字段：" + colName + "中。");
        } catch (Exception localException1) {
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeConn(rs, rs2, ps, ps2, conn);
    }
  }

  public Connection getConn() {
    try {
      String driver = "oracle.jdbc.driver.OracleDriver";
      String dataBaseUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
      String userName = "guohanchen";
      String password = "123456";

      Class.forName(driver);
      return DriverManager.getConnection(dataBaseUrl, userName, password);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void closeConn(ResultSet rs, ResultSet rs2, PreparedStatement pstmt, PreparedStatement pstmt2, Connection conn)
  {
    try {
      if (rs != null) {
        rs.close();
      }
      if (rs2 != null) {
        rs2.close();
      }
      if (pstmt != null) {
        pstmt.close();
      }
      if (pstmt2 != null) {
        pstmt2.close();
      }
      if (conn != null)
        conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

/* 
 * Qualified Name:     report.java.db.util.Util
*
 */