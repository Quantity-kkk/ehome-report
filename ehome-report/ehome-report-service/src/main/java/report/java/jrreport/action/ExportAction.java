package report.java.jrreport.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import report.java.design.action.BaseAction;
import report.java.design.bean.XMLbean;
import report.java.design.util.DesignXmlUtil;
import report.java.entity.JsonToXMLUtil;
import report.java.entity.json.JsonReportEntity;
import report.java.entity.xml.ReportEntity;
import report.java.jrreport.service.PreviewService;
import report.java.jrreport.util.JRUtilNew;
import report.java.jrreport.util.PoiUtil;

public class ExportAction extends BaseAction
{
  private static Logger logger = Logger.getLogger(ExportAction.class);
  private String type;
  private String uid;
  private String reportJson;
  public String tableModel;
  public String dataSetsModel;
  public String parmsModel;
  public String reporttype;
  public String jsonlist;
  public static Map<String, String> exportMap = new HashMap();
  private int backFlag;

  public String exportFlag()
  {
    int maxexport = 10;
    HttpServletRequest request = ServletActionContext.getRequest();
    String uuid = request.getParameter("uid");
    String stat = request.getParameter("stat");
    String mep = (String)JRUtilNew.baseMap.get("maxexport");
    if (mep != null)
      try {
        maxexport = Integer.parseInt(mep);
      }
      catch (Exception localException1) {
      }
    int curexport = 0;
    String cep = (String)exportMap.get("curexport");
    String expuuid = (String)exportMap.get(uuid);
    if ((stat != null) && (stat.equals("1")))
    {
      if ((cep != null) && (cep.equals("1"))) {
        exportMap.put("curexport", "0");
        exportMap.remove(this.uid);
      } else {
        try {
          int xcep = Integer.parseInt(cep) - 1;
          if (xcep < 0)
            xcep = 0;
          exportMap.put("curexport", String.valueOf(xcep));
          exportMap.remove(this.uid);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    else if ((cep == null) || (cep.equals("0"))) {
      exportMap.put("curexport", "1");
      curexport = 1;
    } else {
      try {
        if ((maxexport > Integer.parseInt(cep)) && (expuuid == null)) {
          exportMap.put("curexport", String.valueOf(Integer.parseInt(cep) + 1));
        }
        if (maxexport >= curexport)
          curexport = Integer.parseInt(cep) + 1;
      }
      catch (Exception localException2)
      {
      }
    }
    if (maxexport >= curexport)
    {
      this.backFlag = 1;
    }
    else this.backFlag = 0;

    return "ajaxsuc";
  }

  public String exportExcel()
  {
    long startTime = System.currentTimeMillis();

    HttpServletResponse response = ServletActionContext.getResponse();
    XMLbean xmlb = null;
    if ((this.uid != null) && (this.uid.length() == 32)) {
      if (DesignXmlUtil.xmlMap.get(this.uid) != null) {
        xmlb = (XMLbean)DesignXmlUtil.xmlMap.get(this.uid);
      } else {
        String filePath = JRUtilNew.getBaseFilePath();
        Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + this.uid + ".xml");
        xmlb = (XMLbean)map.get("xml");
      }
    } else {
      JsonReportEntity json = (JsonReportEntity)JSON.parseObject(this.reportJson, JsonReportEntity.class);
      ReportEntity report = JsonToXMLUtil.JsonToXml(json);
      Map map = DesignXmlUtil.openXML(report);
      xmlb = (XMLbean)map.get("xml");
    }
    String etype = xmlb.getType();
    String etableModel = xmlb.getBody();
    String edataSetsModel = xmlb.getDataSets();
    String eparmsModel = xmlb.getParmslist();
    String efieldModel = xmlb.getFillreports();
    JSONArray jsonArray = new JSONArray();
    if (!"".equals(this.jsonlist))
      jsonArray = JSON.parseArray(this.jsonlist);
    JSONArray ejsonlist = jsonArray;
    Map<String,String> parmsMap = new HashMap();
    Map<String,Object> parmMap = new PreviewService().compileParms(eparmsModel);
    HttpServletRequest request = ServletActionContext.getRequest();
    if (parmMap != null)
      for (String key : parmMap.keySet()) {
        String str = request.getParameter(key);
        if (str == null)
          str = "";
        parmsMap.put(key, str);
      }
    try
    {
      String fileName = "";
      try
      {
        String reqUrl = request.getHeader("Referer");
        reqUrl = reqUrl.substring(reqUrl.indexOf("?") + 1);
        String[] parmArr = reqUrl.split("&");
        Map rmap = new HashMap();
        for (String str : parmArr) {
          if (str.split("=").length == 2)
            rmap.put(str.split("=")[0], str.split("=")[1]);
        }
        String filePath = JRUtilNew.getBaseFilePath();
        Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + (String)rmap.get("uid") + ".xml");
        XMLbean lbean = (XMLbean)map.get("xml");
        fileName = (lbean.getTitle() != null) && (!"".equals(lbean.getTitle())) ? lbean.getTitle() : "temp";
      } catch (Exception ex) {
        fileName = "temp";
        response.setHeader("Set-Cookie", "fileDownload=false; path=/");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      }
      JRUtilNew.exportExcel(etableModel, edataSetsModel, eparmsModel, efieldModel, this.uid, ejsonlist, fileName, request, response, Boolean.valueOf(false));
    } catch (Exception e) {
      e.printStackTrace();
      response.setHeader("Set-Cookie", "fileDownload=false; path=/");
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    }

    String cep = (String)exportMap.get("curexport");
    if ((cep != null) && (cep.equals("1"))) {
      exportMap.put("curexport", "0");
      exportMap.remove(this.uid);
    } else {
      try {
        int xcep = Integer.parseInt(cep) - 1;
        if (xcep < 0)
          xcep = 0;
        exportMap.put("curexport", String.valueOf(xcep));
        exportMap.remove(this.uid);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    Long endTime = Long.valueOf(System.currentTimeMillis());
    logger.info("导出总用时：" + (endTime.longValue() - startTime));
    return null;
  }

  public String exportFromDesignNew()
  {
    long startTime = System.currentTimeMillis();

    XMLbean xmlb = null;
    if ((this.uid != null) && (this.uid.length() == 32)) {
      if (DesignXmlUtil.xmlMap.get(this.uid) != null) {
        xmlb = (XMLbean)DesignXmlUtil.xmlMap.get(this.uid);
      } else {
        String filePath = JRUtilNew.getBaseFilePath();
        Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + this.uid + ".xml");
        xmlb = (XMLbean)map.get("xml");
      }
    } else {
      JsonReportEntity json = (JsonReportEntity)JSON.parseObject(this.reportJson, JsonReportEntity.class);
      ReportEntity report = JsonToXMLUtil.JsonToXml(json);
      Map map = DesignXmlUtil.openXML(report);
      xmlb = (XMLbean)map.get("xml");
    }

    JSONArray jsonArray = new JSONArray();
    if (!"".equals(this.jsonlist)) {
      jsonArray = JSON.parseArray(this.jsonlist);
    }
    Map<String,String> parmsMap = new HashMap();
    Map<String,Object> parmMap = new PreviewService().compileParms(PreviewAction.parmsModel);
    HttpServletRequest request = ServletActionContext.getRequest();
    if (parmMap != null) {
      for (String key : parmMap.keySet()) {
        String str = request.getParameter(key);
        if (str == null)
          str = "";
        parmsMap.put(key, str);
      }
    }
    Map dataMap = null;
    try
    {
      dataMap = PreviewReportAction.pubPreDes(xmlb.getType(), xmlb.getBody(), xmlb.getDataSets(), xmlb.getFillreports(), xmlb.getParmslist(), 1, 1, -1, this.uid, Boolean.valueOf(false), xmlb.getToolbar(), Boolean.valueOf(false));
    } catch (Exception e) {
      e.printStackTrace();
    }

    HttpServletResponse response = ServletActionContext.getResponse();
    try {
      if ((dataMap != null) && (dataMap.get("body") != null))
      {
        String fileName = "";
        try
        {
          String reqUrl = request.getHeader("Referer");
          reqUrl = reqUrl.substring(reqUrl.indexOf("?") + 1);
          String[] parmArr = reqUrl.split("&");
          Map rmap = new HashMap();
          for (String str : parmArr) {
            if (str.split("=").length == 2)
              rmap.put(str.split("=")[0], str.split("=")[1]);
          }
          String filePath = JRUtilNew.getBaseFilePath();
          Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + (String)rmap.get("uid") + ".xml");
          XMLbean lbean = (XMLbean)map.get("xml");
          fileName = (lbean.getTitle() != null) && (!"".equals(lbean.getTitle())) ? lbean.getTitle() : "temp";
        } catch (Exception ex) {
          fileName = "temp";
          response.setHeader("Set-Cookie", "fileDownload=false; path=/");
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        }

        PoiUtil.exportExcel(dataMap.get("body").toString(), fileName, request, response);
        dataMap = null;
      } else {
        response.setHeader("Set-Cookie", "fileDownload=false; path=/");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.setHeader("Set-Cookie", "fileDownload=false; path=/");
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    }

    String cep = (String)exportMap.get("curexport");
    if ((cep != null) && (cep.equals("1"))) {
      exportMap.put("curexport", "0");
      exportMap.remove(this.uid);
    } else {
      try {
        int xcep = Integer.parseInt(cep) - 1;
        if (xcep < 0)
          xcep = 0;
        exportMap.put("curexport", String.valueOf(xcep));
        exportMap.remove(this.uid);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    Long endTime = Long.valueOf(System.currentTimeMillis());
    logger.info("导出总用时：" + (endTime.longValue() - startTime));
    return null;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUid() {
    return this.uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getReportJson() {
    return this.reportJson;
  }

  public void setReportJson(String reportJson) {
    this.reportJson = reportJson;
  }

  public String getTableModel() {
    return this.tableModel;
  }

  public void setTableModel(String tableModel) {
    this.tableModel = tableModel;
  }

  public String getDataSetsModel() {
    return this.dataSetsModel;
  }

  public void setDataSetsModel(String dataSetsModel) {
    this.dataSetsModel = dataSetsModel;
  }

  public String getParmsModel() {
    return this.parmsModel;
  }

  public void setParmsModel(String parmsModel) {
    this.parmsModel = parmsModel;
  }

  public String getReporttype() {
    return this.reporttype;
  }

  public void setReporttype(String reporttype) {
    this.reporttype = reporttype;
  }

  public String getJsonlist() {
    return this.jsonlist;
  }

  public void setJsonlist(String jsonlist) {
    this.jsonlist = jsonlist;
  }

  public int getBackFlag() {
    return this.backFlag;
  }

  public void setBackFlag(int backFlag) {
    this.backFlag = backFlag;
  }
}

/* 
 * Qualified Name:     report.java.jrreport.action.ExportAction
*
 */