package report.java.db.bean;

public class DataSetBean
{
  private String dataSetName;
  private String sqlDefine;
  private String parameters;
  private String type;
  private String dataSourceName;

  public String getDataSetName()
  {
    return this.dataSetName;
  }
  public void setDataSetName(String dataSetName) {
    this.dataSetName = dataSetName;
  }
  public String getSqlDefine() {
    return this.sqlDefine;
  }
  public void setSqlDefine(String sqlDefine) {
    this.sqlDefine = sqlDefine;
  }
  public String getParameters() {
    return this.parameters;
  }
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }
  public String getType() {
    return this.type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getDataSourceName() {
    return this.dataSourceName;
  }
  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }
}

/* 
 * Qualified Name:     report.java.db.bean.DataSetBean
*
 */