package report.java.echarts.series;

import java.io.Serializable;

public class Effect
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Type type;
  private Boolean show;
  private Boolean loop;
  private Integer period;
  private Integer scaleSize;
  private String color;
  private String shadowColor;
  private Integer shadowBlur;
  private Integer bounceDistance;

  public Type type()
  {
    return this.type;
  }

  public Effect type(Type type)
  {
    this.type = type;
    return this;
  }

  public Boolean show()
  {
    return this.show;
  }

  public Effect show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public Boolean loop()
  {
    return this.loop;
  }

  public Effect loop(Boolean loop)
  {
    this.loop = loop;
    return this;
  }

  public Integer period()
  {
    return this.period;
  }

  public Effect period(Integer period)
  {
    this.period = period;
    return this;
  }

  public Integer scaleSize()
  {
    return this.scaleSize;
  }

  public Effect scaleSize(Integer scaleSize)
  {
    this.scaleSize = scaleSize;
    return this;
  }

  public String color()
  {
    return this.color;
  }

  public Effect color(String color)
  {
    this.color = color;
    return this;
  }

  public String shadowColor()
  {
    return this.shadowColor;
  }

  public Effect shadowColor(String shadowColor)
  {
    this.shadowColor = shadowColor;
    return this;
  }

  public Integer shadowBlur()
  {
    return this.shadowBlur;
  }

  public Effect shadowBlur(Integer shadowBlur)
  {
    this.shadowBlur = shadowBlur;
    return this;
  }

  public Integer bounceDistance()
  {
    return this.bounceDistance;
  }

  public Effect bounceDistance(Integer bounceDistance)
  {
    this.bounceDistance = bounceDistance;
    return this;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }

  public Boolean getLoop()
  {
    return this.loop;
  }

  public void setLoop(Boolean loop)
  {
    this.loop = loop;
  }

  public Integer getPeriod()
  {
    return this.period;
  }

  public void setPeriod(Integer period)
  {
    this.period = period;
  }

  public Integer getScaleSize()
  {
    return this.scaleSize;
  }

  public void setScaleSize(Integer scaleSize)
  {
    this.scaleSize = scaleSize;
  }

  public String getColor()
  {
    return this.color;
  }

  public void setColor(String color)
  {
    this.color = color;
  }

  public String getShadowColor()
  {
    return this.shadowColor;
  }

  public void setShadowColor(String shadowColor)
  {
    this.shadowColor = shadowColor;
  }

  public Integer getShadowBlur()
  {
    return this.shadowBlur;
  }

  public void setShadowBlur(Integer shadowBlur)
  {
    this.shadowBlur = shadowBlur;
  }

  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  public Integer getBounceDistance()
  {
    return this.bounceDistance;
  }

  public void setBounceDistance(Integer bounceDistance)
  {
    this.bounceDistance = bounceDistance;
  }

  public static enum Type
  {
    scale, bounce;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.Effect
*
 */