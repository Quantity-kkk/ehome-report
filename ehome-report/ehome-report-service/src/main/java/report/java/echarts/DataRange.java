package report.java.echarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.code.Orient;
import report.java.echarts.style.TextStyle;

public class DataRange extends Basic<DataRange>
  implements Component
{
  private Orient orient;
  private Integer itemWidth;
  private Integer itemHeight;
  private Integer min;
  private Integer max;
  private Integer precision;
  private Integer splitNumber;
  private Boolean calculable;
  private Boolean realtime;
  private List<String> color;
  private Object formatter;
  private List<String> text;
  private TextStyle textStyle;
  private Boolean hoverLink;

  public DataRange color(List<String> color)
  {
    this.color = color;
    return this;
  }

  public DataRange text(List<String> text)
  {
    this.text = text;
    return this;
  }

  public Boolean hoverLink()
  {
    return this.hoverLink;
  }

  public DataRange hoverLink(Boolean hoverLink)
  {
    this.hoverLink = hoverLink;
    return this;
  }

  public Orient getOrient()
  {
    return this.orient;
  }

  public void setOrient(Orient orient)
  {
    this.orient = orient;
  }

  public Integer getItemWidth()
  {
    return this.itemWidth;
  }

  public void setItemWidth(Integer itemWidth)
  {
    this.itemWidth = itemWidth;
  }

  public Integer getItemHeight()
  {
    return this.itemHeight;
  }

  public void setItemHeight(Integer itemHeight)
  {
    this.itemHeight = itemHeight;
  }

  public Integer getMin()
  {
    return this.min;
  }

  public void setMin(Integer min)
  {
    this.min = min;
  }

  public Integer getMax()
  {
    return this.max;
  }

  public void setMax(Integer max)
  {
    this.max = max;
  }

  public Integer getPrecision()
  {
    return this.precision;
  }

  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }

  public Integer getSplitNumber()
  {
    return this.splitNumber;
  }

  public void setSplitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
  }

  public Boolean getCalculable()
  {
    return this.calculable;
  }

  public void setCalculable(Boolean calculable)
  {
    this.calculable = calculable;
  }

  public Boolean getRealtime()
  {
    return this.realtime;
  }

  public void setRealtime(Boolean realtime)
  {
    this.realtime = realtime;
  }

  public Object getFormatter()
  {
    return this.formatter;
  }

  public void setFormatter(Object formatter)
  {
    this.formatter = formatter;
  }

  public DataRange textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return this;
  }

  public Orient orient()
  {
    return this.orient;
  }

  public DataRange orient(Orient orient)
  {
    this.orient = orient;
    return this;
  }

  public Integer itemWidth()
  {
    return this.itemWidth;
  }

  public DataRange itemWidth(Integer itemWidth)
  {
    this.itemWidth = itemWidth;
    return this;
  }

  public Integer itemHeight()
  {
    return this.itemHeight;
  }

  public DataRange itemHeight(Integer itemHeight)
  {
    this.itemHeight = itemHeight;
    return this;
  }

  public Integer min()
  {
    return this.min;
  }

  public DataRange min(Integer min)
  {
    this.min = min;
    return this;
  }

  public Integer max()
  {
    return this.max;
  }

  public DataRange max(Integer max)
  {
    this.max = max;
    return this;
  }

  public Integer precision()
  {
    return this.precision;
  }

  public DataRange precision(Integer precision)
  {
    this.precision = precision;
    return this;
  }

  public Integer splitNumber()
  {
    return this.splitNumber;
  }

  public DataRange splitNumber(Integer splitNumber)
  {
    this.splitNumber = splitNumber;
    return this;
  }

  public Boolean calculable()
  {
    return this.calculable;
  }

  public DataRange calculable(Boolean calculable)
  {
    this.calculable = calculable;
    return this;
  }

  public Boolean realtime()
  {
    return this.realtime;
  }

  public DataRange realtime(Boolean realtime)
  {
    this.realtime = realtime;
    return this;
  }

  public List<String> color()
  {
    if (this.color == null) {
      this.color = new ArrayList();
    }
    return this.color;
  }

  public DataRange color(String[] colors)
  {
    if ((colors == null) || (colors.length == 0)) {
      return this;
    }
    color().addAll(Arrays.asList(colors));
    return this;
  }

  public Object formatter()
  {
    return this.formatter;
  }

  public DataRange formatter(Object formatter)
  {
    this.formatter = formatter;
    return this;
  }

  public List<String> text()
  {
    if (this.text == null) {
      this.text = new ArrayList();
    }
    return this.text;
  }

  public DataRange text(String[] texts)
  {
    if ((texts == null) || (texts.length == 0)) {
      return this;
    }
    text().addAll(Arrays.asList(texts));
    return this;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public List<String> getColor()
  {
    return this.color;
  }

  public void setColor(List<String> color)
  {
    this.color = color;
  }

  public List<String> getText()
  {
    return this.text;
  }

  public void setText(List<String> text)
  {
    this.text = text;
  }

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }

  public Boolean getHoverLink()
  {
    return this.hoverLink;
  }

  public void setHoverLink(Boolean hoverLink)
  {
    this.hoverLink = hoverLink;
  }
}

/* 
 * Qualified Name:     report.java.echarts.DataRange
*
 */