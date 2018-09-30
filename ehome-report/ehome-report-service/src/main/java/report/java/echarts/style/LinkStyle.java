package report.java.echarts.style;

import java.io.Serializable;
import report.java.echarts.code.LinkType;

public class LinkStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private LinkType type;
  private String borderColor;
  private Integer borderWidth;

  public LinkType type()
  {
    return this.type;
  }

  public LinkStyle type(LinkType type)
  {
    this.type = type;
    return this;
  }

  public String borderColor()
  {
    return this.borderColor;
  }

  public LinkStyle borderColor(String borderColor)
  {
    this.borderColor = borderColor;
    return this;
  }

  public Integer borderWidth()
  {
    return this.borderWidth;
  }

  public LinkStyle borderWidth(Integer borderWidth)
  {
    this.borderWidth = borderWidth;
    return this;
  }

  public LinkType getType()
  {
    return this.type;
  }

  public void setType(LinkType type)
  {
    this.type = type;
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
}

/*
 * Qualified Name:     report.java.echarts.style.LinkStyle
*
 */