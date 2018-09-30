package report.java.entity.json;

public class MergeCells
{
  private int row;
  private int col;
  private int rowspan;
  private int colspan;

  public void setRow(int row)
  {
    this.row = row;
  }
  public int getRow() {
    return this.row;
  }

  public void setCol(int col) {
    this.col = col;
  }
  public int getCol() {
    return this.col;
  }

  public void setRowspan(int rowspan) {
    this.rowspan = rowspan;
  }
  public int getRowspan() {
    return this.rowspan;
  }

  public void setColspan(int colspan) {
    this.colspan = colspan;
  }
  public int getColspan() {
    return this.colspan;
  }
}

/*
 * Qualified Name:     report.java.entity.json.MergeCells
*
 */