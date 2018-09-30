package report.java.echarts.series;

import report.java.echarts.AbstractData;
import report.java.echarts.style.ItemStyle;

public class MarkLine extends AbstractData<MarkLine>
{
  private Object symbol;
  private Object symbolSize;
  private Object symbolRoate;
  private Effect effect;
  private ItemStyle itemStyle;
  private GeoCoord geoCoord;
  private Boolean smooth;
  private Double smoothness;
  private Integer precision;
  private Bundling bundling;

  public Bundling bundling()
  {
    if (this.bundling == null) {
      this.bundling = new Bundling();
    }
    return this.bundling;
  }

  public MarkLine bundling(Bundling bundling)
  {
    this.bundling = bundling;
    return this;
  }

  public Double smoothness()
  {
    return this.smoothness;
  }

  public MarkLine smoothness(Double smoothness)
  {
    this.smoothness = smoothness;
    return this;
  }

  public Integer precision()
  {
    return this.precision;
  }

  public MarkLine precision(Integer precision)
  {
    this.precision = precision;
    return this;
  }

  public MarkLine effect(Effect effect)
  {
    this.effect = effect;
    return this;
  }

  public MarkLine itemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
    return this;
  }

  public Object symbol()
  {
    return this.symbol;
  }

  public MarkLine symbol(Object symbol)
  {
    this.symbol = symbol;
    return this;
  }

  public Object symbolSize()
  {
    return this.symbolSize;
  }

  public MarkLine symbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
    return this;
  }

  public Object symbolRoate()
  {
    return this.symbolRoate;
  }

  public MarkLine symbolRoate(Object symbolRoate)
  {
    this.symbolRoate = symbolRoate;
    return this;
  }

  public Effect effect()
  {
    if (this.effect == null) {
      this.effect = new Effect();
    }
    return this.effect;
  }

  public ItemStyle itemStyle()
  {
    if (this.itemStyle == null) {
      this.itemStyle = new ItemStyle();
    }
    return this.itemStyle;
  }

  public GeoCoord geoCoord()
  {
    if (this.geoCoord == null) {
      this.geoCoord = new GeoCoord();
    }
    return this.geoCoord;
  }

  public MarkLine geoCoord(String name, String x, String y)
  {
    geoCoord().put(name, x, y);
    return this;
  }

  public Boolean smooth()
  {
    return this.smooth;
  }

  public MarkLine smooth(Boolean smooth)
  {
    this.smooth = smooth;
    return this;
  }

  public Effect getEffect()
  {
    return this.effect;
  }

  public void setEffect(Effect effect)
  {
    this.effect = effect;
  }

  public ItemStyle getItemStyle()
  {
    return this.itemStyle;
  }

  public void setItemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
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

  public Object getSymbolRoate()
  {
    return this.symbolRoate;
  }

  public void setSymbolRoate(Object symbolRoate)
  {
    this.symbolRoate = symbolRoate;
  }

  public GeoCoord getGeoCoord()
  {
    return this.geoCoord;
  }

  public void setGeoCoord(GeoCoord geoCoord)
  {
    this.geoCoord = geoCoord;
  }

  public Boolean getSmooth()
  {
    return this.smooth;
  }

  public void setSmooth(Boolean smooth)
  {
    this.smooth = smooth;
  }

  public Double getSmoothness() {
    return this.smoothness;
  }

  public void setSmoothness(Double smoothness) {
    this.smoothness = smoothness;
  }

  public Integer getPrecision() {
    return this.precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public Bundling getBundling() {
    return this.bundling;
  }

  public void setBundling(Bundling bundling) {
    this.bundling = bundling;
  }

  public static class Bundling
  {
    private Boolean enable;
    private Integer maxTurningAngle;

    public Boolean enable()
    {
      return this.enable;
    }
    public Bundling enable(Boolean enable) {
      this.enable = enable;
      return this;
    }
    public Integer maxTurningAngle() {
      return this.maxTurningAngle;
    }
    public Bundling maxTurningAngle(Integer maxTurningAngle) {
      this.maxTurningAngle = maxTurningAngle;
      return this;
    }

    public Boolean getEnable() {
      return this.enable;
    }

    public void setEnable(Boolean enable) {
      this.enable = enable;
    }

    public Integer getMaxTurningAngle() {
      return this.maxTurningAngle;
    }

    public void setMaxTurningAngle(Integer maxTurningAngle) {
      this.maxTurningAngle = maxTurningAngle;
    }
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.MarkLine
*
 */