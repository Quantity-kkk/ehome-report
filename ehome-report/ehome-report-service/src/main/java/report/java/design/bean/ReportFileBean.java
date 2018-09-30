package report.java.design.bean;

import java.util.ArrayList;
import java.util.List;

public class ReportFileBean
{
  private String uuid;
  private Double version;
  private String name;
  private String lastEditDate;
  private String reportStyle;
  private String reportVersion;
  private String reportMemo;
  private String mainUuid;
  private List<ReportFileBean> reportFiles = new ArrayList();

  public String getUuid() {
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

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastEditDate() {
    return this.lastEditDate;
  }

  public void setLastEditDate(String lastEditDate) {
    this.lastEditDate = lastEditDate;
  }

  public String getReportStyle() {
    return this.reportStyle;
  }

  public void setReportStyle(String reportStyle) {
    this.reportStyle = reportStyle;
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

  public List<ReportFileBean> getReportFiles() {
    return this.reportFiles;
  }

  public void setReportFiles(List<ReportFileBean> reportFiles) {
    this.reportFiles = reportFiles;
  }
}

/* 
 * Qualified Name:     report.java.design.bean.ReportFileBean
*
 */