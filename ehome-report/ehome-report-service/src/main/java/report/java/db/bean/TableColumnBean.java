package report.java.db.bean;

public class TableColumnBean
{
  private String columnName;
  private String columnType;
  private String columnComments;

  public String getColumnName()
  {
    return this.columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public String getColumnType() {
    return this.columnType;
  }
  public void setColumnType(String columnType) {
    this.columnType = columnType;
  }
  public String getColumnComments() {
    return this.columnComments;
  }
  public void setColumnComments(String columnComments) {
    this.columnComments = columnComments;
  }
}

/*
 * Qualified Name:     report.java.db.bean.TableColumnBean
*
 */