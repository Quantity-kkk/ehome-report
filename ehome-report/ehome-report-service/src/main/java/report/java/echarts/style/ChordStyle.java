package report.java.echarts.style;

import java.io.Serializable;

public class ChordStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Integer width;
  private String color;
  private String borderColor;
  private Integer borderWidth;
  private Double opacity;

  public String color()
  {
    return this.color;
  }

  public ChordStyle color(String color)
  {
    this.color = color;
    return this;
  }

  public Double opacity()
  {
    return this.opacity;
  }

  public ChordStyle opacity(Double opacity)
  {
    this.opacity = opacity;
    return this;
  }

  public String borderColor()
  {
    return this.borderColor;
  }

  public ChordStyle borderColor(String borderColor)
  {
    this.borderColor = borderColor;
    return this;
  }

  public Integer width()
  {
    return this.width;
  }

  public ChordStyle width(Integer width)
  {
    this.width = width;
    return this;
  }

  public Integer borderWidth()
  {
    return this.borderWidth;
  }

  public ChordStyle borderWidth(Integer borderWidth)
  {
    this.borderWidth = borderWidth;
    return this;
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

  public Integer getWidth()
  {
    return this.width;
  }

  public void setWidth(Integer width)
  {
    this.width = width;
  }

  public String getColor()
  {
    return this.color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }

  public Double getOpacity()
  {
    return this.opacity;
  }

  public void setOpacity(Double opacity)
  {
    this.opacity = opacity;
  }
}

/*
 * Qualified Name:     report.java.echarts.style.ChordStyle
*
 */