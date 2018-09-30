package report.java.echarts.series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.code.SeriesType;
import report.java.echarts.series.event.Event;

public class EventRiver extends Series<EventRiver>
{
  private Integer weight;
  private List<Event> eventList;

  public Integer weight()
  {
    return this.weight;
  }

  public EventRiver weight(Integer weight)
  {
    this.weight = weight;
    return this;
  }

  public List<Event> eventList()
  {
    if (this.eventList == null) {
      this.eventList = new ArrayList();
    }
    return this.eventList;
  }

  public EventRiver eventList(List<Event> eventList)
  {
    this.eventList = eventList;
    return this;
  }

  public EventRiver eventList(Event[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    eventList().addAll(Arrays.asList(values));
    return this;
  }

  public EventRiver()
  {
    type(SeriesType.eventRiver);
  }

  public EventRiver(String name)
  {
    super(name);
    type(SeriesType.eventRiver);
  }

  public EventRiver(String name, Integer weight) {
    super(name);
    type(SeriesType.eventRiver);
    weight(weight);
  }

  public Integer getWeight()
  {
    return this.weight;
  }

  public void setWeight(Integer weight)
  {
    this.weight = weight;
  }

  public List<Event> getEventList()
  {
    return this.eventList;
  }

  public void setEventList(List<Event> eventList)
  {
    this.eventList = eventList;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.EventRiver
*
 */