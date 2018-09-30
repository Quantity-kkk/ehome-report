package report.java.echarts.series;

import report.java.echarts.AbstractData;
import report.java.echarts.Chart;
import report.java.echarts.Tooltip;
import report.java.echarts.code.SeriesType;
import report.java.echarts.code.Symbol;
import report.java.echarts.style.ItemStyle;

public abstract class Series<T> extends AbstractData<T>
  implements Chart
{
  private Boolean legendHoverLink;
  private Integer xAxisIndex;
  private Integer yAxisIndex;
  private String name;
  private SeriesType type;
  private String stack;
  private Tooltip tooltip;
  private ItemStyle itemStyle;
  private MarkPoint markPoint;
  private MarkLine markLine;
  private Object symbol;
  private Object symbolSize;
  private Object symbolRoate;
  private Boolean showAllSymbol;

  protected Series()
  {
  }

  protected Series(String name)
  {
    this.name = name;
  }

  public Series tooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
   return this;
  }

  public Series itemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
   return this;
  }

  public Series markPoint(MarkPoint markPoint)
  {
    this.markPoint = markPoint;
   return this;
  }

  public Series markLine(MarkLine markLine)
  {
    this.markLine = markLine;
   return this;
  }

  public Boolean legendHoverLink()
  {
    return this.legendHoverLink;
  }

  public T legendHoverLink(Boolean legendHoverLink)
  {
    this.legendHoverLink = legendHoverLink;
   return (T)this;
  }

  public Integer xAxisIndex()
  {
    return this.xAxisIndex;
  }

  public T xAxisIndex(Integer xAxisIndex)
  {
    this.xAxisIndex = xAxisIndex;
   return (T)this;
  }

  public Integer yAxisIndex()
  {
    return this.yAxisIndex;
  }

  public T yAxisIndex(Integer yAxisIndex)
  {
    this.yAxisIndex = yAxisIndex;
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

  public SeriesType type()
  {
    return this.type;
  }

  public T type(SeriesType type)
  {
    this.type = type;
   return (T)this;
  }

  public String stack()
  {
    return this.stack;
  }

  public T stack(String stack)
  {
    this.stack = stack;
   return (T)this;
  }

  public Tooltip tooltip()
  {
    if (this.tooltip == null) {
      this.tooltip = new Tooltip();
    }
    return this.tooltip;
  }

  public ItemStyle itemStyle()
  {
    if (this.itemStyle == null) {
      this.itemStyle = new ItemStyle();
    }
    return this.itemStyle;
  }

  public MarkPoint markPoint()
  {
    if (this.markPoint == null) {
      this.markPoint = new MarkPoint();
    }
    return this.markPoint;
  }

  public MarkLine markLine()
  {
    if (this.markLine == null) {
      this.markLine = new MarkLine();
    }
    return this.markLine;
  }

  public Object symbol()
  {
    return this.symbol;
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
    return this.symbolSize;
  }

  public T symbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
   return (T)this;
  }

  public Object symbolRoate()
  {
    return this.symbolRoate;
  }

  public T symbolRoate(Object symbolRoate)
  {
    this.symbolRoate = symbolRoate;
   return (T)this;
  }

  public Boolean showAllSymbol()
  {
    return this.showAllSymbol;
  }

  public T showAllSymbol(Boolean showAllSymbol)
  {
    this.showAllSymbol = showAllSymbol;
   return (T)this;
  }

  public Boolean getLegendHoverLink()
  {
    return this.legendHoverLink;
  }

  public void setLegendHoverLink(Boolean legendHoverLink)
  {
    this.legendHoverLink = legendHoverLink;
  }

  public Tooltip getTooltip()
  {
    return this.tooltip;
  }

  public void setTooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
  }

  public ItemStyle getItemStyle()
  {
    return this.itemStyle;
  }

  public void setItemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
  }

  public MarkPoint getMarkPoint()
  {
    return this.markPoint;
  }

  public void setMarkPoint(MarkPoint markPoint)
  {
    this.markPoint = markPoint;
  }

  public MarkLine getMarkLine()
  {
    return this.markLine;
  }

  public void setMarkLine(MarkLine markLine)
  {
    this.markLine = markLine;
  }

  public Integer getxAxisIndex()
  {
    return this.xAxisIndex;
  }

  public void setxAxisIndex(Integer xAxisIndex)
  {
    this.xAxisIndex = xAxisIndex;
  }

  public Integer getyAxisIndex()
  {
    return this.yAxisIndex;
  }

  public void setyAxisIndex(Integer yAxisIndex)
  {
    this.yAxisIndex = yAxisIndex;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public SeriesType getType()
  {
    return this.type;
  }

  public void setType(SeriesType type)
  {
    this.type = type;
  }

  public String getStack()
  {
    return this.stack;
  }

  public void setStack(String stack)
  {
    this.stack = stack;
  }

  public Object getSymbol()
  {
    return this.symbol;
  }

  public void setSymbol(Object symbol)
  {
    this.symbol = symbol;
  }

  public Object getSymbolSize()
  {
    return this.symbolSize;
  }

  public void setSymbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
  }

  public Object getSymbolRoate()
  {
    return this.symbolRoate;
  }

  public void setSymbolRoate(Object symbolRoate)
  {
    this.symbolRoate = symbolRoate;
  }

  public Boolean getShowAllSymbol()
  {
    return this.showAllSymbol;
  }

  public void setShowAllSymbol(Boolean showAllSymbol)
  {
    this.showAllSymbol = showAllSymbol;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.Series
*
 */