package report.java.echarts.series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.code.SeriesType;
import report.java.echarts.code.Sort;
import report.java.echarts.series.force.Category;
import report.java.echarts.series.force.Link;
import report.java.echarts.series.force.Node;

public class Chord extends Series<Chord>
{
  private List<Category> categories;
  private List<Node> nodes;
  private List<Link> links;
  private Boolean ribbonType;
  private Integer padding;
  private Object radius;
  private Integer startAngle;
  private Sort sort;
  private Sort sortSub;
  private Boolean showScale;
  private Boolean showScaleText;
  private Boolean clockWise;
  private Integer minRadius;
  private Integer maxRadius;
  private Object[][] matrix;

  public Chord()
  {
    type(SeriesType.chord);
  }

  public Chord(String name)
  {
    super(name);
    type(SeriesType.chord);
  }

  public Chord categories(List<Category> categories)
  {
    this.categories = categories;
    return this;
  }

  public Chord nodes(List<Node> nodes)
  {
    this.nodes = nodes;
    return this;
  }

  public Chord links(List<Link> links)
  {
    this.links = links;
    return this;
  }

  public List<Category> categories()
  {
    if (this.categories == null) {
      this.categories = new ArrayList();
    }
    return this.categories;
  }

  public Chord categories(Category[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    categories().addAll(Arrays.asList(values));
    return this;
  }

  public Chord categories(String[] names)
  {
    if ((names == null) || (names.length == 0)) {
      return this;
    }
    for (String name : names) {
      categories().add(new Category(name));
    }
    return this;
  }

  public Chord categories(Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    for (Object value : values) {
      if ((value instanceof String))
        categories().add(new Category((String)value));
      else if ((value instanceof Category)) {
        categories().add((Category)value);
      }
    }

    return this;
  }

  public List<Node> nodes()
  {
    if (this.nodes == null) {
      this.nodes = new ArrayList();
    }
    return this.nodes;
  }

  public Chord nodes(Node[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    nodes().addAll(Arrays.asList(values));
    return this;
  }

  public List<Link> links()
  {
    if (this.links == null) {
      this.links = new ArrayList();
    }
    return this.links;
  }

  public Chord links(Link[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    links().addAll(Arrays.asList(values));
    return this;
  }

  public Boolean ribbonType()
  {
    return this.ribbonType;
  }

  public Chord ribbonType(Boolean ribbonType)
  {
    this.ribbonType = ribbonType;
    return this;
  }

  public Integer padding()
  {
    return this.padding;
  }

  public Chord padding(Integer padding)
  {
    this.padding = padding;
    return this;
  }

  public Object radius()
  {
    return this.radius;
  }

  public Chord radius(Object value)
  {
    this.radius = value;
    return this;
  }

  public Chord radius(Object width, Object height)
  {
    this.radius = new Object[] { width, height };
    return this;
  }

  public Integer startAngle()
  {
    return this.startAngle;
  }

  public Chord startAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
    return this;
  }

  public Sort sort()
  {
    return this.sort;
  }

  public Chord sort(Sort sort)
  {
    this.sort = sort;
    return this;
  }

  public Sort sortSub()
  {
    return this.sortSub;
  }

  public Chord sortSub(Sort sortSub)
  {
    this.sortSub = sortSub;
    return this;
  }

  public Boolean showScale()
  {
    return this.showScale;
  }

  public Chord showScale(Boolean showScale)
  {
    this.showScale = showScale;
    return this;
  }

  public Boolean showScaleText()
  {
    return this.showScaleText;
  }

  public Chord showScaleText(Boolean showScaleText)
  {
    this.showScaleText = showScaleText;
    return this;
  }

  public Boolean clockWise()
  {
    return this.clockWise;
  }

  public Chord clockWise(Boolean clockWise)
  {
    this.clockWise = clockWise;
    return this;
  }

  public Integer minRadius()
  {
    return this.minRadius;
  }

  public Chord minRadius(Integer minRadius)
  {
    this.minRadius = minRadius;
    return this;
  }

  public Integer maxRadius()
  {
    return this.maxRadius;
  }

  public Chord maxRadius(Integer maxRadius)
  {
    this.maxRadius = maxRadius;
    return this;
  }

  public Object[][] matrix()
  {
    return this.matrix;
  }

  public Chord matrix(Object[][] matrix)
  {
    this.matrix = matrix;
    return this;
  }

  public List<Category> getCategories() {
    return this.categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public List<Node> getNodes() {
    return this.nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public List<Link> getLinks() {
    return this.links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public Boolean getRibbonType() {
    return this.ribbonType;
  }

  public void setRibbonType(Boolean ribbonType) {
    this.ribbonType = ribbonType;
  }

  public Integer getPadding()
  {
    return this.padding;
  }

  public void setPadding(Integer padding)
  {
    this.padding = padding;
  }

  public Object getRadius()
  {
    return this.radius;
  }

  public void setRadius(Object radius)
  {
    this.radius = radius;
  }

  public Integer getStartAngle()
  {
    return this.startAngle;
  }

  public void setStartAngle(Integer startAngle)
  {
    this.startAngle = startAngle;
  }

  public Sort getSort()
  {
    return this.sort;
  }

  public void setSort(Sort sort)
  {
    this.sort = sort;
  }

  public Sort getSortSub()
  {
    return this.sortSub;
  }

  public void setSortSub(Sort sortSub)
  {
    this.sortSub = sortSub;
  }

  public Boolean getShowScale()
  {
    return this.showScale;
  }

  public void setShowScale(Boolean showScale)
  {
    this.showScale = showScale;
  }

  public Boolean getShowScaleText()
  {
    return this.showScaleText;
  }

  public void setShowScaleText(Boolean showScaleText)
  {
    this.showScaleText = showScaleText;
  }

  public Boolean getClockWise()
  {
    return this.clockWise;
  }

  public void setClockWise(Boolean clockWise)
  {
    this.clockWise = clockWise;
  }

  public Object[][] getMatrix()
  {
    return this.matrix;
  }

  public void setMatrix(Object[][] matrix)
  {
    this.matrix = matrix;
  }

  public Integer getMinRadius()
  {
    return this.minRadius;
  }

  public void setMinRadius(Integer minRadius)
  {
    this.minRadius = minRadius;
  }

  public Integer getMaxRadius()
  {
    return this.maxRadius;
  }

  public void setMaxRadius(Integer maxRadius)
  {
    this.maxRadius = maxRadius;
  }
}

/*
 * Qualified Name:     report.java.echarts.series.Chord
*
 */