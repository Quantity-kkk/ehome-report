package report.java.echarts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.axis.Axis;
import report.java.echarts.code.Trigger;
import report.java.echarts.series.Series;
import report.java.echarts.style.ItemStyle;

public class Option
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object backgroundColor;
  private List<Object> color;
  private Object renderAsImage;
  private Boolean calculable;
  private Boolean animation;
  private Timeline timeline;
  private Title title;
  private Toolbox toolbox;
  private Tooltip tooltip;
  private Legend legend;
  private DataRange dataRange;
  private DataZoom dataZoom;
  private Grid grid;
  private List<Polar> polar;
  private List<Axis> xAxis;
  private List<Axis> yAxis;
  private List<Series> series;
  private List<Option> options;
  private ItemStyle itemStyle;

  public ItemStyle itemStyle()
  {
    if (this.itemStyle == null) {
      this.itemStyle = new ItemStyle();
    }
    return this.itemStyle;
  }

  public Option itemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
    return this;
  }

  public List<Polar> polar()
  {
    if (this.polar == null) {
      this.polar = new ArrayList();
    }
    return this.polar;
  }

  public Option polar(List<Polar> polar)
  {
    this.polar = polar;
    return this;
  }

  public Option polar(Polar[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    polar().addAll(Arrays.asList(values));
    return this;
  }

  public Option timeline(Timeline timeline)
  {
    this.timeline = timeline;
    return this;
  }

  public Option title(Title title)
  {
    this.title = title;
    return this;
  }

  public Option title(String text)
  {
    title().text(text);
    return this;
  }

  public Option title(String text, String subtext)
  {
    title().text(text).subtext(subtext);
    return this;
  }

  public Option toolbox(Toolbox toolbox)
  {
    this.toolbox = toolbox;
    return this;
  }

  public Option tooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
    return this;
  }

  public Option tooltip(Trigger trigger)
  {
    tooltip().trigger(trigger);
    return this;
  }

  public Option legend(Legend legend)
  {
    this.legend = legend;
    return this;
  }

  public Option dataRange(DataRange dataRange)
  {
    this.dataRange = dataRange;
    return this;
  }

  public Option dataZoom(DataZoom dataZoom)
  {
    this.dataZoom = dataZoom;
    return this;
  }

  public Option grid(Grid grid)
  {
    this.grid = grid;
    return this;
  }

  public Option xAxis(List<Axis> xAxis)
  {
    this.xAxis = xAxis;
    return this;
  }

  public Option yAxis(List<Axis> yAxis)
  {
    this.yAxis = yAxis;
    return this;
  }

  public Option series(List<Series> series)
  {
    this.series = series;
    return this;
  }

  public Option options(List<Option> options)
  {
    this.options = options;
    return this;
  }

  public Object backgroundColor()
  {
    return this.backgroundColor;
  }

  public Option backgroundColor(Object backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public List<Object> color()
  {
    if (this.color == null) {
      this.color = new ArrayList();
    }
    return this.color;
  }

  public Option color(Object[] colors)
  {
    if ((colors == null) || (colors.length == 0)) {
      return this;
    }
    color().addAll(Arrays.asList(colors));
    return this;
  }

  public Object renderAsImage()
  {
    return this.renderAsImage;
  }

  public Option renderAsImage(Object renderAsImage)
  {
    this.renderAsImage = renderAsImage;
    return this;
  }

  public Boolean calculable()
  {
    return this.calculable;
  }

  public Option calculable(Boolean calculable)
  {
    this.calculable = calculable;
    return this;
  }

  public Boolean animation()
  {
    return this.animation;
  }

  public Option animation(Boolean animation)
  {
    this.animation = animation;
    return this;
  }

  public Timeline timeline()
  {
    if (this.timeline == null) {
      this.timeline = new Timeline();
    }
    return this.timeline;
  }

  public Title title()
  {
    if (this.title == null) {
      this.title = new Title();
    }
    return this.title;
  }

  public Toolbox toolbox()
  {
    if (this.toolbox == null) {
      this.toolbox = new Toolbox();
    }
    return this.toolbox;
  }

  public Tooltip tooltip()
  {
    if (this.tooltip == null) {
      this.tooltip = new Tooltip();
    }
    return this.tooltip;
  }

  public Legend legend()
  {
    if (this.legend == null) {
      this.legend = new Legend();
    }
    return this.legend;
  }

  public Option legend(Object[] values)
  {
    legend().data(values);
    return this;
  }

  public DataRange dataRange()
  {
    if (this.dataRange == null) {
      this.dataRange = new DataRange();
    }
    return this.dataRange;
  }

  public DataZoom dataZoom()
  {
    if (this.dataZoom == null) {
      this.dataZoom = new DataZoom();
    }
    return this.dataZoom;
  }

  public Grid grid()
  {
    if (this.grid == null) {
      this.grid = new Grid();
    }
    return this.grid;
  }

  public List<Axis> xAxis()
  {
    if (this.xAxis == null) {
      this.xAxis = new ArrayList();
    }
    return this.xAxis;
  }

  public Option xAxis(Axis[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    if (xAxis().size() == 2) {
      throw new RuntimeException("xAxis已经存在2个，无法继续添加!");
    }
    if (xAxis().size() + values.length > 2) {
      throw new RuntimeException("添加的xAxis超出了最大允许的范围:2!");
    }
    xAxis().addAll(Arrays.asList(values));
    return this;
  }

  public List<Axis> yAxis()
  {
    if (this.yAxis == null) {
      this.yAxis = new ArrayList();
    }
    return this.yAxis;
  }

  public Option yAxis(Axis[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    if (yAxis().size() == 2) {
      throw new RuntimeException("yAxis已经存在2个，无法继续添加!");
    }
    if (yAxis().size() + values.length > 2) {
      throw new RuntimeException("添加的yAxis超出了最大允许的范围:2!");
    }
    yAxis().addAll(Arrays.asList(values));
    return this;
  }

  public List<Series> series()
  {
    if (this.series == null) {
      this.series = new ArrayList();
    }
    return this.series;
  }

  public Option series(Series[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    series().addAll(Arrays.asList(values));
    return this;
  }

  public List<Option> options()
  {
    if (this.options == null) {
      this.options = new ArrayList();
    }
    return this.options;
  }

  public Option options(Option[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    options().addAll(Arrays.asList(values));
    return this;
  }

  public Timeline getTimeline()
  {
    return this.timeline;
  }

  public void setTimeline(Timeline timeline)
  {
    this.timeline = timeline;
  }

  public Title getTitle()
  {
    return this.title;
  }

  public void setTitle(Title title)
  {
    this.title = title;
  }

  public Toolbox getToolbox()
  {
    return this.toolbox;
  }

  public void setToolbox(Toolbox toolbox)
  {
    this.toolbox = toolbox;
  }

  public Tooltip getTooltip()
  {
    return this.tooltip;
  }

  public void setTooltip(Tooltip tooltip)
  {
    this.tooltip = tooltip;
  }

  public Legend getLegend()
  {
    return this.legend;
  }

  public void setLegend(Legend legend)
  {
    this.legend = legend;
  }

  public DataRange getDataRange()
  {
    return this.dataRange;
  }

  public void setDataRange(DataRange dataRange)
  {
    this.dataRange = dataRange;
  }

  public DataZoom getDataZoom()
  {
    return this.dataZoom;
  }

  public void setDataZoom(DataZoom dataZoom)
  {
    this.dataZoom = dataZoom;
  }

  public Grid getGrid()
  {
    return this.grid;
  }

  public void setGrid(Grid grid)
  {
    this.grid = grid;
  }

  public List<Axis> getxAxis()
  {
    return this.xAxis;
  }

  public void setxAxis(List<Axis> xAxis)
  {
    this.xAxis = xAxis;
  }

  public List<Axis> getyAxis()
  {
    return this.yAxis;
  }

  public void setyAxis(List<Axis> yAxis)
  {
    this.yAxis = yAxis;
  }

  public Object getBackgroundColor()
  {
    return this.backgroundColor;
  }

  public void setBackgroundColor(Object backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  public List<Object> getColor()
  {
    return this.color;
  }

  public void setColor(List<Object> color)
  {
    this.color = color;
  }

  public Object getRenderAsImage()
  {
    return this.renderAsImage;
  }

  public void setRenderAsImage(Object renderAsImage)
  {
    this.renderAsImage = renderAsImage;
  }

  public Boolean getCalculable()
  {
    return this.calculable;
  }

  public void setCalculable(Boolean calculable)
  {
    this.calculable = calculable;
  }

  public Boolean getAnimation()
  {
    return this.animation;
  }

  public void setAnimation(Boolean animation)
  {
    this.animation = animation;
  }

  public List<Series> getSeries()
  {
    return this.series;
  }

  public void setSeries(List<Series> series)
  {
    this.series = series;
  }

  public List<Option> getOptions()
  {
    return this.options;
  }

  public void setOptions(List<Option> options)
  {
    this.options = options;
  }

  public List<Polar> getPolar()
  {
    return this.polar;
  }

  public void setPolar(List<Polar> polar)
  {
    this.polar = polar;
  }

  public ItemStyle getItemStyle()
  {
    return this.itemStyle;
  }

  public void setItemStyle(ItemStyle itemStyle)
  {
    this.itemStyle = itemStyle;
  }
}

/* 
 * Qualified Name:     report.java.echarts.Option
*
 */