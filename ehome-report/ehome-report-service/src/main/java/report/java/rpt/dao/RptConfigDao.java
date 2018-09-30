package report.java.rpt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import report.java.rpt.bean.ColConfigBean;
import report.java.rpt.bean.TabConfigBean;
import report.java.rpt.util.DbConfig;

public class RptConfigDao
{
  private Connection con;

  public RptConfigDao()
  {
    this.con = DbConfig.getDbConfig();
  }

  public ArrayList<TabConfigBean> getTabList() throws SQLException
  {
    ArrayList resultList = new ArrayList();
    TabConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT TAB_ID,TAB_NAME,TAB_TYPE,TAB_DESC,TAB_DSNAME FROM TAB_CONFIG");
    try {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new TabConfigBean();
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setTab_name(rs.getString("TAB_NAME"));
        bean.setTab_type(rs.getString("TAB_TYPE"));
        bean.setTab_desc(rs.getString("TAB_DESC"));
        bean.setTab_dsname(rs.getString("TAB_DSNAME"));
        resultList.add(bean);
        bean = null;
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return resultList;
  }

  public ArrayList<TabConfigBean> getSearchTabList(String tab_name, String tab_desc)
    throws SQLException
  {
    ArrayList resultList = new ArrayList();
    TabConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT TAB_ID,TAB_NAME,TAB_TYPE,TAB_DESC FROM TAB_CONFIG WHERE 1=1");

    if ((!"".equals(tab_name)) && (tab_name != null) && 
      (!"null".equals(tab_name))) {
      sql.append(" AND TAB_NAME LIKE '%" + tab_name + "%'");
    }
    if ((!"".equals(tab_desc)) && (tab_desc != null) && 
      (!"null".equals(tab_desc)))
      sql.append(" AND TAB_DESC LIKE '%" + tab_desc + "%'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new TabConfigBean();
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setTab_name(rs.getString("TAB_NAME"));
        bean.setTab_type(rs.getString("TAB_TYPE"));
        bean.setTab_desc(rs.getString("TAB_DESC"));
        resultList.add(bean);
        bean = null;
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return resultList;
  }

  public ArrayList<ColConfigBean> getColList(String tab_id)
    throws SQLException
  {
    ArrayList resultList = new ArrayList();
    ColConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COL_ID,TAB_ID,COL_NAME,COL_DESC,COL_TYPE,COL_DIC,COL_FLAG,COL_SORTFLAG,COL_OPERATE,COL_ORDER,COL_GROUPFLAG,COL_SIZE,FIELD_TYPE,SHOW_FORMAT,SAVE_FORMAT FROM COL_CONFIG");
    if ((!"".equals(tab_id)) && (tab_id != null)) {
      sql.append(" WHERE TAB_ID='" + tab_id + "'");
    }
    sql.append(" ORDER BY COL_ORDER");
    try {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new ColConfigBean();
        bean.setCol_id(rs.getString("COL_ID"));
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setCol_name(rs.getString("COL_NAME"));
        bean.setCol_desc(rs.getString("COL_DESC"));
        bean.setCol_type(rs.getString("COL_TYPE"));
        bean.setCol_operate(rs.getString("COL_OPERATE"));
        bean.setCol_flag(rs.getString("COL_FLAG"));
        bean.setCol_sortflag(rs.getString("COL_SORTFLAG"));
        bean.setField_type(rs.getString("FIELD_TYPE") == null ? "1" : rs.getString("FIELD_TYPE"));
        bean.setShow_format(rs.getString("SHOW_FORMAT"));
        bean.setSave_format(rs.getString("SAVE_FORMAT"));
        resultList.add(bean);
        bean = null;
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return resultList;
  }

  public int insertTab(TabConfigBean bean) throws SQLException
  {
    bean.setTab_id(UUID.randomUUID().toString().replaceAll("-", ""));
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO TAB_CONFIG(TAB_ID,TAB_NAME,TAB_TYPE,TAB_DESC,TAB_DSNAME)");
    sql.append("VALUES (?,?,?,?,?)");
    try {
      ps = this.con.prepareStatement(sql.toString());
      ps.setString(1, bean.getTab_id());
      ps.setString(2, bean.getTab_name());
      ps.setString(3, bean.getTab_type());
      ps.setString(4, bean.getTab_desc());
      ps.setString(5, bean.getTab_dsname());
      result = ps.executeUpdate();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return result;
  }

  public int insertCol(ColConfigBean bean) throws SQLException
  {
    bean.setCol_id(UUID.randomUUID().toString().replaceAll("-", ""));
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO COL_CONFIG(COL_ID,TAB_ID,COL_NAME,COL_DESC,COL_TYPE,COL_DIC,COL_FLAG,COL_SORTFLAG,COL_OPERATE,COL_ORDER,COL_GROUPFLAG,COL_SIZE,FIELD_TYPE,SHOW_FORMAT,SAVE_FORMAT)");
    sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    try {
      ps = this.con.prepareStatement(sql.toString());
      ps.setString(1, bean.getCol_id());
      ps.setString(2, bean.getTab_id());
      ps.setString(3, bean.getCol_name());
      ps.setString(4, bean.getCol_desc());
      ps.setString(5, bean.getCol_type());
      ps.setString(6, bean.getCol_dic());
      ps.setString(7, bean.getCol_flag());
      ps.setString(8, bean.getCol_sortflag());
      ps.setString(9, bean.getCol_operate());
      ps.setInt(10, bean.getCol_order());
      ps.setString(11, bean.getCol_groupflag());
      ps.setString(12, bean.getCol_size());
      ps.setString(13, bean.getField_type());
      ps.setString(14, bean.getShow_format());
      ps.setString(15, bean.getSave_format());
      result = ps.executeUpdate();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return result;
  }

  public int updateTab(TabConfigBean bean) throws SQLException
  {
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE TAB_CONFIG SET TAB_NAME=?,TAB_TYPE=?,TAB_DESC=?,TAB_DSNAME=? WHERE TAB_ID=?");
    try {
      ps = this.con.prepareStatement(sql.toString());
      ps.setString(1, bean.getTab_name());
      ps.setString(2, bean.getTab_type());
      ps.setString(3, bean.getTab_desc());
      ps.setString(4, bean.getTab_dsname());
      ps.setString(5, bean.getTab_id());
      result = ps.executeUpdate();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return result;
  }

  public int updateCol(ColConfigBean bean) throws SQLException
  {
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE COL_CONFIG SET TAB_ID=?,COL_NAME=?,COL_DESC=?,COL_TYPE=?,COL_DIC=?,COL_FLAG=?,COL_SORTFLAG=?,COL_OPERATE=?,COL_ORDER=?,COL_GROUPFLAG=?,COL_SIZE=?,FIELD_TYPE=?,SHOW_FORMAT=?,SAVE_FORMAT=? WHERE COL_ID=?");
    try {
      ps = this.con.prepareStatement(sql.toString());
      ps.setString(1, bean.getTab_id());
      ps.setString(2, bean.getCol_name());
      ps.setString(3, bean.getCol_desc());
      ps.setString(4, bean.getCol_type());
      ps.setString(5, bean.getCol_dic());
      ps.setString(6, bean.getCol_flag());
      ps.setString(7, bean.getCol_sortflag());
      ps.setString(8, bean.getCol_operate());
      ps.setInt(9, bean.getCol_order());
      ps.setString(10, bean.getCol_groupflag());
      ps.setString(11, bean.getCol_size());
      ps.setString(12, bean.getField_type());
      ps.setString(13, bean.getShow_format());
      ps.setString(14, bean.getSave_format());
      ps.setString(15, bean.getCol_id());
      result = ps.executeUpdate();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return result;
  }

  public TabConfigBean getTabById(String tab_id) throws SQLException
  {
    TabConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT TAB_ID,TAB_NAME,TAB_TYPE,TAB_DESC,TAB_DSNAME FROM TAB_CONFIG");
    if ((!"".equals(tab_id)) && (tab_id != null))
      sql.append(" WHERE TAB_ID='" + tab_id + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new TabConfigBean();
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setTab_name(rs.getString("TAB_NAME"));
        bean.setTab_type(rs.getString("TAB_TYPE"));
        bean.setTab_desc(rs.getString("TAB_DESC"));
        bean.setTab_dsname(rs.getString("TAB_DSNAME"));
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return bean;
  }

  public ColConfigBean getColById(String col_id) throws SQLException
  {
    ColConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COL_ID,TAB_ID,COL_NAME,COL_DESC,COL_TYPE,COL_DIC,COL_FLAG,COL_SORTFLAG,COL_OPERATE,COL_ORDER,COL_GROUPFLAG,COL_SIZE,FIELD_TYPE,SHOW_FORMAT,SAVE_FORMAT FROM COL_CONFIG");
    if ((!"".equals(col_id)) && (col_id != null))
      sql.append(" WHERE COL_ID='" + col_id + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new ColConfigBean();
        bean.setCol_id(rs.getString("COL_ID"));
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setCol_name(rs.getString("COL_NAME"));
        bean.setCol_desc(rs.getString("COL_DESC"));
        bean.setCol_type(rs.getString("COL_TYPE"));
        bean.setCol_dic(rs.getString("COL_DIC"));
        bean.setCol_flag(rs.getString("COL_FLAG"));
        bean.setCol_sortflag(rs.getString("COL_SORTFLAG"));
        bean.setCol_operate(rs.getString("COL_OPERATE"));
        if ((!"".equals(rs.getString("COL_ORDER"))) && 
          (rs.getString("COL_ORDER") != null) && 
          (!"null".equals(rs.getString("COL_ORDER"))))
          bean.setCol_order(Integer.parseInt(rs
            .getString("COL_ORDER")));
        bean.setCol_groupflag(rs.getString("COL_GROUPFLAG"));
        bean.setCol_size(rs.getString("COL_SIZE"));
        bean.setField_type(rs.getString("FIELD_TYPE"));
        bean.setShow_format(rs.getString("SHOW_FORMAT"));
        bean.setSave_format(rs.getString("SAVE_FORMAT"));
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return bean;
  }

  public int delTabById(String tab_id) throws SQLException
  {
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM TAB_CONFIG ");
    if ((!"".equals(tab_id)) && (tab_id != null))
      sql.append(" WHERE TAB_ID='" + tab_id + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      ps.execute();
      if (ps.getUpdateCount() > 0)
        result = 1;
    }
    catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return result;
  }

  public void delColById(String col_id) throws SQLException
  {
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM COL_CONFIG ");
    if ((!"".equals(col_id)) && (col_id != null))
      sql.append(" WHERE COL_ID='" + col_id + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      ps.execute();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null)
        this.con.close();
    }
  }

  public ColConfigBean getColByTabidAndColname(String tab_id, String col_name)
    throws SQLException
  {
    ColConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COL_ID,TAB_ID,COL_NAME,COL_DESC,COL_TYPE,COL_DIC,COL_FLAG,COL_SORTFLAG,COL_OPERATE,COL_ORDER,COL_GROUPFLAG,COL_SIZE,FIELD_TYPE,SHOW_FORMAT,SAVE_FORMAT FROM COL_CONFIG WHERE 1=1");
    if ((!"".equals(tab_id)) && (tab_id != null)) {
      sql.append(" AND TAB_ID='" + tab_id + "'");
    }
    if ((!"".equals(col_name)) && (col_name != null))
      sql.append(" AND COL_NAME='" + col_name + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new ColConfigBean();
        bean.setCol_id(rs.getString("COL_ID"));
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setCol_name(rs.getString("COL_NAME"));
        bean.setCol_desc(rs.getString("COL_DESC"));
        bean.setCol_type(rs.getString("COL_TYPE"));
        bean.setCol_dic(rs.getString("COL_DIC"));
        bean.setCol_flag(rs.getString("COL_FLAG"));
        bean.setCol_sortflag(rs.getString("COL_SORTFLAG"));
        bean.setCol_operate(rs.getString("COL_OPERATE"));
        if ((!"".equals(rs.getString("COL_ORDER"))) && 
          (rs.getString("COL_ORDER") != null) && 
          (!"null".equals(rs.getString("COL_ORDER"))))
          bean.setCol_order(Integer.parseInt(rs
            .getString("COL_ORDER")));
        bean.setCol_groupflag(rs.getString("COL_GROUPFLAG"));
        bean.setCol_size(rs.getString("COL_SIZE"));
        bean.setField_type(rs.getString("FIELD_TYPE"));
        bean.setShow_format(rs.getString("SHOW_FORMAT"));
        bean.setSave_format(rs.getString("SAVE_FORMAT"));
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return bean;
  }

  public TabConfigBean getTabByName(String tab_name) throws SQLException
  {
    TabConfigBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT TAB_ID,TAB_NAME,TAB_TYPE,TAB_DESC FROM TAB_CONFIG");
    if ((!"".equals(tab_name)) && (tab_name != null))
      sql.append(" WHERE TAB_NAME='" + tab_name + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new TabConfigBean();
        bean.setTab_id(rs.getString("TAB_ID"));
        bean.setTab_name(rs.getString("TAB_NAME"));
        bean.setTab_type(rs.getString("TAB_TYPE"));
        bean.setTab_desc(rs.getString("TAB_DESC"));
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      if (this.con != null) {
        this.con.close();
      }
    }
    return bean;
  }
}

/* 
 * Qualified Name:     report.java.rpt.dao.RptConfigDao
*
 */