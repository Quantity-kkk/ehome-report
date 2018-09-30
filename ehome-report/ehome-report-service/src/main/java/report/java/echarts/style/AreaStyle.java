package report.java.echarts.style;

import java.io.Serializable;

public class AreaStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object color;
  private Object type;

  public Object color()
  {
    return this.color;
  }

  public AreaStyle color(Object color)
  {
    this.color = color;
    return this;
  }

  public Object type()
  {
    return this.type;
  }

  public AreaStyle type(Object type)
  {
    this.type = type;
    return this;
  }

  public AreaStyle typeDefault()
  {
    this.type = "default";
    return this;
  }

  public Object getColor()
  {
    return this.color;
  }

  public void setColor(Object color)
  {
    this.color = color;
  }

  public Object getType()
  {
    return this.type;
  }

  public void setType(Object type)
  {
    this.type = type;
  }
}

/* 
 * Qualified Name:     report.java.echarts.style.AreaStyle
*
 */