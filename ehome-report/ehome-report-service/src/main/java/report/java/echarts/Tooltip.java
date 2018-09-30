package report.java.echarts;

import report.java.echarts.code.Trigger;
import report.java.echarts.style.TextStyle;

public class Tooltip extends Basic<Tooltip>
  implements Component
{
  private Boolean showContent;
  private Trigger trigger;
  private Object position;
  private Object formatter;
  private String islandFormatter;
  private Integer showDelay;
  private Integer hideDelay;
  private Double transitionDuration;
  private Boolean enterable;
  private Integer borderRadius;
  private AxisPointer axisPointer;
  private TextStyle textStyle;

  public Tooltip axisPointer(AxisPointer axisPointer)
  {
    this.axisPointer = axisPointer;
    return this;
  }

  public Tooltip textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return this;
  }

  public Boolean showContent()
  {
    return this.showContent;
  }

  public Tooltip showContent(Boolean showContent)
  {
    this.showContent = showContent;
    return this;
  }

  public Trigger trigger()
  {
    return this.trigger;
  }

  public Tooltip trigger(Trigger trigger)
  {
    this.trigger = trigger;
    return this;
  }

  public Object position()
  {
    return this.position;
  }

  public Tooltip position(Object position)
  {
    this.position = position;
    return this;
  }

  public Object formatter()
  {
    return this.formatter;
  }

  public Tooltip formatter(Object formatter)
  {
    this.formatter = formatter;
    return this;
  }

  public String islandFormatter()
  {
    return this.islandFormatter;
  }

  public Tooltip islandFormatter(String islandFormatter)
  {
    this.islandFormatter = islandFormatter;
    return this;
  }

  public Integer showDelay()
  {
    return this.showDelay;
  }

  public Tooltip showDelay(Integer showDelay)
  {
    this.showDelay = showDelay;
    return this;
  }

  public Integer hideDelay()
  {
    return this.hideDelay;
  }

  public Tooltip hideDelay(Integer hideDelay)
  {
    this.hideDelay = hideDelay;
    return this;
  }

  public Double transitionDuration()
  {
    return this.transitionDuration;
  }

  public Tooltip transitionDuration(Double transitionDuration)
  {
    this.transitionDuration = transitionDuration;
    return this;
  }

  public Boolean enterable() {
    return this.enterable;
  }

  public Tooltip enterable(Boolean enterable) {
    this.enterable = enterable;
    return this;
  }

  public Integer borderRadius()
  {
    return this.borderRadius;
  }

  public Tooltip borderRadius(Integer borderRadius)
  {
    this.borderRadius = borderRadius;
    return this;
  }

  public AxisPointer axisPointer()
  {
    if (this.axisPointer == null) {
      this.axisPointer = new AxisPointer();
    }
    return this.axisPointer;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public Boolean getShowContent()
  {
    return this.showContent;
  }

  public void setShowContent(Boolean showContent)
  {
    this.showContent = showContent;
  }

  public Trigger getTrigger()
  {
    return this.trigger;
  }

  public void setTrigger(Trigger trigger)
  {
    this.trigger = trigger;
  }

  public Object getPosition()
  {
    return this.position;
  }

  public void setPosition(Object position)
  {
    this.position = position;
  }

  public Object getFormatter()
  {
    return this.formatter;
  }

  public void setFormatter(Object formatter)
  {
    this.formatter = formatter;
  }

  public String getIslandFormatter()
  {
    return this.islandFormatter;
  }

  public void setIslandFormatter(String islandFormatter)
  {
    this.islandFormatter = islandFormatter;
  }

  public Integer getShowDelay()
  {
    return this.showDelay;
  }

  public void setShowDelay(Integer showDelay)
  {
    this.showDelay = showDelay;
  }

  public Integer getHideDelay()
  {
    return this.hideDelay;
  }

  public void setHideDelay(Integer hideDelay)
  {
    this.hideDelay = hideDelay;
  }

  public Double getTransitionDuration()
  {
    return this.transitionDuration;
  }

  public void setTransitionDuration(Double transitionDuration)
  {
    this.transitionDuration = transitionDuration;
  }

  public Boolean getEnterable() {
    return this.enterable;
  }

  public void setEnterable(Boolean enterable) {
    this.enterable = enterable;
  }

  public Integer getBorderRadius()
  {
    return this.borderRadius;
  }

  public void setBorderRadius(Integer borderRadius)
  {
    this.borderRadius = borderRadius;
  }

  public AxisPointer getAxisPointer()
  {
    return this.axisPointer;
  }

  public void setAxisPointer(AxisPointer axisPointer)
  {
    this.axisPointer = axisPointer;
  }

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }
}

/*
 * Qualified Name:     report.java.echarts.Tooltip
*
 */