package report.java.echarts.style;

import java.io.Serializable;
import report.java.echarts.code.FontStyle;
import report.java.echarts.code.X;

public class TextStyle
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String color;
  private String decoration;
  private X align;
  private Integer fontSize;
  private String fontFamily;
  private String fontFamily2;
  private FontStyle fontStyle;
  private Object fontWeight;

  public String color()
  {
    return this.color;
  }

  public TextStyle color(String color)
  {
    this.color = color;
    return this;
  }

  public String decoration()
  {
    return this.decoration;
  }

  public TextStyle decoration(String decoration)
  {
    this.decoration = decoration;
    return this;
  }

  public X align()
  {
    return this.align;
  }

  public TextStyle align(X align)
  {
    this.align = align;
    return this;
  }

  public Integer fontSize()
  {
    return this.fontSize;
  }

  public TextStyle fontSize(Integer fontSize)
  {
    this.fontSize = fontSize;
    return this;
  }

  public String fontFamily()
  {
    return this.fontFamily;
  }

  public TextStyle fontFamily(String fontFamily)
  {
    this.fontFamily = fontFamily;
    return this;
  }

  public String fontFamily2()
  {
    return this.fontFamily2;
  }

  public TextStyle fontFamily2(String fontFamily2)
  {
    this.fontFamily2 = fontFamily2;
    return this;
  }

  public FontStyle fontStyle()
  {
    return this.fontStyle;
  }

  public TextStyle fontStyle(FontStyle fontStyle)
  {
    this.fontStyle = fontStyle;
    return this;
  }

  public Object fontWeight()
  {
    return this.fontWeight;
  }

  public TextStyle fontWeight(Object fontWeight)
  {
    this.fontWeight = fontWeight;
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

  public String getDecoration()
  {
    return this.decoration;
  }

  public void setDecoration(String decoration)
  {
    this.decoration = decoration;
  }

  public X getAlign()
  {
    return this.align;
  }

  public void setAlign(X align)
  {
    this.align = align;
  }

  public Integer getFontSize()
  {
    return this.fontSize;
  }

  public void setFontSize(Integer fontSize)
  {
    this.fontSize = fontSize;
  }

  public String getFontFamily()
  {
    return this.fontFamily;
  }

  public void setFontFamily(String fontFamily)
  {
    this.fontFamily = fontFamily;
  }

  public String getFontFamily2()
  {
    return this.fontFamily2;
  }

  public void setFontFamily2(String fontFamily2)
  {
    this.fontFamily2 = fontFamily2;
  }

  public FontStyle getFontStyle()
  {
    return this.fontStyle;
  }

  public void setFontStyle(FontStyle fontStyle)
  {
    this.fontStyle = fontStyle;
  }

  public Object getFontWeight()
  {
    return this.fontWeight;
  }

  public void setFontWeight(Object fontWeight)
  {
    this.fontWeight = fontWeight;
  }
}

/*
 * Qualified Name:     report.java.echarts.style.TextStyle
*
 */