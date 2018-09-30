package report.java.echarts.series;

import report.java.echarts.code.SeriesType;

public class Island extends Series<Island>
{
  private Object r;
  private Object calculateStep;

  public Island()
  {
    type(SeriesType.island);
  }

  public Island(String name)
  {
    super(name);
    type(SeriesType.island);
  }

  public Object r()
  {
    return this.r;
  }

  public Island r(Object r)
  {
    this.r = r;
    return this;
  }

  public Object calculateStep()
  {
    return this.calculateStep;
  }

  public Island calculateStep(Object calculateStep)
  {
    this.calculateStep = calculateStep;
    return this;
  }

  public Object getR()
  {
    return this.r;
  }

  public void setR(Object r)
  {
    this.r = r;
  }

  public Object getCalculateStep()
  {
    return this.calculateStep;
  }

  public void setCalculateStep(Object calculateStep)
  {
    this.calculateStep = calculateStep;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.Island
*
 */