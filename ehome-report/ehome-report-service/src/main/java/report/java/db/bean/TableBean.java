package report.java.db.bean;

import java.util.List;

public class TableBean
{
  private String tableName;
  private String tableComments;
  private List<TableColumnBean> tableColumnList;

  public String getTableName()
  {
    return this.tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getTableComments() {
    return this.tableComments;
  }
  public void setTableComments(String tableComments) {
    this.tableComments = tableComments;
  }
  public List<TableColumnBean> getTableColumnList() {
    return this.tableColumnList;
  }
  public void setTableColumnList(List<TableColumnBean> tableColumnList) {
    this.tableColumnList = tableColumnList;
  }
}

/* 
 * Qualified Name:     report.java.db.bean.TableBean
*
 */