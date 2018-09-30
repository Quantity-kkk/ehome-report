package report.java.echarts.data;

import java.io.Serializable;
import report.java.echarts.style.TextStyle;

public class AxisData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object value;
  private TextStyle textStyle;

  public AxisData(Object value)
  {
    this.value = value;
  }

  public AxisData(Object value, TextStyle textStyle)
  {
    this.value = value;
    this.textStyle = textStyle;
  }

  public Object value()
  {
    return this.value;
  }

  public AxisData value(Object value)
  {
    this.value = value;
    return this;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public AxisData textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return this;
  }

  public Object getValue()
  {
    return this.value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }
}

/*
 * Qualified Name:     report.java.echarts.data.AxisData
*
 */