package report.java.echarts;

import report.java.echarts.code.Orient;

public class DataZoom extends Basic<DataZoom>
  implements Component
{
  private Orient orient;
  private String dataBackgroundColor;
  private String fillerColor;
  private String handleColor;
  private Object xAxisIndex;
  private Object yAxisIndex;
  private Integer start;
  private Integer end;
  private Boolean realtime;
  private Boolean zoomLock;
  private Boolean showDetail;

  public Orient orient()
  {
    return this.orient;
  }

  public DataZoom orient(Orient orient)
  {
    this.orient = orient;
    return this;
  }

  public String dataBackgroundColor()
  {
    return this.dataBackgroundColor;
  }

  public DataZoom dataBackgroundColor(String dataBackgroundColor)
  {
    this.dataBackgroundColor = dataBackgroundColor;
    return this;
  }

  public String fillerColor()
  {
    return this.fillerColor;
  }

  public DataZoom fillerColor(String fillerColor)
  {
    this.fillerColor = fillerColor;
    return this;
  }

  public String handleColor()
  {
    return this.handleColor;
  }

  public DataZoom handleColor(String handleColor)
  {
    this.handleColor = handleColor;
    return this;
  }

  public Object xAxisIndex()
  {
    return this.xAxisIndex;
  }

  public DataZoom xAxisIndex(Object xAxisIndex)
  {
    this.xAxisIndex = xAxisIndex;
    return this;
  }

  public Object yAxisIndex()
  {
    return this.yAxisIndex;
  }

  public DataZoom yAxisIndex(Object yAxisIndex)
  {
    this.yAxisIndex = yAxisIndex;
    return this;
  }

  public Integer start()
  {
    return this.start;
  }

  public DataZoom start(Integer start)
  {
    this.start = start;
    return this;
  }

  public Integer end()
  {
    return this.end;
  }

  public DataZoom end(Integer end)
  {
    this.end = end;
    return this;
  }

  public Boolean realtime()
  {
    return this.realtime;
  }

  public DataZoom realtime(Boolean realtime)
  {
    this.realtime = realtime;
    return this;
  }

  public Boolean zoomLock()
  {
    return this.zoomLock;
  }

  public DataZoom zoomLock(Boolean zoomLock)
  {
    this.zoomLock = zoomLock;
    return this;
  }

  public Boolean showDetail()
  {
    return this.showDetail;
  }

  public DataZoom showDetail(Boolean showDetail)
  {
    this.showDetail = showDetail;
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

  public String getDataBackgroundColor()
  {
    return this.dataBackgroundColor;
  }

  public void setDataBackgroundColor(String dataBackgroundColor)
  {
    this.dataBackgroundColor = dataBackgroundColor;
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

  public Object getxAxisIndex()
  {
    return this.xAxisIndex;
  }

  public void setxAxisIndex(Object xAxisIndex)
  {
    this.xAxisIndex = xAxisIndex;
  }

  public Object getyAxisIndex()
  {
    return this.yAxisIndex;
  }

  public void setyAxisIndex(Object yAxisIndex)
  {
    this.yAxisIndex = yAxisIndex;
  }

  public Integer getStart()
  {
    return this.start;
  }

  public void setStart(Integer start)
  {
    this.start = start;
  }

  public Integer getEnd()
  {
    return this.end;
  }

  public void setEnd(Integer end)
  {
    this.end = end;
  }

  public Boolean getRealtime()
  {
    return this.realtime;
  }

  public void setRealtime(Boolean realtime)
  {
    this.realtime = realtime;
  }

  public Boolean getZoomLock()
  {
    return this.zoomLock;
  }

  public void setZoomLock(Boolean zoomLock)
  {
    this.zoomLock = zoomLock;
  }

  public Boolean getShowDetail()
  {
    return this.showDetail;
  }

  public void setShowDetail(Boolean showDetail)
  {
    this.showDetail = showDetail;
  }
}

/* 
 * Qualified Name:     report.java.echarts.DataZoom
*
 */