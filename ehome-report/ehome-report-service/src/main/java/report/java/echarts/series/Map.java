package report.java.echarts.series;

import report.java.echarts.code.Calculation;
import report.java.echarts.code.SelectedMode;
import report.java.echarts.code.SeriesType;

public class Map extends Series<Map>
{
  private Object selectedMode;
  private String mapType;
  private MapLocation mapLocation;
  private Calculation mapValueCalculation;
  private Integer mapValuePrecision;
  private Boolean showLegendSymbol;
  private Boolean dataRangeHoverLink;
  private Boolean roam;
  private ScaleLimit scaleLimit;
  private Object nameMap;
  private Object textFixed;
  private GeoCoord geoCoord;

  public Map()
  {
    type(SeriesType.map);
  }

  public Map(String name)
  {
    super(name);
    type(SeriesType.map);
  }

  public Map mapLocation(MapLocation mapLocation)
  {
    this.mapLocation = mapLocation;
    return this;
  }

  public Object selectedMode()
  {
    return this.selectedMode;
  }

  public Map selectedMode(Object selectedMode)
  {
    this.selectedMode = selectedMode;
    return this;
  }

  public Map selectedMode(SelectedMode selectedMode)
  {
    this.selectedMode = selectedMode;
    return this;
  }

  public String mapType()
  {
    return this.mapType;
  }

  public Map mapType(String mapType)
  {
    this.mapType = mapType;
    return this;
  }

  public MapLocation mapLocation()
  {
    if (this.mapLocation == null) {
      this.mapLocation = new MapLocation();
    }
    return this.mapLocation;
  }

  public Calculation mapValueCalculation()
  {
    return this.mapValueCalculation;
  }

  public Map mapValueCalculation(Calculation mapValueCalculation)
  {
    this.mapValueCalculation = mapValueCalculation;
    return this;
  }

  public Integer mapValuePrecision()
  {
    return this.mapValuePrecision;
  }

  public Map mapValuePrecision(Integer mapValuePrecision)
  {
    this.mapValuePrecision = mapValuePrecision;
    return this;
  }

  public Boolean showLegendSymbol()
  {
    return this.showLegendSymbol;
  }

  public Map showLegendSymbol(Boolean showLegendSymbol)
  {
    this.showLegendSymbol = showLegendSymbol;
    return this;
  }

  public Boolean dataRangeHoverLink()
  {
    return this.dataRangeHoverLink;
  }

  public Map dataRangeHoverLink(Boolean dataRangeHoverLink)
  {
    this.dataRangeHoverLink = dataRangeHoverLink;
    return this;
  }

  public Boolean roam()
  {
    return this.roam;
  }

  public Map roam(Boolean roam)
  {
    this.roam = roam;
    return this;
  }

  public ScaleLimit scaleLimit()
  {
    if (this.scaleLimit == null) {
      this.scaleLimit = new ScaleLimit();
    }
    return this.scaleLimit;
  }

  public Map scaleLimit(ScaleLimit scaleLimit)
  {
    this.scaleLimit = scaleLimit;
    return this;
  }

  public Object nameMap()
  {
    return this.nameMap;
  }

  public Map nameMap(Object nameMap)
  {
    this.nameMap = nameMap;
    return this;
  }

  public Object textFixed()
  {
    return this.textFixed;
  }

  public Map textFixed(Object textFixed)
  {
    this.textFixed = textFixed;
    return this;
  }

  public GeoCoord geoCoord()
  {
    if (this.geoCoord == null) {
      this.geoCoord = new GeoCoord();
    }
    return this.geoCoord;
  }

  public Map geoCoord(String name, String x, String y)
  {
    geoCoord().put(name, x, y);
    return this;
  }

  public Object getSelectedMode()
  {
    return this.selectedMode;
  }

  public void setSelectedMode(Object selectedMode)
  {
    this.selectedMode = selectedMode;
  }

  public String getMapType()
  {
    return this.mapType;
  }

  public void setMapType(String mapType)
  {
    this.mapType = mapType;
  }

  public MapLocation getMapLocation()
  {
    return this.mapLocation;
  }

  public void setMapLocation(MapLocation mapLocation)
  {
    this.mapLocation = mapLocation;
  }

  public Calculation getMapValueCalculation()
  {
    return this.mapValueCalculation;
  }

  public void setMapValueCalculation(Calculation mapValueCalculation)
  {
    this.mapValueCalculation = mapValueCalculation;
  }

  public Integer getMapValuePrecision()
  {
    return this.mapValuePrecision;
  }

  public void setMapValuePrecision(Integer mapValuePrecision)
  {
    this.mapValuePrecision = mapValuePrecision;
  }

  public Boolean getShowLegendSymbol()
  {
    return this.showLegendSymbol;
  }

  public void setShowLegendSymbol(Boolean showLegendSymbol)
  {
    this.showLegendSymbol = showLegendSymbol;
  }

  public Boolean getDataRangeHoverLink()
  {
    return this.dataRangeHoverLink;
  }

  public void setDataRangeHoverLink(Boolean dataRangeHoverLink)
  {
    this.dataRangeHoverLink = dataRangeHoverLink;
  }

  public Boolean getRoam()
  {
    return this.roam;
  }

  public void setRoam(Boolean roam)
  {
    this.roam = roam;
  }

  public ScaleLimit getScaleLimit()
  {
    return this.scaleLimit;
  }

  public void setScaleLimit(ScaleLimit scaleLimit)
  {
    this.scaleLimit = scaleLimit;
  }

  public Object getNameMap()
  {
    return this.nameMap;
  }

  public void setNameMap(Object nameMap)
  {
    this.nameMap = nameMap;
  }

  public Object getTextFixed()
  {
    return this.textFixed;
  }

  public void setTextFixed(Object textFixed)
  {
    this.textFixed = textFixed;
  }

  public GeoCoord getGeoCoord()
  {
    return this.geoCoord;
  }

  public void setGeoCoord(GeoCoord geoCoord)
  {
    this.geoCoord = geoCoord;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.Map
*
 */