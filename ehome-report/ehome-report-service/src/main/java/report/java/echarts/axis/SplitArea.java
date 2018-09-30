package report.java.echarts.axis;

import java.io.Serializable;
import report.java.echarts.style.AreaStyle;

public class SplitArea
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean show;
  private AreaStyle areaStyle;

  public Boolean show()
  {
    return this.show;
  }

  public SplitArea show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public SplitArea areaStyle(AreaStyle areaStyle)
  {
    this.areaStyle = areaStyle;
    return this;
  }

  public AreaStyle areaStyle()
  {
    if (this.areaStyle == null) {
      this.areaStyle = new AreaStyle();
    }
    return this.areaStyle;
  }

  public AreaStyle getAreaStyle()
  {
    return this.areaStyle;
  }

  public void setAreaStyle(AreaStyle areaStyle)
  {
    this.areaStyle = areaStyle;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }
}

/*
 * Qualified Name:     report.java.echarts.axis.SplitArea
*
 */