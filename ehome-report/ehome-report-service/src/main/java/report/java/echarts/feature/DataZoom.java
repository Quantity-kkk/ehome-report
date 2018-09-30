package report.java.echarts.feature;

import java.util.HashMap;
import java.util.Map;

public class DataZoom extends Feature
{
  public DataZoom()
  {
    show(Boolean.valueOf(true));
    Map title = new HashMap();
    title.put("dataZoom", "区域缩放");
    title.put("dataZoomReset", "区域缩放后退");
    title(title);
  }
}

/*
 * Qualified Name:     report.java.echarts.feature.DataZoom
*
 */