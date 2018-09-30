package report.java.echarts.json;

import report.java.echarts.Option;

public class GsonOption extends Option
{
  public void view()
  {
    OptionUtil.browse(this);
  }

  public String toString()
  {
    return GsonUtil.format(this);
  }

  public String toPrettyString()
  {
    return GsonUtil.prettyFormat(this);
  }

  public String exportToHtml(String fileName)
  {
    return exportToHtml(System.getProperty("java.io.tmpdir"), fileName);
  }

  public String exportToHtml(String filePath, String fileName)
  {
    return OptionUtil.exportToHtml(this, filePath, fileName);
  }
}

/* 
 * Qualified Name:     report.java.echarts.json.GsonOption
*
 */