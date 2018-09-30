package report.java.rpt.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.rpt.bean.ColConfigBean;
import report.java.rpt.bean.TabConfigBean;
import report.java.rpt.service.RptConfigService;

public class RptConfigAction extends BaseAction
{
  private String tab_id;
  private String col_id;
  private List<TabConfigBean> tabConfigList;
  private List<ColConfigBean> colConfigList;
  private List<DataSourceBean> dataSourceBeanList;
  private TabConfigBean tabConfigBean;
  private ColConfigBean colConfigBean;
  private String tab_name;
  private String tab_desc;
  private int backFlag;
  private String realPath = BaseAction.realPath + "db-config.xml";

  public String getTabList() throws Exception
  {
    RptConfigService rptConfigService = new RptConfigService();
    this.tabConfigList = rptConfigService.getTabList();

    return "ajaxsuc";
  }

  public String getSearchTabList() throws Exception
  {
    RptConfigService rptConfigService = new RptConfigService();
    this.tabConfigList = rptConfigService.getSearchTabList(this.tab_name, this.tab_desc);
    return "ajaxsuc";
  }

  public String getColList() throws Exception
  {
    RptConfigService rptConfigService = new RptConfigService();
    this.colConfigList = rptConfigService.getColList(this.tab_id);
    return "ajaxsuc";
  }

  public String getdataSourceBeanList() throws Exception
  {
    this.dataSourceBeanList = XmlUtil.selectXMLAll(this.realPath);
    return "success";
  }

  public String getTabById() throws Exception
  {
    RptConfigService rptConfigService = new RptConfigService();
    this.tabConfigBean = rptConfigService.getTabById(this.tab_id);
    this.dataSourceBeanList = XmlUtil.selectXMLAll(this.realPath);
    return "success";
  }

  public String getColById() throws Exception
  {
    RptConfigService rptConfigService = new RptConfigService();
    this.colConfigBean = rptConfigService.getColById(this.col_id);
    String accessType = ServletActionContext.getRequest().getParameter("accessType");
    if ("0".equals(accessType)) {
      return "add";
    }
    return "ajaxsuc";
  }

  public String insertTab()
    throws Exception
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    TabConfigBean tabConfigBean = new TabConfigBean();
    String tabName = request.getParameter("tab_name");

