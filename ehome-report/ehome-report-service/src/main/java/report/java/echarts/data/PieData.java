package report.java.echarts.data;

import java.io.Serializable;

public class PieData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object value;
  private String name;

  public PieData(String name, Object value)
  {
    this.value = value;
    this.name = name;
  }

  public Object value()
  {
    return this.value;
  }

  public PieData value(Object value)
  {
    this.value = value;
    return this;
  }

  public String name()
  {
    return this.name;
  }

  public PieData name(String name)
  {
    this.name = name;
    return this;
  }

  public Object getValue()
  {
    return this.value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}

/* 
 * Qualified Name:     report.java.echarts.data.PieData
*
 */