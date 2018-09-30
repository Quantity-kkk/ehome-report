package report.java.admin.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;

public class DataSourceAction extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private String dataSourceName;
  private String dataSourceType;
  private String dataSourceDirver;
  private String dataSourceUrl;
  private String dataSourceUsername;
  private String dataSourcePassword;
  private String realPath = BaseAction.realPath + "db-config.xml";
  private List<DataSourceBean> dataSourceBeanList;
  private Map<String, Object> dataMap;

  public String selectDataSourceAll()
  {
    this.dataSourceBeanList = XmlUtil.selectXMLAll(this.realPath);
    return this.SUCCESS;
  }

  public String addDataSource()
  {
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(this.dataSourceName);
    dataSourceBean.setType(this.dataSourceType);
    dataSourceBean.setDriver(this.dataSourceDirver);
    dataSourceBean.setDataBaseUrl(this.dataSourceUrl);
    dataSourceBean.setUserName(this.dataSourceUsername);
    dataSourceBean.setPassword(this.dataSourcePassword);
    dataSourceBean.setState("0");
    XmlUtil.addXML(dataSourceBean, this.realPath);
    this.dataMap = new HashMap();
    this.dataMap.put("flag", "1");
    return this.SUCCESS;
  }

  public String deleteDataSource()
  {
    XmlUtil.deleteXML(this.dataSourceName, this.realPath);
    this.dataMap = new HashMap();
    this.dataMap.put("flag", "1");
    if (DBUtil.dataSourceMap.containsKey(this.dataSourceName)) {
      DBUtil.dataSourceMap.remove(this.dataSourceName);
    }
    return this.SUCCESS;
  }

  public String testDataSource()
  {
    this.dataMap = new HashMap();
    Connection connection = null;
    try
    {
      Class.forName(this.dataSourceDirver);
      connection = DriverManager.getConnection(this.dataSourceUrl, this.dataSourceUsername, this.dataSourcePassword);
      if (connection != null) {
        this.dataMap.put("flag", "1");
        connection.close();
        connection = null;
      } else {
        this.dataMap.put("flag", "0");
      }
    } catch (Exception e) {
      this.dataMap.put("flag", "0");
    }

    return this.SUCCESS;
  }

  public String updateDataSource()
  {
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(this.dataSourceName);
    dataSourceBean.setType(this.dataSourceType);
    dataSourceBean.setDriver(this.dataSourceDirver);
    dataSourceBean.setDataBaseUrl(this.dataSourceUrl);
    dataSourceBean.setUserName(this.dataSourceUsername);
    dataSourceBean.setPassword(this.dataSourcePassword);
    dataSourceBean.setState("0");
    XmlUtil.updateXML(dataSourceBean, this.realPath);
    this.dataMap = new HashMap();
    this.dataMap.put("flag", "1");
    if (DBUtil.dataSourceMap.containsKey(this.dataSourceName)) {
      DBUtil.dataSourceMap.remove(this.dataSourceName);
    }
    return this.SUCCESS;
  }

  public String getDataSourceName() {
    return this.dataSourceName;
  }

  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }

  public String getDataSourceType() {
    return this.dataSourceType;
  }

  public void setDataSourceType(String dataSourceType) {
    this.dataSourceType = dataSourceType;
  }

  public String getDataSourceDirver() {
    return this.dataSourceDirver;
  }

  public void setDataSourceDirver(String dataSourceDirver) {
    this.dataSourceDirver = dataSourceDirver;
  }

  public String getDataSourceUrl() {
    return this.dataSourceUrl;
  }

  public void setDataSourceUrl(String dataSourceUrl) {
    this.dataSourceUrl = dataSourceUrl;
  }

  public String getDataSourceUsername() {
    return this.dataSourceUsername;
  }

  public void setDataSourceUsername(String dataSourceUsername) {
    this.dataSourceUsername = dataSourceUsername;
  }

  public String getDataSourcePassword() {
    return this.dataSourcePassword;
  }

  public void setDataSourcePassword(String dataSourcePassword) {
    this.dataSourcePassword = dataSourcePassword;
  }

  public List<DataSourceBean> getDataSourceBeanList() {
    return this.dataSourceBeanList;
  }

  public void setDataSourceBeanList(List<DataSourceBean> dataSourceBeanList) {
    this.dataSourceBeanList = dataSourceBeanList;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDatamap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }
}

/*
 * Qualified Name:     report.java.admin.action.DataSourceAction
*
 */