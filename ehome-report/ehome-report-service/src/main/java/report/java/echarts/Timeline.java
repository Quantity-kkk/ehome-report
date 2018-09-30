package report.java.echarts;

import report.java.echarts.code.ControlPosition;
import report.java.echarts.code.TimeLineType;
import report.java.echarts.style.CheckpointStyle;
import report.java.echarts.style.ControlStyle;
import report.java.echarts.style.LineStyle;

public class Timeline extends AbstractData<Timeline>
  implements Component
{
  private Boolean show;
  private TimeLineType type;
  private Boolean notMerge;
  private Boolean realtime;
  private Object x;
  private Object y;
  private Object x2;
  private Object y2;
  private Object width;
  private Object height;
  private String backgroundColor;
  private String borderColor;
  private Integer borderWidth;
  private Integer padding;
  private ControlPosition controlPosition;
  private Boolean autoPlay;
  private Boolean loop;
  private Integer playInterval;
  private LineStyle lineStyle;
  private Label label;
  private CheckpointStyle checkpointStyle;
  private ControlStyle controlStyle;
  private Object symbol;
  private Object symbolSize;
  private Integer currentIndex;

  public Boolean show()
  {
    return this.show;
  }

  public Timeline show(Boolean show)
  {
    this.show = show;
    return this;
  }

  public TimeLineType type()
  {
    return this.type;
  }

  public Timeline type(TimeLineType type)
  {
    this.type = type;
    return this;
  }

  public Boolean notMerge()
  {
    return this.notMerge;
  }

  public Timeline notMerge(Boolean notMerge)
  {
    this.notMerge = notMerge;
    return this;
  }

  public Boolean realtime()
  {
    return this.realtime;
  }

  public Timeline realtime(Boolean realtime)
  {
    this.realtime = realtime;
    return this;
  }

  public Object x()
  {
    return this.x;
  }

  public Timeline x(Object x)
  {
    this.x = x;
    return this;
  }

  public Object y()
  {
    return this.y;
  }

  public Timeline y(Object y)
  {
    this.y = y;
    return this;
  }

  public Object x2()
  {
    return this.x2;
  }

  public Timeline x2(Object x2)
  {
    this.x2 = x2;
    return this;
  }

  public Object y2()
  {
    return this.y2;
  }

  public Timeline y2(Object y2)
  {
    this.y2 = y2;
    return this;
  }

  public Object width()
  {
    return this.width;
  }

  public Timeline width(Object width)
  {
    this.width = width;
    return this;
  }

  public Object height()
  {
    return this.height;
  }

  public Timeline height(Object height)
  {
    this.height = height;
    return this;
  }

  public String backgroundColor()
  {
    return this.backgroundColor;
  }

  public Timeline backgroundColor(String backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public String borderColor()
  {
    return this.borderColor;
  }

  public Timeline borderColor(String borderColor)
  {
    this.borderColor = borderColor;
    return this;
  }

  public Integer borderWidth()
  {
    return this.borderWidth;
  }

  public Timeline borderWidth(Integer borderWidth)
  {
    this.borderWidth = borderWidth;
    return this;
  }

  public Integer padding()
  {
    return this.padding;
  }

  public Timeline padding(Integer padding)
  {
    this.padding = padding;
    return this;
  }

  public ControlPosition controlPosition()
  {
    return this.controlPosition;
  }

  public Timeline controlPosition(ControlPosition controlPosition)
  {
    this.controlPosition = controlPosition;
    return this;
  }

  public Boolean autoPlay()
  {
    return this.autoPlay;
  }

  public Timeline autoPlay(Boolean autoPlay)
  {
    this.autoPlay = autoPlay;
    return this;
  }

  public Boolean loop()
  {
    return this.loop;
  }

  public Timeline loop(Boolean loop)
  {
    this.loop = loop;
    return this;
  }

  public Integer playInterval()
  {
    return this.playInterval;
  }

  public Timeline playInterval(Integer playInterval)
  {
    this.playInterval = playInterval;
    return this;
  }

  public LineStyle lineStyle()
  {
    if (this.lineStyle == null) {
      this.lineStyle = new LineStyle();
    }
    return this.lineStyle;
  }

  public Label label()
  {
    if (this.label == null) {
      this.label = new Label();
    }
    return this.label;
  }

  public CheckpointStyle checkpointStyle()
  {
    if (this.checkpointStyle == null) {
      this.checkpointStyle = new CheckpointStyle();
    }
    return this.checkpointStyle;
  }

  public ControlStyle controlStyle()
  {
    if (this.controlStyle == null) {
      this.controlStyle = new ControlStyle();
    }
    return this.controlStyle;
  }

  public Object symbol()
  {
    return this.symbol;
  }

  public Timeline symbol(Object symbol)
  {
    this.symbol = symbol;
    return this;
  }

  public Object symbolSize()
  {
    return this.symbolSize;
  }

  public Timeline symbolSize(Object symbolSize)
  {
    this.symbolSize = symbolSize;
    return this;
  }

  public Integer currentIndex()
  {
    return this.currentIndex;
  }

  public Timeline currentIndex(Integer currentIndex)
  {
    this.currentIndex = currentIndex;
    return this;
  }

  public LineStyle getLineStyle()
  {
    return this.lineStyle;
  }

  public void setLineStyle(LineStyle lineStyle)
  {
    this.lineStyle = lineStyle;
  }

  public Label getLabel()
  {
    return this.label;
  }

  public void setLabel(Label label)
  {
    this.label = label;
  }

  public CheckpointStyle getCheckpointStyle()
  {
    return this.checkpointStyle;
  }

  public void setCheckpointStyle(CheckpointStyle checkpointStyle)
  {
    this.checkpointStyle = checkpointStyle;
  }

  public ControlStyle getControlStyle()
  {
    return this.controlStyle;
  }

  public void setControlStyle(ControlStyle controlStyle)
  {
    this.controlStyle = controlStyle;
  }
}

/*
 * Qualified Name:     report.java.echarts.Timeline
*
 */