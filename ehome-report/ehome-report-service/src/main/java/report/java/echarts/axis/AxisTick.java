package report.java.echarts.axis;

import java.io.Serializable;
import report.java.echarts.style.LineStyle;

public class AxisTick
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private Object interval;
  private Boolean onGap;
  private Boolean inside;
  private Integer length;
  private LineStyle lineStyle;
  private Integer splitNumber;

  public Boolean show()
  {
    return this.show;
  }

  public AxisTick show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public Object interval()
  {
    return this.interval;
  }

  public AxisTick interval(Object interval)
  {
    this.interval = interval;
    return this;
  }

  public Boolean onGap()
  {
    return this.onGap;
  }

  public AxisTick onGap(Boolean onGap)
  {
    this.onGap = onGap;
    return this;
  }

  public Boolean inside()
  {
    return this.inside;
  }

  public AxisTick inside(Boolean inside)
  {
    this.inside = inside;
    return this;
  }

  public Integer length()
  {
    return this.length;
  }

  public AxisTick length(Integer length)
  {
    this.length = length;
    return this;
  }

  public LineStyle lineStyle()
  {
    if (this.lineStyle == null) {
      this.lineStyle = new LineStyle();
    }
    return this.lineStyle;
  }

  public AxisTick lineStyle(LineStyle style)
  {
    this.lineStyle = style;
    return this;
  }

  public Integer splitNumber()
  {
    return this.splitNumber;
  }

  public AxisTick splitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
    return this;
  }

  public LineStyle getLineStyle()
  {
    return this.lineStyle;
  }

  public void setLineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }

  public Object getInterval()
  {
    return this.interval;
  }

  public void setInterval(Object interval)
  {
    this.interval = interval;
  }

  public Boolean getOnGap()
  {
    return this.onGap;
  }

  public void setOnGap(Boolean onGap)
  {
    this.onGap = onGap;
  }

  public Boolean getInside()
  {
    return this.inside;
  }

  public void setInside(Boolean inside)
  {
    this.inside = inside;
  }

  public Integer getLength()
  {
    return this.length;
  }

  public void setLength(Integer length)
  {
    this.length = length;
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
 * Qualified Name:     report.java.echarts.axis.AxisTick
*
 */