package report.java.db.bean;

import java.util.List;

public class DataSourceBean
{
  private String dataSourceName;
  private String type;
  private String state;
  private String clientCharset;
  private String dataBaseCharset;
  private String driver;
  private String dataBaseUrl;
  private String userName;
  private String password;
  private List<TableBean> tableList;

  public String getState()
  {
    return this.state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public String getDataSourceName() {
    return this.dataSourceName;
  }
  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }
  public String getType() {
    return this.type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getClientCharset() {
    return this.clientCharset;
  }
  public void setClientCharset(String clientCharset) {
    this.clientCharset = clientCharset;
  }
  public String getDataBaseCharset() {
    return this.dataBaseCharset;
  }
  public void setDataBaseCharset(String dataBaseCharset) {
    this.dataBaseCharset = dataBaseCharset;
  }
  public String getDriver() {
    return this.driver;
  }
  public void setDriver(String driver) {
    this.driver = driver;
  }
  public String getDataBaseUrl() {
    return this.dataBaseUrl;
  }
  public void setDataBaseUrl(String dataBaseUrl) {
    this.dataBaseUrl = dataBaseUrl;
  }
  public String getUserName() {
    return this.userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public List<TableBean> getTableList() {
    return this.tableList;
  }
  public void setTableList(List<TableBean> tableList) {
    this.tableList = tableList;
  }
}

/*
 * Qualified Name:     report.java.db.bean.DataSourceBean
*
 */