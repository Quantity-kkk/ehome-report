package report.java.echarts.axis;

import java.io.Serializable;
import report.java.echarts.style.LineStyle;

public class SplitLine
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private Boolean onGap;
  private LineStyle lineStyle;

  public Boolean show()
  {
    return this.show;
  }

  public SplitLine show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public Boolean onGap()
  {
    return this.onGap;
  }

  public SplitLine onGap(Boolean onGap)
  {
    this.onGap = onGap;
    return this;
  }

  public SplitLine lineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
    return this;
  }

  public LineStyle lineStyle()
  {
    if (this.lineStyle == null) {
      this.lineStyle = new LineStyle();
    }
    return this.lineStyle;
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

  public Boolean getOnGap()
  {
    return this.onGap;
  }

  public void setOnGap(Boolean onGap)
  {
    this.onGap = onGap;
  }
}

/* 
 * Qualified Name:     report.java.echarts.axis.SplitLine
*
 */