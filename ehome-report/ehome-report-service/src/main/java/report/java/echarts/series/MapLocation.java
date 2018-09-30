package report.java.echarts.series;

import java.io.Serializable;
import report.java.echarts.code.X;
import report.java.echarts.code.Y;

public class MapLocation
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Object x;
  private Object y;
  private Object width;
  private Object height;

  public MapLocation()
  {
  }

  public MapLocation(Object x, Object y)
  {
    this.x = x;
    this.y = y;
  }

  public MapLocation(Object x, Object y, Object width)
  {
    this.x = x;
    this.y = y;
    this.width = width;
  }

  public MapLocation(Object x, Object y, Object width, Object height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public MapLocation(X x, Y y)
  {
    this.x = x;
    this.y = y;
  }

  public MapLocation(X x, Y y, Object width)
  {
    this.x = x;
    this.y = y;
    this.width = width;
  }

  public MapLocation(X x, Y y, Object width, Object height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Object x()
  {
    return this.x;
  }

  public MapLocation x(Object x)
  {
    this.x = x;
    return this;
  }

  public MapLocation x(X x)
  {
    this.x = x;
    return this;
  }

  public Object y()
  {
    return this.y;
  }

  public MapLocation y(Y y)
  {
    this.y = y;
    return this;
  }

  public MapLocation y(Object y)
  {
    this.y = y;
    return this;
  }

  public Object width()
  {
    return this.width;
  }

  public MapLocation width(Object width)
  {
    this.width = width;
    return this;
  }

  public Object height()
  {
    return this.height;
  }

  public MapLocation height(Object height)
  {
    this.height = height;
    return this;
  }

  public Object getX()
  {
    return this.x;
  }

  public void setX(Object x)
  {
    this.x = x;
  }

  public Object getY()
  {
    return this.y;
  }

  public void setY(Object y)
  {
    this.y = y;
  }

  public Object getWidth()
  {
    return this.width;
  }

  public void setWidth(Object width)
  {
    this.width = width;
  }

  public Object getHeight()
  {
    return this.height;
  }

  public void setHeight(Object height)
  {
    this.height = height;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.MapLocation
*
 */