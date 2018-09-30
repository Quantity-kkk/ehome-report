package report.java.echarts.series;

import report.java.echarts.code.SeriesType;

public class Line extends Series<Line>
{
  private Boolean smooth;

  public Line()
  {
    type(SeriesType.line);
  }

  public Line(String name)
  {
    super(name);
    type(SeriesType.line);
  }

  public Boolean smooth()
  {
    return this.smooth;
  }

  public Line smooth(Boolean smooth)
  {
    this.smooth = smooth;
    return this;
  }

  public Boolean getSmooth()
  {
    return this.smooth;
  }

  public void setSmooth(Boolean smooth)
  {
    this.smooth = smooth;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.Line
*
 */