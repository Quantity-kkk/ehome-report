package report.java.jrreport.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import report.java.design.action.BaseAction;
import report.java.design.bean.XMLbean;
import report.java.design.util.DesignXmlUtil;
import report.java.entity.JsonToXMLUtil;
import report.java.entity.json.JsonReportEntity;
import report.java.entity.xml.ReportEntity;
import report.java.jrreport.service.PreviewReportService;
import report.java.jrreport.util.JRUtilNew;

public class PreviewReportAction extends BaseAction
{
  private static Logger logger = Logger.getLogger(PreviewReportAction.class);
  private String reportJson;
  public static String tableModel;
  public static String dataSetsModel;
  public static String parmsModel;
  public static String fieldModel;
  public static String reporttype;
  public static Map<String, Object> tableMap = new HashMap();

  public static List<String> dateFields = new ArrayList();
  public static int pdFlag;
  public static int currentPage;
  public static int pageType;
  public static int pageSize;
  public static String uid;
  private String opt;
  private String uuid;
  private String parms;
  private Map<String, Object> dataMap;
  private static String jsonlist;

  public String previewDesignNew()
  {
    pdFlag = 1;
    this.dataMap = new HashMap();
    if ((this.reportJson == null) || ("".equals(this.reportJson))) {
      XMLbean xmlb = null;
      if (DesignXmlUtil.xmlMap.get(uid) != null) {
        xmlb = (XMLbean)DesignXmlUtil.xmlMap.get(uid);
      } else {
        String filePath = JRUtilNew.getBaseFilePath();
        Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + uid + ".xml");
        xmlb = (XMLbean)map.get("xml");
      }

      if (pageSize == 0)
      {
        this.dataMap = pubPreDes(xmlb.getType(), xmlb.getBody(), xmlb.getDataSets(), xmlb.getFillreports(), xmlb.getParmslist(), currentPage, Integer.parseInt(xmlb.getPageorder()), Integer.parseInt(xmlb.getPagesize()), uid, Boolean.valueOf(false), xmlb.getToolbar(), Boolean.valueOf(false));
      }
      else this.dataMap = pubPreDes(xmlb.getType(), xmlb.getBody(), xmlb.getDataSets(), xmlb.getFillreports(), xmlb.getParmslist(), currentPage, pageType, pageSize, uid, Boolean.valueOf(false), xmlb.getToolbar(), Boolean.valueOf(false)); 
    }
    else
    {
      JsonReportEntity json = (JsonReportEntity)JSON.parseObject(this.reportJson, JsonReportEntity.class);
      ReportEntity report = JsonToXMLUtil.JsonToXml(json);
      Map map = DesignXmlUtil.openXML(report);
      XMLbean xmlb = (XMLbean)map.get("xml");
      this.dataMap = pubPreDes(xmlb.getType(), xmlb.getBody(), xmlb.getDataSets(), xmlb.getFillreports(), xmlb.getParmslist(), currentPage, pageType, pageSize, uid, Boolean.valueOf(false), xmlb.getToolbar(), Boolean.valueOf(false));
    }
    this.message = this.opt;
    return this.SUCCESS;
  }

  public static String getSubReport(String uuid, String parmsModel) {
    Map dMap = new HashMap();

    XMLbean xmlb = null;
    if (DesignXmlUtil.xmlMap.get(uuid) != null) {
      xmlb = (XMLbean)DesignXmlUtil.xmlMap.get(uuid);
    } else {
      String filePath = JRUtilNew.getBaseFilePath();
      Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + uuid + ".xml");
      xmlb = (XMLbean)map.get("xml");
    }
    dMap = pubPreDes(xmlb.getType(), xmlb.getBody(), xmlb.getDataSets(), xmlb.getFillreports(), xmlb.getParmslist(), 1, pageType, -1, uid, Boolean.valueOf(true), xmlb.getToolbar(), Boolean.valueOf(false));
    return dMap.get("body").toString();
  }

  public String previewReportJavabean()
  {
    String listJson = ServletActionContext.getRequest().getParameter("list");

    JSONArray jsonArray = JSONArray.parseArray(listJson);

    String filePath = JRUtilNew.getBaseFilePath();
    Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + this.uuid + ".xml");
    XMLbean lbean = (XMLbean)map.get("xml");
    Map parmMap = new PreviewReportService().compileParms(lbean.getParmslist());
    HttpServletRequest request = ServletActionContext.getRequest();

    if (reporttype.equals("D"))
    {
      this.dataMap = JRUtilNew.getJRPrintD(lbean.getBody(), lbean.getDataSets(), "", lbean.getFillreports(), currentPage, pageType, pageSize, this.uuid, jsonArray);
      request.getSession().setAttribute("previewJasperPrintType", "D");
    }
    return this.SUCCESS;
  }

  public String initPreParm()
  {
    pdFlag = 1;
    String parms = "";
    this.dataMap = new HashMap();
    if ((this.reportJson == null) || ("".equals(this.reportJson))) {
      String filePath = JRUtilNew.getBaseFilePath();
      Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + uid + ".xml");
      XMLbean xmlb = (XMLbean)map.get("xml");
      parms = xmlb.getParmslist();
    }
    else {
      JsonReportEntity json = (JsonReportEntity)JSON.parseObject(this.reportJson, JsonReportEntity.class);
      ReportEntity report = JsonToXMLUtil.JsonToXml(json);
      Map map = DesignXmlUtil.openXML(report);
      XMLbean xmlb = (XMLbean)map.get("xml");
      parms = xmlb.getParmslist();
    }
    String searchTab = "";
    JSONArray jsonArr = new JSONArray();
    Map<String,Object> parmMap = new PreviewReportService().compileParms(parms);
    HttpServletRequest request = ServletActionContext.getRequest();
    if (parmMap != null) {
      for (Map.Entry entry : parmMap.entrySet()) {
        Element e = (Element)entry.getValue();
        String key = (String)entry.getKey();
        String cname = e.attributeValue("cname");
        String ptype = e.attributeValue("type");
        String showtype = e.attributeValue("showtype");
        String vl = e.attributeValue("vl");
        if (vl == null) {
          vl = "";
        }
        String sty = "";
        if ("dynamic".equals(ptype))
          sty = " style='display:none'";
        else {
          sty = "";
        }
        String str = request.getParameter(key);
        if (str == null)
          str = vl;
        JSONObject tempJson = new JSONObject();
        String fileStr = "";
        if ("select".equals(showtype)) {
          String keylist = request.getParameter(key + "_selectlist");
          if (keylist == null) {
            keylist = vl;
          }
          if ((keylist == null) || ("".equals(keylist)) || (keylist.indexOf(":") == -1)) {
            fileStr = "<select name='" + key + "' id='" + key + "'><option value=''></option><option value='" + str + "'>" + str + "</option></select>";
          }
          else if (keylist.indexOf(",") != -1) {
            fileStr = "<select name='" + key + "' id='" + key + "'><option value=''></option>";
            String[] keys = keylist.split(",");
            for (int i = 0; i < keys.length; i++) {
              fileStr = fileStr + "<option value='" + keys[i].split(":")[1] + "'>" + keys[i].split(":")[0] + "</option>";
            }
            fileStr = fileStr + "</select>";
          } else {
            fileStr = "<select name='" + key + "' id='" + key + "'><option value='" + keylist.split(":")[1] + "'>" + keylist.split(":")[0] + "</option></select>";
          }

          tempJson.put("keylist", keylist);
        } else {
          fileStr = "<input type='text' name='" + key + "' id='" + key + "' value='" + str + "'/>";
        }
        if ((cname != null) && (cname != ""))
          searchTab = searchTab + "<span" + sty + ">" + cname + ":  " + fileStr + "</span>";
        else {
          searchTab = searchTab + "<span" + sty + ">" + key + ":  " + fileStr + "</span>";
        }
        tempJson.put("showtype", showtype);
        tempJson.put("ptype", ptype);
        tempJson.put("cname", cname);
        tempJson.put("key", key);
        tempJson.put("value", str);

        jsonArr.add(tempJson);
      }

    }

    this.dataMap.put("searchParm", searchTab);
    this.dataMap.put("searchParmJson", jsonArr);

    return this.SUCCESS;
  }

  public String initPreParmExt()
  {
    pdFlag = 1;
    this.dataMap = new HashMap();
    String searchTab = "";
    Map<String,Object> parmMap = new PreviewReportService().compileParms(parmsModel);
    HttpServletRequest request = ServletActionContext.getRequest();
    if (parmMap != null) {
      for (Map.Entry entry : parmMap.entrySet()) {
        Element e = (Element)entry.getValue();
        String key = (String)entry.getKey();
        String cname = e.attributeValue("cname");
        String ptype = e.attributeValue("type");
        String showtype = e.attributeValue("showtype");
        String vl = e.attributeValue("vl");
        String dbtype = e.attributeValue("dbtype");
        String dbtxt = "text";
        if (dbtype.equals("date"))
          dbtxt = "date";
        else if ((dbtype.equals("double")) || (dbtype.equals("int"))) {
          dbtxt = "number";
        }
        String sty = "";
        if ("dynamic".equals(ptype))
          sty = " style='display:none'";
        else {
          sty = "";
        }
        String str = request.getParameter(key);
        if (str == null)
          str = "";
        if ((dbtype.equals("date")) && (str.length() == 8)) {
          str = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
        }
        String fileStr = "";
        if ("select".equals(showtype)) {
          String keylist = request.getParameter(key + "_selectlist");
          if ((keylist == null) || ("".equals(keylist)) || (keylist.indexOf(":") == -1)) {
            fileStr = "<select type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "'><option value=''></option><option value='" + str + "'";
            if (str.length() > 0) {
              fileStr = fileStr + " selected=\"selected\"";
            }
            fileStr = fileStr + ">" + str + "</option></select>";
          }
          else if (keylist.indexOf(",") != -1) {
            fileStr = "<select type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "'><option value=''></option>";
            String[] keys = keylist.split(",");
            for (int i = 0; i < keys.length; i++) {
              fileStr = fileStr + "<option value='" + keys[i].split(":")[1] + "'";
              if (keys[i].split(":")[1].equals(str)) {
                fileStr = fileStr + " selected=\"selected\"";
              }
              fileStr = fileStr + ">" + keys[i].split(":")[0] + "</option>";
            }
            fileStr = fileStr + "</select>";
          } else {
            fileStr = "<select type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "'><option value='" + keylist.split(":")[1] + "'";
            if (keylist.split(":")[1].equals(str)) {
              fileStr = fileStr + " selected=\"selected\"";
            }
            fileStr = fileStr + ">" + keylist.split(":")[0] + "</option></select>";
          }
        }
        else if ("multiple".equals(showtype)) {
          String keylist = request.getParameter(key + "_selectlist");
          if ((keylist == null) || ("".equals(keylist)) || (keylist.indexOf(":") == -1)) {
            fileStr = "<select type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "' multiple='multiple'><option value=''></option><option value='" + str + "'";
            if (str.length() > 0) {
              fileStr = fileStr + " selected=\"selected\"";
            }
            fileStr = fileStr + ">" + str + "</option></select>";
          }
          else if (keylist.indexOf(",") != -1) {
            fileStr = "<select type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "' multiple='multiple'>";
            String[] keys = keylist.split(",");
            for (int i = 0; i < keys.length; i++) {
              fileStr = fileStr + "<option value='" + keys[i].split(":")[1] + "'";
              if ((str + ",").indexOf(keys[i].split(":")[1] + ",") != -1) {
                fileStr = fileStr + " selected=\"selected\"";
              }
              fileStr = fileStr + ">" + keys[i].split(":")[0] + "</option>";
            }
            fileStr = fileStr + "</select>";
          } else {
            fileStr = "<select type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "' multiple='multiple'><option value='" + keylist.split(":")[1] + "'";
            if (keylist.split(":")[1].equals(str)) {
              fileStr = fileStr + " selected=\"selected\"";
            }
            fileStr = fileStr + ">" + keylist.split(":")[0] + "</option></select>";
          }
        }
        else {
          fileStr = "<input type='" + dbtxt + "' name='" + key + "' id='" + key + "' vl='" + vl + "' value='" + str + "'/>";
        }
        if ((cname != null) && (cname != ""))
          searchTab = searchTab + "<p" + sty + ">" + cname + ":  " + fileStr + "</p>";
        else {
          searchTab = searchTab + "<p" + sty + ">" + key + ":  " + fileStr + "</p>";
        }

      }

    }

    this.dataMap.put("searchParm", searchTab);

    return this.SUCCESS;
  }

  public String initPreParms()
  {
    pdFlag = 1;
    this.dataMap = new HashMap();
    String filePath = JRUtilNew.getBaseFilePath();
    Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + uid + ".xml");
    XMLbean lbean = (XMLbean)map.get("xml");
    this.dataMap.put("tableModel", lbean.getBody());
    this.dataMap.put("dataSetsModel", lbean.getDataSets());
    this.dataMap.put("parmsModel", lbean.getParmslist());
    this.dataMap.put("parmsExtModel", lbean.getParmsExtlist());
    this.dataMap.put("fieldModel", lbean.getFillreports());

    return this.SUCCESS;
  }

  public String preDes()
  {
    Map parmsMap = new HashMap();
    pdFlag = 1;
    String filePath = JRUtilNew.getBaseFilePath();
    Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + this.uuid + ".xml");
    XMLbean lbean = (XMLbean)map.get("xml");
    Map<String,Object> parmMap = new PreviewReportService().compileParms(lbean.getParmslist());
    HttpServletRequest request = ServletActionContext.getRequest();
    if (parmMap != null) {
      for (String key : parmMap.keySet()) {
        Element node = (Element)parmMap.get(key);
        String str = request.getParameter(key);
        if (str == null)
          str = "";
        parmsMap.put(key, str);
      }
    }
    this.message = this.opt;
    Map jasperPrint = pubPreDes(lbean.getType(), lbean.getBody(), lbean.getDataSets(), lbean.getFillreports(), lbean.getParmslist(), currentPage, pageType, pageSize, uid, Boolean.valueOf(false), lbean.getToolbar(), Boolean.valueOf(false));
    request = ServletActionContext.getRequest();
    if (lbean.getType().equals("D")) {
      request.setAttribute("jasperPrintType", "D");
      request.setAttribute("jasperPrint", jasperPrint);
      return "success1";
    }if (lbean.getType().equals("N")) {
      request.setAttribute("jasperPrintType", "N");
      request.setAttribute("jasperPrint", jasperPrint);
      return "success1";
    }
    return "failed";
  }

  public static void exportFile(String tableModel, String dataSetsModel, String fieldModel, String parmsModel, String uid, String defaultFilename, HttpServletRequest request, HttpServletResponse response)
  {
    Map parmsMap = new HashMap();
    JSONArray jsonArray = new JSONArray();
    if (!"".equals(jsonlist))
      jsonArray = JSONArray.parseArray(jsonlist);
    Map<String,Object> parmMap = new PreviewReportService().compileParms(parmsModel);
    if (parmMap != null) {
      for (String key : parmMap.keySet()) {
        Element node = (Element)parmMap.get(key);
        String str = request.getParameter(key);
        if (str == null)
          str = "";
        parmsMap.put(key, str);
      }
    }
    JRUtilNew.exportExcel(tableModel, dataSetsModel, parmsModel, fieldModel, uid, jsonArray, defaultFilename, request, response, Boolean.valueOf(false));
  }

  public static Map<String, Object> pubPreDes(String reporttype, String tableModel, String dataSetsModel, String fieldModel, String parmsModel, int currentPage, int pageType, int pageSize, String uid, Boolean issubreport, String toolbar, Boolean iscustom)
  {
    Map parmsMap = new HashMap();
    JSONArray jsonArray = new JSONArray();
    if (!"".equals(jsonlist)) {
      jsonArray = JSONArray.parseArray(jsonlist);
    }
    String strSuc = "";
    String tableStr = "";

    Map dataMap = null;

    Map<String,Object> parmMap = new PreviewReportService().compileParms(parmsModel);
    HttpServletRequest request = ServletActionContext.getRequest();
    if (parmMap != null) {
      for (String key : parmMap.keySet()) {
        Element node = (Element)parmMap.get(key);
        String str = request.getParameter(key);
        if (str == null)
          str = "";
        parmsMap.put(key, str);
      }
    }
    if (reporttype.equals("D")) {
      dataMap = JRUtilNew.getJRPrintDNew(tableModel, dataSetsModel, parmsModel, fieldModel, currentPage, pageType, pageSize, uid, jsonArray, issubreport, toolbar, iscustom);

      request.getSession().setAttribute("previewJasperPrintType", "D");
      strSuc = "success1";
    } else {
      String str = new PreviewReportService().previewCharts(tableModel, dataSetsModel, parmsModel);

      HashMap hMap = new HashMap();
      hMap.put("chartsOpt", str);
      dataMap = hMap;
      request.getSession().setAttribute("previewJasperPrintType", "C");

      strSuc = "success1";
    }
    dateFields.clear();

    return dataMap;
  }

  public String getReportJson() {
    return this.reportJson;
  }

  public void setReportJson(String reportJson) {
    this.reportJson = reportJson;
  }

  public String getFieldModel() {
    return fieldModel;
  }

  public void setFieldModel(String fieldModel) {
    this.fieldModel = fieldModel;
  }

  public String getTableModel() {
    return tableModel;
  }

  public void setTableModel(String tableModel) {
    this.tableModel = tableModel;
  }

  public String getDataSetsModel() {
    return dataSetsModel;
  }

  public void setDataSetsModel(String dataSetsModel) {
    this.dataSetsModel = dataSetsModel;
  }

  public String getParmsModel() {
    return parmsModel;
  }

  public void setParmsModel(String parmsModel) {
    this.parmsModel = parmsModel;
  }

  public String getReporttype() {
    return reporttype;
  }

  public void setReporttype(String reporttype) {
    this.reporttype = reporttype;
  }

  public String getOpt() {
    return this.opt;
  }

  public void setOpt(String opt) {
    this.opt = opt;
  }

  public String getUuid() {
    return this.uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getParms() {
    return this.parms;
  }

  public void setParms(String parms) {
    this.parms = parms;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }

  public String getJsonlist() {
    return jsonlist;
  }

  public void setJsonlist(String jsonlist) {
    this.jsonlist = jsonlist;
  }

  public static int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public static int getPageType() {
    return pageType;
  }

  public void setPageType(int pageType) {
    this.pageType = pageType;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public String getUid() {
    return this.uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }
}

/*
 * Qualified Name:     report.java.jrreport.action.PreviewReportAction
*
 */