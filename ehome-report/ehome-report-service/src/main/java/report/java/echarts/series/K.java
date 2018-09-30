package report.java.echarts.series;

import report.java.echarts.code.SeriesType;

public class K extends Series<K>
{
  public K()
  {
    type(SeriesType.k);
  }

  public K(String name)
  {
    super(name);
    type(SeriesType.k);
  }

  public K data(Double open, Double close, Double min, Double max)
  {
    Object[] kData = { open, close, min, max };
    super.data(kData);
    return this;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.K
*
 */