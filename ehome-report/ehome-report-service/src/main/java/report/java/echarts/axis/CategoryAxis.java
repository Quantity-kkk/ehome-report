package report.java.echarts.axis;

import report.java.echarts.code.AxisType;

public class CategoryAxis extends Axis<CategoryAxis>
{
  private Boolean boundaryGap;

  public Boolean boundaryGap()
  {
    return this.boundaryGap;
  }

  public CategoryAxis boundaryGap(Boolean boundaryGap)
  {
    this.boundaryGap = boundaryGap;
    return this;
  }

  public CategoryAxis()
  {
    type(AxisType.category);
  }

  public Boolean getBoundaryGap()
  {
    return this.boundaryGap;
  }

  public void setBoundaryGap(Boolean boundaryGap)
  {
    this.boundaryGap = boundaryGap;
  }
}

/* 
 * Qualified Name:     report.java.echarts.axis.CategoryAxis
*
 */