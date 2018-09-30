package report.java.rpt.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.design.bean.XMLbean;
import report.java.design.util.DesignXmlUtil;
import report.java.jrreport.util.JRUtilNew;
import report.java.rpt.bean.ColExtendBean;
import report.java.rpt.bean.CusReportBean;
import report.java.rpt.dao.RptCustomDao;
import report.java.rpt.util.RptCustomPreview;
import report.java.rpt.util.RptGlobal;

public class RptCustomService
{
  public ArrayList<CusReportBean> getTabList()
    throws SQLException
  {
    ArrayList resultList = null;
    try {
      RptCustomDao rptCustomDao = new RptCustomDao();
      resultList = rptCustomDao.getTabList();
    } catch (Exception ext) {
      ext.printStackTrace();
    }
    return resultList;
  }

  public ArrayList<CusReportBean> getSearchRepList(String rep_name) throws SQLException {
    ArrayList resultList = null;
    try {
      RptCustomDao rptCustomDao = new RptCustomDao();
      resultList = rptCustomDao.getSearchRepList(rep_name);
    } catch (Exception ext) {
      ext.printStackTrace();
    }
    return resultList;
  }

  public void delRepById(String rep_id) throws SQLException {
    try {
      RptCustomDao rptCustomDao = new RptCustomDao();
      rptCustomDao.delRepById(rep_id);

      String filePath = ServletActionContext.getRequest().getSession().getServletContext().getRealPath(RptGlobal.FILE_PATH);
      File file = new File(filePath + System.getProperty("file.separator") + rep_id + ".xml");
      file.delete();
    } catch (Exception ext) {
      ext.printStackTrace();
    }
  }

  public int insertReport(CusReportBean bean) throws SQLException {
    int result = 0;
    try {
      RptCustomDao rptCustomDao = new RptCustomDao();
      result = rptCustomDao.insertReport(bean);
    }
    catch (Exception ext) {
      ext.printStackTrace();
    }
    return result;
  }

  public int insertWhere(CusReportBean bean) throws SQLException {
    int result = 0;
    try {
      RptCustomDao rptCustomDao = new RptCustomDao();
      result = rptCustomDao.insertWhere(bean);
    }
    catch (Exception ext) {
      ext.printStackTrace();
    }
    return result;
  }

  public String findConitionById(String rep_id) throws SQLException {
    String searchTab = "";
    RptCustomDao rptCustomDao = new RptCustomDao();
    CusReportBean bean = rptCustomDao.findByRepid(rep_id);
    if (bean != null) {
      String where_id = bean.getWhere_id();
      if ((!"".equals(where_id)) && (where_id != null))
      {
        rptCustomDao = new RptCustomDao();
        List conditionList = rptCustomDao.getColExtendList(where_id);

        for (int i = 0; i < conditionList.size(); i++) {
          ColExtendBean tempBean = (ColExtendBean)conditionList.get(i);

          if ((tempBean.getCol_dic() != null) && (tempBean.getCol_dic().length() > 0) && (tempBean.getCol_dic().toLowerCase().startsWith("select "))) {
            XMLbean lbean = null;
            if (DesignXmlUtil.xmlMap.get(rep_id) != null) {
              lbean = (XMLbean)DesignXmlUtil.xmlMap.get(rep_id);
            } else {
              String filePath = JRUtilNew.getBaseFilePath();
              Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + rep_id + ".xml");
              lbean = (XMLbean)map.get("xml");
            }
            Map<String,Object> dsMap = RptCustomPreview.compileReport(lbean.getDataSets());
            String dsName = "";
            for (String key : dsMap.keySet()) {
              dsName = ((Map)((Map)dsMap.get(key)).get("query")).get("datasourcename").toString();
            }
            DataSourceBean dataSourceBean = new DataSourceBean();
            dataSourceBean.setDataSourceName(dsName);
            String realPath = BaseAction.realPath + "db-config.xml";
            dataSourceBean = XmlUtil.selectXML(dataSourceBean, realPath);
            Connection conn = DBUtil.getConn(dataSourceBean);
            if (conn != null) {
              List<Map<String,Object>> diclist = DBUtil.getResultToList(conn, Boolean.valueOf(true), tempBean.getCol_dic(), new Object[0]);
              StringBuilder str = new StringBuilder();
              if (diclist.size() > 0) {
                Map ndic = new HashMap();
                for (Map map : diclist) {
                  if ((map.get("opt_code") != null) && (map.get("opt_name") != null)) {
                    str.append("|" + map.get("opt_code") + "-" + (map.get("opt_name") != null ? map.get("opt_name").toString().replace(",", "").replace("|", "") : ""));
                  }
                }
                if (str != null) {
                  tempBean.setCol_dic(str.toString().substring(1));
                }
              }
            }
          }
          searchTab = searchTab + getSerarchForm(tempBean);
        }

      }

    }

