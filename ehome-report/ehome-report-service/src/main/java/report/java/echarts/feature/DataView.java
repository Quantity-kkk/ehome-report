package report.java.echarts.feature;

public class DataView extends Feature
{
  public DataView()
  {
    show(Boolean.valueOf(true));
    title("数据视图");
    readOnly(Boolean.valueOf(false));
    lang(new String[] { "数据视图", "关闭", "刷新" });
  }
}

/* 
 * Qualified Name:     report.java.echarts.feature.DataView
*
 */