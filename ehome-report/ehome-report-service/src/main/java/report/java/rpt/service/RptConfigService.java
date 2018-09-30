package report.java.rpt.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import report.java.rpt.bean.ColConfigBean;
import report.java.rpt.bean.TabConfigBean;
import report.java.rpt.dao.RptConfigDao;

public class RptConfigService
{
  public Connection conn = null;

  public RptConfigService() throws SQLException
  {
  }

  public ArrayList<TabConfigBean> getTabList() throws SQLException {
    ArrayList resultList = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      resultList = rptConfigDao.getTabList();
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return resultList;
  }

  public ArrayList<TabConfigBean> getSearchTabList(String tab_name, String tab_desc) throws SQLException {
    ArrayList resultList = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      resultList = rptConfigDao.getSearchTabList(tab_name, tab_desc);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return resultList;
  }

  public ArrayList<ColConfigBean> getColList(String tab_id) throws SQLException {
    ArrayList resultList = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      resultList = rptConfigDao.getColList(tab_id);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return resultList;
  }

  public int insertTab(TabConfigBean bean) throws SQLException
  {
    int result = 0;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      result = rptConfigDao.insertTab(bean);
    }
    catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return result;
  }

  public int insertCol(ColConfigBean bean) throws SQLException
  {
    int result = 0;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      result = rptConfigDao.insertCol(bean);
    }
    catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return result;
  }

  public int updateTab(TabConfigBean bean) throws SQLException
  {
    int result = 0;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      result = rptConfigDao.updateTab(bean);
    }
    catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return result;
  }

  public int updateCol(ColConfigBean bean) throws SQLException
  {
    int result = 0;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      result = rptConfigDao.updateCol(bean);
    }
    catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return result;
  }

  public TabConfigBean getTabById(String tab_id) throws SQLException {
    TabConfigBean tabConfigBean = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      tabConfigBean = rptConfigDao.getTabById(tab_id);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return tabConfigBean;
  }

  public ColConfigBean getColById(String col_id) throws SQLException {
    ColConfigBean colConfigBean = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      colConfigBean = rptConfigDao.getColById(col_id);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return colConfigBean;
  }

  public int delTabById(String tab_id) throws SQLException
  {
    int result = 0;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      result = rptConfigDao.delTabById(tab_id);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return result;
  }

  public void delColById(String col_id) throws SQLException {
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      rptConfigDao.delColById(col_id);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null)
        this.conn.close();
    }
  }

  public ColConfigBean getColByTabidAndColname(String tab_id, String col_name)
    throws SQLException
  {
    ColConfigBean colConfigBean = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      colConfigBean = rptConfigDao.getColByTabidAndColname(tab_id, col_name);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return colConfigBean;
  }

  public TabConfigBean getTabByName(String tab_name) throws SQLException
  {
    TabConfigBean tabConfigBean = null;
    try {
      RptConfigDao rptConfigDao = new RptConfigDao();
      tabConfigBean = rptConfigDao.getTabByName(tab_name);
    } catch (Exception ext) {
      ext.printStackTrace();
    } finally {
      if (this.conn != null) {
        this.conn.close();
      }
    }
    return tabConfigBean;
  }
}

/* 
 * Qualified Name:     report.java.rpt.service.RptConfigService
*
 */