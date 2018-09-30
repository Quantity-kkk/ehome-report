package report.java.echarts.style;

import java.io.Serializable;

public class ShadowStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String color;
  private Object width;
  private String type;

  public String color()
  {
    return this.color;
  }

  public ShadowStyle color(String color)
  {
    this.color = color;
    return this;
  }

  public Object width()
  {
    return this.width;
  }

  public ShadowStyle width(Object width)
  {
    this.width = width;
    return this;
  }

  public String type()
  {
    return this.type;
  }

  public ShadowStyle type(String type)
  {
    this.type = type;
    return this;
  }

  public ShadowStyle typeDefault()
  {
    this.type = "default";
    return this;
  }

  public String getColor()
  {
    return this.color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }

  public Object getWidth()
  {
    return this.width;
  }

  public void setWidth(Object width)
  {
    this.width = width;
  }

  public String getType()
  {
    return this.type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}

/* 
 * Qualified Name:     report.java.echarts.style.ShadowStyle
*
 */