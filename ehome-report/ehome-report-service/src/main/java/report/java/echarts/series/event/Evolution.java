package report.java.echarts.series.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Evolution
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private String time;
  private Integer value;
  private Detail detail;

  public String time()
  {
    return this.time;
  }

  public Evolution time(String time)
  {
    this.time = time;
    return this;
  }

  public Evolution time(Date time)
  {
    this.time = FORMAT.format(time);
    return this;
  }

  public Integer value()
  {
    return this.value;
  }

  public Evolution value(Integer value)
  {
    this.value = value;
    return this;
  }

  public Detail detail()
  {
    return this.detail;
  }

  public Evolution detail(Detail detail)
  {
    this.detail = detail;
    return this;
  }

  public Evolution detail(String link, String text)
  {
    this.detail = new Detail(link, text);
    return this;
  }

  public Evolution detail(String link, String text, String img)
  {
    this.detail = new Detail(link, text, img);
    return this;
  }

  public Evolution()
  {
  }

  public Evolution(String time, Integer value)
  {
    this.time = time;
    this.value = value;
  }

  public String getTime()
  {
    return this.time;
  }

  public void setTime(String time)
  {
    this.time = time;
  }

  public Integer getValue()
  {
    return this.value;
  }

  public void setValue(Integer value)
  {
    this.value = value;
  }

  public Detail getDetail()
  {
    return this.detail;
  }

  public void setDetail(Detail detail)
  {
    this.detail = detail;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.event.Evolution
*
 */