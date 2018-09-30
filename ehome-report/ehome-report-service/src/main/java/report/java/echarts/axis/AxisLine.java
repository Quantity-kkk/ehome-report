package report.java.echarts.axis;

import java.io.Serializable;
import report.java.echarts.style.LineStyle;

public class AxisLine
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private Boolean onZero;
  private LineStyle lineStyle;

  public Boolean show()
  {
    return this.show;
  }

  public AxisLine show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public Boolean onZero()
  {
    return this.onZero;
  }

  public AxisLine onZero(Boolean onZero)
  {
    this.onZero = onZero;
    return this;
  }

  public LineStyle lineStyle()
  {
    if (this.lineStyle == null) {
      this.lineStyle = new LineStyle();
    }
    return this.lineStyle;
  }

  public AxisLine lineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
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

  public Boolean getOnZero()
  {
    return this.onZero;
  }

  public void setOnZero(Boolean onZero)
  {
    this.onZero = onZero;
  }
}

/* 
 * Qualified Name:     report.java.echarts.axis.AxisLine
*
 */