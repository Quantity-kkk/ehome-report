package report.java.echarts.series.event;

import java.io.Serializable;

public class Detail
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String link;
  private String text;
  private String img;

  public String link()
  {
    return this.link;
  }

  public Detail link(String link)
  {
    this.link = link;
    return this;
  }

  public String text()
  {
    return this.text;
  }

  public Detail text(String text)
  {
    this.text = text;
    return this;
  }

  public String img()
  {
    return this.img;
  }

  public Detail img(String img)
  {
    this.img = img;
    return this;
  }

  public Detail()
  {
  }

  public Detail(String link, String text)
  {
    this.link = link;
    this.text = text;
  }

  public Detail(String link, String text, String img)
  {
    this.link = link;
    this.text = text;
    this.img = img;
  }

  public String getLink()
  {
    return this.link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }

  public String getText()
  {
    return this.text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getImg()
  {
    return this.img;
  }

  public void setImg(String img)
  {
    this.img = img;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.event.Detail
*
 */