package report.java.entity.json;

import java.util.List;

public class Row
{
  private List<Col> col;
  private int rowspan;
  private String rowCategory;

  public void setCol(List<Col> col)
  {
    this.col = col;
  }
  public List<Col> getCol() {
    return this.col;
  }

  public void setRowspan(int rowspan) {
    this.rowspan = rowspan;
  }
  public int getRowspan() {
    return this.rowspan;
  }

  public void setRowCategory(String rowCategory) {
    this.rowCategory = rowCategory;
  }
  public String getRowCategory() {
    return this.rowCategory;
  }
}

/*
 * Qualified Name:     report.java.entity.json.Row
*
 */