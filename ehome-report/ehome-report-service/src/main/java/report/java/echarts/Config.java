package report.java.echarts;

public abstract interface Config
{
  public static final String CHART_TYPE_LINE = "line";
  public static final String CHART_TYPE_BAR = "bar";
  public static final String CHART_TYPE_SCATTER = "scatter";
  public static final String CHART_TYPE_PIE = "pie";
  public static final String CHART_TYPE_RADAR = "radar";
  public static final String CHART_TYPE_MAP = "map";
  public static final String CHART_TYPE_K = "k";
  public static final String CHART_TYPE_ISLAND = "island";
  public static final String CHART_TYPE_FORCE = "force";
  public static final String CHART_TYPE_CHORD = "chord";
  public static final String CHART_TYPE_GAUGE = "gauge";
  public static final String CHART_TYPE_FUNNEL = "funnel";
  public static final String COMPONENT_TYPE_TITLE = "title";
  public static final String COMPONENT_TYPE_LEGEND = "legend";
  public static final String COMPONENT_TYPE_DATARANGE = "dataRange";
  public static final String COMPONENT_TYPE_DATAVIEW = "dataView";
  public static final String COMPONENT_TYPE_DATAZOOM = "dataZoom";
  public static final String COMPONENT_TYPE_TOOLBOX = "toolbox";
  public static final String COMPONENT_TYPE_TOOLTIP = "tooltip";
  public static final String COMPONENT_TYPE_GRID = "grid";
  public static final String COMPONENT_TYPE_AXIS = "axis";
  public static final String COMPONENT_TYPE_POLAR = "polar";
  public static final String COMPONENT_TYPE_X_AXIS = "xAxis";
  public static final String COMPONENT_TYPE_Y_AXIS = "yAxis";
  public static final String COMPONENT_TYPE_AXIS_CATEGORY = "categoryAxis";
  public static final String COMPONENT_TYPE_AXIS_VALUE = "valueAxis";
  public static final String COMPONENT_TYPE_TIMELINE = "timeline";
  public static final String backgroundColor = "rgba(0,0,0,0)";
  public static final String[] color = { "#ff7f50", "#87cefa", "#da70d6", "#32cd32", "#6495ed", 
    "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", 
    "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", 
    "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0" };

  public static final Integer DRAG_ENABLE_TIME = Integer.valueOf(120);
  public static final Integer EFFECT_ZLEVEL = Integer.valueOf(7);

  public static final String[] symbolList = { "circle", "rectangle", "triangle", "diamond", 
    "emptyCircle", "emptyRectangle", "emptyTriangle", "emptyDiamond" };
  public static final String loadingText = "Loading...";
  public static final Boolean calculable = Boolean.valueOf(false);
  public static final String calculableColor = "rgba(255,165,0,0.6)";
  public static final String calculableHolderColor = "#ccc";
  public static final String nameConnector = " & ";
  public static final String valueConnector = "=";
  public static final Boolean animation = Boolean.valueOf(true);
  public static final Boolean addDataAnimation = Boolean.valueOf(true);
  public static final Integer animationThreshold = Integer.valueOf(2000);
  public static final Integer animationDuration = Integer.valueOf(2000);
  public static final String animationEasing = "ExponentialOut";
}

/*
 * Qualified Name:     report.java.echarts.Config
*
 */