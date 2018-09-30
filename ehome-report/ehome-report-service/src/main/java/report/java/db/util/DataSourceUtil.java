package report.java.db.util;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import report.java.db.bean.DataSourceBean;
import report.java.db.bean.TableBean;
import report.java.db.bean.TableColumnBean;

public class DataSourceUtil
{
  public DataSourceBean getDataSource(DataSourceBean dataSourceBean)
    throws SQLException
  {
    Connection conn = DBUtil.getConn(dataSourceBean);
    if (conn != null) {
      String sql = "select table_name,comments from user_tab_comments";
      PreparedStatement ps = null;
      ResultSet rs = null;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      TableBean tableBean = null;
      List tableList = new ArrayList();
      while (rs.next()) {
        tableBean = new TableBean();
        tableBean.setTableName(rs.getString("table_name"));
        tableBean.setTableComments(rs.getString("comments"));
        tableList.add(tableBean);
      }
      DBUtil.closeRsPs(rs, ps);
      DBUtil.closeConn(conn);
      dataSourceBean.setTableList(tableList);
    }
    return dataSourceBean;
  }

  public TableBean getTableColumnList(DataSourceBean dataSourceBean, TableBean tableBean) throws SQLException {
    Connection conn = DBUtil.getConn(dataSourceBean);
    if (conn != null) {
      String sql = "select c.COMMENTS,t.COLUMN_NAME,t.DATA_TYPE from user_tab_columns t,user_col_comments c where t.table_name = c.table_name and t.column_name = c.column_name and t.table_name = '" + tableBean.getTableName() + "'";
      PreparedStatement ps = null;
      ResultSet rs = null;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      TableColumnBean tableColumnBean = null;
      List tableColumnList = new ArrayList();
      while (rs.next()) {
        tableColumnBean = new TableColumnBean();
        tableColumnBean.setColumnName(rs.getString("COLUMN_NAME"));
        tableColumnBean.setColumnComments(rs.getString("COMMENTS"));
        tableColumnBean.setColumnType(rs.getString("DATA_TYPE"));
        tableColumnList.add(tableColumnBean);
      }
      DBUtil.closeRsPs(rs, ps);
      DBUtil.closeConn(conn);

      tableBean.setTableColumnList(tableColumnList);
    }
    return tableBean;
  }

