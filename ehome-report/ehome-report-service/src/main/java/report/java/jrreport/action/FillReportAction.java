package report.java.jrreport.action;

import report.java.design.action.BaseAction;
import report.java.jrreport.service.PreviewReportService;

public class FillReportAction extends BaseAction
{
  private String dataSourceName;
  private String tableName;
  private String dataArray;
  private String saveFlag;

  public String saveFillReport()
  {
    this.saveFlag = PreviewReportService.saveFillReport(this.dataSourceName, this.tableName, this.dataArray);
    return this.SUCCESS;
  }

  public String getDataSourceName()
  {
    return this.dataSourceName;
  }
  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }
  public String getTableName() {
    return this.tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getDataArray() {
    return this.dataArray;
  }
  public void setDataArray(String dataArray) {
    this.dataArray = dataArray;
  }

  public String getSaveFlag()
  {
    return this.saveFlag;
  }

  public void setSaveFlag(String saveFlag)
  {
    this.saveFlag = saveFlag;
  }
}

/* 
 * Qualified Name:     report.java.jrreport.action.FillReportAction
*
 */