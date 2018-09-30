package report.java.echarts.data;

public class PointData extends BasicData<PointData>
{
  public PointData()
  {
  }

  public PointData(String name, Object value)
  {
    super(name, value);
  }

  public PointData(String name, Object symbol, Object symbolSize)
  {
    super(name, symbol, symbolSize);
  }

  public PointData(Object value, Object symbol)
  {
    super(value, symbol);
  }

  public PointData(Object value, Object symbol, Object symbolSize)
  {
    super(value, symbol, symbolSize);
  }
}

/* 
 * Qualified Name:     report.java.echarts.data.PointData
*
 */