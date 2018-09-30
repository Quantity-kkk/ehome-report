package report.java.design.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import report.java.design.bean.ReportFileBean;
import report.java.design.service.DesignService;
import report.java.design.util.DesignXmlUtil;

public class DesignAction extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private String expressionData;
  private String tableXMLData;
  private String fileName;
  private String dataSetsModel;
  private String parmsModel;
  private String parmsExtModel;
  private String fieldModel;
  private String uuid;
  private String type;
  private String desc;
  private String reportVersion;
  private String reportMemo;
  private String mainUuid;
  private ReportFileBean conditions;
  private List<ReportFileBean> reportFileBeanList;
  private Map<String, Object> dataMap;

  public String saveDesignXML()
  {
    if (DesignService.saveXML(this.uuid, this.type, this.desc, this.dataSetsModel, this.tableXMLData, this.expressionData, this.parmsModel, this.parmsExtModel, this.fieldModel, filePath() + System.getProperty("file.separator") + this.fileName, this.reportVersion, this.reportMemo, this.mainUuid))
      setMessage("保存成功！");
    else {
      setMessage("保存失败！");
    }
    return this.SUCCESS;
  }

  public String openDesignXML() {
    this.dataMap = new HashMap();
    String fileName = DesignService.openDesignXML(filePath(), this.uuid);
    this.dataMap = DesignXmlUtil.openXML(filePath() + System.getProperty("file.separator") + fileName);
    return this.SUCCESS;
  }

  public String updateDesignXML() {
    String fileName = DesignService.openDesignXML(filePath(), this.uuid);
    if (DesignXmlUtil.updateXML(this.dataSetsModel, this.tableXMLData, this.expressionData, this.parmsModel, this.parmsExtModel, this.fieldModel, filePath() + System.getProperty("file.separator") + fileName))
      setMessage("修改成功！");
    else {
      setMessage("修改失败！");
    }
    return this.SUCCESS;
  }
  public static void main(String[] args) {
  }

  public String selectAllReportFile() {
    this.dataMap = new HashMap();
    List<ReportFileBean> list = DesignService.findAllReportFile(filePath());
    for (ReportFileBean bean : list) {
      this.dataMap.put(this.dataMap.size()+"", bean);
    }
    return this.SUCCESS;
  }

  public String selectAllParmsByUUID() {
    String str = filePath() + System.getProperty("file.separator") + this.uuid + ".xml";
    this.dataMap = DesignXmlUtil.getParmsByUUID(str);
    return this.SUCCESS;
  }

  public String getUuid() {
    return this.uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getTableXMLData() {
    return this.tableXMLData;
  }

  public void setTableXMLData(String tableXMLData) {
    this.tableXMLData = tableXMLData;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDataSetsModel()
  {
    return this.dataSetsModel;
  }

  public void setDataSetsModel(String dataSetsModel) {
    this.dataSetsModel = dataSetsModel;
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

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDesc() {
    return this.desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getExpressionData() {
    return this.expressionData;
  }

  public void setExpressionData(String expressionData) {
    this.expressionData = expressionData;
  }

  public String getParmsModel() {
    return this.parmsModel;
  }

  public void setParmsModel(String parmsModel) {
    this.parmsModel = parmsModel;
  }

  public String getParmsExtModel() {
    return this.parmsExtModel;
  }

  public void setParmsExtModel(String parmsExtModel) {
    this.parmsExtModel = parmsExtModel;
  }

  public String getFieldModel() {
    return this.fieldModel;
  }

  public void setFieldModel(String fieldModel) {
    this.fieldModel = fieldModel;
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
 * Qualified Name:     report.java.design.action.DesignAction
*
 */