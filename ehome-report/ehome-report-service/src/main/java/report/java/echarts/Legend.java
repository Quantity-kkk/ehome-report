package report.java.echarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.code.Orient;
import report.java.echarts.style.TextStyle;

public class Legend extends Basic<Legend>
  implements Data<Legend>, Component
{
  private Orient orient;
  private Integer itemWidth;
  private Integer itemHeight;
  private TextStyle textStyle;
  private Object selectedMode;
  private List<Object> data;

  public Legend()
  {
  }

  public Legend(Object[] values)
  {
    data(values);
  }

  public Legend textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return this;
  }

  public Legend data(List<Object> data)
  {
    this.data = data;
    return this;
  }

  public Orient orient()
  {
    return this.orient;
  }

  public Legend orient(Orient orient)
  {
    this.orient = orient;
    return this;
  }

  public Integer itemWidth()
  {
    return this.itemWidth;
  }

  public Legend itemWidth(Integer itemWidth)
  {
    this.itemWidth = itemWidth;
    return this;
  }

  public Integer itemHeight()
  {
    return this.itemHeight;
  }

  public Legend itemHeight(Integer itemHeight)
  {
    this.itemHeight = itemHeight;
    return this;
  }

  public TextStyle textStyle()
  {
    if (this.textStyle == null) {
      this.textStyle = new TextStyle();
    }
    return this.textStyle;
  }

  public Object selectedMode()
  {
    return this.selectedMode;
  }

  public Legend selectedMode(Object selectedMode)
  {
    this.selectedMode = selectedMode;
    return this;
  }

  public List<Object> data()
  {
    if (this.data == null) {
      this.data = new ArrayList();
    }
    return this.data;
  }

  public Legend data(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    data().addAll(Arrays.asList(values));
    return this;
  }

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }

  public List<Object> getData()
  {
    return this.data;
  }

  public void setData(List<Object> data)
  {
    this.data = data;
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

  public Object getSelectedMode()
  {
    return this.selectedMode;
  }

  public void setSelectedMode(Object selectedMode)
  {
    this.selectedMode = selectedMode;
  }
}

/*
 * Qualified Name:     report.java.echarts.Legend
*
 */