package report.java.echarts;

import java.util.HashMap;
import java.util.Map;

public class RoamController extends Basic<RoamController>
  implements Component
{
  private Integer width;
  private Integer height;
  private String fillerColor;
  private String handleColor;
  private Integer step;
  private Map<String, Boolean> mapTypeControl;

  public Integer width()
  {
    return this.width;
  }

  public RoamController width(Integer width)
  {
    this.width = width;
    return this;
  }

  public Integer height()
  {
    return this.height;
  }

  public RoamController height(Integer height)
  {
    this.height = height;
    return this;
  }

  public String fillerColor()
  {
    return this.fillerColor;
  }

  public RoamController fillerColor(String fillerColor)
  {
    this.fillerColor = fillerColor;
    return this;
  }

  public String handleColor()
  {
    return this.handleColor;
  }

  public RoamController handleColor(String handleColor)
  {
    this.handleColor = handleColor;
    return this;
  }

  public Integer step()
  {
    return this.step;
  }

  public RoamController step(Integer step)
  {
    this.step = step;
    return this;
  }

  public Map<String, Boolean> mapTypeControl()
  {
    return this.mapTypeControl;
  }

  public RoamController mapTypeControl(String key, Boolean value)
  {
    if (this.mapTypeControl == null) {
      this.mapTypeControl = new HashMap();
    }
    this.mapTypeControl.put(key, value);
    return this;
  }

  public Integer getWidth()
  {
    return this.width;
  }

  public void setWidth(Integer width)
  {
    this.width = width;
  }

  public Integer getHeight()
  {
    return this.height;
  }

  public void setHeight(Integer height)
  {
    this.height = height;
  }

  public String getFillerColor()
  {
    return this.fillerColor;
  }

  public void setFillerColor(String fillerColor)
  {
    this.fillerColor = fillerColor;
  }

  public String getHandleColor()
  {
    return this.handleColor;
  }

  public void setHandleColor(String handleColor)
  {
    this.handleColor = handleColor;
  }

  public Integer getStep()
  {
    return this.step;
  }

  public void setStep(Integer step)
  {
    this.step = step;
  }

  public Map<String, Boolean> getMapTypeControl()
  {
    return this.mapTypeControl;
  }

  public void setMapTypeControl(Map<String, Boolean> mapTypeControl)
  {
    this.mapTypeControl = mapTypeControl;
  }
}

/* 
 * Qualified Name:     report.java.echarts.RoamController
*
 */