  public List<TableColumnBean> executeSQL(DataSourceBean dataSourceBean, String sqlStr)
  {
    if (dataSourceBean == null)
      return null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List tableColumnList = new ArrayList();
    try {
      Connection conn = DBUtil.getConn(dataSourceBean);
      if (conn != null)
      {
        sqlStr = sqlStr.replaceAll("  ", " ");
        sqlStr = sqlStr.replaceAll("\\?", "''");
        if (!sqlStr.equals("")) {
          sqlStr = sqlStr.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
        }
        System.out.println(sqlStr);
        ps = conn.prepareStatement(sqlStr);
        rs = ps.executeQuery();
        TableColumnBean tableColumnBean = null;
        ResultSetMetaData rsm = rs.getMetaData();

        for (int i = 1; i <= rsm.getColumnCount(); i++) {
          tableColumnBean = new TableColumnBean();
          tableColumnBean.setColumnName(rsm.getColumnName(i));
          tableColumnBean.setColumnType(rsm.getColumnTypeName(i));
          tableColumnList.add(tableColumnBean);
        }

        DBUtil.closeRsPs(rs, ps);
        DBUtil.closeConn(conn);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return null;
    }
    return tableColumnList;
  }

  public List<TableBean> getTableInfo(DataSourceBean dataSourceBean)
  {
    if (dataSourceBean == null)
      return null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection conn = null;
    List tableList = new ArrayList();
    try {
      conn = DBUtil.getConn(dataSourceBean);
      if (conn != null) {
        String dbtype = dataSourceBean.getType().toLowerCase();
        String sqlStr = null;
        if (dbtype.equals("mysql")) {
          if ((dataSourceBean.getDataBaseUrl().length() > 0) && (dataSourceBean.getDataBaseUrl().indexOf("://") != -1)) {
            String table_schema = "";
            if ((dataSourceBean.getDataBaseUrl().split("://").length == 2) && (dataSourceBean.getDataBaseUrl().split("://")[1].split("/").length == 2)) {
              table_schema = dataSourceBean.getDataBaseUrl().split("://")[1].split("/")[1];
            }
            if (table_schema.indexOf("?") != -1) {
              table_schema = table_schema.split("?")[0];
            }
            if (table_schema.length() > 0)
              sqlStr = "select table_name tabName,table_comment tabComment from information_schema.tables where table_schema='" + table_schema + "' and table_type='base table'";
          }
        }
        else if (dbtype.equals("oracle"))
          sqlStr = "select t.table_name tabName,f.comments tabComment from user_tables t inner join user_tab_comments f on t.table_name = f.table_name";
        else if (dbtype.equals("sqlserver"))
          sqlStr = "select name tabName,name tabComment from sys.tables";
        else if (dbtype.equals("db2")) {
          sqlStr = "select tabname tabName,remarks tabComment from syscat.tables where tabschema = current schema";
        }
        if (sqlStr != null) {
          ps = conn.prepareStatement(sqlStr);
          rs = ps.executeQuery();
          TableBean tableBean = null;
          while (rs.next()) {
            tableBean = new TableBean();
            tableBean.setTableName(rs.getString("tabName"));
            tableBean.setTableComments(rs.getString("tabComment"));
            tableList.add(tableBean);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.closeRsPs(rs, ps);
      DBUtil.closeConn(conn);
    }
    return tableList;
  }

  public List<TableColumnBean> getColumnsInfo(DataSourceBean dataSourceBean, String tableName)
  {
    if ((dataSourceBean == null) || (tableName == null))
      return null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection conn = null;
    List tableColumnList = new ArrayList();
    try {
      conn = DBUtil.getConn(dataSourceBean);
      if (conn != null) {
        String dbtype = dataSourceBean.getType().toLowerCase();
        String sqlStr = null;
        if (dbtype.equals("mysql")) {
          if ((dataSourceBean.getDataBaseUrl().length() > 0) && (dataSourceBean.getDataBaseUrl().indexOf("://") != -1)) {
            String table_schema = "";
            if ((dataSourceBean.getDataBaseUrl().split("://").length == 2) && (dataSourceBean.getDataBaseUrl().split("://")[1].split("/").length == 2)) {
              table_schema = dataSourceBean.getDataBaseUrl().split("://")[1].split("/")[1];
            }
            if (table_schema.indexOf("?") != -1) {
              table_schema = table_schema.split("?")[0];
            }
            if (table_schema.length() > 0)
              sqlStr = "select column_name colName,data_type colType,column_comment colComment from information_schema.columns where table_schema='" + table_schema + "' and table_name='" + tableName + "'";
          }
        }
        else if (dbtype.equals("oracle"))
          sqlStr = "select  a.column_name colName,a.data_type colType,t.comments colComment from all_tab_columns a left join user_col_comments t on a.column_name=t.column_name where a.table_name=upper('" + tableName + "')";
        else if (dbtype.equals("sqlserver"))
          sqlStr = "SELECT col.name AS colName,ISNULL(ep.[value], '') AS colComments ,      t.name AS colType FROM    dbo.syscolumns col        LEFT  JOIN dbo.systypes t ON col.xtype = t.xusertype        inner JOIN dbo.sysobjects obj ON col.id = obj.id AND obj.xtype = 'U' AND obj.status >= 0   LEFT  JOIN sys.extended_properties ep ON col.id = ep.major_id AND col.colid = ep.minor_id AND ep.name = 'MS_Description' WHERE   obj.name = '" + tableName + "'";
        else if (dbtype.equals("db2")) {
          sqlStr = "select colname colName,typename colType,remarks colComment from syscat.columns where tabname=upper('" + tableName + "')";
        }
        if (sqlStr != null) {
          ps = conn.prepareStatement(sqlStr);
          rs = ps.executeQuery();
          TableColumnBean tableColumnBean = null;
          while (rs.next()) {
            tableColumnBean = new TableColumnBean();
            tableColumnBean.setColumnName(rs.getString("colName"));
            tableColumnBean.setColumnType(rs.getString("colType"));
            tableColumnBean.setColumnComments(rs.getString("colComment"));
            tableColumnList.add(tableColumnBean);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.closeRsPs(rs, ps);
      DBUtil.closeConn(conn);
    }
    return tableColumnList;
  }

  public List<TableColumnBean> getObjectProperty(String objectName, String methodName)
  {
    List tableColumnList = new ArrayList();
    try
    {
      Class object = Class.forName(objectName);
      try {
        Method m = object.getMethod(methodName, new Class[0]);

        Type returnType = m.getGenericReturnType();
        if ((returnType instanceof ParameterizedType)) {
          Type[] types = ((ParameterizedType)returnType).getActualTypeArguments();
          for (Type type : types) {
            if ((type != null) && (type.toString().indexOf(" ") != -1))
              tableColumnList = getProperties(type.toString().split(" ")[1]);
          }
        }
      }
      catch (SecurityException e)
      {
        e.printStackTrace();
      }
      catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return tableColumnList;
  }

  public List<TableColumnBean> getProperties(String objectStr)
  {
    List tableColumnList = new ArrayList();
    try
    {
      Class c = Class.forName(objectStr);
      Field[] fields = c.getDeclaredFields();
      TableColumnBean tableColumnBean = null;
      for (Field field : fields) {
        tableColumnBean = new TableColumnBean();
        tableColumnBean.setColumnName(field.getName());
        tableColumnBean.setColumnType(handPropertyType(field.getGenericType().toString()));
        tableColumnList.add(tableColumnBean);
        System.out.println(field.getGenericType() + "  " + field.getName());
      }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return tableColumnList;
  }

  private String handPropertyType(String type)
  {
    String tempType = "";
    if (type.equals("class java.lang.String"))
      tempType = "String";
    else if (type.equals("class java.lang.Integer"))
      tempType = "Integer";
    else if (type.equals("class java.lang.Double"))
      tempType = "Double";
    else if (type.equals("class java.lang.Boolean"))
      tempType = "Boolean";
    else if (type.equals("class java.util.Date"))
      tempType = "Date";
    else if (type.equals("class java.lang.Short"))
      tempType = "Short";
    else
      tempType = type;
    return tempType;
  }
}

/*
 * Qualified Name:     report.java.db.util.DataSourceUtil
*
 */