package report.java.echarts.series.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Event
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private Integer weight;
  private List<Evolution> evolution;

  public String name()
  {
    return this.name;
  }

  public Event name(String name)
  {
    this.name = name;
    return this;
  }

  public Integer weight()
  {
    return this.weight;
  }

  public Event weight(Integer weight)
  {
    this.weight = weight;
    return this;
  }

  public List<Evolution> evolution()
  {
    if (this.evolution == null) {
      this.evolution = new ArrayList();
    }
    return this.evolution;
  }

  public Event evolution(List<Evolution> evolution)
  {
    this.evolution = evolution;
    return this;
  }

  public Event evolution(Evolution[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    evolution().addAll(Arrays.asList(values));
    return this;
  }

  public Event()
  {
  }

  public Event(String name)
  {
    this.name = name;
  }

  public Event(String name, Integer weight)
  {
    this.name = name;
    this.weight = weight;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Integer getWeight()
  {
    return this.weight;
  }

  public void setWeight(Integer weight)
  {
    this.weight = weight;
  }

  public List<Evolution> getEvolution()
  {
    return this.evolution;
  }

  public void setEvolution(List<Evolution> evolution)
  {
    this.evolution = evolution;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.event.Event
*
 */