package report.java.design.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import report.java.common.utils.Aes;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DataSourceUtil;
import report.java.db.util.XmlUtil;
import report.java.design.service.DesignService;
import report.java.jrreport.util.JRDataFormat;

public class DataSourceAction extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private String dataSourceName;
  private String dataSourceType;
  private String dataSourceDirver;
  private String dataSourceUrl;
  private String dataSourceUsername;
  private String dataSourcePassword;
  private String dataSetName;
  private String dataSetsModel;
  private String SQLText;
  private String name;
  private String type;
  private String newType;
  private String value;
  private String tableName;
  private String realPath = BaseAction.realPath + "db-config.xml";
  private List<DataSourceBean> dataSourceBeanList;
  private Map<String, Object> dataMap;
  private List tableList;
  private List fieldList;

  public List getTableList()
  {
    return this.tableList;
  }

  public void setTableList(List tableList) {
    this.tableList = tableList;
  }

  public String selectDataSourceAllForSet() {
    this.dataSourceBeanList = XmlUtil.selectXMLAll(this.realPath);
    return this.SUCCESS;
  }

  public String getDataSourceNameAll() {
    this.dataSourceBeanList = XmlUtil.selectXMLAll(this.realPath);
    return this.SUCCESS;
  }

  public String getDataTableNameAll() {
    this.tableList = DesignService.selectTable(this.dataSourceName);
    return this.SUCCESS;
  }

  public String parFields() {
    this.dataSetsModel = DesignService.parFields(this.dataSetsModel, this.dataSetName);
    return this.SUCCESS;
  }

  public String parFieldsForJSON() {
    this.dataMap = new HashMap();
    String name = this.dataSourceName;
    String sqlStr = this.value;
    sqlStr = sqlStr.replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(name);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, this.realPath);
    DataSourceUtil sourceUtil = new DataSourceUtil();
    if (sqlStr.endsWith(";")) {
      sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
    }

    List list = new ArrayList();
    if ("javabean".equals(name)) {
      String className = "";
      if (sqlStr.indexOf("|") != -1)
        className = sqlStr.split("\\|")[0];
      else {
        className = sqlStr;
      }
      list = sourceUtil.getProperties(className);
    } else {
      list = sourceUtil.executeSQL(dsb, sqlStr);
    }
    if (list == null) {
      this.dataMap.put("flag", Boolean.valueOf(false));
    } else {
      this.dataMap.put("flag", Boolean.valueOf(true));
      this.dataMap.put("list", list);
    }
    return this.SUCCESS;
  }

  public String getTableInfo()
  {
    this.dataMap = new HashMap();
    String ds = this.dataSourceName;
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(ds);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, this.realPath);
    DataSourceUtil sourceUtil = new DataSourceUtil();
    List list = sourceUtil.getTableInfo(dataSourceBean);
    if (list == null) {
      this.dataMap.put("flag", Boolean.valueOf(false));
    } else {
      this.dataMap.put("flag", Boolean.valueOf(true));
      this.dataMap.put("list", list);
    }
    return this.SUCCESS;
  }

  public String getColumnsInfo()
  {
    this.dataMap = new HashMap();
    String ds = this.dataSourceName;
    String tbl = this.tableName;
    DataSourceBean dataSourceBean = new DataSourceBean();
    dataSourceBean.setDataSourceName(ds);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, this.realPath);
    DataSourceUtil sourceUtil = new DataSourceUtil();
    List list = sourceUtil.getColumnsInfo(dataSourceBean, tbl);
    if (list == null) {
      this.dataMap.put("flag", Boolean.valueOf(false));
    } else {
      this.dataMap.put("flag", Boolean.valueOf(true));
      this.dataMap.put("list", list);
    }
    return this.SUCCESS;
  }

  public String selectDic() {
    String sql = Aes.aesDecrypt(this.SQLText);
    if ((sql != null) && (!sql.equals("")) && (sql.toLowerCase().indexOf("update ") == -1) && (sql.toLowerCase().indexOf("insert ") == -1) && (sql.toLowerCase().indexOf("delete ") == -1))
      this.dataSetsModel = DesignService.selectDic(sql, this.dataSourceName);
    else {
      this.dataSetsModel = "";
    }
    return this.SUCCESS;
  }

  public String convertDataFormat() {
    this.dataSetsModel = JRDataFormat.convertData(Integer.parseInt(this.name), this.type, this.value, this.newType);
    return this.SUCCESS;
  }

  public String getFieldNames()
  {
    this.fieldList = DesignService.getFieldNames(this.dataSourceName, this.tableName);
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

  public List getFieldList() {
    return this.fieldList;
  }

  public void setFieldList(List fieldList) {
    this.fieldList = fieldList;
  }

  public String getDataSetName() {
    return this.dataSetName;
  }

  public void setDataSetName(String dataSetName) {
    this.dataSetName = dataSetName;
  }

  public String getDataSetsModel() {
    return this.dataSetsModel;
  }

  public void setDataSetsModel(String dataSetsModel) {
    this.dataSetsModel = dataSetsModel;
  }

  public String getSQLText() {
    return this.SQLText;
  }

  public void setSQLText(String sQLText) {
    this.SQLText = sQLText;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getNewType() {
    return this.newType;
  }

  public void setNewType(String newType) {
    this.newType = newType;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }
}

/*
 * Qualified Name:     report.java.design.action.DataSourceAction
*
 */