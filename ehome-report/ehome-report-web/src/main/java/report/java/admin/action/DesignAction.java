package report.java.admin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import report.java.design.action.BaseAction;
import report.java.design.bean.ReportFileBean;
import report.java.design.service.DesignService;

public class DesignAction extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private String uuid;
  private String fileName;
  private String reportVersion;
  private String reportMemo;
  private String mainUuid;
  private ReportFileBean conditions;
  private List<ReportFileBean> reportFileBeanList;
  private Map<String, Object> dataMap;

  public String selectReportFileAll()
  {
    this.reportFileBeanList = DesignService.findAllReportFile(filePath(), this.conditions);
    return this.SUCCESS;
  }

  public String deleteReport()
  {
    DesignService.deleteReport(filePath() + System.getProperty("file.separator") + this.fileName);
    this.dataMap = new HashMap();
    this.dataMap.put("flag", "1");
    return this.SUCCESS;
  }

  public String versionReport()
  {
    try
    {
      String version = DesignService.versionReport(this.uuid, this.reportVersion, this.reportMemo);
      setMessage("生成新版本成功，版本号为：" + version + "!");
    } catch (Exception ex) {
      setMessage("生成新版本失败！");
    }

    return this.SUCCESS;
  }

  public String getUuid() {
    return this.uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }

  public List<ReportFileBean> getReportFileBeanList() {
    return this.reportFileBeanList;
  }

  public void setReportFileBeanList(List<ReportFileBean> reportFileBeanList) {
    this.reportFileBeanList = reportFileBeanList;
  }

  public ReportFileBean getConditions() {
    return this.conditions;
  }

  public void setConditions(ReportFileBean conditions) {
    this.conditions = conditions;
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
}

/*
 * Qualified Name:     report.java.admin.action.DesignAction
*
 */