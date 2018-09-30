package report.java.echarts.series;

import report.java.echarts.code.SeriesType;

public class Bar extends Series<Bar>
{
  private Integer barMinHeight;
  private String barGap;
  private String barCategoryGap;

  public Bar()
  {
    type(SeriesType.bar);
  }

  public Bar(String name)
  {
    super(name);
    type(SeriesType.bar);
  }

  public Integer barMinHeight()
  {
    return this.barMinHeight;
  }

  public Bar barMinHeight(Integer barMinHeight)
  {
    this.barMinHeight = barMinHeight;
    return this;
  }

  public String barGap()
  {
    return this.barGap;
  }

  public Bar barGap(String barGap)
  {
    this.barGap = barGap;
    return this;
  }

  public String barCategoryGap()
  {
    return this.barCategoryGap;
  }

  public Bar barCategoryGap(String barCategoryGap)
  {
    this.barCategoryGap = barCategoryGap;
    return this;
  }

  public Integer getBarMinHeight()
  {
    return this.barMinHeight;
  }

  public void setBarMinHeight(Integer barMinHeight)
  {
    this.barMinHeight = barMinHeight;
  }

  public String getBarGap()
  {
    return this.barGap;
  }

  public void setBarGap(String barGap)
  {
    this.barGap = barGap;
  }

  public String getBarCategoryGap()
  {
    return this.barCategoryGap;
  }

  public void setBarCategoryGap(String barCategoryGap)
  {
    this.barCategoryGap = barCategoryGap;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.Bar
*
 */