package report.java.echarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.axis.AxisLabel;
import report.java.echarts.axis.AxisLine;
import report.java.echarts.axis.SplitArea;
import report.java.echarts.axis.SplitLine;
import report.java.echarts.code.PolarType;
import report.java.echarts.style.TextStyle;

public class Polar extends AbstractData<Polar>
  implements Component
{
  private Object[] center;
  private Object radius;
  private Integer startAngle;
  private Integer splitNumber;
  private Name name;
  private Object[] boundaryGap;
  private Boolean scale;
  private Integer precision;
  private Integer power;
  private AxisLine axisLine;
  private AxisLabel axisLabel;
  private SplitArea splitArea;
  private SplitLine splitLine;
  private PolarType type;
  private List<Object> indicator;

  public Object[] center()
  {
    return this.center;
  }

  public Polar center(Object[] center)
  {
    this.center = center;
    return this;
  }

  public Polar indicator(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    indicator().addAll(Arrays.asList(values));
    return this;
  }

  public Object radius()
  {
    return this.radius;
  }

  public Polar name(Name name)
  {
    this.name = name;
    return this;
  }

  public Object[] boundaryGap()
  {
    return this.boundaryGap;
  }

  public Polar boundaryGap(Object[] boundaryGap)
  {
    this.boundaryGap = boundaryGap;
    return this;
  }

  public Polar axisLine(AxisLine axisLine)
  {
    this.axisLine = axisLine;
    return this;
  }

  public Polar axisLabel(AxisLabel axisLabel)
  {
    this.axisLabel = axisLabel;
    return this;
  }

  public Polar splitArea(SplitArea splitArea)
  {
    this.splitArea = splitArea;
    return this;
  }

  public Polar splitLine(SplitLine splitLine)
  {
    this.splitLine = splitLine;
    return this;
  }

  public Polar indicator(List<Object> indicator)
  {
    this.indicator = indicator;
    return this;
  }

  public Polar center(Object width, Object height)
  {
    this.center = new Object[] { width, height };
    return this;
  }

  public Polar radius(Object value)
  {
    this.radius = value;
    return this;
  }

  public Polar radius(Object width, Object height)
  {
    this.radius = new Object[] { width, height };
    return this;
  }

  public Integer startAngle()
  {
    return this.startAngle;
  }

  public Polar startAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
    return this;
  }

  public Integer splitNumber()
  {
    return this.splitNumber;
  }

  public Polar splitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
    return this;
  }

  public Name name()
  {
    if (this.name == null) {
      this.name = new Name();
    }
    return this.name;
  }

  public Polar boundaryGap(Object min, Object max)
  {
    this.boundaryGap = new Object[] { min, max };
    return this;
  }

  public Boolean scale()
  {
    return this.scale;
  }

  public Polar scale(Boolean scale)
  {
    this.scale = scale;
    return this;
  }

  public Integer precision()
  {
    return this.precision;
  }

  public Polar precision(Integer precision)
  {
    this.precision = precision;
    return this;
  }

  public Integer power()
  {
    return this.power;
  }

  public Polar power(Integer power)
  {
    this.power = power;
    return this;
  }

  public AxisLine axisLine()
  {
    if (this.axisLine == null) {
      this.axisLine = new AxisLine();
    }
    return this.axisLine;
  }

  public AxisLabel axisLabel()
  {
    if (this.axisLabel == null) {
      this.axisLabel = new AxisLabel();
    }
    return this.axisLabel;
  }

  public SplitArea splitArea()
  {
    if (this.splitArea == null) {
      this.splitArea = new SplitArea();
    }
    return this.splitArea;
  }

  public SplitLine splitLine()
  {
    if (this.splitLine == null) {
      this.splitLine = new SplitLine();
    }
    return this.splitLine;
  }

  public PolarType type()
  {
    return this.type;
  }

  public Polar type(PolarType type)
  {
    this.type = type;
    return this;
  }

  public List<Object> indicator()
  {
    if (this.indicator == null) {
      this.indicator = new ArrayList();
    }
    return this.indicator;
  }

  public Name getName()
  {
    return this.name;
  }

  public void setName(Name name)
  {
    this.name = name;
  }

  public Object[] getBoundaryGap()
  {
    return this.boundaryGap;
  }

  public void setBoundaryGap(Object[] boundaryGap)
  {
    this.boundaryGap = boundaryGap;
  }

  public AxisLine getAxisLine()
  {
    return this.axisLine;
  }

  public void setAxisLine(AxisLine axisLine)
  {
    this.axisLine = axisLine;
  }

  public AxisLabel getAxisLabel()
  {
    return this.axisLabel;
  }

  public void setAxisLabel(AxisLabel axisLabel)
  {
    this.axisLabel = axisLabel;
  }

  public SplitArea getSplitArea()
  {
    return this.splitArea;
  }

  public void setSplitArea(SplitArea splitArea)
  {
    this.splitArea = splitArea;
  }

  public SplitLine getSplitLine()
  {
    return this.splitLine;
  }

  public void setSplitLine(SplitLine splitLine)
  {
    this.splitLine = splitLine;
  }

  public List<Object> getIndicator()
  {
    return this.indicator;
  }

  public void setIndicator(List<Object> indicator)
  {
    this.indicator = indicator;
  }

  public Object[] getCenter()
  {
    return this.center;
  }

  public void setCenter(Object[] center)
  {
    this.center = center;
  }

  public Object getRadius()
  {
    return this.radius;
  }

  public void setRadius(Object radius)
  {
    this.radius = radius;
  }

  public Integer getStartAngle()
  {
    return this.startAngle;
  }

  public void setStartAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
  }

  public Integer getSplitNumber()
  {
    return this.splitNumber;
  }

  public void setSplitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
  }

  public Boolean getScale()
  {
    return this.scale;
  }

  public void setScale(Boolean scale)
  {
    this.scale = scale;
  }

  public Integer getPrecision()
  {
    return this.precision;
  }

  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }

  public Integer getPower()
  {
    return this.power;
  }

  public void setPower(Integer power)
  {
    this.power = power;
  }

  public PolarType getType()
  {
    return this.type;
  }

  public void setType(PolarType type)
  {
    this.type = type;
  }

  public static class Name
  {
    private Boolean show;
    private TextStyle textStyle;

    public Name() {
      show(Boolean.valueOf(true));
      textStyle(new TextStyle());
      this.textStyle.color("#333");
    }

    public Boolean show()
    {
      return this.show;
    }

    public Name show(Boolean show)
    {
      this.show = show;
      return this;
    }

    public TextStyle textStyle()
    {
      if (this.textStyle == null) {
        this.textStyle = new TextStyle();
      }
      return this.textStyle;
    }

    public Name textStyle(TextStyle textStyle)
    {
      this.textStyle = textStyle;
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

    public TextStyle getTextStyle()
    {
      return this.textStyle;
    }

    public void setTextStyle(TextStyle textStyle)
    {
      this.textStyle = textStyle;
    }
  }
}

/* 
 * Qualified Name:     report.java.echarts.Polar
*
 */