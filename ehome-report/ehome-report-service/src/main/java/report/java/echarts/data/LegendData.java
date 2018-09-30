package report.java.echarts.data;

import java.io.Serializable;
import report.java.echarts.style.TextStyle;

public class LegendData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private TextStyle textStyle;
  private String icon;

  public LegendData(String name)
  {
    this.name = name;
  }

  public LegendData(String name, TextStyle textStyle)
  {
    this.name = name;
    this.textStyle = textStyle;
  }

  public LegendData(String name, TextStyle textStyle, String icon)
  {
    this.name = name;
    this.textStyle = textStyle;
    this.icon = icon;
  }

  public String name()
  {
    return this.name;
  }

  public LegendData name(String name)
  {
    this.name = name;
    return this;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public LegendData textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return this;
  }

  public String icon()
  {
    return this.icon;
  }

  public LegendData icon(String icon)
  {
    this.icon = icon;
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

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }

  public String getIcon()
  {
    return this.icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }
}

/*
 * Qualified Name:     report.java.echarts.data.LegendData
*
 */