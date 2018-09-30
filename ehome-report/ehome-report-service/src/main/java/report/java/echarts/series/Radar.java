package report.java.echarts.series;

import report.java.echarts.code.SeriesType;

public class Radar extends Series<Radar>
{
  private Integer polarIndex;

  public Radar()
  {
    type(SeriesType.radar);
  }

  public Radar(String name)
  {
    super(name);
    type(SeriesType.radar);
  }

  public Integer polarIndex()
  {
    return this.polarIndex;
  }

  public Radar polarIndex(Integer polarIndex)
  {
    this.polarIndex = polarIndex;
    return this;
  }

  public Integer getPolarIndex()
  {
    return this.polarIndex;
  }

  public void setPolarIndex(Integer polarIndex)
  {
    this.polarIndex = polarIndex;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.Radar
*
 */