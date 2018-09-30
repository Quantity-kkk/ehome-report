package report.java.echarts.feature;

import java.util.HashMap;
import java.util.Map;
import report.java.echarts.code.Magic;
import report.java.echarts.series.Funnel;

public class MagicType extends Feature
{
  private Option option;

  public Feature option(Option option)
  {
    this.option = option;
    return this;
  }

  public Option option()
  {
    return this.option;
  }

  public Option getOption()
  {
    return this.option;
  }

  public void setOption(Option option)
  {
    this.option = option;
  }

  public MagicType(Magic[] magics)
  {
    show(Boolean.valueOf(true));
    Map title = new HashMap();
    title.put("line", "折线图切换");
    title.put("bar", "柱形图切换");
    title.put("stack", "堆积");
    title.put("tiled", "平铺");
    title(title);
    if ((magics == null) || (magics.length == 0))
      type(new Object[] { Magic.bar, Magic.line, Magic.stack, Magic.tiled });
    else
      type(magics);
  }

  public static class Option
  {
    private Funnel funnel;

    public Option funnel(Funnel funnel)
    {
      this.funnel = funnel;
      return this;
    }

    public Funnel funnel() {
      return this.funnel;
    }

    public Funnel getFunnel() {
      return this.funnel;
    }

    public void setFunnel(Funnel funnel) {
      this.funnel = funnel;
    }
  }
}

/*
 * Qualified Name:     report.java.echarts.feature.MagicType
*
 */