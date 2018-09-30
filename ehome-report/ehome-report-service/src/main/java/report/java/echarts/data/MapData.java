package report.java.echarts.data;

import java.io.Serializable;

public class MapData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private Object value;
  private Boolean selected;

  public MapData(String name, Object value)
  {
    this.name = name;
    this.value = value;
  }

  public MapData(String name, Object value, Boolean selected)
  {
    this.name = name;
    this.value = value;
    this.selected = selected;
  }

  public String name()
  {
    return this.name;
  }

  public MapData name(String name)
  {
    this.name = name;
    return this;
  }

  public Object value()
  {
    return this.value;
  }

  public MapData value(Object value)
  {
    this.value = value;
    return this;
  }

  public Boolean selected()
  {
    return this.selected;
  }

  public MapData selected(Boolean selected)
  {
    this.selected = selected;
    return this;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Object getValue()
  {
    return this.value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  public Boolean getSelected()
  {
    return this.selected;
  }

  public void setSelected(Boolean selected)
  {
    this.selected = selected;
  }
}

/*
 * Qualified Name:     report.java.echarts.data.MapData
*
 */