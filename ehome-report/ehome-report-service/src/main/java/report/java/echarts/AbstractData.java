package report.java.echarts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractData<T>
  implements Data<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean clickable;
  private Boolean hoverable;
  protected List<Object> data;

  public List<Object> data()
  {
    if (this.data == null) {
      this.data = new ArrayList();
    }
    return this.data;
  }

  public T data(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return (T)this;
    }
    data().addAll(Arrays.asList(values));
    return (T)this;
  }

  public Boolean clickable()
  {
    return this.clickable;
  }

  public T clickable(Boolean clickable)
  {
    this.clickable = clickable;
    return (T)this;
  }

  public Boolean getClickable()
  {
    return this.clickable;
  }

  public void setClickable(Boolean clickable)
  {
    this.clickable = clickable;
  }

  public Boolean hoverable()
  {
    return this.hoverable;
  }

  public T hoverable(Boolean hoverable)
  {
    this.hoverable = hoverable;
    return (T)this;
  }

  public Boolean getHoverable()
  {
    return this.hoverable;
  }

  public void setHoverable(Boolean hoverable)
  {
    this.hoverable = hoverable;
  }

  public List<Object> getData()
  {
    return this.data;
  }

  public void setData(List<Object> data)
  {
    this.data = data;
  }
}
