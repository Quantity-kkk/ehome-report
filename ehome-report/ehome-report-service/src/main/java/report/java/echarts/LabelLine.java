package report.java.echarts;

import java.io.Serializable;
import report.java.echarts.style.LineStyle;

public class LabelLine
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private Integer length;
  private LineStyle lineStyle;

  public Boolean show()
  {
    return this.show;
  }

  public LabelLine show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public Integer length()
  {
    return this.length;
  }

  public LabelLine length(Integer length)
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

  public LabelLine lineStyle(LineStyle style)
  {
    this.lineStyle = style;
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
}

/* 
 * Qualified Name:     report.java.echarts.LabelLine
*
 */