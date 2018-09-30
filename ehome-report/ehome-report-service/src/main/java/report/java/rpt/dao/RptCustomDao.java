package report.java.rpt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import report.java.rpt.bean.ColExtendBean;
import report.java.rpt.bean.CusReportBean;
import report.java.rpt.util.DbConfig;

public class RptCustomDao
{
  private Connection con;

  public RptCustomDao()
    throws SQLException
  {
    this.con = DbConfig.getDbConfig();
  }

  public ArrayList<CusReportBean> getTabList() throws SQLException {
    ArrayList resultList = new ArrayList();
    CusReportBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT REP_ID,REP_NAME,REP_TYPE,TEL_NO,BR_NO,WHERE_ID,REP_SCOPE,REP_FLAG,REP_RMKS,TAB_NAME FROM CUS_REPORT");
    try {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new CusReportBean();
        bean.setRep_id(rs.getString("REP_ID"));
        bean.setRep_name(rs.getString("REP_NAME"));
        bean.setRep_type(rs.getString("REP_TYPE"));
        bean.setTel_no(rs.getString("TEL_NO"));
        bean.setBr_no(rs.getString("BR_NO"));
        bean.setWhere_id(rs.getString("WHERE_ID"));
        bean.setRep_scope(rs.getString("REP_SCOPE"));
        bean.setRep_flag(rs.getString("REP_FLAG"));
        bean.setRep_rmks(rs.getString("REP_RMKS"));
        bean.setTab_name(rs.getString("TAB_NAME"));
        resultList.add(bean);
        bean = null;
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      this.con.close();
    }
    return resultList;
  }

  public ArrayList<CusReportBean> getSearchRepList(String rep_name) throws SQLException {
    ArrayList resultList = new ArrayList();
    CusReportBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT REP_ID,REP_NAME,REP_TYPE,TEL_NO,BR_NO,WHERE_ID,REP_SCOPE,REP_FLAG,REP_RMKS,TAB_NAME FROM CUS_REPORT WHERE 1=1 ");
    if ((!"".equals(rep_name)) && (rep_name != null) && (!"null".equals(rep_name)))
      sql.append(" AND REP_NAME LIKE '%" + rep_name + "%'");
    try {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new CusReportBean();
        bean.setRep_id(rs.getString("REP_ID"));
        bean.setRep_name(rs.getString("REP_NAME"));
        bean.setRep_type(rs.getString("REP_TYPE"));
        bean.setTel_no(rs.getString("TEL_NO"));
        bean.setBr_no(rs.getString("BR_NO"));
        bean.setWhere_id(rs.getString("WHERE_ID"));
        bean.setRep_scope(rs.getString("REP_SCOPE"));
        bean.setRep_flag(rs.getString("REP_FLAG"));
        bean.setRep_rmks(rs.getString("REP_RMKS"));
        bean.setTab_name(rs.getString("TAB_NAME"));
        resultList.add(bean);
        bean = null;
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      this.con.close();
    }
    return resultList;
  }

  public void delRepById(String rep_id) throws SQLException
  {
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM CUS_REPORT ");
    if ((!"".equals(rep_id)) && (rep_id != null))
      sql.append(" WHERE REP_ID='" + rep_id + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      ps.execute();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      this.con.close();
    }
  }

