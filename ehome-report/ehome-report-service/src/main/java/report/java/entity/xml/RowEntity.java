package report.java.entity.xml;

import java.util.List;

public class RowEntity
{
  private String category;
  private List<ColEntity> cols;

  public String getCategory()
  {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public List<ColEntity> getCols() {
    return this.cols;
  }

  public void setCols(List<ColEntity> cols) {
    this.cols = cols;
  }
}

/*
 * Qualified Name:     report.java.entity.xml.RowEntity
*
 */