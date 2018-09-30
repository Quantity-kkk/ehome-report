package report.java.rpt.action;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.rpt.bean.CusReportBean;
import report.java.rpt.bean.ReportFormBean;
import report.java.rpt.service.RptCustomService;
import report.java.rpt.util.DbConfig;
import report.java.rpt.util.RptCustomCreate;

public class RptCustomAction extends BaseAction
{
  private String realPath = BaseAction.realPath + "db-config.xml";
  private List<CusReportBean> cusReportList;
  private String rep_name;
  private String rep_id;
  private ReportFormBean reportFormBean;
  private int isSuccess;
  private String testSql;
  private String dsname;
  private Map<String, Object> dataMap;

  public String getTabList()
    throws Exception
  {
    RptCustomService rptCustomService = new RptCustomService();
    this.cusReportList = rptCustomService.getTabList();
    return "success";
  }

  public String getTabListMy() throws Exception
  {
    RptCustomService rptCustomService = new RptCustomService();
    this.cusReportList = rptCustomService.getTabList();
    return "success";
  }

  public String getSearchRepList() throws Exception
  {
    RptCustomService rptCustomService = new RptCustomService();
    this.cusReportList = rptCustomService.getSearchRepList(this.rep_name);
    return "success";
  }

  public String getSearchRepListMy() throws Exception
  {
    RptCustomService rptCustomService = new RptCustomService();
    this.cusReportList = rptCustomService.getSearchRepList(this.rep_name);
    return "list";
  }

  public String delRepById() throws Exception
  {
    RptCustomService rptCustomService = new RptCustomService();
    rptCustomService.delRepById(this.rep_id);
    this.dataMap = new HashMap();
    this.dataMap.put("flag", "1");
    return "success";
  }

  public String createReport() throws Exception
  {
    CusReportBean bean = new CusReportBean();
    bean.setRep_name(this.reportFormBean.getRep_name());
    bean.setRep_scope(this.reportFormBean.getRep_scope());
    bean.setRep_type(this.reportFormBean.getRep_type());
    bean.setRep_flag("1");
    bean.setTab_name(this.reportFormBean.getResult_table_name());

    new RptCustomService().insertReport(bean);

    String condText = this.reportFormBean.getCondText();
    if ((condText != null) && (!"".equals(condText))) {
      String[] wf = condText.split("@");
      for (int n = 0; n < wf.length; n++) {
        String[] exps = wf[n].split("#");
        Arrays.sort(exps, 1, exps.length);
        bean.setWhere_field(exps[0]);
        bean.setWhere_operate1(exps[1]);
        if (exps.length >= 3)
          bean.setWhere_operate2(exps[2]);
        else {
          bean.setWhere_operate2("");
        }
        bean.setWhere_order(n + 1);
        new RptCustomService().insertWhere(bean);
      }

    }

    bean.setReportSql(this.reportFormBean.getReportSql());
    bean.setParms(this.reportFormBean.getCondText());
    bean.setCn_name(this.reportFormBean.getCn_name());
    bean.setRep_dsname(this.reportFormBean.getResult_dsname());
    new RptCustomCreate().reportCreate(bean);
    this.dataMap = new HashMap();
    this.dataMap.put("flag", "1");
    return "success";
  }

  public String sqlTest() throws Exception
  {
    DataSourceBean dataSourceBean = new DataSourceBean();

    dataSourceBean.setDataSourceName(this.dsname);
    DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, this.realPath);
    Connection conn = DBUtil.getConn(dsb);
    this.isSuccess = DbConfig.sqlTest(this.testSql, conn);
    return "ajaxsuc";
  }

  public String initSearch()
    throws Exception
  {
    this.dataMap = new HashMap();
    String searchTab = "";
    RptCustomService rptCustomService = new RptCustomService();
    searchTab = rptCustomService.findConitionById(this.rep_id);

    this.dataMap.put("searchParm", searchTab);

    return this.SUCCESS;
  }

  public List<CusReportBean> getCusReportList() {
    return this.cusReportList;
  }

  public void setCusReportList(List<CusReportBean> cusReportList) {
    this.cusReportList = cusReportList;
  }

  public String getRep_name() {
    return this.rep_name;
  }

  public void setRep_name(String rep_name) {
    this.rep_name = rep_name;
  }

  public String getRep_id() {
    return this.rep_id;
  }

  public void setRep_id(String rep_id) {
    this.rep_id = rep_id;
  }

  public ReportFormBean getReportFormBean() {
    return this.reportFormBean;
  }

  public void setReportFormBean(ReportFormBean reportFormBean) {
    this.reportFormBean = reportFormBean;
  }

  public int getIsSuccess() {
    return this.isSuccess;
  }

  public void setIsSuccess(int isSuccess) {
    this.isSuccess = isSuccess;
  }

  public String getTestSql() {
    return this.testSql;
  }

  public void setTestSql(String testSql) {
    this.testSql = testSql;
  }

  public String getDsname() {
    return this.dsname;
  }

  public void setDsname(String dsname) {
    this.dsname = dsname;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }
}

/*
 * Qualified Name:     report.java.rpt.action.RptCustomAction
*
 */