    TabConfigBean oldTabBean = new RptConfigService().getTabByName(tabName);
    if (oldTabBean != null) {
      this.backFlag = 2;
    } else {
      tabConfigBean.setTab_name(request.getParameter("tab_name"));
      tabConfigBean.setTab_desc(request.getParameter("tab_desc"));
      tabConfigBean.setTab_dsname(request.getParameter("DataSourceName"));
      tabConfigBean.setTab_type("1");
      this.backFlag = new RptConfigService().insertTab(tabConfigBean);
    }
    return "ajaxsuc";
  }

  public String insertCol()
    throws Exception
  {
    HttpServletRequest request = ServletActionContext.getRequest();

    String tabId = request.getParameter("tab_id");
    String colName = request.getParameter("col_name");

    ColConfigBean tempBean = new RptConfigService().getColByTabidAndColname(tabId, colName);

    if (tempBean == null) {
      ColConfigBean colConfigBean = new ColConfigBean();
      colConfigBean.setTab_id(tabId);
      colConfigBean.setCol_name(colName);
      colConfigBean.setCol_desc(request.getParameter("col_desc"));
      colConfigBean.setCol_type(request.getParameter("col_type"));
      colConfigBean.setCol_dic(request.getParameter("col_dic"));
      colConfigBean.setCol_flag(request.getParameter("col_flag"));
      colConfigBean.setCol_sortflag(request.getParameter("col_sortflag"));
      colConfigBean.setCol_operate(request.getParameter("col_operate"));
      String order = request.getParameter("col_order");
      if (!"".equals(order))
        colConfigBean.setCol_order(Integer.parseInt(order));
      else
        colConfigBean.setCol_order(0);
      colConfigBean.setCol_groupflag(request.getParameter("col_groupflag"));
      colConfigBean.setCol_size(request.getParameter("col_size"));
      colConfigBean.setField_type(request.getParameter("field_type"));
      colConfigBean.setShow_format(request.getParameter("show_format"));
      colConfigBean.setSave_format(request.getParameter("save_format"));
      this.backFlag = new RptConfigService().insertCol(colConfigBean);
    } else {
      this.backFlag = 2;
    }
    return "ajaxsuc";
  }

  public String updateTab() throws Exception
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    TabConfigBean tabConfigBean = new TabConfigBean();
    String tabId = request.getParameter("tab_id");
    String tabName = request.getParameter("tab_name");
    TabConfigBean tempBean = new RptConfigService().getTabById(tabId);
    if (tempBean != null) {
      String tempName = tempBean.getTab_name();
      if (tempName.equalsIgnoreCase(tabName)) {
        tabConfigBean.setTab_id(request.getParameter("tab_id"));
        tabConfigBean.setTab_name(request.getParameter("tab_name"));
        tabConfigBean.setTab_desc(request.getParameter("tab_desc"));
        tabConfigBean.setTab_dsname(request.getParameter("DataSourceName"));
        tabConfigBean.setTab_type("1");

        this.backFlag = new RptConfigService().updateTab(tabConfigBean);
      } else {
        TabConfigBean tempBean1 = new RptConfigService().getTabByName(tabName);
        if (tempBean1 != null) {
          this.backFlag = 2;
        }
        else {
          tabConfigBean.setTab_id(request.getParameter("tab_id"));
          tabConfigBean.setTab_name(request.getParameter("tab_name"));
          tabConfigBean.setTab_desc(request.getParameter("tab_desc"));
          tabConfigBean.setTab_dsname(request.getParameter("tab_dsname"));
          tabConfigBean.setTab_type("1");
          this.backFlag = new RptConfigService().updateTab(tabConfigBean);
        }
      }
    }
    return "ajaxsuc";
  }

  public String updateCol() throws Exception
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    ColConfigBean colConfigBean = new ColConfigBean();
    String colId = request.getParameter("col_id");
    String colName = request.getParameter("col_name");
    String tabId = request.getParameter("tab_id");
    ColConfigBean tempBean = new RptConfigService().getColById(colId);
    if (tempBean != null) {
      String tempColName = tempBean.getCol_name();
      if (tempColName.equalsIgnoreCase(colName))
      {
        colConfigBean.setCol_id(colId);
        colConfigBean.setTab_id(tabId);
        colConfigBean.setCol_name(colName);
        colConfigBean.setCol_desc(request.getParameter("col_desc"));
        colConfigBean.setCol_type(request.getParameter("col_type"));
        colConfigBean.setCol_dic(request.getParameter("col_dic"));
        colConfigBean.setCol_flag(request.getParameter("col_flag"));
        colConfigBean.setCol_sortflag(request.getParameter("col_sortflag"));
        colConfigBean.setCol_operate(request.getParameter("col_operate"));
        if ((!"".equals(request.getParameter("col_order"))) && (request.getParameter("col_order") != null))
          colConfigBean.setCol_order(Integer.parseInt(request.getParameter("col_order")));
        else
          colConfigBean.setCol_order(0);
        colConfigBean.setCol_groupflag(request.getParameter("col_groupflag"));
        colConfigBean.setCol_size(request.getParameter("col_size"));
        colConfigBean.setField_type(request.getParameter("field_type"));
        colConfigBean.setShow_format(request.getParameter("show_format"));
        colConfigBean.setSave_format(request.getParameter("save_format"));
        this.backFlag = new RptConfigService().updateCol(colConfigBean);
      }
      else {
        ColConfigBean tempBean1 = new RptConfigService().getColByTabidAndColname(tabId, colName);
        if (tempBean1 != null) {
          this.backFlag = 2;
        } else {
          colConfigBean.setCol_id(colId);
          colConfigBean.setTab_id(tabId);
          colConfigBean.setCol_name(colName);
          colConfigBean.setCol_desc(request.getParameter("col_desc"));
          colConfigBean.setCol_type(request.getParameter("col_type"));
          colConfigBean.setCol_dic(request.getParameter("col_dic"));
          colConfigBean.setCol_flag(request.getParameter("col_flag"));
          colConfigBean.setCol_sortflag(request.getParameter("col_sortflag"));
          colConfigBean.setCol_operate(request.getParameter("col_operate"));
          if ((!"".equals(request.getParameter("col_order"))) && (request.getParameter("col_order") != null))
            colConfigBean.setCol_order(Integer.parseInt(request.getParameter("col_order")));
          else
            colConfigBean.setCol_order(0);
          colConfigBean.setCol_groupflag(request.getParameter("col_groupflag"));
          colConfigBean.setCol_size(request.getParameter("col_size"));
          colConfigBean.setField_type(request.getParameter("field_type"));
          colConfigBean.setShow_format(request.getParameter("show_format"));
          colConfigBean.setSave_format(request.getParameter("save_format"));
          this.backFlag = new RptConfigService().updateCol(colConfigBean);
        }
      }
    }
    return "ajaxsuc";
  }

  public String delTabById()
    throws Exception
  {
    List colConfigList = new RptConfigService().getColList(this.tab_id);
    if (colConfigList.size() > 0)
      this.backFlag = 2;
    else {
      this.backFlag = new RptConfigService().delTabById(this.tab_id);
    }
    return "ajaxsuc";
  }

  public String delColById() throws Exception
  {
    RptConfigService rptConfigService = new RptConfigService();
    rptConfigService.delColById(this.col_id);
    this.backFlag = 1;
    return "ajaxsuc";
  }

  public String getTab_id()
  {
    return this.tab_id;
  }

  public String getCol_id() {
    return this.col_id;
  }

  public void setCol_id(String col_id) {
    this.col_id = col_id;
  }

  public void setTab_id(String tab_id) {
    this.tab_id = tab_id;
  }

  public List<TabConfigBean> getTabConfigList() {
    return this.tabConfigList;
  }

  public void setTabConfigList(List<TabConfigBean> tabConfigList) {
    this.tabConfigList = tabConfigList;
  }

  public List<ColConfigBean> getColConfigList() {
    return this.colConfigList;
  }

  public void setColConfigList(List<ColConfigBean> colConfigList) {
    this.colConfigList = colConfigList;
  }

  public TabConfigBean getTabConfigBean() {
    return this.tabConfigBean;
  }

  public void setTabConfigBean(TabConfigBean tabConfigBean) {
    this.tabConfigBean = tabConfigBean;
  }

  public ColConfigBean getColConfigBean() {
    return this.colConfigBean;
  }

  public void setColConfigBean(ColConfigBean colConfigBean) {
    this.colConfigBean = colConfigBean;
  }

  public String getTab_name() {
    return this.tab_name;
  }

  public void setTab_name(String tab_name) {
    this.tab_name = tab_name;
  }

  public String getTab_desc() {
    return this.tab_desc;
  }

  public void setTab_desc(String tab_desc) {
    this.tab_desc = tab_desc;
  }

  public int getBackFlag() {
    return this.backFlag;
  }

  public void setBackFlag(int backFlag) {
    this.backFlag = backFlag;
  }

  public List<DataSourceBean> getDataSourceBeanList() {
    return this.dataSourceBeanList;
  }

  public void setDataSourceBeanList(List<DataSourceBean> dataSourceBeanList) {
    this.dataSourceBeanList = dataSourceBeanList;
  }
}

/* 
 * Qualified Name:     report.java.rpt.action.RptConfigAction
*
 */