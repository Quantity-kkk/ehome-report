package report.java.echarts.series.gauge;

import java.io.Serializable;
import report.java.echarts.style.TextStyle;

public class Detail
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private String backgroundColor;
  private String borderColor;
  private Integer borderWidth;
  private Object width;
  private Object height;
  private Object offsetCenter;
  private String formatter;
  private TextStyle textStyle;

  public Boolean show()
  {
    return this.show;
  }

  public Detail show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public String backgroundColor()
  {
    return this.backgroundColor;
  }

  public Detail backgroundColor(String backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public String borderColor()
  {
    return this.borderColor;
  }

  public Detail borderColor(String borderColor)
  {
    this.borderColor = borderColor;
    return this;
  }

  public Integer borderWidth()
  {
    return this.borderWidth;
  }

  public Detail borderWidth(Integer borderWidth)
  {
    this.borderWidth = borderWidth;
    return this;
  }

  public Object width()
  {
    return this.width;
  }

  public Detail width(Object width)
  {
    this.width = width;
    return this;
  }

  public Object height()
  {
    return this.height;
  }

  public Detail height(Object height)
  {
    this.height = height;
    return this;
  }

  public Object offsetCenter()
  {
    return this.offsetCenter;
  }

  public Detail offsetCenter(Object offsetCenter)
  {
    this.offsetCenter = offsetCenter;
    return this;
  }

  public String formatter()
  {
    return this.formatter;
  }

  public Detail formatter(String formatter)
  {
    this.formatter = formatter;
    return this;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }

  public String getBackgroundColor()
  {
    return this.backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  public String getBorderColor()
  {
    return this.borderColor;
  }

  public void setBorderColor(String borderColor)
  {
    this.borderColor = borderColor;
  }

  public Integer getBorderWidth()
  {
    return this.borderWidth;
  }

  public void setBorderWidth(Integer borderWidth)
  {
    this.borderWidth = borderWidth;
  }

  public Object getWidth()
  {
    return this.width;
  }

  public void setWidth(Object width)
  {
    this.width = width;
  }

  public Object getHeight()
  {
    return this.height;
  }

  public void setHeight(Object height)
  {
    this.height = height;
  }

  public Object getOffsetCenter()
  {
    return this.offsetCenter;
  }

  public void setOffsetCenter(Object offsetCenter)
  {
    this.offsetCenter = offsetCenter;
  }

  public String getFormatter()
  {
    return this.formatter;
  }

  public void setFormatter(String formatter)
  {
    this.formatter = formatter;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.gauge.Detail
*
 */