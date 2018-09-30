package report.java.echarts;

import java.io.Serializable;
import report.java.echarts.code.Position;
import report.java.echarts.style.TextStyle;

public abstract class AbstractLabel<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private Object position;
  private Object interval;
  private Integer rotate;
  private Boolean clickable;
  private Object formatter;
  private TextStyle textStyle;
  private Integer margin;
  private String color;

  public T textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return (T)this;
  }

  public Boolean show()
  {
    return this.show;
  }

  public T show(Boolean show)
  {
    this.show = show;
    return (T)this;
  }

  public Object position()
  {
    return this.position;
  }

  public T position(Object position)
  {
    this.position = position;
    return (T)this;
  }

  public T position(Position position)
  {
    this.position = position;
    return (T)this;
  }

  public Object interval()
  {
    return this.interval;
  }

  public T interval(Object interval)
  {
    this.interval = interval;
    return (T)this;
  }

  public Integer rotate()
  {
    return this.rotate;
  }

  public T rotate(Integer rotate)
  {
    this.rotate = rotate;
    return (T)this;
  }

  public Boolean clickable()
  {
    return this.clickable;
  }

  public T clickable(Boolean clickable)
  {
    this.clickable = clickable;
    return (T)this;
  }

  public Object formatter()
  {
    return this.formatter;
  }

  public T formatter(Object formatter)
  {
    this.formatter = formatter;
    return (T)this;
  }

  public String color()
  {
    return this.color;
  }

  public T color(String color)
  {
    this.color = color;
    return (T)this;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public Integer margin()
  {
    return this.margin;
  }

  public T margin(Integer margin)
  {
    this.margin = margin;
    return (T)this;
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

  public Object getPosition()
  {
    return this.position;
  }

  public void setPosition(Object position)
  {
    this.position = position;
  }

  public Object getInterval()
  {
    return this.interval;
  }

  public void setInterval(Object interval)
  {
    this.interval = interval;
  }

  public Integer getRotate()
  {
    return this.rotate;
  }

  public void setRotate(Integer rotate)
  {
    this.rotate = rotate;
  }

  public Boolean getClickable()
  {
    return this.clickable;
  }

  public void setClickable(Boolean clickable)
  {
    this.clickable = clickable;
  }

  public Object getFormatter()
  {
    return this.formatter;
  }

  public void setFormatter(Object formatter)
  {
    this.formatter = formatter;
  }

  public Integer getMargin()
  {
    return this.margin;
  }

  public void setMargin(Integer margin)
  {
    this.margin = margin;
  }

  public String getColor()
  {
    return this.color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }
}

/* 
 * Qualified Name:     report.java.echarts.AbstractLabel
*
 */