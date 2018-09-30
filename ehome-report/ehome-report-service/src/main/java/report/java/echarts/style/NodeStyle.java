package report.java.echarts.style;

import report.java.echarts.code.BrushType;

public class NodeStyle extends LinkStyle
{
  private BrushType brushType;
  private String color;

  public BrushType brushType()
  {
    return this.brushType;
  }

  public NodeStyle brushType(BrushType brushType)
  {
    this.brushType = brushType;
    return this;
  }

  public String color()
  {
    return this.color;
  }

  public NodeStyle color(String color)
  {
    this.color = color;
    return this;
  }

  public BrushType getBrushType()
  {
    return this.brushType;
  }

  public void setBrushType(BrushType brushType)
  {
    this.brushType = brushType;
  }

  public String getColor()
  {
    return this.color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }
}

/*
 * Qualified Name:     report.java.echarts.style.NodeStyle
*
 */