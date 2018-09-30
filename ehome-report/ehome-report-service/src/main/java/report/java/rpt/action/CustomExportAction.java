package report.java.rpt.action;

import com.alibaba.fastjson.JSONArray;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import report.java.design.action.BaseAction;
import report.java.design.bean.XMLbean;
import report.java.design.util.DesignXmlUtil;
import report.java.jrreport.util.JRUtilNew;
import report.java.rpt.service.RptCustomService;

public class CustomExportAction extends BaseAction
{
  private String uid;
  private String type;

  public String customExport()
    throws Exception
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    String fileName = "";
    JSONArray jsonArray = new JSONArray();
    List conditionList = new RptCustomService().customPreview(this.uid);

    XMLbean lbean = null;
    if (DesignXmlUtil.xmlMap.get(this.uid) != null) {
      lbean = (XMLbean)DesignXmlUtil.xmlMap.get(this.uid);
    } else {
      String filePath = JRUtilNew.getBaseFilePath();
      Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + this.uid + ".xml");
      lbean = (XMLbean)map.get("xml");
    }

    fileName = (lbean.getTitle() != null) && (!"".equals(lbean.getTitle())) ? lbean.getTitle() : "temp";
    try {
      if (lbean != null)
        JRUtilNew.exportExcel(lbean.getBody(), lbean.getDataSets(), lbean.getParmslist(), lbean.getFillreports(), this.uid, jsonArray, fileName, request, response, Boolean.valueOf(true));
    }
    catch (Exception e) {
      response.setHeader("Set-Cookie", "fileDownload=false; path=/");
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      e.printStackTrace();
    }
    return null;
  }

  public String getUid() {
    return this.uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }
}

/* 
 * Qualified Name:     report.java.rpt.action.CustomExportAction
*
 */