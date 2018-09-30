package report.java.echarts.data;

import java.io.Serializable;
import report.java.echarts.Tooltip;
import report.java.echarts.style.ItemStyle;

public class SeriesData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object value;
  private Tooltip tooltip;
  private ItemStyle itemStyle;

  public SeriesData(Object value)
  {
    this.value = value;
  }

  public SeriesData(Object value, Tooltip tooltip)
  {
    this.value = value;
    this.tooltip = tooltip;
  }

  public SeriesData(Object value, ItemStyle itemStyle)
  {
    this.value = value;
    this.itemStyle = itemStyle;
  }

  public SeriesData(Object value, Tooltip tooltip, ItemStyle itemStyle)
  {
    this.value = value;
    this.tooltip = tooltip;
    this.itemStyle = itemStyle;
  }

  public Object value()
  {
    return this.value;
  }

  public SeriesData value(Object value)
  {
    this.value = value;
    return this;
  }

  public Tooltip tooltip()
  {
    if (this.tooltip == null) {
      this.tooltip = new Tooltip();
    }
    return this.tooltip;
  }

  public SeriesData tooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
    return this;
  }

  public ItemStyle itemStyle()
  {
    if (this.itemStyle == null) {
      this.itemStyle = new ItemStyle();
    }
    return this.itemStyle;
  }

  public SeriesData itemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
    return this;
  }
}

/* 
 * Qualified Name:     report.java.echarts.data.SeriesData
*
 */