package report.java.echarts.style;

import java.io.Serializable;
import report.java.echarts.style.itemstyle.Emphasis;
import report.java.echarts.style.itemstyle.Normal;

public class ItemStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Normal normal;
  private Emphasis emphasis;

  public Normal normal()
  {
    if (this.normal == null) {
      this.normal = new Normal();
    }
    return this.normal;
  }

  public ItemStyle normal(Normal normal)
  {
    this.normal = normal;
    return this;
  }

  public Emphasis emphasis()
  {
    if (this.emphasis == null) {
      this.emphasis = new Emphasis();
    }
    return this.emphasis;
  }

  public ItemStyle emphasis(Emphasis emphasis)
  {
    this.emphasis = emphasis;
    return this;
  }

  public Normal getNormal()
  {
    return this.normal;
  }

  public void setNormal(Normal normal)
  {
    this.normal = normal;
  }

  public Emphasis getEmphasis()
  {
    return this.emphasis;
  }

  public void setEmphasis(Emphasis emphasis)
  {
    this.emphasis = emphasis;
  }
}

/* 
 * Qualified Name:     report.java.echarts.style.ItemStyle
*
 */