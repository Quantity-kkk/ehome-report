package report.java.jrreport.action;

import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import report.java.design.action.BaseAction;

public class PreviewAction extends BaseAction
{
  private static Logger logger = Logger.getLogger(PreviewAction.class);
  public static String tableModel;
  public static String dataSetsModel;
  public static String parmsModel;
  public static String type;
  public static Map<String, Object> tableMap = new HashMap();
  public static Map<String, Object> parmsMap = new HashMap();
  public static List<String> dateFields = new ArrayList();
  public static int pdFlag;
  private String opt;
  private String uuid;
  private String parms;
  public static JsonArray jsonlist;

  public String previewDesign()
  {
    pdFlag = 1;
    this.message = this.opt;
    return this.SUCCESS;
  }

  public String preDes()
  {
    pdFlag = 1;
    this.message = this.opt;
    return "success1";
  }

  public String getTableModel() {
    return tableModel;
  }

  public void setTableModel(String tableModel) {
    tableModel = tableModel;
  }

  public String getDataSetsModel() {
    return dataSetsModel;
  }

  public void setDataSetsModel(String dataSetsModel) {
    dataSetsModel = dataSetsModel;
  }

  public String getParmsModel() {
    return parmsModel;
  }
  public void setParmsModel(String parmsModel) {
    parmsModel = parmsModel;
  }
  public String getType() {
    return type;
  }

  public void setType(String type) {
    type = type;
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
  public JsonArray getJsonlist() {
    return jsonlist;
  }
  public void setJsonlist(JsonArray jsonlist) {
    jsonlist = jsonlist;
  }
}

/* 
 * Qualified Name:     report.java.jrreport.action.PreviewAction
*
 */