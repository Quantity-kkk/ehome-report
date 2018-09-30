package report.java.echarts.series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import report.java.echarts.code.Roam;
import report.java.echarts.code.SeriesType;
import report.java.echarts.code.Symbol;
import report.java.echarts.series.force.Category;
import report.java.echarts.series.force.Link;
import report.java.echarts.series.force.Node;

public class Force extends Series<Force>
{
  private List<Category> categories;
  private List<Node> nodes;
  private List<Link> links;
  private Object center;
  private Object size;
  private Boolean preventOverlap;
  private Object coolDown;
  private Boolean ratioScaling;
  private Integer minRadius;
  private Integer maxRadius;
  private Object linkSymbol;
  private Integer linkSymbolSize;
  private Double scaling;
  private Double gravity;
  private Boolean draggable;
  private Boolean large;
  private Boolean useWorker;
  private Integer steps;
  private Object roam;

  public Force()
  {
    type(SeriesType.force);
  }

  public Force(String name)
  {
    super(name);
    type(SeriesType.force);
  }

  public Object coolDown()
  {
    return this.coolDown;
  }

  public Force coolDown(Object coolDown)
  {
    this.coolDown = coolDown;
    return this;
  }

  public Boolean ratioScaling()
  {
    return this.ratioScaling;
  }

  public Force ratioScaling(Boolean ratioScaling)
  {
    this.ratioScaling = ratioScaling;
    return this;
  }

  public Boolean preventOverlap()
  {
    return this.preventOverlap;
  }

  public Force preventOverlap(Boolean preventOverlap)
  {
    this.preventOverlap = preventOverlap;
    return this;
  }

  public Force categories(List<Category> categories)
  {
    this.categories = categories;
    return this;
  }

  public Force nodes(List<Node> nodes)
  {
    this.nodes = nodes;
    return this;
  }

  public Force links(List<Link> links)
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

  public Force categories(Category[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    categories().addAll(Arrays.asList(values));
    return this;
  }

  public Force categories(String[] names)
  {
    if ((names == null) || (names.length == 0)) {
      return this;
    }
    for (String name : names) {
      categories().add(new Category(name));
    }
    return this;
  }

  public Force categories(Object[] values)
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

  public Force nodes(Node[] values)
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

  public Force links(Link[] values)
  {
    if ((values == null) || (values.length == 0)) {
      return this;
    }
    links().addAll(Arrays.asList(values));
    return this;
  }

  public Object center()
  {
    return this.center;
  }

  public Force center(Object center)
  {
    this.center = center;
    return this;
  }

  public Object size()
  {
    return this.size;
  }

  public Force size(Object size)
  {
    this.size = size;
    return this;
  }

  public Integer minRadius()
  {
    return this.minRadius;
  }

  public Force minRadius(Integer minRadius)
  {
    this.minRadius = minRadius;
    return this;
  }

  public Integer maxRadius()
  {
    return this.maxRadius;
  }

  public Force maxRadius(Integer maxRadius)
  {
    this.maxRadius = maxRadius;
    return this;
  }

  public Object linkSymbol()
  {
    return this.linkSymbol;
  }

  public Force linkSymbol(Symbol linkSymbol)
  {
    this.linkSymbol = linkSymbol;
    return this;
  }

  public Force linkSymbol(String linkSymbol)
  {
    this.linkSymbol = linkSymbol;
    return this;
  }

  public Integer linkSymbolSize()
  {
    return this.linkSymbolSize;
  }

  public Force linkSymbolSize(Integer linkSymbolSize)
  {
    this.linkSymbolSize = linkSymbolSize;
    return this;
  }

  public Double scaling()
  {
    return this.scaling;
  }

  public Force scaling(Double scaling)
  {
    this.scaling = scaling;
    return this;
  }

  public Double gravity()
  {
    return this.gravity;
  }

  public Force gravity(Double gravity)
  {
    this.gravity = gravity;
    return this;
  }

  public Boolean draggable()
  {
    return this.draggable;
  }

  public Force draggable(Boolean draggable)
  {
    this.draggable = draggable;
    return this;
  }

  public Boolean large()
  {
    return this.large;
  }

  public Force large(Boolean large)
  {
    this.large = large;
    return this;
  }

  public Boolean useWorker()
  {
    return this.useWorker;
  }

  public Force useWorker(Boolean useWorker)
  {
    this.useWorker = useWorker;
    return this;
  }

  public Integer steps()
  {
    return this.steps;
  }

  public Force steps(Integer steps)
  {
    this.steps = steps;
    return this;
  }

  public Object roam()
  {
    return this.roam;
  }

  public Force roam(Boolean roam)
  {
    this.roam = roam;
    return this;
  }

  public Force roam(Roam roam)
  {
    this.roam = roam;
    return this;
  }

  public List<Category> getCategories()
  {
    return this.categories;
  }

  public void setCategories(List<Category> categories)
  {
    this.categories = categories;
  }

  public List<Node> getNodes()
  {
    return this.nodes;
  }

  public void setNodes(List<Node> nodes)
  {
    this.nodes = nodes;
  }

  public List<Link> getLinks()
  {
    return this.links;
  }

  public void setLinks(List<Link> links)
  {
    this.links = links;
  }

  public Object getCenter()
  {
    return this.center;
  }

  public void setCenter(Object center)
  {
    this.center = center;
  }

  public Object getSize()
  {
    return this.size;
  }

  public void setSize(Object size)
  {
    this.size = size;
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

  public Object getLinkSymbol()
  {
    return this.linkSymbol;
  }

  public void setLinkSymbol(Object linkSymbol)
  {
    this.linkSymbol = linkSymbol;
  }

  public Integer getLinkSymbolSize()
  {
    return this.linkSymbolSize;
  }

  public void setLinkSymbolSize(Integer linkSymbolSize)
  {
    this.linkSymbolSize = linkSymbolSize;
  }

  public Double getScaling()
  {
    return this.scaling;
  }

  public void setScaling(Double scaling)
  {
    this.scaling = scaling;
  }

  public Double getGravity()
  {
    return this.gravity;
  }

  public void setGravity(Double gravity)
  {
    this.gravity = gravity;
  }

  public Boolean getDraggable()
  {
    return this.draggable;
  }

  public void setDraggable(Boolean draggable)
  {
    this.draggable = draggable;
  }

  public Boolean getLarge()
  {
    return this.large;
  }

  public void setLarge(Boolean large)
  {
    this.large = large;
  }

  public Boolean getUseWorker()
  {
    return this.useWorker;
  }

  public void setUseWorker(Boolean useWorker)
  {
    this.useWorker = useWorker;
  }

  public Integer getSteps()
  {
    return this.steps;
  }

  public void setSteps(Integer steps)
  {
    this.steps = steps;
  }

  public Object getCoolDown()
  {
    return this.coolDown;
  }

  public void setCoolDown(Object coolDown)
  {
    this.coolDown = coolDown;
  }

  public Boolean getRatioScaling()
  {
    return this.ratioScaling;
  }

  public void setRatioScaling(Boolean ratioScaling)
  {
    this.ratioScaling = ratioScaling;
  }

  public Boolean getPreventOverlap()
  {
    return this.preventOverlap;
  }

  public void setPreventOverlap(Boolean preventOverlap)
  {
    this.preventOverlap = preventOverlap;
  }

  public Object getRoam()
  {
    return this.roam;
  }

  public void setRoam(Object roam)
  {
    this.roam = roam;
  }
}

/* 
 * Qualified Name:     report.java.echarts.series.Force
*
 */