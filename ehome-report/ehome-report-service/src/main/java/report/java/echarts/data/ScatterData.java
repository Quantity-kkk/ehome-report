package report.java.echarts.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScatterData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private List<Object> value;

  public ScatterData(Object width, Object height)
  {
    value(new Object[] { width, height });
  }

  public ScatterData(Object width, Object height, Object size)
  {
    value(new Object[] { width, height, size });
  }

  public List<Object> value()
  {
    if (this.value == null) {
      this.value = new ArrayList();
    }
    return this.value;
  }

  private ScatterData value(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    value().addAll(Arrays.asList(values));
    return this;
  }

  public List<Object> getValue()
  {
    return this.value;
  }

  public void setValue(List<Object> value)
  {
    this.value = value;
  }
}

/* 
 * Qualified Name:     report.java.echarts.data.ScatterData
*
 */