package report.java.echarts.data;

import java.io.Serializable;

public class KData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Double[] value;

  public KData(Double open, Double close, Double min, Double max)
  {
    this.value = new Double[4];
    this.value[0] = open;
    this.value[1] = close;
    this.value[2] = min;
    this.value[3] = max;
  }

  public Double[] value()
  {
    return this.value;
  }

  public KData value(Double[] value)
  {
    this.value = value;
    return this;
  }

  public Double[] getValue()
  {
    return this.value;
  }

  public void setValue(Double[] value)
  {
    this.value = value;
  }
}

/*
 * Qualified Name:     report.java.echarts.data.KData
*
 */