  public int insertReport(CusReportBean bean) throws SQLException {
    bean.setRep_id(UUID.randomUUID().toString().replaceAll("-", ""));
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO CUS_REPORT(REP_ID,REP_NAME,REP_TYPE,TEL_NO,BR_NO,WHERE_ID,");
    sql.append("REP_SCOPE,REP_FLAG,REP_RMKS,TAB_NAME) VALUES (?,?,?,?,?,?,?,?,?,?)");
    try {
      ps = this.con.prepareStatement(sql.toString());
      ps.setString(1, bean.getRep_id());
      ps.setString(2, bean.getRep_name());
      ps.setString(3, bean.getRep_type());
      ps.setString(4, bean.getTel_no());
      ps.setString(5, bean.getBr_no());
      ps.setString(6, bean.getRep_id());
      ps.setString(7, bean.getRep_scope());
      ps.setString(8, bean.getRep_flag());
      ps.setString(9, bean.getRep_rmks());
      ps.setString(10, bean.getTab_name());
      result = ps.executeUpdate();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
      this.con.close();
    }
    return result;
  }

  public int insertWhere(CusReportBean bean) throws SQLException {
    int result = 0;
    PreparedStatement ps = null;
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO CUS_CONDITION(WHERE_ID,WHERE_FIELD,WHERE_OPERATE1,");
    sql.append("WHERE_OPERATE2,WHERE_ORDER) VALUES (?,?,?,?,?)");
    try {
      ps = this.con.prepareStatement(sql.toString());
      ps.setString(1, bean.getRep_id());
      ps.setString(2, bean.getWhere_field());
      ps.setString(3, bean.getWhere_operate1());
      ps.setString(4, bean.getWhere_operate2());
      ps.setInt(5, bean.getWhere_order());
      result = ps.executeUpdate();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      ps.close();
      this.con.close();
    }
    return result;
  }

  public CusReportBean findByRepid(String rep_id) throws SQLException
  {
    CusReportBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT REP_ID,REP_NAME,REP_TYPE,TEL_NO,BR_NO,WHERE_ID,REP_SCOPE,REP_FLAG,REP_RMKS,TAB_NAME FROM CUS_REPORT");
    if ((!"".equals(rep_id)) && (rep_id != null))
      sql.append(" WHERE REP_ID='" + rep_id + "'");
    try
    {
      ps = this.con.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new CusReportBean();
        bean.setRep_id(rs.getString("REP_ID"));
        bean.setRep_name(rs.getString("REP_NAME"));
        bean.setRep_type(rs.getString("REP_TYPE"));
        bean.setWhere_id(rs.getString("WHERE_ID"));
        bean.setTab_name(rs.getString("TAB_NAME"));
      }
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      ps.close();
      this.con.close();
    }
    return bean;
  }

  public ArrayList<ColExtendBean> getColExtendList(String where_id) throws SQLException
  {
    ArrayList resultList = new ArrayList();
    ColExtendBean bean = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COL_ID,COL_NAME,COL_DESC,COL_TYPE,COL_DIC,COL_SIZE,FIELD_TYPE,SHOW_FORMAT,SAVE_FORMAT,b.WHERE_OPERATE1,b.WHERE_OPERATE2,b.WHERE_ORDER FROM COL_CONFIG config,");
    sql.append("(SELECT cond.WHERE_FIELD,cond.WHERE_OPERATE1 as WHERE_OPERATE1,cond.WHERE_OPERATE2 as WHERE_OPERATE2,cond.WHERE_ORDER as WHERE_ORDER,rep.TAB_NAME FROM cus_condition cond");
    if ((!"".equals(where_id)) && (where_id != null) && (!"null".equals(where_id)))
      sql.append(" LEFT JOIN CUS_REPORT rep ON cond.WHERE_ID = rep.WHERE_ID WHERE rep.WHERE_ID = '" + where_id + "') b ");
    else {
      sql.append(" LEFT JOIN CUS_REPORT rep ON cond.WHERE_ID = rep.WHERE_ID WHERE rep.WHERE_ID = '') b ");
    }
    sql.append("WHERE config.TAB_ID = b.TAB_NAME AND config.COL_NAME = b.WHERE_FIELD ORDER BY WHERE_ORDER");
    try
    {
      ps = this.con.prepareStatement(sql.toString());

      rs = ps.executeQuery();
      while (rs.next()) {
        bean = new ColExtendBean();
        bean.setCol_id(rs.getString("COL_ID"));
        bean.setCol_name(rs.getString("COL_NAME"));
        bean.setCol_desc(rs.getString("COL_DESC"));
        bean.setCol_type(rs.getString("COL_TYPE"));
        bean.setCol_dic(rs.getString("COL_DIC"));
        bean.setCol_size(rs.getString("COL_SIZE"));
        bean.setField_type(rs.getString("FIELD_TYPE"));
        bean.setShow_format(rs.getString("SHOW_FORMAT"));
        bean.setSave_format(rs.getString("SAVE_FORMAT"));
        bean.setWhere_operate1(rs.getString("WHERE_OPERATE1"));
        bean.setWhere_operate2(rs.getString("WHERE_OPERATE2"));
        resultList.add(bean);
        bean = null;
      }
      rs.close();
    } catch (SQLException sqlext) {
      sqlext.printStackTrace();
    } finally {
      if (ps != null) {
        ps.close();
      }
      this.con.close();
    }
    return resultList;
  }
}

/* 
 * Qualified Name:     report.java.rpt.dao.RptCustomDao
*
 */