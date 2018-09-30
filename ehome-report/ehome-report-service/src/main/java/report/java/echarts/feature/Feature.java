package report.java.echarts.feature;

import java.io.Serializable;
import report.java.echarts.code.Magic;
import report.java.echarts.style.LineStyle;
import report.java.echarts.style.TextStyle;

public class Feature
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public static final DataView dataView = new DataView();

  public static final DataZoom dataZoom = new DataZoom();

  public static final Mark mark = new Mark();

  public static final SaveAsImage saveAsImage = new SaveAsImage();

  public static final MagicType magicType = new MagicType(new Magic[0]);

  public static final Restore restore = new Restore();
  private Boolean show;
  private Object title;
  private Object type;
  private Boolean readOnly;
  private Object lang;
  private LineStyle lineStyle;
  private TextStyle textStyle;
  private String icon;

  public Boolean show()
  {
    return this.show;
  }

  public Feature show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public Object title()
  {
    return this.title;
  }

  public Feature title(Object title)
  {
    this.title = title;
    return this;
  }

  public Object type()
  {
    return this.type;
  }

  public Feature type(Object type)
  {
    this.type = type;
    return this;
  }

  public Boolean readOnly()
  {
    return this.readOnly;
  }

  public Feature readOnly(Boolean readOnly)
  {
    this.readOnly = readOnly;
    return this;
  }

  public Object lang()
  {
    return this.lang;
  }

  public Feature lang(Object lang)
  {
    this.lang = lang;
    return this;
  }

  public LineStyle lineStyle()
  {
    return this.lineStyle;
  }

  public Feature lineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
    return this;
  }

  public TextStyle textStyle()
  {
    return this.textStyle;
  }

  public Feature textStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
    return this;
  }

  public String icon()
  {
    return this.icon;
  }

  public Feature icon(String icon)
  {
    this.icon = icon;
    return this;
  }

  public Boolean getShow()
  {
    return this.show;
  }

  public void setShow(Boolean show)
  {
    this.show = show;
  }

  public Object getTitle()
  {
    return this.title;
  }

  public void setTitle(Object title)
  {
    this.title = title;
  }

  public Object getType()
  {
    return this.type;
  }

  public void setType(Object type)
  {
    this.type = type;
  }

  public Boolean getReadOnly()
  {
    return this.readOnly;
  }

  public void setReadOnly(Boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  public Object getLang()
  {
    return this.lang;
  }

  public void setLang(Object lang)
  {
    this.lang = lang;
  }

  public LineStyle getLineStyle()
  {
    return this.lineStyle;
  }

  public void setLineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
  }

  public TextStyle getTextStyle()
  {
    return this.textStyle;
  }

  public void setTextStyle(TextStyle textStyle)
  {
    this.textStyle = textStyle;
  }

  public String getIcon()
  {
    return this.icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }
}

/*
 * Qualified Name:     report.java.echarts.feature.Feature
*
 */