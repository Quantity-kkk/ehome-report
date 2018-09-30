package report.java.echarts.series;

import report.java.echarts.Label;
import report.java.echarts.Title;
import report.java.echarts.axis.AxisTick;
import report.java.echarts.axis.SplitLine;
import report.java.echarts.code.SeriesType;
import report.java.echarts.series.gauge.Detail;
import report.java.echarts.series.gauge.Pointer;

public class Gauge extends Series<Gauge>
{
  private Object[] center;
  private Object radius;
  private Integer startAngle;
  private Integer endAngle;
  private Integer min;
  private Integer max;
  private Integer precision;
  private Integer splitNumber;
  private Line axisLine;
  private AxisTick axisTick;
  private Label axisLabel;
  private SplitLine splitLine;
  private Pointer pointer;
  private Title title;
  private Detail detail;

  public Gauge()
  {
    type(SeriesType.gauge);
  }

  public Gauge(String name)
  {
    super(name);
    type(SeriesType.gauge);
  }

  public Object[] center()
  {
    return this.center;
  }

  public Gauge center(Object[] center)
  {
    this.center = center;
    return this;
  }

  public Object radius()
  {
    return this.radius;
  }

  public Gauge axisLine(Line axisLine)
  {
    this.axisLine = axisLine;
    return this;
  }

  public Gauge axisTick(AxisTick axisTick)
  {
    this.axisTick = axisTick;
    return this;
  }

  public Gauge axisLabel(Label axisLabel)
  {
    this.axisLabel = axisLabel;
    return this;
  }

  public Gauge splitLine(SplitLine splitLine)
  {
    this.splitLine = splitLine;
    return this;
  }

  public Gauge pointer(Pointer pointer)
  {
    this.pointer = pointer;
    return this;
  }

  public Gauge title(Title title)
  {
    this.title = title;
    return this;
  }

  public Gauge detail(Detail detail)
  {
    this.detail = detail;
    return this;
  }

  public Gauge center(Object width, Object height)
  {
    this.center = new Object[] { width, height };
    return this;
  }

  public Gauge radius(Object radius)
  {
    this.radius = radius;
    return this;
  }

  public Gauge radius(Object width, Object height)
  {
    this.radius = new Object[] { width, height };
    return this;
  }

  public Integer startAngle()
  {
    return this.startAngle;
  }

  public Gauge startAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
    return this;
  }

  public Integer endAngle()
  {
    return this.endAngle;
  }

  public Gauge endAngle(Integer endAngle)
  {
    this.endAngle = endAngle;
    return this;
  }

  public Integer min()
  {
    return this.min;
  }

  public Gauge min(Integer min)
  {
    this.min = min;
    return this;
  }

  public Integer max()
  {
    return this.max;
  }

  public Gauge max(Integer max)
  {
    this.max = max;
    return this;
  }

  public Integer precision()
  {
    return this.precision;
  }

  public Gauge precision(Integer precision)
  {
    this.precision = precision;
    return this;
  }

  public Integer splitNumber()
  {
    return this.splitNumber;
  }

  public Gauge splitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
    return this;
  }

  public Line axisLine()
  {
    if (this.axisLine == null) {
      this.axisLine = new Line();
    }
    return this.axisLine;
  }

  public AxisTick axisTick()
  {
    if (this.axisTick == null) {
      this.axisTick = new AxisTick();
    }
    return this.axisTick;
  }

  public Label axisLabel()
  {
    if (this.axisLabel == null) {
      this.axisLabel = new Label();
    }
    return this.axisLabel;
  }

  public SplitLine splitLine()
  {
    if (this.splitLine == null) {
      this.splitLine = new SplitLine();
    }
    return this.splitLine;
  }

  public Pointer pointer()
  {
    if (this.pointer == null) {
      this.pointer = new Pointer();
    }
    return this.pointer;
  }

  public Title title()
  {
    if (this.title == null) {
      this.title = new Title();
    }
    return this.title;
  }

  public Detail detail()
  {
    if (this.detail == null) {
      this.detail = new Detail();
    }
    return this.detail;
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

  public Line getAxisLine()
  {
    return this.axisLine;
  }

  public void setAxisLine(Line axisLine)
  {
    this.axisLine = axisLine;
  }

  public AxisTick getAxisTick()
  {
    return this.axisTick;
  }

  public void setAxisTick(AxisTick axisTick)
  {
    this.axisTick = axisTick;
  }

  public Label getAxisLabel()
  {
    return this.axisLabel;
  }

  public void setAxisLabel(Label axisLabel)
  {
    this.axisLabel = axisLabel;
  }

  public SplitLine getSplitLine()
  {
    return this.splitLine;
  }

  public void setSplitLine(SplitLine splitLine)
  {
    this.splitLine = splitLine;
  }

  public Pointer getPointer()
  {
    return this.pointer;
  }

  public void setPointer(Pointer pointer)
  {
    this.pointer = pointer;
  }

  public Title getTitle()
  {
    return this.title;
  }

  public void setTitle(Title title)
  {
    this.title = title;
  }

  public Detail getDetail()
  {
    return this.detail;
  }

  public void setDetail(Detail detail)
  {
    this.detail = detail;
  }

  public Integer getStartAngle()
  {
    return this.startAngle;
  }

  public void setStartAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
  }

  public Integer getEndAngle()
  {
    return this.endAngle;
  }

  public void setEndAngle(Integer endAngle)
  {
    this.endAngle = endAngle;
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

  public Integer getPrecision()
  {
    return this.precision;
  }

  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }

  public Integer getSplitNumber()
  {
    return this.splitNumber;
  }

  public void setSplitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.Gauge
*
 */