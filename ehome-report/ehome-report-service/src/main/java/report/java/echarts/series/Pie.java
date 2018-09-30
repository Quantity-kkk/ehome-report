package report.java.echarts.series;

import report.java.echarts.code.RoseType;
import report.java.echarts.code.SelectedMode;
import report.java.echarts.code.SeriesType;

public class Pie extends Series<Pie>
{
  private Object[] center;
  private Object radius;
  private Integer startAngle;
  private Integer minAngle;
  private Boolean clockWise;
  private RoseType roseType;
  private Integer selectedOffset;
  private SelectedMode selectedMode;

  public Pie()
  {
    type(SeriesType.pie);
  }

  public Pie(String name)
  {
    super(name);
    type(SeriesType.pie);
  }

  public Object[] center()
  {
    return this.center;
  }

  public Pie center(Object[] center)
  {
    this.center = center;
    return this;
  }

  public Object radius()
  {
    return this.radius;
  }

  public Pie center(Object width, Object height)
  {
    this.center = new Object[] { width, height };
    return this;
  }

  public Pie radius(Object radius)
  {
    this.radius = radius;
    return this;
  }

  public Pie radius(Object width, Object height)
  {
    this.radius = new Object[] { width, height };
    return this;
  }

  public Integer startAngle()
  {
    return this.startAngle;
  }

  public Pie startAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
    return this;
  }

  public Integer minAngle()
  {
    return this.minAngle;
  }

  public Pie minAngle(Integer minAngle)
  {
    this.minAngle = minAngle;
    return this;
  }

  public Boolean clockWise()
  {
    return this.clockWise;
  }

  public Pie clockWise(Boolean clockWise)
  {
    this.clockWise = clockWise;
    return this;
  }

  public RoseType roseType()
  {
    return this.roseType;
  }

  public Pie roseType(RoseType roseType)
  {
    this.roseType = roseType;
    return this;
  }

  public Integer selectedOffset()
  {
    return this.selectedOffset;
  }

  public Pie selectedOffset(Integer selectedOffset)
  {
    this.selectedOffset = selectedOffset;
    return this;
  }

  public SelectedMode selectedMode()
  {
    return this.selectedMode;
  }

  public Pie selectedMode(SelectedMode selectedMode)
  {
    this.selectedMode = selectedMode;
    return this;
  }

  public Object[] getCenter()
  {
    return this.center;
  }

  public void setCenter(Object[] center)
  {
    this.center = center;
  }

  public Object getRadius()
  {
    return this.radius;
  }

  public void setRadius(Object radius)
  {
    this.radius = radius;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.Pie
*
 */