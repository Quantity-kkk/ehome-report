package report.java.rpt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import report.java.design.action.BaseAction;
import report.java.design.bean.XMLbean;
import report.java.design.util.DesignXmlUtil;
import report.java.jrreport.action.PreviewReportAction;
import report.java.jrreport.util.JRUtilNew;
import report.java.rpt.bean.ColExtendBean;
import report.java.rpt.service.RptCustomService;

public class CustomPreviewAction extends BaseAction
{
  private String rep_id;
  private Map<String, Object> dataMap;
  public static List<String> dateFields = new ArrayList();
  public int currentPage;
  public int pageType;
  public int pageSize;

  public String customPreview()
    throws Exception
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    String tempWhere = "";

    List<ColExtendBean> conditionList = new RptCustomService().customPreview(this.rep_id);
    for (ColExtendBean bean : conditionList) {
      String colName = bean.getCol_name();
      String[] searchValue = request.getParameterValues(colName);
      String tempValue = "";
      String colType = bean.getCol_type();
      if (searchValue != null) {
        if ("1".equals(colType)) {
          tempValue = searchValue[0];
          if (!"".equals(tempValue)) {
            tempValue = "'" + searchValue[0] + "'";

            String colOperate = bean.getWhere_operate1();
            if (!"".equals(colOperate)) {
              if ("like".equalsIgnoreCase(colOperate))
                tempWhere = tempWhere + " AND " + colName + " " + colOperate + " '%" + tempValue.replaceAll("'", "") + "%' ";
              else
                tempWhere = tempWhere + " AND " + colName + " " + colOperate + " " + tempValue + " ";
            } else {
              colOperate = "=";
              tempWhere = tempWhere + " AND " + colName + " " + colOperate + " " + tempValue + " ";
            }

          }

        }
        else if (("2".equals(colType)) || ("3".equals(colType)))
        {
          String tempStr = "";
          String colOperate1 = bean.getWhere_operate1();
          if (!"".equals(colOperate1)) {
            if (colOperate1.contains(">")) {
              tempStr = searchValue[0];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate1 + " " + tempStr + " ";
              }
            } else {
              tempStr = searchValue[1];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate1 + " " + tempStr + " ";
              }
            }
          }
          String colOperate2 = bean.getWhere_operate2();
          if (!"".equals(colOperate2))
            if (colOperate2.contains(">")) {
              tempStr = searchValue[0];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate2 + " " + tempStr + " ";
              }
            } else {
              tempStr = searchValue[1];
              if (!"".equals(tempStr)) {
                tempStr = "'" + tempStr + "'";
                tempWhere = tempWhere + " AND " + colName + " " + colOperate2 + " " + tempStr + " ";
              }
            }
        }
        else if ("4".equals(colType)) {
          if (searchValue != null) {
            for (int i = 0; i < searchValue.length; i++) {
              if (i != searchValue.length - 1)
                tempValue = tempValue + "'" + searchValue[i] + "'" + ",";
              else {
                tempValue = tempValue + "'" + searchValue[i] + "'";
              }
            }
            String colOperate = bean.getWhere_operate1();
            tempWhere = tempWhere + " AND " + colName + " " + colOperate + " (" + tempValue + ") ";
          }
        } else {
          if (!"5".equals(colType))
            continue;
          tempValue = searchValue[0];
          if (!"".equals(tempValue)) {
            tempValue = "'" + searchValue[0] + "'";
            String colOperate = bean.getWhere_operate1();
            tempWhere = tempWhere + " AND " + colName + " " + colOperate + " " + tempValue + " ";
          }
        }
      }
    }

    XMLbean lbean = null;
    if (DesignXmlUtil.xmlMap.get(this.rep_id) != null) {
      lbean = (XMLbean)DesignXmlUtil.xmlMap.get(this.rep_id);
    } else {
      String filePath = JRUtilNew.getBaseFilePath();
      Map map = DesignXmlUtil.openXML(filePath + System.getProperty("file.separator") + this.rep_id + ".xml");
      lbean = (XMLbean)map.get("xml");
    }
    if (lbean != null) {
      this.dataMap = PreviewReportAction.pubPreDes(lbean.getType(), lbean.getBody(), lbean.getDataSets(), lbean.getFillreports(), lbean.getParmslist(), this.currentPage, this.pageType, this.pageSize, this.rep_id, Boolean.valueOf(false), lbean.getToolbar(), Boolean.valueOf(true));
    }

    dateFields.clear();
    return "success";
  }

  public String getRep_id() {
    return this.rep_id;
  }

  public void setRep_id(String rep_id) {
    this.rep_id = rep_id;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }

  public int getCurrentPage() {
    return this.currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getPageType() {
    return this.pageType;
  }

  public void setPageType(int pageType) {
    this.pageType = pageType;
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}

/* 
 * Qualified Name:     report.java.rpt.action.CustomPreviewAction
*
 */