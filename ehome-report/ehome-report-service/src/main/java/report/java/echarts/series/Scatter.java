package report.java.echarts.series;

import report.java.echarts.code.SeriesType;

public class Scatter extends Series<Scatter>
{
  private Boolean large;
  private Long largeThreshold;

  public Scatter()
  {
    type(SeriesType.scatter);
  }

  public Scatter(String name)
  {
    super(name);
    type(SeriesType.scatter);
  }

  public Boolean large()
  {
    return this.large;
  }

  public Scatter large(Boolean large)
  {
    this.large = large;
    return this;
  }

  public Long largeThreshold()
  {
    return this.largeThreshold;
  }

  public Scatter largeThreshold(Long largeThreshold)
  {
    this.largeThreshold = largeThreshold;
    return this;
  }

  public Boolean getLarge()
  {
    return this.large;
  }

  public void setLarge(Boolean large)
  {
    this.large = large;
  }

  public Long getLargeThreshold()
  {
    return this.largeThreshold;
  }

  public void setLargeThreshold(Long largeThreshold)
  {
    this.largeThreshold = largeThreshold;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.Scatter
*
 */