package report.java.entity.xml;

import java.util.List;

public class ReportEntity
{
  private String uuid;
  private Double version;
  private String reportStyle;
  private String reportDescription;
  private String reportVersion;
  private String reportMemo;
  private String mainUuid;
  private String iscustom;
  private List<DataSetEntity> dataSets;
  private List<ParmEntity> parmsList;
  private Integer bodyPageorder;
  private Integer bodyPagesize;
  private Integer maxR;
  private Integer maxC;
  private String toolbar;
  private List<RowEntity> rows;

  public String getUuid()
  {
    return this.uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Double getVersion() {
    return this.version;
  }

  public void setVersion(Double version) {
    this.version = version;
  }

  public String getReportStyle() {
    return this.reportStyle;
  }

  public void setReportStyle(String reportStyle) {
    this.reportStyle = reportStyle;
  }

  public String getReportDescription() {
    return this.reportDescription;
  }

  public void setReportDescription(String reportDescription) {
    this.reportDescription = reportDescription;
  }

  public String getReportVersion() {
    return this.reportVersion;
  }

  public void setReportVersion(String reportVersion) {
    this.reportVersion = reportVersion;
  }

  public String getReportMemo() {
    return this.reportMemo;
  }

  public void setReportMemo(String reportMemo) {
    this.reportMemo = reportMemo;
  }

  public String getMainUuid() {
    return this.mainUuid;
  }

  public void setMainUuid(String mainUuid) {
    this.mainUuid = mainUuid;
  }

  public String getIscustom() {
    return this.iscustom;
  }

  public void setIscustom(String iscustom) {
    this.iscustom = iscustom;
  }

  public List<DataSetEntity> getDataSets() {
    return this.dataSets;
  }

  public void setDataSets(List<DataSetEntity> dataSets) {
    this.dataSets = dataSets;
  }

  public List<ParmEntity> getParmsList() {
    return this.parmsList;
  }

  public void setParmsList(List<ParmEntity> parmsList) {
    this.parmsList = parmsList;
  }

  public Integer getBodyPageorder() {
    return this.bodyPageorder;
  }

  public void setBodyPageorder(Integer bodyPageorder) {
    this.bodyPageorder = bodyPageorder;
  }

  public Integer getBodyPagesize() {
    return this.bodyPagesize;
  }

  public void setBodyPagesize(Integer bodyPagesize) {
    this.bodyPagesize = bodyPagesize;
  }

  public Integer getMaxR() {
    return this.maxR;
  }

  public void setMaxR(Integer maxR) {
    this.maxR = maxR;
  }

  public Integer getMaxC() {
    return this.maxC;
  }

  public void setMaxC(Integer maxC) {
    this.maxC = maxC;
  }

  public String getToolbar() {
    return this.toolbar;
  }

  public void setToolbar(String toolbar) {
    this.toolbar = toolbar;
  }

  public List<RowEntity> getRows() {
    return this.rows;
  }

  public void setRows(List<RowEntity> rows) {
    this.rows = rows;
  }
}

/* 
 * Qualified Name:     report.java.entity.xml.ReportEntity
*
 */