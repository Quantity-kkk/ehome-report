package report.java.echarts.series.force;

import java.io.Serializable;
import report.java.echarts.style.ItemStyle;

public class Link
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object source;
  private Object target;
  private Integer weight;
  private ItemStyle itemStyle;

  public Link()
  {
  }

  public Link(Object source, Object target, Integer weight)
  {
    this.source = source;
    this.target = target;
    this.weight = weight;
  }

  public Object source()
  {
    return this.source;
  }

  public Link source(Object source)
  {
    this.source = source;
    return this;
  }

  public Object target()
  {
    return this.target;
  }

  public Link target(Object target)
  {
    this.target = target;
    return this;
  }

  public Integer weight()
  {
    return this.weight;
  }

  public Link weight(Integer weight)
  {
    this.weight = weight;
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

  public Object getSource()
  {
    return this.source;
  }

  public void setSource(Object source)
  {
    this.source = source;
  }

  public Object getTarget()
  {
    return this.target;
  }

  public void setTarget(Object target)
  {
    this.target = target;
  }

  public Integer getWeight()
  {
    return this.weight;
  }

  public void setWeight(Integer weight)
  {
    this.weight = weight;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.force.Link
*
 */