package report.java.echarts.style;

import java.io.Serializable;

public class ControlStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Color normal;
  private Color emphasis;

  public Color normal()
  {
    if (this.normal == null) {
      this.normal = new Color();
    }
    return this.normal;
  }

  public ControlStyle normal(Color normal)
  {
    this.normal = normal;
    return this;
  }

  public Color emphasis()
  {
    if (this.emphasis == null) {
      this.emphasis = new Color();
    }
    return this.emphasis;
  }

  public ControlStyle emphasis(Color emphasis)
  {
    this.emphasis = emphasis;
    return this;
  }

  public Color getNormal()
  {
    return this.normal;
  }

  public void setNormal(Color normal)
  {
    this.normal = normal;
  }

  public Color getEmphasis()
  {
    return this.emphasis;
  }

  public void setEmphasis(Color emphasis)
  {
    this.emphasis = emphasis;
  }

  public class Color
  {
    private String color;

    public Color()
    {
    }

    public String color() {
      return this.color;
    }

    public Color color(String color)
    {
      this.color = color;
      return this;
    }

    public String getColor()
    {
      return this.color;
    }

    public void setColor(String color)
    {
      this.color = color;
    }
  }
}

/*
 * Qualified Name:     report.java.echarts.style.ControlStyle
*
 */