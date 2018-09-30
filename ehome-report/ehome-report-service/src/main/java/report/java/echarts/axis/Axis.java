package report.java.echarts.axis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.AbstractData;
import report.java.echarts.Component;
import report.java.echarts.code.AxisType;
import report.java.echarts.code.NameLocation;
import report.java.echarts.code.X;
import report.java.echarts.code.Y;
import report.java.echarts.style.LineStyle;

public abstract class Axis<T> extends AbstractData<T>
  implements Component
{
  private Boolean show;
  private AxisType type;
  private Object position;
  private String name;
  private NameLocation nameLocation;
  private LineStyle nameTextStyle;
  private AxisLine axisLine;
  private AxisTick axisTick;
  private AxisLabel axisLabel;
  private SplitLine splitLine;
  private SplitArea splitArea;

  public Boolean show()
  {
    return this.show;
  }

  public T show(Boolean show)
  {
    this.show = show;
    return (T)this;
  }

  public AxisType type()
  {
    return this.type;
  }

  public AxisType getType()
  {
    return this.type;
  }

  public void setType(AxisType type)
  {
    this.type = type;
  }

  public Object getPosition()
  {
    return this.position;
  }

  public void setPosition(Object position)
  {
    this.position = position;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public NameLocation getNameLocation()
  {
    return this.nameLocation;
  }

  public void setNameLocation(NameLocation nameLocation)
  {
    this.nameLocation = nameLocation;
  }

  public T type(AxisType type)
  {
    this.type = type;
    return (T)this;
  }

  public Object position()
  {
    return this.position;
  }

  public T position(Object position)
  {
    this.position = position;
    return (T)this;
  }

  public T position(X position)
  {
    this.position = position;
    return (T)this;
  }

  public T position(Y position)
  {
    this.position = position;
    return (T)this;
  }

  public String name()
  {
    return this.name;
  }

  public T name(String name)
  {
    this.name = name;
    return (T)this;
  }

  public NameLocation nameLocation()
  {
    return this.nameLocation;
  }

  public T nameLocation(NameLocation nameLocation)
  {
    this.nameLocation = nameLocation;
    return (T)this;
  }

  public LineStyle nameTextStyle()
  {
    if (this.nameTextStyle == null) {
      this.nameTextStyle = new LineStyle();
    }
    return this.nameTextStyle;
  }

  public T nameTextStyle(LineStyle style)
  {
    this.nameTextStyle = style;
    return (T)this;
  }

  public AxisLine axisLine()
  {
    if (this.axisLine == null) {
      this.axisLine = new AxisLine();
    }
    return this.axisLine;
  }

  public T axisLine(AxisLine axisLine)
  {
    this.axisLine = axisLine;
    return (T)this;
  }

  public AxisTick axisTick()
  {
    if (this.axisTick == null) {
      this.axisTick = new AxisTick();
    }
    return this.axisTick;
  }

  public T axisTick(AxisTick axisTick)
  {
    this.axisTick = axisTick;
    return (T)this;
  }

  public AxisLabel axisLabel()
  {
    if (this.axisLabel == null) {
      this.axisLabel = new AxisLabel();
    }
    return this.axisLabel;
  }

  public T axisLabel(AxisLabel label)
  {
    this.axisLabel = label;
    return (T)this;
  }

  public SplitLine splitLine()
  {
    if (this.splitLine == null) {
      this.splitLine = new SplitLine();
    }
    return this.splitLine;
  }

  public T splitLine(SplitLine splitLine)
  {
    if (this.splitLine == null) {
      this.splitLine = splitLine;
    }
    return (T)this;
  }

  public SplitArea splitArea()
  {
    if (this.splitArea == null) {
      this.splitArea = new SplitArea();
    }
    return this.splitArea;
  }

  public T splitArea(SplitArea splitArea)
  {
    this.splitArea = splitArea;
    return (T)this;
  }

  public T data(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return (T)this;
    }
    if (this.data == null) {
      if (this.type == AxisType.category)
        this.data = new ArrayList();
      else {
        throw new RuntimeException("数据轴不能添加类目信息!");
      }
    }
    this.data.addAll(Arrays.asList(values));
    return (T)this;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }

  public LineStyle getNameTextStyle()
  {
    return this.nameTextStyle;
  }

  public void setNameTextStyle(LineStyle nameTextStyle)
  {
    this.nameTextStyle = nameTextStyle;
  }

  public AxisLine getAxisLine()
  {
    return this.axisLine;
  }

  public void setAxisLine(AxisLine axisLine)
  {
    this.axisLine = axisLine;
  }

  public AxisTick getAxisTick()
  {
    return this.axisTick;
  }

  public void setAxisTick(AxisTick axisTick)
  {
    this.axisTick = axisTick;
  }

  public AxisLabel getAxisLabel()
  {
    return this.axisLabel;
  }

  public void setAxisLabel(AxisLabel axisLabel)
  {
    this.axisLabel = axisLabel;
  }

  public SplitLine getSplitLine()
  {
    return this.splitLine;
  }

  public void setSplitLine(SplitLine splitLine)
  {
    this.splitLine = splitLine;
  }

  public SplitArea getSplitArea()
  {
    return this.splitArea;
  }

  public void setSplitArea(SplitArea splitArea)
  {
    this.splitArea = splitArea;
  }
}

/*
 * Qualified Name:     report.java.echarts.axis.Axis
*
 */