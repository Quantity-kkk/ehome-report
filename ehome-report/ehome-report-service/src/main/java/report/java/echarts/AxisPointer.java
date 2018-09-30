package report.java.echarts;

import java.io.Serializable;
import report.java.echarts.code.PointerType;
import report.java.echarts.style.CrossStyle;
import report.java.echarts.style.LineStyle;
import report.java.echarts.style.ShadowStyle;

public class AxisPointer
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private PointerType type;
  private LineStyle lineStyle;
  private CrossStyle crossStyle;
  private ShadowStyle shadowStyle;

  public AxisPointer lineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
    return this;
  }

  public AxisPointer crossStyle(CrossStyle crossStyle)
  {
    this.crossStyle = crossStyle;
    return this;
  }

  public AxisPointer shadowStyle(ShadowStyle shadowStyle)
  {
    this.shadowStyle = shadowStyle;
    return this;
  }

  public PointerType type()
  {
    return this.type;
  }

  public AxisPointer type(PointerType type)
  {
    this.type = type;
    return this;
  }

  public LineStyle lineStyle()
  {
    if (this.lineStyle == null) {
      this.lineStyle = new LineStyle();
    }
    return this.lineStyle;
  }

  public CrossStyle crossStyle()
  {
    if (this.crossStyle == null) {
      this.crossStyle = new CrossStyle();
    }
    return this.crossStyle;
  }

  public ShadowStyle shadowStyle()
  {
    if (this.shadowStyle == null) {
      this.shadowStyle = new ShadowStyle();
    }
    return this.shadowStyle;
  }

  public LineStyle getLineStyle()
  {
    return this.lineStyle;
  }

  public void setLineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
  }

  public CrossStyle getCrossStyle()
  {
    return this.crossStyle;
  }

  public void setCrossStyle(CrossStyle crossStyle)
  {
    this.crossStyle = crossStyle;
  }

  public ShadowStyle getShadowStyle()
  {
    return this.shadowStyle;
  }

  public void setShadowStyle(ShadowStyle shadowStyle)
  {
    this.shadowStyle = shadowStyle;
  }

  public PointerType getType()
  {
    return this.type;
  }

  public void setType(PointerType type)
  {
    this.type = type;
  }
}

/* 
 * Qualified Name:     report.java.echarts.AxisPointer
*
 */