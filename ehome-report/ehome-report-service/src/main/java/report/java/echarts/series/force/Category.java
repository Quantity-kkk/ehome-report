package report.java.echarts.series.force;

import java.io.Serializable;
import report.java.echarts.style.ItemStyle;

public class Category
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private Object symbol;
  private Object symbolSize;
  private Boolean draggable;
  private ItemStyle itemStyle;

  public Category()
  {
  }

  public Category(String name)
  {
    this.name = name;
  }

  public String name()
  {
    return this.name;
  }

  public Category name(String name)
  {
    this.name = name;
    return this;
  }

  public Object symbol()
  {
    return this.symbol;
  }

  public Category symbol(Object symbol)
  {
    this.symbol = symbol;
    return this;
  }

  public Object symbolSize()
  {
    return this.symbolSize;
  }

  public Category symbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
    return this;
  }

  public Boolean draggable()
  {
    return this.draggable;
  }

  public Category draggable(Boolean draggable)
  {
    this.draggable = draggable;
    return this;
  }

  public ItemStyle itemStyle()
  {
    if (this.itemStyle == null) {
      this.itemStyle = new ItemStyle();
    }
    return this.itemStyle;
  }

  public ItemStyle getItemStyle()
  {
    return this.itemStyle;
  }

  public void setItemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
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

  public Boolean getDraggable()
  {
    return this.draggable;
  }

  public void setDraggable(Boolean draggable)
  {
    this.draggable = draggable;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.force.Category
*
 */