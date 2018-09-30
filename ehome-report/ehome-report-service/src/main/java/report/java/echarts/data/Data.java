package report.java.echarts.data;

import report.java.echarts.Tooltip;

public class Data extends BasicData<Data>
{
  private Integer valueIndex;
  private Object min;
  private Object max;
  private String icon;
  private Boolean selected;
  private Tooltip tooltip;
  private Double smoothRadian;

  public Data()
  {
  }

  public Data(String name)
  {
    super(name);
  }

  public Data(String name, Object value)
  {
    super(name, value);
  }

  public Data(String name, Object symbol, Object symbolSize)
  {
    super(name, symbol, symbolSize);
  }

  public Data(Object value, Object symbol)
  {
    super(value, symbol);
  }

  public Data(Object value, Object symbol, Object symbolSize)
  {
    super(value, symbol, symbolSize);
  }

  public Double smoothRadian()
  {
    return this.smoothRadian;
  }

  public Data smoothRadian(Double smoothRadian)
  {
    this.smoothRadian = smoothRadian;
    return this;
  }

  public Tooltip tooltip()
  {
    if (this.tooltip == null) {
      this.tooltip = new Tooltip();
    }
    return this.tooltip;
  }

  public Data tooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
    return this;
  }

  public Boolean selected()
  {
    return this.selected;
  }

  public Data selected(Boolean selected)
  {
    this.selected = selected;
    return this;
  }

  public String icon()
  {
    return this.icon;
  }

  public Data icon(String icon)
  {
    this.icon = icon;
    return this;
  }

  public Object min()
  {
    return this.min;
  }

  public Data min(Object min)
  {
    this.min = min;
    return this;
  }

  public Object max()
  {
    return this.max;
  }

  public Data max(Object max)
  {
    this.max = max;
    return this;
  }

  public Integer valueIndex()
  {
    return this.valueIndex;
  }

  public Data valueIndex(Integer valueIndex)
  {
    this.valueIndex = valueIndex;
    return this;
  }

  public Integer getValueIndex()
  {
    return this.valueIndex;
  }

  public Data setValueIndex(Integer valueIndex)
  {
    this.valueIndex = valueIndex;
    return this;
  }

  public Object getMin()
  {
    return this.min;
  }

  public void setMin(Object min)
  {
    this.min = min;
  }

  public Object getMax()
  {
    return this.max;
  }

  public void setMax(Object max)
  {
    this.max = max;
  }

  public String getIcon()
  {
    return this.icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public Boolean getSelected()
  {
    return this.selected;
  }

  public void setSelected(Boolean selected)
  {
    this.selected = selected;
  }

  public Tooltip getTooltip()
  {
    return this.tooltip;
  }

  public void setTooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
  }

  public Double getSmoothRadian() {
    return this.smoothRadian;
  }

  public void setSmoothRadian(Double smoothRadian) {
    this.smoothRadian = smoothRadian;
  }
}

/*
 * Qualified Name:     report.java.echarts.data.Data
*
 */