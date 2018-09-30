package report.java.echarts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import report.java.echarts.code.Orient;
import report.java.echarts.code.Tool;
import report.java.echarts.feature.Feature;

public class Toolbox extends Basic<Toolbox>
  implements Component
{
  private Map<String, Feature> feature;
  private Orient orient;
  private List<Object> color;
  private String disableColor;
  private String effectiveColor;
  private Integer itemSize;
  private Boolean showTitle;

  public Toolbox color(List<Object> color)
  {
    this.color = color;
    return this;
  }

  public Orient orient()
  {
    return this.orient;
  }

  public Toolbox orient(Orient orient)
  {
    this.orient = orient;
    return this;
  }

  public List<Object> color()
  {
    if (this.color == null) {
      this.color = new ArrayList();
    }
    return this.color;
  }

  public String disableColor()
  {
    return this.disableColor;
  }

  public Toolbox disableColor(String disableColor)
  {
    this.disableColor = disableColor;
    return this;
  }

  public String effectiveColor()
  {
    return this.effectiveColor;
  }

  public Toolbox effectiveColor(String effectiveColor)
  {
    this.effectiveColor = effectiveColor;
    return this;
  }

  public Integer itemSize()
  {
    return this.itemSize;
  }

  public Toolbox itemSize(Integer itemSize)
  {
    this.itemSize = itemSize;
    return this;
  }

  public Boolean showTitle()
  {
    return this.showTitle;
  }

  public Toolbox showTitle(Boolean showTitle)
  {
    this.showTitle = showTitle;
    return this;
  }

  public Map<String, Feature> feature()
  {
    if (this.feature == null) {
      this.feature = new HashMap();
    }
    return this.feature;
  }

  private Toolbox _addFeature(Feature value)
  {
    if (value == null) {
      return this;
    }

    String name = value.getClass().getSimpleName();
    name = name.substring(0, 1).toLowerCase() + name.substring(1);
    _addFeatureOnce(name, value);
    return this;
  }

  public Toolbox feature(Object[] values)
  {
    if ((values == null) && (values.length == 0)) {
      return this;
    }
    if (this.feature == null) {
      this.feature = new HashMap();
    }
    for (Object t : values) {
      if ((t instanceof Feature))
        _addFeature((Feature)t);
      else if ((t instanceof Tool)) {
        switch (((Tool)t).ordinal()) {
        case 1:
          _addFeatureOnce(t, Feature.dataView);
          break;
        case 2:
          _addFeatureOnce(t, Feature.dataZoom);
          break;
        case 3:
          _addFeatureOnce(t, Feature.magicType);
          break;
        case 4:
          _addFeatureOnce(t, Feature.mark);
          break;
        case 5:
          _addFeatureOnce(t, Feature.restore);
          break;
        case 6:
          _addFeatureOnce(t, Feature.saveAsImage);
        }

      }

    }

    return this;
  }

  private void _addFeatureOnce(Object name, Feature feature)
  {
    String _name = String.valueOf(name);
    if (!feature().containsKey(_name))
      feature().put(_name, feature);
  }

  public List<Object> getColor()
  {
    return this.color;
  }

  public void setColor(List<Object> color)
  {
    this.color = color;
  }

  public Map<String, Feature> getFeature()
  {
    return this.feature;
  }

  public void setFeature(Map<String, Feature> feature)
  {
    this.feature = feature;
  }

  public Orient getOrient()
  {
    return this.orient;
  }

  public void setOrient(Orient orient)
  {
    this.orient = orient;
  }

  public String getDisableColor()
  {
    return this.disableColor;
  }

  public void setDisableColor(String disableColor)
  {
    this.disableColor = disableColor;
  }

  public String getEffectiveColor()
  {
    return this.effectiveColor;
  }

  public void setEffectiveColor(String effectiveColor)
  {
    this.effectiveColor = effectiveColor;
  }

  public Integer getItemSize()
  {
    return this.itemSize;
  }

  public void setItemSize(Integer itemSize)
  {
    this.itemSize = itemSize;
  }

  public Boolean getShowTitle()
  {
    return this.showTitle;
  }

  public void setShowTitle(Boolean showTitle)
  {
    this.showTitle = showTitle;
  }
}

/*
 * Qualified Name:     report.java.echarts.Toolbox
*
 */