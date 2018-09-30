package report.java.echarts.series;

import java.io.Serializable;

public class ScaleLimit
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Double min;
  private Double max;

  public ScaleLimit()
  {
  }

  public ScaleLimit(Double min, Double max)
  {
    this.min = min;
    this.max = max;
  }

  public Double min()
  {
    return this.min;
  }

  public ScaleLimit min(Double min)
  {
    this.min = min;
    return this;
  }

  public Double max()
  {
    return this.max;
  }

  public ScaleLimit max(Double max)
  {
    this.max = max;
    return this;
  }

  public Double getMin()
  {
    return this.min;
  }

  public void setMin(Double min)
  {
    this.min = min;
  }

  public Double getMax()
  {
    return this.max;
  }

  public void setMax(Double max)
  {
    this.max = max;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.ScaleLimit
*
 */