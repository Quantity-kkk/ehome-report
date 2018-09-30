package report.java.entity.xml;

import java.util.List;

public class DataSetEntity
{
  private String name;
  private Boolean isdic;
  private String datasourcename;
  private String commandtext;
  private List<String> parameters;
  private String keyName;
  private String optCode;
  private String optName;
  private List<FieldEntity> fileds;

  public String getName()
  {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getIsdic() {
    return this.isdic;
  }

  public void setIsdic(Boolean isdic) {
    this.isdic = isdic;
  }

  public String getDatasourcename() {
    return this.datasourcename;
  }

  public void setDatasourcename(String datasourcename) {
    this.datasourcename = datasourcename;
  }

  public String getCommandtext() {
    return this.commandtext;
  }

  public void setCommandtext(String commandtext) {
    this.commandtext = commandtext;
  }

  public List<String> getParameters() {
    return this.parameters;
  }

  public void setParameters(List<String> parameters) {
    this.parameters = parameters;
  }

  public String getKeyName() {
    return this.keyName;
  }

  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }

  public String getOptCode() {
    return this.optCode;
  }

  public void setOptCode(String optCode) {
    this.optCode = optCode;
  }

  public String getOptName() {
    return this.optName;
  }

  public void setOptName(String optName) {
    this.optName = optName;
  }

  public List<FieldEntity> getFileds() {
    return this.fileds;
  }

  public void setFileds(List<FieldEntity> fileds) {
    this.fileds = fileds;
  }
}

/* 
 * Qualified Name:     report.java.entity.xml.DataSetEntity
*
 */