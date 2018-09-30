package report.java.echarts;

import java.io.Serializable;
import report.java.echarts.code.X;
import report.java.echarts.code.Y;

public abstract class Basic<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private Object x;
  private Object y;
  private String backgroundColor;
  private String borderColor;
  private Integer borderWidth;
  private Object padding;
  private Integer itemGap;

  public Boolean show()
  {
    return this.show;
  }

  public T show(Boolean show)
  {
    this.show = show;
    return (T)this;
  }

  public Object x()
  {
    return this.x;
  }

  public T x(Object x)
  {
    this.x = x;
    return (T)this;
  }

  public Object y()
  {
    return this.y;
  }

  public T y(Object y)
  {
    this.y = y;
    return (T)this;
  }

  public String backgroundColor()
  {
    return this.backgroundColor;
  }

  public T backgroundColor(String backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    return (T)this;
  }

  public String borderColor()
  {
    return this.borderColor;
  }

  public T borderColor(String borderColor)
  {
    this.borderColor = borderColor;
    return (T)this;
  }

  public Integer borderWidth()
  {
    return this.borderWidth;
  }

  public T borderWidth(Integer borderWidth)
  {
    this.borderWidth = borderWidth;
    return (T)this;
  }

  public Object padding()
  {
    return this.padding;
  }

  public T padding(Integer padding)
  {
    this.padding = padding;
    return (T)this;
  }

  public T padding(Integer[] padding)
  {
    if ((padding != null) && (padding.length > 4)) {
      throw new RuntimeException("padding属性最多可以接收4个参数!");
    }
    this.padding = padding;
    return (T)this;
  }

  public Integer itemGap()
  {
    return this.itemGap;
  }

  public T itemGap(Integer itemGap)
  {
    this.itemGap = itemGap;
    return (T)this;
  }

  public T x(X x)
  {
    this.x = x;
    return (T)this;
  }

  public T y(Y y)
  {
    this.y = y;
    return (T)this;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }

  public Object getX()
  {
    return this.x;
  }

  public void setX(Object x)
  {
    this.x = x;
  }

  public Object getY()
  {
    return this.y;
  }

  public void setY(Object y)
  {
    this.y = y;
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

  public Object getPadding()
  {
    return this.padding;
  }

  public void setPadding(Integer padding)
  {
    this.padding = padding;
  }

  public Integer getItemGap()
  {
    return this.itemGap;
  }

  public void setItemGap(Integer itemGap)
  {
    this.itemGap = itemGap;
  }
}

/* 
 * Qualified Name:     report.java.echarts.Basic
*
 */