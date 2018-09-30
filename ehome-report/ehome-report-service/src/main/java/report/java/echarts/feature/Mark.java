package report.java.echarts.feature;

import java.util.HashMap;
import java.util.Map;
import report.java.echarts.code.LineType;
import report.java.echarts.style.LineStyle;

public class Mark extends Feature
{
  public Mark()
  {
    show(Boolean.valueOf(true));
    Map title = new HashMap();
    title.put("mark", "辅助线开关");
    title.put("markUndo", "删除辅助线");
    title.put("markClear", "清空辅助线");
    title(title);
    lineStyle(new LineStyle());
    lineStyle().width(Integer.valueOf(2));
    lineStyle().color("#1e90ff");
    lineStyle().type(LineType.dashed);
  }
}

/* 
 * Qualified Name:     report.java.echarts.feature.Mark
*
 */