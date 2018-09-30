package report.java.echarts.axis;

import report.java.echarts.code.AxisType;

public class ValueAxis extends Axis<ValueAxis>
{
  private Integer min;
  private Integer max;
  private Boolean scale;
  private Integer precision;
  private Integer power;
  private Integer splitNumber;
  private Double[] boundaryGap;

  public ValueAxis()
  {
    type(AxisType.value);
  }

  public Integer min()
  {
    return this.min;
  }

  public ValueAxis min(Integer min)
  {
    this.min = min;
    return this;
  }

  public Integer max()
  {
    return this.max;
  }

  public ValueAxis max(Integer max)
  {
    this.max = max;
    return this;
  }

  public Boolean scale()
  {
    return this.scale;
  }

  public ValueAxis scale(Boolean scale)
  {
    this.scale = scale;
    return this;
  }

  public Integer precision()
  {
    return this.precision;
  }

  public ValueAxis precision(Integer precision)
  {
    this.precision = precision;
    return this;
  }

  public Integer power()
  {
    return this.power;
  }

  public ValueAxis power(Integer power)
  {
    this.power = power;
    return this;
  }

  public Integer splitNumber()
  {
    return this.splitNumber;
  }

  public ValueAxis splitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
    return this;
  }

  public Double[] boundaryGap()
  {
    if (this.boundaryGap == null) {
      this.boundaryGap = new Double[2];
    }
    return this.boundaryGap;
  }

  public ValueAxis boundaryGap(Double[] boundaryGap)
  {
    this.boundaryGap = boundaryGap;
    return this;
  }

  public ValueAxis boundaryGap(Double min, Double max)
  {
    boundaryGap()[0] = min;
    boundaryGap()[1] = max;
    return this;
  }

  public Integer getMin()
  {
    return this.min;
  }

  public void setMin(Integer min)
  {
    this.min = min;
  }

  public Integer getMax()
  {
    return this.max;
  }

  public void setMax(Integer max)
  {
    this.max = max;
  }

  public Boolean getScale()
  {
    return this.scale;
  }

  public void setScale(Boolean scale)
  {
    this.scale = scale;
  }

  public Integer getPrecision()
  {
    return this.precision;
  }

  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }

  public Integer getPower()
  {
    return this.power;
  }

  public void setPower(Integer power)
  {
    this.power = power;
  }

  public Integer getSplitNumber()
  {
    return this.splitNumber;
  }

  public void setSplitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
  }

  public Double[] getBoundaryGap()
  {
    return this.boundaryGap;
  }

  public void setBoundaryGap(Double[] boundaryGap)
  {
    this.boundaryGap = boundaryGap;
  }
}

/* 
 * Qualified Name:     report.java.echarts.axis.ValueAxis
*
 */