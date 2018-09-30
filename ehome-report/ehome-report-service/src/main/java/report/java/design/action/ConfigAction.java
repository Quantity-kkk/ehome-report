package report.java.design.action;

import com.alibaba.fastjson.JSON;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import report.java.design.service.DesignService;
import report.java.design.util.DesignXmlUtil;
import report.java.entity.JsonToXMLUtil;
import report.java.entity.json.JsonReportEntity;
import report.java.entity.xml.ReportEntity;

public class ConfigAction extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private String report;
  private String uuid;
  private Map<String, Object> dataMap;

  public String saveReport()
  {
    this.dataMap = new HashMap();
    try {
      JsonReportEntity json = (JsonReportEntity)JSON.parseObject(this.report, JsonReportEntity.class);
      String sonStr = JSON.toJSONString(json);
      System.out.println(sonStr);
      ReportEntity report = JsonToXMLUtil.JsonToXml(json);
      String filePath = filePath() + System.getProperty("file.separator") + report.getUuid() + ".xml";
      if (DesignXmlUtil.reportToXML(report, filePath).booleanValue()) {
        DesignXmlUtil.xmlMap.remove(report.getUuid());
        this.dataMap.put("flag", "success");
        this.dataMap.put("msg", "保存成功！");
        this.dataMap.put("data", json);
      } else {
        this.dataMap.put("flag", "error");
        this.dataMap.put("msg", "保存失败！");
      }
    } catch (Exception e) {
      e.printStackTrace();
      this.dataMap.put("flag", "error");
      this.dataMap.put("msg", "保存失败！");
    }

    return this.SUCCESS;
  }

  public String selectReport()
  {
    this.dataMap = new HashMap();
    try
    {
      String fileName = DesignService.openDesignXML(filePath(), this.uuid);
      ReportEntity model = DesignXmlUtil.openXMLNew(filePath() + System.getProperty("file.separator") + fileName);
      JsonReportEntity json = JsonToXMLUtil.XmlToJson(model);
      String jsonStr = JSON.toJSONString(json);

      this.dataMap.put("flag", "success");
      this.dataMap.put("data", jsonStr);
      this.dataMap.put("json", json);
    } catch (Exception e) {
      this.dataMap.put("flag", "error");
      this.dataMap.put("msg", "获取失败！");
    }

    return this.SUCCESS;
  }

  public String getReport() {
    return this.report;
  }

  public void setReport(String report) {
    this.report = report;
  }

  public String getUuid() {
    return this.uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }
}

/*
 * Qualified Name:     report.java.design.action.ConfigAction
*
 */