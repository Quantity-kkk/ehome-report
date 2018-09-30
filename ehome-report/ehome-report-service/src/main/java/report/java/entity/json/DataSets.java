package report.java.entity.json;

import java.util.List;

public class DataSets
{
  private boolean dic;
  private boolean isvalid;
  private String dataSourceName;
  private String dataSetName;
  private String commandText;
  private String keyName;
  private String optCode;
  private String optName;
  private List<Field> field;
  private List<Parms> parms;

  public void setDic(boolean dic)
  {
    this.dic = dic;
  }
  public boolean getDic() {
    return this.dic;
  }

  public void setIsvalid(boolean isvalid) {
    this.isvalid = isvalid;
  }
  public boolean getIsvalid() {
    return this.isvalid;
  }

  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }
  public String getDataSourceName() {
    return this.dataSourceName;
  }

  public void setDataSetName(String dataSetName) {
    this.dataSetName = dataSetName;
  }
  public String getDataSetName() {
    return this.dataSetName;
  }

  public void setCommandText(String commandText) {
    this.commandText = commandText;
  }
  public String getCommandText() {
    return this.commandText;
  }

  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }
  public String getKeyName() {
    return this.keyName;
  }

  public void setOptCode(String optCode) {
    this.optCode = optCode;
  }
  public String getOptCode() {
    return this.optCode;
  }

  public void setOptName(String optName) {
    this.optName = optName;
  }
  public String getOptName() {
    return this.optName;
  }

  public void setField(List<Field> field) {
    this.field = field;
  }
  public List<Field> getField() {
    return this.field;
  }

  public void setParms(List<Parms> parms) {
    this.parms = parms;
  }
  public List<Parms> getParms() {
    return this.parms;
  }
}

/*
 * Qualified Name:     report.java.entity.json.DataSets
*
 */