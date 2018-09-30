package report.java.echarts.style;

import java.io.Serializable;
import report.java.echarts.Label;

public class CheckpointStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object symbol;
  private Object symbolSize;
  private String color;
  private String borderColor;
  private Object borderWidth;
  private Label label;

  public CheckpointStyle label(Label label)
  {
    this.label = label;
    return this;
  }

  public Object symbol()
  {
    return this.symbol;
  }

  public CheckpointStyle symbol(Object symbol)
  {
    this.symbol = symbol;
    return this;
  }

  public Object symbolSize()
  {
    return this.symbolSize;
  }

  public CheckpointStyle symbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
    return this;
  }

  public String color()
  {
    return this.color;
  }

  public CheckpointStyle color(String color)
  {
    this.color = color;
    return this;
  }

  public String borderColor()
  {
    return this.borderColor;
  }

  public CheckpointStyle borderColor(String borderColor)
  {
    this.borderColor = borderColor;
    return this;
  }

  public Object borderWidth()
  {
    return this.borderWidth;
  }

  public CheckpointStyle borderWidth(Object borderWidth)
  {
    this.borderWidth = borderWidth;
    return this;
  }

  public Label label()
  {
    if (this.label == null) {
      this.label = new Label();
    }
    return this.label;
  }

  public Label getLabel()
  {
    return this.label;
  }

  public void setLabel(Label label)
  {
    this.label = label;
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

  public String getColor()
  {
    return this.color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }

  public String getBorderColor()
  {
    return this.borderColor;
  }

  public void setBorderColor(String borderColor)
  {
    this.borderColor = borderColor;
  }

  public Object getBorderWidth()
  {
    return this.borderWidth;
  }

  public void setBorderWidth(Object borderWidth)
  {
    this.borderWidth = borderWidth;
  }
}

/* 
 * Qualified Name:     report.java.echarts.style.CheckpointStyle
*
 */