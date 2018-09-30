package report.java.echarts.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.code.MarkType;
import report.java.echarts.code.Symbol;
import report.java.echarts.style.ItemStyle;
import report.java.echarts.style.TextStyle;

public abstract class BasicData<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private String text;
  private Object value;
  private Object x;
  private Object y;
  private Integer xAxis;
  private Integer yAxis;
  private MarkType type;
  private Object symbol;
  private Object symbolSize;
  private ItemStyle itemStyle;
  private TextStyle textStyle;

  public BasicData()
  {
  }

  protected BasicData(String name)
  {
    this.name = name;
  }

  public BasicData(String name, Object value)
  {
    this.name = name;
    this.value = value;
  }

  public BasicData(String name, Object symbol, Object symbolSize)
  {
    this.name = name;
    this.symbol = symbol;
    this.symbolSize = symbolSize;
  }

  public BasicData(Object value, Object symbol)
  {
    this.value = value;
    this.symbol = symbol;
  }

  public BasicData(Object value, Object symbol, Object symbolSize)
  {
    this.value = value;
    this.symbol = symbol;
    this.symbolSize = symbolSize;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public T textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return (T)this;
  }

  public String text()
  {
    return this.text;
  }

  public T text(String text)
  {
    this.text = text;
    return (T)this;
  }

  public String name()
  {
    return this.name;
  }

  public T name(String name)
  {
    this.name = name;
    return (T)this;
  }

  public Object value()
  {
    return (T)this.value;
  }

  public T value(Object value)
  {
    this.value = value;
    return (T)this;
  }

  public T value(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return (T)this;
    }
    if (this.value == null) {
      this.value = new ArrayList(values.length);
    }
    if ((this.value instanceof List)) {
      ((List)this.value).addAll(Arrays.asList(values));
    }
    return (T)this;
  }

  public Object x()
  {
    return (T)this.x;
  }

  public T x(Object x)
  {
    this.x = x;
    return (T)this;
  }

  public Object y()
  {
    return (T)this.y;
  }

  public T y(Object y)
  {
    this.y = y;
    return (T)this;
  }

  public Integer xAxis()
  {
    return this.xAxis;
  }

  public T xAxis(Integer xAxis)
  {
    this.xAxis = xAxis;
    return (T)this;
  }

  public Integer yAxis()
  {
    return this.yAxis;
  }

  public T yAxis(Integer yAxis)
  {
    this.yAxis = yAxis;
    return (T)this;
  }

  public MarkType type()
  {
    return this.type;
  }

  public T type(MarkType type)
  {
    this.type = type;
    return (T)this;
  }

  public Object symbol()
  {
    return (T)this.symbol;
  }

  public T symbol(Object symbol)
  {
    this.symbol = symbol;
    return (T)this;
  }

  public T symbol(Symbol symbol)
  {
    this.symbol = symbol;
    return (T)this;
  }

  public Object symbolSize()
  {
    return (T)this.symbolSize;
  }

  public T symbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
    return (T)this;
  }

  public ItemStyle itemStyle()
  {
    if (this.itemStyle == null) {
      this.itemStyle = new ItemStyle();
    }
    return this.itemStyle;
  }

  public T itemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
    return (T)this;
  }

  public String getName()
  {
    return this.name;
  }

  public T setName(String name)
  {
    this.name = name;
    return (T)this;
  }

  public Object getValue()
  {
    return (T)this.value;
  }

  public T setValue(Object value)
  {
    this.value = value;
    return (T)this;
  }

  public Object getX()
  {
    return (T)this.x;
  }

  public T setX(Object x)
  {
    this.x = x;
    return (T)this;
  }

  public Object getY()
  {
    return (T)this.y;
  }

  public T setY(Object y)
  {
    this.y = y;
    return (T)this;
  }

  public Integer getxAxis()
  {
    return this.xAxis;
  }

  public T setxAxis(Integer xAxis)
  {
    this.xAxis = xAxis;
    return (T)this;
  }

  public Integer getyAxis()
  {
    return this.yAxis;
  }

  public T setyAxis(Integer yAxis)
  {
    this.yAxis = yAxis;
    return (T)this;
  }

  public MarkType getType()
  {
    return this.type;
  }

  public T setType(MarkType type)
  {
    this.type = type;
    return (T)this;
  }

  public Object getSymbol()
  {
    return (T)this.symbol;
  }

  public T setSymbol(Object symbol)
  {
    this.symbol = symbol;
    return (T)this;
  }

  public Object getSymbolSize()
  {
    return (T)this.symbolSize;
  }

  public T setSymbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
    return (T)this;
  }

  public ItemStyle getItemStyle()
  {
    return this.itemStyle;
  }

  public T setItemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
    return (T)this;
  }

  public String getText()
  {
    return this.text;
  }

  public void setText(String text)
  {
    this.text = text;
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