    return searchTab;
  }

  private String getSerarchForm(ColExtendBean bean)
  {
    String searchStr = "";
    if (bean != null) {
      int colType = Integer.parseInt(bean.getCol_type());
      switch (colType) {
      case 1:
        searchStr = "<span class='findspan1'><span class='findspan2'>" + bean.getCol_desc() + "</span> <span class='findspan3'><input type='text' colsize='" + bean.getCol_size() + "'  name='" + bean.getCol_name() + "' coloperate='" + bean.getWhere_operate1() + "'/></span></span>";
        break;
      case 2:
        String dataStr0 = "";
        if (!"".equals(bean.getWhere_operate1())) {
          dataStr0 = dataStr0 + "<input type='text' class='numstyle' colsize='" + bean.getCol_size() + "'  name='" + bean.getCol_name() + "' eletype='" + bean.getWhere_operate1() + "' coloperate='" + bean.getWhere_operate1() + "'/>";
        }

        if (!"".equals(bean.getWhere_operate2())) {
          dataStr0 = dataStr0 + "-<input type='text' class='numstyle' colsize='" + bean.getCol_size() + "'  name='" + bean.getCol_name() + "' eletype='" + bean.getWhere_operate2() + "' coloperate='" + bean.getWhere_operate2() + "'/>";
        }
        searchStr = "<span class='findspan1'><span class='findspan2'>" + bean.getCol_desc() + "</span><span class='findspan3'>" + dataStr0 + "</span></span>";
        break;
      case 3:
        String dataStr = "";
        if (!"".equals(bean.getWhere_operate1())) {
          dataStr = dataStr + "<input type='text' colsize='" + bean.getCol_size() + "'  name='" + bean.getCol_name() + "' class='datetime' eletype='" + bean.getWhere_operate1() + "' coloperate='" + bean.getWhere_operate1() + "' saveformat='" + bean.getSave_format() + "'/>";
        }

        if (!"".equals(bean.getWhere_operate2())) {
          dataStr = dataStr + "-<input type='text' colsize='" + bean.getCol_size() + "'  name='" + bean.getCol_name() + "' class='datetime' eletype='" + bean.getWhere_operate2() + "' coloperate='" + bean.getWhere_operate2() + "' saveformat='" + bean.getSave_format() + "'/>";
        }
        searchStr = "<span class='findspan1'><span class='findspan2'>" + bean.getCol_desc() + "</span><span class='findspan3'>" + dataStr + "</span></span>";
        break;
      case 4:
        String colDic = bean.getCol_dic();
        if (("".equals(colDic)) || (colDic == null)) break;
        String tempStr = "";
        String[] dicArr = colDic.split("\\|");
        for (int i = 0; i < dicArr.length; i++) {
          String[] keyValue = dicArr[i].split("-");
          tempStr = tempStr + "<label class='findspanChec'><input type='checkbox' name='" + bean.getCol_name() + "' value='" + keyValue[0] + "' coloperate='" + bean.getWhere_operate1() + "'/>" + keyValue[1] + "</label>";
        }
        searchStr = "<span class='findspan1'><span class='findspan2'>" + bean.getCol_desc() + "</span> <span class='findspan3'>" + tempStr + "</span></span>";

        break;
      case 5:
        String colDic1 = bean.getCol_dic();
        if (("".equals(colDic1)) || (colDic1 == null)) break;
          tempStr = "<option value=''></option>";
          dicArr = colDic1.split("\\|");
        for (int i = 0; i < dicArr.length; i++) {
          String[] keyValue = dicArr[i].split("-");
          tempStr = tempStr + "<option value='" + keyValue[0] + "' coloperate='" + bean.getWhere_operate1() + "'>" + keyValue[1] + "</option>";
        }
        searchStr = "<span class='findspan1'><span class='findspan2'>" + bean.getCol_desc() + "</span> <span class='findspan3'> <select name='" + bean.getCol_name() + "'>" + tempStr + "</select></span></span>";

        break;
      }

    }

    return searchStr;
  }

  public List<ColExtendBean> customPreview(String rep_id) throws SQLException
  {
    List conditionList = null;
    RptCustomDao rptCustomDao = new RptCustomDao();
    CusReportBean bean = rptCustomDao.findByRepid(rep_id);
    if (bean != null) {
      String where_id = bean.getWhere_id();
      if ((!"".equals(where_id)) && (where_id != null))
      {
        rptCustomDao = new RptCustomDao();
        conditionList = rptCustomDao.getColExtendList(where_id);
      }
    }
    return conditionList;
  }

  public CusReportBean getCusReportById(String rep_id) throws SQLException
  {
    CusReportBean cusReportBean = null;
    RptCustomDao rptCustomDao = new RptCustomDao();
    cusReportBean = rptCustomDao.findByRepid(rep_id);
    return cusReportBean;
  }
}
